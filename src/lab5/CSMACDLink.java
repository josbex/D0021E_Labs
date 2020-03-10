package lab5;

import java.util.ArrayList;

import Sim.Event;
import Sim.EventHandle;
import Sim.IdealLink;
import Sim.Message;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.TimerEvent;


public class CSMACDLink extends IdealLink {
	
	//The switching of isIdle needs to happen at some time when a frame is being sent, i.e. the range 0 to delay.
	boolean isIdle;
	//This list also needs to be reset after a collision or a frame is delivered.
	ArrayList<Message> transmittingNodes;
	//Needed to keep track of what frames to cancel in case of collision
	ArrayList<EventHandle> framesInLink;
	//The time a frame is in the link
	int delay;
	
	final private int _now = 0;

	public CSMACDLink(int delay) {
		super();
		transmittingNodes = new ArrayList<Message>();
		framesInLink = new ArrayList<EventHandle>();
		this.delay = delay;
	}

	// Called when a message enters the link
	public void recv(SimEnt src, Event ev) {
		if (ev instanceof CheckLinkStatus){
			//Respond with a link status message
			send(src, new LinkStatus(isIdle), _now);
		}
		else if (ev instanceof Message) {
			Message msg = (Message) ev;
			transmittingNodes.add(msg);
			if(transmittingNodes.size() == 1){
				//Calculate propagation delay of link signal
				//Set isIdle to false after this delay
				send(this, new TimerEvent(), propagationDelay(delay));
			}
			this.printMsg("Link recv msg, passes it through");
			if (src == _connectorA) {
				framesInLink.add(send(_connectorB, ev, delay));
				framesInLink.add(send(this, new FrameDelivered(), delay));
			} else {
				framesInLink.add(send(_connectorA, ev, delay));
				framesInLink.add(send(this, new FrameDelivered(), delay));
			}
			if(transmittingNodes.size()>1){
				this.printMsg("Collision detected on the link");
				//collision occurred
				//Cancel all frames in link and send collision detected message to all nodes
				cancelCurrentFrames(framesInLink);
				sendCollisionMsg(transmittingNodes);
				//Empty the transmittingNodes list and set isIdle to true
				transmittingNodes.clear();
				framesInLink.clear();
				isIdle = true;
			}
		}
		else if(ev instanceof TimerEvent){
			this.isIdle = false;
		}
		//Needs to send a frame delivered if the message was sent without collisions.
		else if(ev instanceof FrameDelivered){
			//send frame delivered to the node that managed to send its frame.
		}
	}
	
	private void sendCollisionMsg(ArrayList<Message> messages){
		for(Message msg: messages){
			//Send collision detected to all nodes in the transmitting nodes list
			//send(this, new CollisionDetected(), 0);
		}
	}
	
	private void cancelCurrentFrames(ArrayList<EventHandle> currentFrames){
		for(EventHandle f: currentFrames){
			SimEngine.instance().deregister(f);
		}
	}
	
	private double propagationDelay(int range){
		return Math.random() * range;
	}
	

}
