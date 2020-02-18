package lab3;

import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.TimerEvent;

public class MobileNode extends Node {

	private int changeAfterNrPkts;
	private int newRouterInterface;

	public MobileNode(int network, int node, int changeAfterNrPkts, int newRouterInterface) {
		super(network, node);
		this.changeAfterNrPkts = changeAfterNrPkts;
		this.newRouterInterface = newRouterInterface;
		this._identifierString = "MOBILE_NODE " + _id.networkId() + "." + _id.nodeId();
	}
	
	
	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			if (_stopSendingAfter > _sentmsg) {
				_sentmsg++;
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 0);
				send(this, new TimerEvent(), _timeBetweenSending);
				this.printMsg("Sent message with seq: " + _seq + " at time " + SimEngine.getTime());
				_seq++;
				if(_sentmsg == changeAfterNrPkts){
					send(_peer, new SwitchRouterEvent(_id, newRouterInterface), 0);
					this.printMsg("SwitchEvent was sent!!!!!!!!!!!");
				}
			}
		}

		if (ev instanceof Message) {
			this.printMsg("Receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
		}
	}

}
