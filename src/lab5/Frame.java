package lab5;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class Frame implements Event {

	private NetworkAddr source;
	private NetworkAddr destination;

	private int seq;

	// The time it takes for an entire frame to be transmitted
	public static int transmissionDelay;

	public Frame(NetworkAddr from, NetworkAddr to, int seq) {
		this.source = from;
		this.destination = to;
		this.seq = seq;
	}

	public NetworkAddr source() {
		return source;
	}

	public NetworkAddr destination() {
		return destination;
	}

	public int seq() {
		return seq;
	}

	public void entering(SimEnt locale) {}

	public String getNameofEvent() {
		return "Frame";
	}

}
