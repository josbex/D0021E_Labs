package lab4;

import java.util.Arrays;

import java.util.HashMap;
import java.util.Map;

import ANSIColors.Color;
import Sim.Event;
import Sim.IdealLink;
import Sim.Link;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.RouteTableEntry;
//import Sim.RouteTableEntry;
import Sim.Router;
import Sim.SimEnt;
import lab3.MobileNode;
import lab3.MovableRouter;
import lab3.SwitchRouterEvent;

public class AgentRouter extends SimEnt {

	private int _now = 0;
	
	private static int counter;
	protected NetworkAddr _id;
	private HashMap<NetworkAddr, NetworkAddr> reroutes;
	private HashMap<NetworkAddr, Integer> routingTable;
	private Link[] interfaces;
	//protected Lab4RouteTableEntry[] _routingTable;
	
	public AgentRouter(int interfaceNumber, int network, int address) {
		//super(interfaces);
		this._id = new NetworkAddr(network, address);
		//this._id = new NetworkAddr(Router.counter, 0);
		reroutes = new HashMap<NetworkAddr, NetworkAddr>();
		interfaces = new Link[interfaceNumber];
		routingTable = new HashMap<NetworkAddr, Integer>();
		this._identifierString = "AGENTROUTER " + AgentRouter.counter++;
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
		if (event instanceof BindingUpdate) {
			NetworkAddr hoa = ((MIPNode) source).getHoA();
			//New router hands host a new network address, a care of address.
			((MIPNode) source).setNodeID(new NetworkAddr(_id.networkId(), getAvailableAddr()));
			NetworkAddr coa = ((MIPNode) source).getAddr();
			this.printMsg("NODE (" + hoa.networkId() + "." + hoa.nodeId() + ") is now NODE (" + coa.networkId() + "." + coa.nodeId() + ")");
			//Connect the host to a interface of the new router.
			IdealLink newLink = new IdealLink();
			((MIPNode) source).setPeer(newLink);
			int slot = getAvailableInterface();
			if(slot != -1){
				connectInterface(slot, newLink, hoa);
				//Update the rerouting table of the Home agent.
				AgentRouter HA = ((BindingUpdate) event).getHA();
				HA.setReroute(hoa,coa);
			}
			else{
				this.printMsg("No available interfaces!");
			}	
		}
		if (event instanceof Message) {
			Message  msg = (Message) event;
			//Get the intended destination of the message, check if that host has moved, 
			//in that case forward msg to the care of address of the host.
			NetworkAddr dest = msg.destination();
			NetworkAddr coa = reroutes.get(dest);
			if(coa != null){
				this.printMsg("Forwarding message to foreign host (" + coa.networkId() +"." +coa.nodeId() + ")");
				dest = coa;
			}
			this.printMsg("Handles packet with seq: " + ((Message) event).seq() + " from node: " + ((Message) event).source().networkId() + "." + ((Message) event).source().nodeId());
			SimEnt sendNext = getInterface(dest);
			this.printMsg("Sends to node: " + dest.networkId() + "." + dest.nodeId());
			send(sendNext, event, _now);
			printRouterTable("RoutingTable for router: " + ((Message) event).source().networkId());
		}
	}
	
	/*
	public void updateRoutingTable(int interfaceNumber, SimEnt link, MIPNode node) {
		if (interfaceNumber < _interfaces) {
			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
		} else
			this.printMsg("Trying to connect to port not in router");

		((Link) link).setConnector(this);
	}
	
	protected MIPNode getNode(NetworkAddr addr){
		MIPNode routerInterface = null;
		for (int i = 0; i < _interfaces; i++)
			if (_routingTable[i] != null) {
				if (((MIPNode) _routingTable[i].node()).getAddr().networkId() == addr.networkId() && ((MIPNode) _routingTable[i].node()).getAddr().nodeId() == addr.nodeId()) {
					routerInterface = (MIPNode)_routingTable[i].node();
				}
			}
		return routerInterface;
	}	
	*/
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
		int i = 0;
		
		for (Map.Entry<NetworkAddr, Integer> entry : routingTable.entrySet()) {
			System.out.println("Entry " + i++ + ": " + entry.getKey().toString());
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
	
	public int getAvailableAddr(){
		int[] takenAddrs = new int[routingTable.size()];
		int j = 0;
		for (Map.Entry<NetworkAddr, Integer> entry : routingTable.entrySet()) {
			takenAddrs[j] = entry.getKey().nodeId();
			j++;
		}
		Arrays.sort(takenAddrs);
		return takenAddrs[takenAddrs.length-1]+1;
	}
	
	public NetworkAddr getAddr() {
		return _id;
	}
	
}
