package lab5;

import java.util.ArrayList;

import Sim.Event;
import Sim.EventHandle;
import Sim.IdealLink;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.TimerEvent;


public class CSMACDLink extends IdealLink {
	
	//The switching of isIdle needs to happen at some time when a frame is being sent, i.e. the range 0 to delay.
	boolean isIdle;
	boolean collisionDetected;
	//This list also needs to be reset after a collision or a frame is delivered.
	ArrayList<Message> transmittingNodes;
	//Needed to keep track of what frames to cancel in case of collision
	ArrayList<EventHandle> framesInLink;
	//The time a frame is in the link
	int delay;
	EventHandle state, switchState;
	
	final private int _now = 0;

	public CSMACDLink(int delay) {
		super();
		transmittingNodes = new ArrayList<Message>();
		framesInLink = new ArrayList<EventHandle>();
		this.delay = delay;
		this.isIdle = true;
		this.collisionDetected = false;
	}

	// Called when a message enters the link
	public void recv(SimEnt src, Event ev) {
		if (ev instanceof CheckLinkStatus){
			//Respond with a link status message
			this.printMsg("Sending state of link to Node: " + ((CheckLinkStatus) ev).getSource().toString());
			if (src == _connectorA) {
				send(_connectorA, new LinkStatus(isIdle, ((CheckLinkStatus) ev).getSource()), _now);
			} else {
				send(_connectorB, new LinkStatus(isIdle, ((CheckLinkStatus) ev).getSource()), _now);
			}
		}
		else if (ev instanceof Message) {
			Message msg = (Message) ev;
			transmittingNodes.add(msg);
			if(transmittingNodes.size() == 1){
				state = send(this, new FrameDelivered(msg.source()), delay);
				//Calculate propagation delay of link signal
				//Set isIdle to false after this delay
				//FrameDelivered messages represent ACK messages that a frame has been delivered
				switchState = send(this, new TimerEvent(), propagationDelay(delay));
			}
			this.printMsg("Link recv msg, passes it through");
			if (src == _connectorA) {
				framesInLink.add(send(_connectorB, ev, delay));
				framesInLink.add(send(_connectorA, new FrameDelivered(msg.source()), delay));
			} else {
				framesInLink.add(send(_connectorA, ev, delay));
				framesInLink.add(send(_connectorB, new FrameDelivered(msg.source()), delay));
			}
			//if amount of messages in the link are more than 1 at a time, reset the link
			if(transmittingNodes.size()> 1){
				this.printMsg("Collision detected on the link");
				if (src == _connectorA) {
					send(_connectorA, new BroadcastMessage(msg.destination(), collidedNodes(transmittingNodes)), _now);
				} else {
					send(_connectorB, new BroadcastMessage(msg.destination(), collidedNodes(transmittingNodes)), _now);
				}
				clearState();
			}

		}
		//Packet was delivered without collisions
		else if(ev instanceof FrameDelivered){
			this.printMsg("Frame was delivered from Node: " + ((FrameDelivered) ev).getSource().toString());
			transmittingNodes.clear();
			framesInLink.clear();
			this.isIdle = true;
		}
		else if(ev instanceof TimerEvent){
			this.isIdle = false;
		}
	}
	
	private ArrayList<NetworkAddr> collidedNodes(ArrayList<Message> msgs){
		ArrayList<NetworkAddr> nodes = new ArrayList<NetworkAddr>();
		for(Message m: msgs){
			nodes.add(m.source());
		}
		return nodes;
	}
	
	private void clearState(){
		SimEngine.instance().deregister(state);
		SimEngine.instance().deregister(switchState);
		clearFramesInLink(framesInLink);
		transmittingNodes.clear();
		framesInLink.clear();
		this.isIdle = true;
	}
	
	private void clearFramesInLink(ArrayList<EventHandle> currentFrames){
		for(EventHandle f: currentFrames){
			SimEngine.instance().deregister(f);
		}
	}
	
	private int RemoveCurrentMsg(NetworkAddr source){
		for(int i = 0; i < transmittingNodes.size(); i++){
			if(source.equals(transmittingNodes.get(i).source())){
				transmittingNodes.remove(i);
				return i;
			}
		}
		return -1;
	}
	
	private void removeFrameFromLink(int index){
		SimEngine.instance().deregister(framesInLink.get(index));
		SimEngine.instance().deregister(framesInLink.get(index+1));
		framesInLink.remove(index+1);
		framesInLink.remove(index);
	}

	
	private double propagationDelay(int range){
		return Math.random() * range;
	}
	

}
