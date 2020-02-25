package lab2;

import Sim.*;

import java.util.Random;

public class GaussianGenerator extends Node {

	private Random u;
	private TimeLogger timeLogger;
	private double mean, standardDeviation;

	private int _toNetwork;
	private int _toHost;
	private int _seq;
	private int nrOfPackets;

	public GaussianGenerator(int network, int node, double mean, double standardDeviation) {
		super(network, node);
		u = new Random();
		timeLogger = new TimeLogger();

		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	public void StartSending(int network, int node, int nrOfPackets) {
		this.nrOfPackets = nrOfPackets;
		this._toNetwork = network;
		this._toHost = node;
		_seq = 1;
		send(this, new TimerEvent(), gaussianDouble());
	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
				//Send set amount of packets for each TimerEvent
				if (this._sentmsg < this.nrOfPackets) {
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq), 0);

					double normalDelay;
					do {
						_sentmsg++;
						normalDelay = gaussianDouble();
					} while (normalDelay < 0.0); // Throw away event if negative delay

					send(this, new TimerEvent(), normalDelay);
					this.printMsg("Sent message with seq: " + _seq + " at time " + SimEngine.getTime());
					timeLogger.logTime("Gaussian_Generator", (normalDelay));
					_seq++;
				}
		}
		if (ev instanceof Message)
		{
			this.printMsg("Receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());

		}
	}

	private double gaussianDouble() {
		return this.mean + (u.nextGaussian() * this.standardDeviation);
	}

}
