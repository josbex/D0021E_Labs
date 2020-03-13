package lab5;

import java.util.ArrayList;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class BroadcastMessage implements Event {
	
	private NetworkAddr dest;
	private ArrayList<NetworkAddr> collidedNodes;
	
	public BroadcastMessage(NetworkAddr dest, ArrayList<NetworkAddr> collidedNodes){
		this.dest = dest;
		this.collidedNodes = collidedNodes;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "BroadcastMessage";
	}

	public NetworkAddr getDest() {
		return dest;
	}
	
	public ArrayList<NetworkAddr> getCollidedNodes(){
		return collidedNodes;
	}


}
