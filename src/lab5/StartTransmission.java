package lab5;

import Sim.Event;
import Sim.SimEnt;

public class StartTransmission implements Event {

	private Frame frame;

	public StartTransmission(Frame frame) {
		this.frame = frame;
	}

	public Frame getFrame() {
		return this.frame;
	}

	public void entering(SimEnt locale) {}

	public String getNameofEvent() {
		return "StartTransmission";
	}
}
