package lab2;

import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.TimerEvent;

public class CBR_Genrator extends Node {

	private int time;
	private int nrOfPackets;
	private int limit;
	private int _toNetwork;
	private int _toHost;
	private int _seq;
	

	public CBR_Genrator(int network, int node) {
		super(network, node);	
	}


	/**
	 * 
	 * @param network
	 * @param node
	 * @param nrOfPackets: amount of packets sent per second during the sending phase
	 * @param timeLimit: how long the sending phase is 
	 */
	public void StartSending(int network, int node, int nrOfPackets, int timeLimit)
	{
		this.nrOfPackets = nrOfPackets;
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
			if (SimEngine.getTime() > limit)
			{
				time = 0;
				//Send set amount of packets per second for each timerevent
				for(int i = 0; i < nrOfPackets; i++){
					_sentmsg++;
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq), time);
					System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq + " at time "+SimEngine.getTime());
					_seq++;
					time += 1/nrOfPackets;
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

}
