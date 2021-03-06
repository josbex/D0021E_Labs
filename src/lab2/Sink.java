package lab2;

import Sim.*;

public class Sink extends Node {

	private TimeLogger timeLogger;

	public Sink(int network, int node) {
		super(network, node);
		this.timeLogger = new TimeLogger();
	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof Message) {
			this.printMsg("receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
			timeLogger.logTime("Sink", SimEngine.getTime());
		}
	}

}
