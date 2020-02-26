package lab4;

import Sim.Event;
import Sim.SimEnt;


public class BindingUpdate implements Event {
	
	private AgentRouter HA;

	public BindingUpdate(AgentRouter homeAgent) {
		this.HA = homeAgent;

	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "Binding Update Event";
	}
	
	public AgentRouter getHA() {
		return this.HA;
	}
	
	
}
