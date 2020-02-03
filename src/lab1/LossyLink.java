package lab1;
import Sim.*;

import ANSIColors.Color;

public class LossyLink extends Link {

	private double delay, initialJitter, simulatedJitter, dropProb;

	public LossyLink(double delay, double jitter, double dropProb) {
		this.delay = delay;
		this.initialJitter = jitter;
		this.simulatedJitter = jitter;
		this.dropProb = dropProb;
	}

	public void recv(SimEnt source, Event event) {
		if (event instanceof Message)
		{
			if (Math.random() < this.dropProb) {
				this.printMsg(Color.red("DROP ") + "msg seq: " + ((Message) event).seq());
			} else {
				this.printMsg("Recv msg, passes it through");
				if (source == _connectorA) {
					send(_connectorB, event, calculateDelay());
				} else {
					send(_connectorA, event, calculateDelay());
				}
			}
		}
		
	}
	
	private double calculateDelay(){
		return this.delay + this.initialJitter * (2 * Math.random() - 1);
	}

}
