package lab5;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * 
 * @author 
 * This class is just a request message the nodes sends out when they are sensing the link. Its response is a LinkStatus.
 */
public class CheckLinkStatus implements Event {
	
	private NetworkAddr source;
	private NetworkAddr dest;

	public CheckLinkStatus(NetworkAddr source, NetworkAddr dest){
		this.source = source;
		this.dest = dest;
	}
	public void entering(SimEnt locale) {
		
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "CheckLinkStatus";
	}

	public NetworkAddr getSource() {
		return source;
	}
	
	public NetworkAddr getDest() {
		return dest;
	}


}
