package Sim;

import lab1.ExtendedMessage;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {

	private static int counter = 0;

	private NetworkAddr _id;
	private SimEnt _peer;
	private int _sentmsg=0;
	private int _seq = 0;


	
	public Node (int network, int node)
	{
		super();
		_id = new NetworkAddr(network, node);

		this.identifierString = "Node " + _id.networkId() + "." + _id.nodeId();
		Node.counter++;
	}	
	
	
	// Sets the peer to communicate with. This node is single homed
	
	public void setPeer (SimEnt peer)
	{
		_peer = peer;
		
		if(_peer instanceof Link)
		{
			 ((Link) _peer).setConnector(this);
		}
	}
	
	
	public NetworkAddr getAddr()
	{
		return _id;
	}
	
//**********************************************************************************	
	// Just implemented to generate some traffic for demo.
	// In one of the labs you will create some traffic generators
	
	private int _stopSendingAfter = 0; //messages
	private int _timeBetweenSending = 10; //time between messages
	private int _toNetwork = 0;
	private int _toHost = 0;
	
	public void StartSending(int network, int node, int number, int timeInterval, int startSeq)
	{
		_stopSendingAfter = number;
		_timeBetweenSending = timeInterval;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(),0);	
	}
	
//**********************************************************************************	
	
	// This method is called upon that an event destined for this node triggers.
	
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof TimerEvent)
		{
			if (_stopSendingAfter > _sentmsg)
			{
				_sentmsg++;
				send(_peer, new ExtendedMessage(_id, new NetworkAddr(_toNetwork, _toHost),_seq, SimEngine.getTime()),0);
				send(this, new TimerEvent(),_timeBetweenSending);
				this.printMsg("Sent message with seq: "+_seq + " at time "+SimEngine.getTime());
				_seq++;
			}
		}
		if (ev instanceof ExtendedMessage) {
			((ExtendedMessage) ev).delay = SimEngine.getTime() - ((ExtendedMessage) ev).getTimestamp();
			this.printMsg("Receives extended message with seq: "+((ExtendedMessage) ev).seq() + " at time "+SimEngine.getTime()+". Time to receive: "+((ExtendedMessage) ev).delay +". Average jitter of link: "+((ExtendedMessage) ev).getAvgJitter());
		}
		else if (ev instanceof Message)
		{
			this.printMsg("Receives message with seq: "+((Message) ev).seq() + " at time "+SimEngine.getTime());
		}
	}
}
