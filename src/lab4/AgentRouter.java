package lab4;

import java.util.Arrays;

import java.util.HashMap;
import java.util.Map;

import ANSIColors.Color;
import Sim.Event;
import Sim.Link;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.SimEnt;


public class AgentRouter extends SimEnt {

	private int _now = 0;
	
	private static int counter;
	protected NetworkAddr _id;
	private HashMap<NetworkAddr, NetworkAddr> reroutes;
	private HashMap<NetworkAddr, Integer> routingTable;
	private Link[] interfaces;
	
	public AgentRouter(int interfaceNumber, int network, int address) {
		//super(interfaces);
		this._id = new NetworkAddr(network, address);
		reroutes = new HashMap<NetworkAddr, NetworkAddr>();
		interfaces = new Link[interfaceNumber];
		routingTable = new HashMap<NetworkAddr, Integer>();
		AgentRouter.counter++;
		this._identifierString = "AGENTROUTER " + AgentRouter.counter;
	}
	
	public void connectInterface(int interfaceNumber, Link link, NetworkAddr route) {
		if (interfaceNumber < interfaces.length) {
			interfaces[interfaceNumber] = link;
			routingTable.put(route, interfaceNumber);
		} else
			this.printMsg("Trying to connect to port not in router");

		link.setConnector(this);
	}
	
	public void setReroute(NetworkAddr hoa, NetworkAddr coa){
		reroutes.put(hoa, coa);
	}

	public void recv(SimEnt source, Event event) {
		//printRouterTable("RoutingTable for router: " + this.identifierString());
		if (event instanceof BindingUpdate) {
			NetworkAddr hoa = ((BindingUpdate) event).getHoA();
			NetworkAddr coa = ((BindingUpdate) event).getCoA();
			
			if (hoa.networkId() == this._id.networkId()) {
				this.setReroute(hoa, coa);
				this.printMsg("BINDING Message: Node " + hoa.toString() + " to " + coa.toString());
			} else {
				send(interfaces[routingTable.get(new NetworkAddr(hoa.networkId(), 0))], event, 0);
			}	
		}
		if (event instanceof WrappedMessage) {
			if (((WrappedMessage) event).getDestination() == _id) {
				NetworkAddr wrappedDest = ((WrappedMessage) event).getWrapped().destination();
				send(
						getInterface(wrappedDest),
						((WrappedMessage) event).getWrapped(),
						_now
				);
				this.printMsg("Unwrapping message to " + wrappedDest.toString());
			} else {
				NetworkAddr dest = ((WrappedMessage) event).getDestination();
				SimEnt sendNext = getInterface(dest);
				this.printMsg("Sends WRAPPED to node: " + dest.toString());
				send(sendNext, event, _now);
			}
		}
		if (event instanceof Message) {
			Message  msg = (Message) event;
			Event sendEvent = event;
			SimEnt sendNext;
			//Get the intended destination of the message, check if that host has moved, 
			//in that case forward msg to the care of address of the host.
			NetworkAddr dest = msg.destination();
			NetworkAddr coa = reroutes.get(dest);
			this.printMsg("Handles packet with seq: " + ((Message) event).seq() + " from node: " + ((Message) event).source().networkId() + "." + ((Message) event).source().nodeId());
			if(coa != null){
				this.printMsg("Forwarding message meant for (" + dest.networkId() +"." +dest.nodeId() + ") to foreign host (" + coa.networkId() +"." +coa.nodeId() + ")");
				dest = coa;
				sendEvent = new WrappedMessage(this._id, dest, msg);
				sendNext = getInterface(new NetworkAddr(dest.networkId(), 0));
			}
			else{
				sendNext = getInterface(dest);
			}
			this.printMsg("Sends to node: " + dest.toString());
			send(sendNext, sendEvent, _now);
		}
	}
	
	protected SimEnt getInterface(NetworkAddr addr) {
		SimEnt routerInterface = null;
		NetworkAddr coa = reroutes.get(addr);
		Integer interfaceNumber;
		if (coa != null)
			interfaceNumber = routingTable.get(coa);
		else
			interfaceNumber = routingTable.get(addr);
		
		if (interfaceNumber != null) {
			routerInterface = interfaces[interfaceNumber];
		} else {
			this.printMsg("No interface for address " + addr.toString());
		}
		return routerInterface;
	}
	
	
	public void printRouterTable(String msg) {
		System.out.println(msg);
		for (Map.Entry<NetworkAddr, Integer> entry : routingTable.entrySet()) {
			System.out.println("Entry " + entry.getValue().toString() + ": " + entry.getKey().toString());
		}
	}
	
	public int getNetworkAddr() {
		return _id.networkId();
	}
	
	public int getAvailableInterface(){
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public NetworkAddr getAvailableAddr(){
		int[] takenAddrs = new int[routingTable.size()];
		int j = 0;
		for (Map.Entry<NetworkAddr, Integer> entry : routingTable.entrySet()) {
			takenAddrs[j] = entry.getKey().nodeId();
			j++;
		}
		Arrays.sort(takenAddrs);
		return new NetworkAddr(this._id.networkId(), takenAddrs[takenAddrs.length-1]+1);
	}
	
	public NetworkAddr getAddr() {
		return _id;
	}
	
}
