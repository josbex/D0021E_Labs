package lab2;

import Sim.*;

public class CBRGenrator extends Node {

	private double time;
	private int nrOfPackets;
	private int limit;
	private int _toNetwork;
	private int _toHost;
	private int _seq;
	private TimeLogger timeLogger;
	private int timeInterval;


	public CBRGenrator(int network, int node) {
		super(network, node);
		this.timeLogger = new TimeLogger();
	}


	/**
	 * @param network
	 * @param node
	 * @param nrOfPackets: amount of packets sent per second during the sending phase
	 * @param timeLimit:   how long the sending phase is
	 */
	public void StartSending(int network, int node, int nrOfPackets, int timeInterval) {
		this.nrOfPackets = nrOfPackets;
		this.timeInterval = timeInterval;
		_toNetwork = network;
		_toHost = node;
		_seq = 1;
		send(this, new TimerEvent(), 0);
	}

	//**********************************************************************************	

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {

				//Send set amount of packets per second for each timerevent
				for (int i = 0; i < nrOfPackets; i++) {
					_sentmsg++;
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), timeInterval);
					this.printMsg(" sent message with seq: " + _seq + " at time " + SimEngine.getTime() + timeInterval);
					timeLogger.logTime("CBR_Generator", timeInterval);
					_seq++;
					
				}

		}
		if (ev instanceof Message) {
			System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());

		}
	}

}
