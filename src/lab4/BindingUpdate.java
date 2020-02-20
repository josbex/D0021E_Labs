package lab4;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;
import lab3.MobileNode;

public class BindingUpdate implements Event {
	
	private NetworkAddr foreignAddress;
	
	public BindingUpdate(MobileNode node, NetworkAddr foreignAddress) {
		this.foreignAddress = foreignAddress;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "Binding Update Event";
	}
	
	public NetworkAddr getForeignAddress() {
		return this.foreignAddress;
	}

}
