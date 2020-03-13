package lab5;

import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.Router;
import Sim.SimEnt;

public class BroadcastRouter extends Router {

	public BroadcastRouter(int interfaces) {
		super(interfaces);
	}
	
	public void recv(SimEnt source, Event event) {
		if (event instanceof Message) {
			this.printMsg("Handles packet with seq: " + ((Message) event).seq() + " from node: " + ((Message) event).source().networkId() + "." + ((Message) event).source().nodeId());
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			this.printMsg("Sends to node: " + ((Message) event).destination().networkId() + "." + ((Message) event).destination().nodeId());
			send(sendNext, event, _now);
		}
		else if(event instanceof CheckLinkStatus){
			this.printMsg("Checking the link");
			SimEnt sendNext = getBroadcastInterface(((CheckLinkStatus) event).getDest());
			send(sendNext, event, _now);
		}
		else if(event instanceof LinkStatus){
			SimEnt sendNext = getBroadcastInterface(((LinkStatus) event).getDest());
			send(sendNext, event, _now);
		}
		else if(event instanceof BroadcastMessage){
			broadcastMsg(((BroadcastMessage)event).getDest(), (BroadcastMessage)(event));
		}
		else if(event instanceof FrameDelivered){
			SimEnt sendNext = getBroadcastInterface(((FrameDelivered) event).getSource());
			send(sendNext, event, _now);
		}
	}
	
	protected SimEnt getBroadcastInterface(NetworkAddr networkAddress) {
		SimEnt routerInterface = null;
		for (int i = 0; i < _interfaces; i++)
			if (_routingTable[i] != null) {
				if (((Node) _routingTable[i].node()).getAddr().equals(networkAddress)) {
					routerInterface = _routingTable[i].link();
				}
			}
		return routerInterface;
	}
	
	private void broadcastMsg(NetworkAddr dest, BroadcastMessage event){
		SimEnt routerInterface = null;
		for (int i = 0; i < _interfaces; i++)
			if (_routingTable[i] != null) {
				if (!(((Node) _routingTable[i].node()).getAddr().equals(dest))) {
					routerInterface = _routingTable[i].link();
					this.printMsg("Sends broadcast message to node: " + ((Node) _routingTable[i].node()).getAddr().toString());
					send(routerInterface, event, _now);
				}
			}
	}
	

}
