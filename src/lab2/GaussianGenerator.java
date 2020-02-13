package lab2;

import Sim.*;

import java.util.Random;

public class GaussianGenerator extends Node {

	private Random u;
	private TimeLogger timeLogger;
	private int limit;
	private int mean, standardDeviation;

	private int _toNetwork;
	private int _toHost;
	private int _seq;

	public GaussianGenerator(int network, int node, int mean, int standardDeviation) {
		super(network, node);
		u = new Random();
		timeLogger = new TimeLogger();

		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	public void StartSending(int network, int node, int timeLimit) {
		this.limit = timeLimit;
		this._toNetwork = network;
		this._toHost = node;
		_seq = 1;
		send(this, new TimerEvent(),0);
	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			//Stop sending packets if it is not the sending phase
			if (SimEngine.getTime() < limit)
			{
				int nrOfPackets = gaussianInt();

				//Send set amount of packets for each timerevent
				for (int i = 0; i < nrOfPackets; i++) {
					double randomDelay = Math.random();
					_sentmsg++;
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq), randomDelay);
					this.printMsg("Sent message with seq: " + _seq + " at time " + SimEngine.getTime());
					timeLogger.logTime("Gaussian_Generator", (SimEngine.getTime() + randomDelay));
					_seq++;
				}
				//next timerevent occurs after a second
				send(this, new TimerEvent(), 1);
			}
		}
		if (ev instanceof Message)
		{
			this.printMsg("Receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());

		}
	}

	private int gaussianInt() {
		return (int) Math.round(this.mean + (u.nextGaussian() * this.standardDeviation) );
	}

}
