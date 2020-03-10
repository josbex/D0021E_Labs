package lab5;

import Sim.Event;
import Sim.SimEnt;

/**
 * 
 * @author josef
 * This class is just a message sending out the current status of the link, idle or occupied. 
 */
public class LinkStatus implements Event{
	
	private boolean isIdle;
	
	public LinkStatus(boolean status){
		this.isIdle = status;
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
