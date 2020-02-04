package lab1;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Sim.Event;
import Sim.SimEnt;
import Sim.Link;
import Sim.Message;

public class LossyLink extends Link  {
	
	private static int counter = 0;
	
	private int id;
	
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	
	private ArrayList<Double> delays;
	
	private double delay, initialJitter, simulatedJitter, dropProb;
	
	public LossyLink(double delay, double jitter, double dropProb){
		this.id = LossyLink.counter++;
		this.delay = delay;
		this.initialJitter = jitter;
		this.simulatedJitter = jitter;
		this.dropProb = dropProb;
		this.delays = new ArrayList<Double>();
	}
	
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}
	
	public void recv(SimEnt source, Event event) {
		if (event instanceof Message)
		{
			if (Math.random() < this.dropProb) {
				System.out.println("Link recv msg, but drops it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				return;
			}
			System.out.println("Link recv msg, passes it through");
			if (source == _connectorA)
			{
				send(_connectorB, event, calculateDelay());
			}
			else
			{
				send(_connectorA, event, calculateDelay());
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
			BufferedWriter writer = new BufferedWriter(new FileWriter("delays" + id +".txt", true));
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

}
