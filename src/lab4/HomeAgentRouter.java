package lab4;

import ANSIColors.Color;
import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.SimEnt;
import lab3.MobileNode;
import lab3.MovableRouter;
import lab3.SwitchRouterEvent;

public class HomeAgentRouter extends MovableRouter {

	protected NetworkAddr _id;
	
	public HomeAgentRouter(int interfaces, int network, int address) {
		super(interfaces);
		this._id = new NetworkAddr(network, address);
		// TODO Auto-generated constructor stub
	}

	public void recv(SimEnt source, Event event) {
		/*if(event instanceof SwitchRouterEvent) {
			this.printMsg(Color.green("SWITCH EVENT RECV"));
			printRouterTable("Routing table before switch");
			switchRouterInterface(((SwitchRouterEvent) event).getSourceAddr(), ((SwitchRouterEvent) event).getNewRouterInterface());
		}*/
		if (event instanceof BindingUpdate) {
			//SimEnt networkInterface = this.getInterface( ((BindingUpdate) event).getForeignAddress().nodeId());
		}
		if (event instanceof Message) {
			this.printMsg("Handles packet with seq: " + ((Message) event).seq() + " from node: " + ((Message) event).source().networkId() + "." + ((Message) event).source().nodeId());
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			this.printMsg("Sends to node: " + ((Message) event).destination().networkId() + "." + ((Message) event).destination().nodeId());
			send(sendNext, event, _now);
		}
	}
	
	public int getNetworkAddr() {
		return _id.networkId();
	}
	
	public void disconnectFromInterface(MobileNode node) {
		for (int i = 0; i < _interfaces; i++) {
			if ( ((MobileNode) _routingTable[i].node() ).getAddr().nodeId() == node.getAddr().nodeId()) {
				_routingTable[i] = null;
				break;
			}
		}
	}
	
}
