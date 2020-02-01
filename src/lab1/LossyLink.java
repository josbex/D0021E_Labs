package lab1;
import Sim.Event;
import Sim.EventHandle;
import Sim.SimEnt;
import Sim.Link;
import Sim.Message;

public class LossyLink extends Link  {
	
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	private int _now=0;
	
	private double delay, initialJitter, simulatedJitter, dropProb;
	
	public LossyLink(double delay, double jitter, double dropProb){
		this.delay = delay;
		this.initialJitter = jitter;
		this.simulatedJitter = jitter;
		this.dropProb = dropProb;
	}
	
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}
	
	public void recv(SimEnt source, Event event) {
		if (event instanceof Message)
		{
			System.out.println("Link recv msg, passes it through");
			EventHandle eh;
			if (source == _connectorA)
			{
				eh = send(_connectorB, event, calculateDelay());
			}
			else
			{
				eh = send(_connectorA, event, calculateDelay());
			}
			
			if (Math.random() >= this.dropProb) {
				eraseScheduledEvent(eh);
				System.out.println("Link recv msg, but drops it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
		}
		
	}
	
	private double calculateDelay(){
		return this.delay + this.initialJitter * (2 * Math.random() - 1);
	}

}
