package lab2;


import java.util.Random;
import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.TimerEvent;

public class PoissonGenerator extends Node {

	private double lambda;
	private int nrOfPackets;
	private int limit;
	private int _toNetwork;
	private int _toHost;
	private int _seq;
	private TimeLogger timeLogger;

	public PoissonGenerator(int network, int node) {
		super(network, node);
		this.timeLogger = new TimeLogger();
	}

	public void StartSending(int network, int node,  double lambda, int timeLimit)
	{
		this.lambda = lambda;
		this.limit = timeLimit;
		_toNetwork = network;
		_toHost = node;
		_seq = 1;
		send(this, new TimerEvent(),0);	
	}

	//**********************************************************************************	

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof TimerEvent)
		{
			//Stop sending packets if it is not the sending phase
			if (SimEngine.getTime() < limit)
			{
				nrOfPackets = poissonDitribution(lambda);

				//Send set amount of packets for each timerevent
				for(int i = 0; i < nrOfPackets; i++){

					double randomDelay = Math.random();
					_sentmsg++;
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq), randomDelay);
					System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq + " at time "+(SimEngine.getTime()+randomDelay));
					timeLogger.logTime("Poisson_Generator", (SimEngine.getTime()+randomDelay));
					_seq++;
				}
				//next timerevent occurs after a second
				send(this, new TimerEvent(), 1);
			}
		}
		if (ev instanceof Message)
		{
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "+((Message) ev).seq() + " at time "+SimEngine.getTime());

		}
	}

	//Knuths algorithm for generating a poisson distrubuted random variable.
	//Taken from https://en.wikipedia.org/wiki/Poisson_distribution#Generating_Poisson-distributed_random_variables
	private int poissonDitribution(double lambda) {
		//Uses ranodm istead of math.randon to get uniform distributed values.
		Random u = new Random();
		double L = Math.exp(-lambda);
		int k = 0;
		double p = 1.0;
		while (p >= L){
			p = p * u.nextDouble();
			k++;
		} 
		return k - 1;
	}

}

