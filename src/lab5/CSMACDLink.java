package lab5;

import java.util.ArrayList;

import ANSIColors.Color;
import Sim.*;


public class CSMACDLink extends IdealLink {
	

	private int propagationDelay; // The time for a bit to propagate through the link
	private int nodesTransmitting; // Amount of nodes currently trying to transmit

	private class Transmission {
		SimEnt src;
		Frame frame;
		EventHandle frameDeliveryHandle;

		Transmission(SimEnt source, Frame ev, EventHandle frameDeliveryHandle) {
			this.src = source;
			this.frame = ev;
			this.frameDeliveryHandle = frameDeliveryHandle;
		}
	}

	private Transmission transmission;

	ArrayList<SimEnt> connectors;
	
	final private int _now = 0;

	public CSMACDLink(int propagationDelay) {
		super();
		connectors = new ArrayList<SimEnt>();
		this.propagationDelay = propagationDelay;
		this.nodesTransmitting = 0;
	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof StartTransmission) {

			Frame frame = ((StartTransmission) ev).getFrame();

			if (this.transmission == null) {
				// If transmission is null, there is nothing currently transmitting on the link
				EventHandle frameDeliveryHandle = send(this, new FrameDelivered(), this.propagationDelay + Frame.transmissionDelay);
				this.transmission = new Transmission(src, frame, frameDeliveryHandle);
			} else {
				// Collision just happened, frame is now garbage
				SimEngine.instance().deregister(this.transmission.frameDeliveryHandle);
				this.printMsg("Frames from " + this.transmission.src.identifierString() + " and " + src.identifierString() + " collided.");
				this.transmission.frame = null;
			}

			this.nodesTransmitting++;
			sendToAll(src, new LinkStatusChanged(LinkStatus.busy), this.propagationDelay);

		} else if (ev instanceof StopTransmission) {

			if ( --(this.nodesTransmitting) == 0 ) {
				sendToAll(src, new LinkStatusChanged(LinkStatus.idle), this.propagationDelay);
				this.transmission = null;
			}

		} else if (ev instanceof FrameDelivered) {

			send(this.transmission.src, ev, _now);
			sendToAll(this.transmission.src, this.transmission.frame, _now);
			this.transmission = null;

		}
	}

	/*
	 * Send to all except src
	 */
	private void sendToAll(SimEnt src, Event ev, int delayExecution) {
		for (SimEnt peer : connectors)
			if (peer != src)
				send(peer, ev, delayExecution);
	}

	@Override
	public void setConnector(SimEnt connectTo) {
		if (!connectors.contains(connectTo))
			connectors.add(connectTo);
	}

}
