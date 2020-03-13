package lab5;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * 
 * @author josef
 * This class is just a message sending out the current status of the link, idle or occupied. 
 */
public class LinkStatus implements Event{
	
	private boolean isIdle;
	private NetworkAddr from;
	
	public LinkStatus(boolean status, NetworkAddr from){
		this.isIdle = status;
		this.from = from;
	}
	
	public NetworkAddr getDest(){
		return from;
	}
	
	public boolean isIdle(){
		return isIdle;
	}
	
	public void entering(SimEnt locale) {
	}
	
	public String getNameofEvent() {
		return "LinkStatus";
	}

}
