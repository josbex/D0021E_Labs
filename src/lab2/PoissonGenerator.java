package lab2;


import java.sql.Time;
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

	public PoissonGenerator(int network, int node, double lambda) {
		super(network, node);
		this.timeLogger = new TimeLogger();
		this._identifierString = "POISSON_NODE " + _id.networkId() + "." + _id.nodeId();
		this.lambda = lambda;
	}

	public void StartSending(int network, int node, int nrOfPackets)
	{
		_toNetwork = network;
		this.nrOfPackets = nrOfPackets ;
		_toHost = node;
		_seq = 1;
		send(this, new TimerEvent(), poissonDistribution());
	}

	//**********************************************************************************	

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof TimerEvent) {
			//Send set amount of packets for each timerevent
			if (this._sentmsg < this.nrOfPackets) {
				double poissonDelay = poissonDistribution();
				_sentmsg++;
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq), 0);
				send(this, new TimerEvent(), poissonDelay);
				this.printMsg("sent message with seq: "+_seq + " at time "+(SimEngine.getTime()+poissonDelay));
				timeLogger.logTime("Poisson_Generator", (poissonDelay));
				_seq++;
			}
		}
		if (ev instanceof Message)
		{
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "+((Message) ev).seq() + " at time "+SimEngine.getTime());

		}
	}

	// Knuths algorithm for generating a poisson distributed random variable.
	// Taken from https://en.wikipedia.org/wiki/Poisson_distribution#Generating_Poisson-distributed_random_variables
	private int poissonDistribution() {
		//Uses random instead of math.random to get uniform distributed values.
		Random u = new Random();
		double L = Math.exp(-this.lambda);
		int k = 0;
		double p = 1.0;
		while (p >= L){
			p = p * u.nextDouble();
			k++;
		} 
		return k - 1;
	}

}

