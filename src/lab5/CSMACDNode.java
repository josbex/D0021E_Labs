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
		this.allowedToSend = false;
		send(this, new TimerEvent(), timeBetweenChecking);
	}

//**********************************************************************************	

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			//Keep checking if link is idle when wanting to a transmission or retransmission in case of a collision
			send(_peer, new CheckLinkStatus(_id,new NetworkAddr(_toNetwork, _toHost)), 0);
			this.printMsg("Checking status of link!!!!!!!!!");
		}
		/*
		else if (ev instanceof CheckForCollision){
			//During the transmission, keep checking the no collisions occur
			send(_peer, new CheckForCollision(_id), timeBetweenChecking);
			this.printMsg("Checking for collisions!!!!!!!");
		}
		*/
		else if (ev instanceof LinkStatus){
			LinkStatus state = (LinkStatus) ev;
			allowedToSend = state.isIdle();
			if(allowedToSend){
				if(_stopSendingAfter > _sentmsg){
					CurrentMsg = new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq);
					send(_peer, CurrentMsg, 0);
					//send(_peer, new BroadcastMessage(_id), 0);
					this.printMsg("Sent message with seq: " + _seq + " at time " + SimEngine.getTime() + " to node: " + CurrentMsg.destination().toString());
					_sentmsg++;
					_seq++;
					//send(this, new CheckForCollision(_id), timeBetweenChecking);
				}
			}
			else{
				if(_stopSendingAfter > _sentmsg){
					send(this, new TimerEvent(), timeBetweenChecking);
				}
			}
		}
		else if(ev instanceof BroadcastMessage){
			//increase counter of how many times this packet has collided
			collisionCounter++;
			//reset the message to be sent to the collided message, decrease seq and sentmsg by one.
			_sentmsg--;
			_seq--;
			//start checking if idle (i.e. timerevent) after the exponential back off period.
			send(this, new TimerEvent(), exponentialBackOff(collisionCounter));
		}
		/*
		else if(ev instanceof NoCollisionDetected){
			send(this, new CheckForCollision(_id), timeBetweenChecking);
		}
		*/
		else if(ev instanceof FrameDelivered){
			this.printMsg("Frame was succesfully transmitted from " + _id.toString() + " to " + CurrentMsg.destination().toString());
			//Reset collision counter for next frame
			collisionCounter = 0;
		}
		else if (ev instanceof Message) {
			this.printMsg("Receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime() + " from source: " + ((Message) ev).source().toString());
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
