package lab4;

import java.util.Arrays;
import java.util.HashMap;

import ANSIColors.Color;
import Sim.Event;
import Sim.IdealLink;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.RouteTableEntry;
import Sim.Router;
import Sim.SimEnt;
import lab3.MobileNode;
import lab3.MovableRouter;
import lab3.SwitchRouterEvent;

public class AgentRouter extends MovableRouter {

	protected NetworkAddr _id;
	private HashMap<NetworkAddr, NetworkAddr> reroutes;
	
	public AgentRouter(int interfaces, int network, int address) {
		super(interfaces);
		this._id = new NetworkAddr(network, address);
		//this._id = new NetworkAddr(Router.counter, 0);
		reroutes = new HashMap<NetworkAddr, NetworkAddr>();
		this._identifierString = "AGENTROUTER " + Router.counter;
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
				connectInterface(slot, newLink, (MIPNode) source);
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
			SimEnt sendNext = getInterface(dest.networkId());
			this.printMsg("Sends to node: " + dest.networkId() + "." + dest.nodeId());
			send(sendNext, event, _now);
		}
	}
	
	/*
	protected SimEnt getInterface(NetworkAddr addr) {
		SimEnt routerInterface = null;
		for (int i = 0; i < _interfaces; i++)
			if (_routingTable[i] != null) {
				if (((MIPNode) _routingTable[i].node()).getAddr().networkId() == addr.networkId() && ((MIPNode) _routingTable[i].node()).getAddr().nodeId() == addr.nodeId()) {
					routerInterface = _routingTable[i].link();
				}
			}
		return routerInterface;
	}
	*/
	
	public int getNetworkAddr() {
		return _id.networkId();
	}
	
	public int getAvailableInterface(){
		for (int i = 0; i < _interfaces; i++) {
			if (_routingTable[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public int getAvailableAddr(){
		int[] takenAddrs = new int[_interfaces];
		int j = 0;
		for (int i = 0; i < _interfaces; i++) {
			if (_routingTable[i] != null) {
				takenAddrs[j] = ((MIPNode) _routingTable[i].node()).getAddr().nodeId();
				j++;
			}
		}
		Arrays.sort(takenAddrs);
		return takenAddrs[takenAddrs.length-1]+1;
	}
	
	
}
