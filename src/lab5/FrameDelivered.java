package lab5;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class FrameDelivered implements Event {

	NetworkAddr source;
	
	public FrameDelivered(NetworkAddr source){
		this.source = source;
	}
	
	public NetworkAddr getSource(){
		return source;
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "FrameDelivered";
	}

}
