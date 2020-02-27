package lab3;

import ANSIColors.Color;
import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.RouteTableEntry;
import Sim.Router;
import Sim.SimEnt;

public class MovableRouter extends Router{

	public MovableRouter(int interfaces) {
		super(interfaces);
		this._identifierString = "MOVABLE_ROUTER " + Router.counter;
	}
	
	
	public void recv(SimEnt source, Event event) {
		if(event instanceof SwitchRouterEvent){
			this.printMsg(Color.green("SWITCH EVENT RECV"));
			//printRouterTable("Routing table before switch");
			switchRouterInterface(((SwitchRouterEvent) event).getSourceAddr(), ((SwitchRouterEvent) event).getNewRouterInterface());
		}
		if (event instanceof Message) {
			this.printMsg("Handles packet with seq: " + ((Message) event).seq() + " from node: " + ((Message) event).source().networkId() + "." + ((Message) event).source().nodeId());
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			this.printMsg("Sends to node: " + ((Message) event).destination().networkId() + "." + ((Message) event).destination().nodeId());
			send(sendNext, event, _now);
		}
	}
	
	private void switchRouterInterface(NetworkAddr sourceAddr, int newRouterInterface){
		if(_routingTable[newRouterInterface] != null){
			this.printMsg(Color.red("ERR:") + " Selected interface not available!");
			return;
		}
		for ( int i  = 0; i < _interfaces; i++) {
			RouteTableEntry route = _routingTable[i];
			if (route != null && ((Node) route.node()).getAddr() == sourceAddr) {
				RouteTableEntry oldEntry = _routingTable[i];
				_routingTable[i] = null;
				_routingTable[newRouterInterface] = oldEntry;
				this.printMsg(route.node().identifierString() + " is now on interface " + newRouterInterface);
				break;
			}
		}
		//printRouterTable("Routing table after switch");
	}
	
	public void printRouterTable(String msg) {
		System.out.println(msg);
		for(int i = 0; i<_routingTable.length; i++) {
			if (_routingTable[i] != null) {
				System.out.println("Entry " + i + ": " + ((Node)_routingTable[i].node()).identifierString());
			} else {
				System.out.println("Entry " + i + ": --");
			}
		}
	}

}
