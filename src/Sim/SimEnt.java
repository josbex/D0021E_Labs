package Sim;

// All entities like, nodes, switch, router, link etc that handles events
// need to inherit from this class

public abstract class SimEnt {

	protected static int counter = 0;

	protected String _identifierString;

	protected SimEnt() {
		this._identifierString = "SIMENT " + SimEnt.counter;
		SimEnt.counter++;
	}

	// Called when erasing an entity like node or link etc. The SimEngine is called in case
	// that de-registration of the entity is needed

	protected final void kill() {
		this.destructor();
	}

	// To be implemented in child classes if cleaning up is needed when the entity is killed

	protected void destructor() {
		// no op, can be added in child classes
	}

	// This method schedules a coming event in the SimEngine

	protected final EventHandle send(SimEnt destination, Event event, double delayExecution) {
		// this object is the registrator/source submitting the event
		return SimEngine.instance().register(this, destination, event, delayExecution);
	}


	//Erases a scheduled event from the SimEngine

	protected final void eraseScheduledEvent(EventHandle handleToEvent) {
		SimEngine.instance().deregister(handleToEvent);
	}


	// To be implemented in child classes acting on events/messages received

	public abstract void recv(SimEnt source, Event event);

	public void printMsg(String msg) {
		System.out.printf("[%06d][%10s]: %s\n", (int) SimEngine.getTime(), this._identifierString, msg);
		//System.out.println("[" + this._identifierString + " | " + SimEngine.getTime() + "]: " + msg);
	}

	public String identifierString() {
		return _identifierString;
	}

}
