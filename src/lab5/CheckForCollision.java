package lab5;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class CheckForCollision implements Event {
	
	NetworkAddr source;
	
	public CheckForCollision(NetworkAddr id){
		this.source = id;
	}
	
	public NetworkAddr getSource(){
		return source;
	}
	
	
	public void entering(SimEnt locale) {	
	}

	public String getNameofEvent() {
		return "CheckForCollision";
	}

}
