package lab4;

import Sim.Event;
import Sim.SimEnt;

public class ACKTimeoutEvent implements Event {

	public void entering(SimEnt locale) {
	}

	public String getNameofEvent() {
		return "WaitForACKEvent";
	}

}
