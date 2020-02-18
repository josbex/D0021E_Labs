package lab3;

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
		//System.out.println(event.getNameofEvent());
		if(event instanceof SwitchRouterEvent){ //Event is sent out but never recieved?
			//printRouterTable();
			this.printMsg("Switch event was recieved!!!!!!!!");
			switchRouterInterface(((SwitchRouterEvent) event).getSourceAddr(), ((SwitchRouterEvent) event).getNewRouterInterface());
		}
		if (event instanceof Message) {
			this.printMsg("Handles packet with seq: " + ((Message) event).seq() + " from node: " + ((Message) event).source().networkId() + "." + ((Message) event).source().nodeId());
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			this.printMsg("Sends to node: " + ((Message) event).destination().networkId() + "." + ((Message) event).destination().nodeId());
			send(sendNext, event, _now);
			printRouterTable();
			switchRouterInterface(((Message) event).source(), 4);
		}
	}
	
	private void switchRouterInterface(NetworkAddr sourceAddr, int newRouterInterface){
		if(_routingTable[newRouterInterface] != null){
			System.out.println("Selected interface not available!");
			return;
		}
		for ( int i  = 0; i < _interfaces; i++){
			if(((Node)_routingTable[i].node()).getAddr() == sourceAddr){
				RouteTableEntry oldEntry = _routingTable[i];
				_routingTable[i] = null;
				_routingTable[newRouterInterface] = oldEntry;
				break;
			}
		}
		printRouterTable();
	}
	
	public void printRouterTable(){
		for(int i = 0; i<_routingTable.length; i++){
			if(_routingTable[i] != null){
				System.out.println("Entry " + i + ": " + ((Node)_routingTable[i].node()).identifierString());
			}else{
				System.out.println("Entry " + i + ": --");
			}
		}
	}

}
