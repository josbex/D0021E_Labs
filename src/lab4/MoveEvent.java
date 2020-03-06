package lab4;

import Sim.Event;
import Sim.SimEnt;

public class MoveEvent implements Event {
	public void entering(SimEnt locale) {}

	public String getNameofEvent() {
		return "MoveEvent";
	}
}
