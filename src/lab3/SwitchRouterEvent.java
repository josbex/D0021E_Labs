package lab3;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

public class SwitchRouterEvent implements Event {

	private int newRouterInterface;
	private NetworkAddr sourceAddr;

	public SwitchRouterEvent(NetworkAddr sourceAddr, int newRouterInterface){
		this.sourceAddr = sourceAddr;
		this.newRouterInterface = newRouterInterface;
	}
	
	public NetworkAddr getSourceAddr(){
		return sourceAddr;
	}
	
	public int getNewRouterInterface(){
		return newRouterInterface;
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
	}
	
	

}
