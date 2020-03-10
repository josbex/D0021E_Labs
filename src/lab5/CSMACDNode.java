package lab5;

import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.TimerEvent;

public class CSMACDNode extends Node {

	private int timeBetweenChecking;
	private Message CurrentMsg;
	private int collisionCounter;
	private boolean allowedToSend;
	
	public CSMACDNode(int network, int node) {
		super(network, node);
		this.collisionCounter = 0;
	}
	
	
	public void StartSending(int network, int node, int number, int timeInterval, int startSeq) {
		_stopSendingAfter = number;
		this.timeBetweenChecking = timeInterval;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(), 0);
	}

//**********************************************************************************	

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			send(_peer, new CheckLinkStatus(), 0);
		}
		else if (ev instanceof LinkStatus){
			LinkStatus state = (LinkStatus) ev;
			allowedToSend = state.isIdle();
			if(allowedToSend){
				if(_stopSendingAfter > _sentmsg){
					CurrentMsg = new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq);
					send(_peer, CurrentMsg, 0);
					this.printMsg("Sent message with seq: " + _seq + " at time " + SimEngine.getTime());
					_sentmsg++;
					_seq++;
				}
			}
			else{
				if(_stopSendingAfter > _sentmsg){
					send(this, new TimerEvent(), timeBetweenChecking);
				}
			}
		}
		else if(ev instanceof CollisionDetected){
			//increase counter of how many times this packet has collided
			collisionCounter++;
			//reset the message to be sent to the collided message, decrease seq and sentmsg by one.
			_sentmsg--;
			_seq--;
			//start checking if idle (i.e. timerevent) after the exponential back off period.
			send(this, new TimerEvent(), exponentialBackOff(collisionCounter));
		}
		else if(ev instanceof FrameDelivered){
			//Reset collision counter for next frame
			collisionCounter = 0;
		}
		else if (ev instanceof Message) {
			this.printMsg("Receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
		}
	}
	
	/**
	 * @param c: number of collisions current frame has had
	 * @return random number K, deciding the back off time period
	 */
	private int exponentialBackOff(int c){
		int max = (int)(Math.pow(2,c))-1;
		int range = max + 1;
		return (int)(Math.random() * range);
	}
	

}
