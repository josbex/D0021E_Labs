package lab5;

import Sim.Event;
import Sim.SimEnt;

/**
 * 
 * @author 
 * This class is just a request message the nodes sends out when they are sensing the link. Its response is a LinkStatus.
 */
public class CheckLinkStatus implements Event {

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "CheckLinkStatus";
	}

}
