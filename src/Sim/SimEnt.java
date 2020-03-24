package Sim;

// All entities like, nodes, switch, router, link etc that handles events
// need to inherit from this class

import lab5.Logger;

public abstract class SimEnt {

	protected static int counter = 0;

	private static Logger logger = new Logger("sim_output.log");
	private static int msgCount = 0;

	private static void newMessage(String formattedMsg) {
		SimEnt.logger.log(formattedMsg);
		if (++msgCount > 100) // Hardcoded value
			SimEnt.logger.writeLog();
	}

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
		String formattedMsg = String.format("[%08d][%10s]: %s", (int) SimEngine.getTime(), this._identifierString, msg);
		System.out.println(formattedMsg);
		SimEnt.newMessage(formattedMsg);
	}

	public String identifierString() {
		return _identifierString;
	}

}
