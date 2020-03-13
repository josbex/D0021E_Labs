package lab5;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class BroadcastMessage implements Event {
	
	private NetworkAddr dest;
	
	public BroadcastMessage(NetworkAddr dest){
		this.dest = dest;
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


}
