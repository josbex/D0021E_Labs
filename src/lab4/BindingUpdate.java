package lab4;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;


public class BindingUpdate implements Event {
	
	//private AgentRouter HA;
	private NetworkAddr hoa;
	private NetworkAddr coa;

	public BindingUpdate(NetworkAddr hoa, NetworkAddr coa) {
		//this.HA = homeAgent;
		this.hoa = hoa;
		this.coa = coa;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "Binding Update Event";
	}
	
	/*
	public AgentRouter getHA() {
		return this.HA;
	}
	*/
	
	public NetworkAddr getHoA() {
		return hoa;
	}
	
	public NetworkAddr getCoA() {
		return coa;
	}
	
}
