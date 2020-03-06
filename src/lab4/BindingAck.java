package lab4;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class BindingAck implements Event {

	private NetworkAddr source;
	private NetworkAddr destination;
	
	public BindingAck(NetworkAddr source, NetworkAddr destination) {
		this.source = source;
		this.destination = destination;
	}

	public void entering(SimEnt locale) {}

	public String getNameofEvent() {
		return "BindingAck";
	}
	
	public NetworkAddr getSource() {
		return this.source;
	}
	
	public NetworkAddr getDestination() {
		return this.destination;
	}
}
