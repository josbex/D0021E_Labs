package lab2;

import Sim.*;

public class CBRGenerator extends Node {

	private int nrOfPackets;
	private int _toNetwork;
	private int _toHost;
	private int _seq;
	private TimeLogger timeLogger;
	private int timeInterval;


	public CBRGenerator(int network, int node, int timeInterval) {
		super(network, node);
		this.timeLogger = new TimeLogger();
		this.timeInterval = timeInterval;
	}


	/**
	 * @param network
	 * @param node
	 * @param nrOfPackets: amount of packets sent per second during the sending phase
	 */
	public void StartSending(int network, int node, int nrOfPackets) {
		this.nrOfPackets = nrOfPackets;
		_toNetwork = network;
		_toHost = node;
		_seq = 1;
		send(this, new TimerEvent(), timeInterval);
	}

	//**********************************************************************************	

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
				//Send set amount of packets per second for each timerevent
				if (this._sentmsg < this.nrOfPackets) {
					_sentmsg++;
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 0);
					send(this, new TimerEvent(), timeInterval);
					this.printMsg(" sent message with seq: " + _seq + " at time " + SimEngine.getTime());// + timeInterval);
					timeLogger.logTime("CBR_Generator", timeInterval);
					_seq++;
					
				}
		}
		if (ev instanceof Message) {
			System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
		}
	}

}
