package lab5;

import Sim.Event;
import Sim.SimEnt;

/**
 * This class is just a message sending out the current status of the link, idle or busy.
 */
public class LinkStatusChanged implements Event{

	private LinkStatus linkStatus;

	public LinkStatusChanged(LinkStatus status){
		this.linkStatus = status;
	}

	public LinkStatus getLinkStatus() {
		return this.linkStatus;
	}

	public boolean isIdle() {
		return this.linkStatus == LinkStatus.idle;
	}
	
	public void entering(SimEnt locale) {}
	
	public String getNameofEvent() {
		return "LinkStatus";
	}

}
