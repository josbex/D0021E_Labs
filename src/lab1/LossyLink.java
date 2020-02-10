package lab1;

import ANSIColors.Color;
import Sim.Event;
import Sim.Link;
import Sim.Message;
import Sim.SimEnt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class LossyLink extends Link {

	private ArrayList<Double> delays;

	private double delay, initialJitter, simulatedJitter, dropProb;
	private int packetsReceived, packetsDropped;

	public LossyLink(double delay, double jitter, double dropProb) {
		this.delay = delay;
		this.initialJitter = jitter;
		this.dropProb = dropProb;

		this.simulatedJitter = jitter;
		this.packetsDropped = 0;
		this.packetsReceived = 0;
		this.delays = new ArrayList<Double>();
	}

	public void recv(SimEnt source, Event event) {
		this.packetsReceived++;
		this.printMsg("Recv msg from [" + source.identifierString() + "] seq: " + ((Message) event).seq());
		if (event instanceof Message) {
			if (Math.random() < this.dropProb) {
				this.packetsDropped++;
				this.printMsg(Color.red("    DROP ") + "msg seq: " + ((Message) event).seq());
			} else {
				if (source == _connectorA) {
					send(_connectorB, event, calculateDelay());
					this.printMsg(Color.green("    PASS ") + "through to [" + _connectorB.identifierString() + "]");
				} else {
					send(_connectorA, event, calculateDelay());
					this.printMsg(Color.green("    PASS ") + "through to [" + _connectorA.identifierString() + "]");
				}
			}
		}
	}

	/*
	 * Decides randomly what the delay will be within the range of the set delay +/- the initial jitter.
	 */
	private double calculateDelay() {
		double newDelay = this.delay + this.initialJitter * (2 * Math.random() - 1);
		this.delays.add(newDelay);
		writeDelayToFile(newDelay, currentJitter());
		return newDelay;
	}

	/*
	 * Calculates what the jitter of the link is
	 * by taking the difference between the most recent delay and the next most recent.
	 */
	private double currentJitter() {
		if (this.delays.size() > 1) {
			return this.delays.get(this.delays.size() - 1) - this.delays.get(this.delays.size() - 2);
		} else {
			return this.initialJitter;
		}
	}

	/**
	 * Writes each delay and jitter to a file as these values change.
	 *
	 * @param delay  : the calculated delay of the link
	 * @param jitter : the current jitter of the link, based on previous delays
	 */
	private void writeDelayToFile(double delay, double jitter) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("delays_" + this._identifierString + ".txt", true));
			writer.append(delay + " " + jitter + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double averageDelay() {
		double sum = 0.0;
		for (double d : this.delays) {
			sum += d;
		}
		return sum / this.delays.size();
	}

	public void printSummary() {
		System.out.println(Color.blue(this._identifierString) + " Summary");
		System.out.println("    " + "          Jitter: " + this.simulatedJitter);
		System.out.printf("    " + " Dropped Packets: %d/%d (%.2f%%)\n", this.packetsDropped, this.packetsReceived,
				100 * ((double) this.packetsDropped) / ((double) this.packetsReceived));
		System.out.printf("    " + "   Average delay: %.2f\n", this.averageDelay());
	}
}
