package lab4;

import Sim.Event;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.SimEnt;


public class WrappedMessage implements Event {
	private NetworkAddr source;
	private NetworkAddr destination;
	private Message wrapped;

	public WrappedMessage(NetworkAddr from, NetworkAddr to, Message wrapped) {
		this.source = from;
		this.destination = to;
		this.wrapped = wrapped;
	}
	
	@Override
	public void entering(SimEnt locale) {}

	@Override
	public String getNameofEvent() {
		return "WrappedMessage";
	}
	
	public NetworkAddr getSource() {
		return source;
	}
	
	public NetworkAddr getDestination() {
		return destination;
	}
	
	public Message getWrapped() {
		return wrapped;
	}

}
