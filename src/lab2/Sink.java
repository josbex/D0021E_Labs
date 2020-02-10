package lab2;

import Sim.Event;
import Sim.Message;
import Sim.Node;
import Sim.SimEngine;
import Sim.SimEnt;

public class Sink extends Node {
	
	private TimeLogger timeLogger;

	public Sink(int network, int node) {
		super(network, node);
		this.timeLogger = new TimeLogger();
	}

	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message)
		{
			System.out.println("Sink node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
			timeLogger.logTime("Sink", SimEngine.getTime());
		}
	}

}
