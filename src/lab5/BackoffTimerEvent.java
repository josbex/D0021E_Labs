package lab5;

import Sim.Event;
import Sim.SimEnt;

public class BackoffTimerEvent implements Event {
	public void entering(SimEnt locale) {}

	public String getNameofEvent() {
		return "BackoffTimer";
	}
}
