package lab5;

import java.util.HashMap;
import java.util.Map;

import ANSIColors.Color;
import Sim.*;


public class CSMACDLink extends IdealLink {

	private static enum ConnectorStatus {
		idle,
		transmitting;
	}

	private static class Transmission {
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

	public int propagationDelay; // The time for a bit to propagate through the link

	HashMap<SimEnt, ConnectorStatus> connectors;
	
	final private int _now = 0;

	public CSMACDLink(int propagationDelay) {
		super();
		connectors = new HashMap<SimEnt, ConnectorStatus>();
		this.propagationDelay = propagationDelay;
	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof StartTransmission) {

			if (this.transmission == null) {
				// If transmission is null, there is nothing currently transmitting on the link
				EventHandle frameDeliveryHandle = send(this, new FrameDelivered(), this.propagationDelay + Frame.transmissionDelay);
				this.transmission = new Transmission(src, ((StartTransmission) ev).getFrame(), frameDeliveryHandle);
			} else {
				// Collision just happened, frame is now garbage
				SimEngine.instance().deregister(this.transmission.frameDeliveryHandle);
				this.printMsg("Frames from " + this.transmission.src.identifierString() + " and " + src.identifierString() + " collided.");
				this.transmission.frame = null;
			}

			this.setConnectorStatus(src, ConnectorStatus.transmitting);

		} else if (ev instanceof StopTransmission) {

			this.setConnectorStatus(src, ConnectorStatus.idle);

		} else if (ev instanceof FrameDelivered) {

			send(this.transmission.src, ev, _now);
			sendToAll(this.transmission.src, this.transmission.frame, _now);
			this.transmission = null;

		}
	}

	private void setConnectorStatus(SimEnt src, ConnectorStatus status) {
		this.connectors.put(src, status);

		if (status == ConnectorStatus.transmitting) {
			// A node started transmitting, send initial signal to other nodes
			sendToAll(src, new LinkStatusChanged(LinkStatus.busy), this.propagationDelay);
		} else {
			// A node stopped transmitting
			SimEnt otherTransmittingNode = null;
			for (Map.Entry<SimEnt, ConnectorStatus> e : this.connectors.entrySet()) {
				if (e.getValue() == ConnectorStatus.transmitting && otherTransmittingNode == null)
					// Found a transmitting node
					otherTransmittingNode = e.getKey();
				else if (e.getValue() == ConnectorStatus.transmitting)
					// At least two nodes are still transmitting, no change in link status
					return;
			}

			if (otherTransmittingNode != null) {
				// Only one node is now transmitting, we can tell it that the link is idle.
				send(otherTransmittingNode, new LinkStatusChanged(LinkStatus.idle), this.propagationDelay);
			} else {
				// Last node stopped transmission, tell all other nodes that link is idle.
				sendToAll(src, new LinkStatusChanged(LinkStatus.idle), this.propagationDelay);
				this.transmission = null;
			}
		}
	}

	/*
	 * Send to all except src
	 */
	private void sendToAll(SimEnt src, Event ev, int delayExecution) {
		for (SimEnt peer : connectors.keySet())
			if (peer != src)
				send(peer, ev, delayExecution);
	}

	@Override
	public void setConnector(SimEnt connectTo) {
		if (!connectors.containsKey(connectTo))
			connectors.put(connectTo, ConnectorStatus.idle);
	}

}
