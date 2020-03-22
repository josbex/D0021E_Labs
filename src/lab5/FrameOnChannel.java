package lab5;

import Sim.Event;
import Sim.SimEnt;

public class FrameOnChannel implements Event {

	private boolean status;
	private static int counter;
	private int sequence;

	public FrameOnChannel(boolean status) {
		this.status = status;
		this.sequence = FrameOnChannel.counter++;
	}

	public boolean getStatus() {
		return this.status;
	}

	public int getSequence() {
		return sequence;
	}

	public void entering(SimEnt locale) {}

	public String getNameofEvent() {
		return "FrameOnChannel";
	}
}
