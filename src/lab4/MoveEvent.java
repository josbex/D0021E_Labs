package lab4;

import Sim.Event;
import Sim.SimEnt;

public class MoveEvent implements Event {
	@Override
	public void entering(SimEnt locale) {}

	@Override
	public String getNameofEvent() {
		return "MoveEvent";
	}
}
