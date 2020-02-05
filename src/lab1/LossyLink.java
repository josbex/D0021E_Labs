package lab1;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Sim.Event;
import Sim.SimEnt;
import Sim.Link;
import Sim.Message;

import ANSIColors.Color;


public class LossyLink extends Link  {
	
	//private static int counter = 0;
	
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	
	private ArrayList<Double> delays;
	
	private double delay, initialJitter, simulatedJitter, dropProb;
	private int packetsReceived, packetsDropped;

	public LossyLink(double delay, double jitter, double dropProb){
		this.delay = delay;
		this.initialJitter = jitter;
		this.simulatedJitter = jitter;
		this.dropProb = dropProb;
		this.packetsDropped = 0;
		this.packetsReceived = 0;
		this.delays = new ArrayList<Double>();
	}

	public void recv(SimEnt source, Event event) {
		this.packetsReceived++;
		if (event instanceof Message)
		{
			if (Math.random() < this.dropProb) {
				this.printMsg(Color.red("DROP ") + "msg seq: " + ((Message) event).seq());
				this.packetsDropped++;
			} else {
				this.printMsg("Recv msg, passes it through");
				if (source == _connectorA) {
					send(_connectorB, event, calculateDelay());
				} else {
					send(_connectorA, event, calculateDelay());
				}
			}
		}

	}
	
	private double calculateDelay(){
		double newDelay = this.delay + this.initialJitter * (2 * Math.random() - 1);
		this.delays.add(newDelay);
		writeDelayToFile(newDelay, currentJitter());
		return newDelay;
	}
	
	private double currentJitter() {
		if (this.delays.size() > 1) {
			return this.delays.get(this.delays.size() - 1) - this.delays.get(this.delays.size() - 2);
		} else {
			return this.initialJitter;
		}
	}
	
	private void writeDelayToFile(double delay, double jitter) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("delays_" + this.identifierString +".txt", true));
			writer.append(delay + " " + jitter + "\n" );
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		System.out.println(Color.green(this.identifierString) + " Summary");
		System.out.println("    " + "          Jitter: " + this.simulatedJitter);
		System.out.printf("    " + " Dropped Packets: %d/%d (%.2f%%)\n", this.packetsDropped, this.packetsReceived,
				100 * ((double) this.packetsDropped)/((double) this.packetsReceived));
		System.out.println("    " + "   Average delay: " + this.averageDelay());
	}

	private String dropPercentage() {
		int division = (this.packetsDropped * 1000) / (this.packetsReceived);
		return Integer.toString(division / 10) + "." + Integer.toString(division / 1000);
	}

}
