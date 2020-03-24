package lab5;

import ANSIColors.Color;
import Sim.*;

import java.util.Random;

public class CSMACDNode extends Node {

	private final int NOW = 0;

	private Frame currentFrame; // Current frame either queued or in transmission

	private LinkStatus linkStatus; // If link is idle or busy
	private boolean isTransmitting;
	private boolean backingOff;

	private int collisionCounter; // Times current frame has collided
	private int maxBackoffExponent; // Maximum collisions when calculating exponential backoff
	private int maxCollisions; // Maximum amount of collisions
	private int droppedFrames; // Amount of dropped frames

	private NetworkAddr toNode;
	private Random r;

	private Logger arrivalLogger;
	private Logger collisionLogger;


	public CSMACDNode(int network, int node) {
		super(network, node);
		this.linkStatus = LinkStatus.idle;
		this.isTransmitting = false;
		this.backingOff = false;
		this.collisionCounter = 0;
		this.droppedFrames = 0;

		this.arrivalLogger = new Logger("NODE" + network + "_" + node + ".log");
		this.collisionLogger = new Logger("NODE" + network + "_" + node + "_Collisions.log");
		r = new Random();
	}

	public void StartSending(NetworkAddr toNode, int number, int timeInterval, int startSeq, int maxBackoffExponent, int maxCollisions) {
		this._stopSendingAfter = number;
		this.toNode = toNode;
		this._timeBetweenSending = timeInterval;
		this._seq = startSeq;
		this.maxBackoffExponent = maxBackoffExponent;
		this.maxCollisions = maxCollisions;
		send(this, new TimerEvent(), this._timeBetweenSending);
	}

	@Override
	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {

			// Constructing new frame
			_sentmsg++;
			this.currentFrame = new Frame(_id, toNode, _seq++);
			// If link is idle, send immediately
			if (this.linkStatus.isIdle())
				this.startTransmitting();

		} else if (ev instanceof BackoffTimerEvent) {

			this.backingOff = false;
			// If link is idle, send immediately
			if (this.linkStatus.isIdle())
				this.startTransmitting();

		} else if (ev instanceof FrameDelivered) {

			// Frame has now been successfully transmitted
			this.arrivalLogger.log("" + (int) SimEngine.getTime() + ", " + this.currentFrame.seq());

			this.stopTransmitting();
			this.collisionCounter = 0;
			this.currentFrame = null;

			if (this._sentmsg < this._stopSendingAfter)
				send(this, new TimerEvent(), this._timeBetweenSending);
			else
				this.printMsg(Color.blue("Successfully sent all frames"));


		} else if (ev instanceof LinkStatusChanged) {

			this.linkStatus = ((LinkStatusChanged) ev).getLinkStatus();

			if (this.isTransmitting && this.linkStatus.isBusy()) {
				// Collision detected, stop transmission and start backoff timer
				this.collisionCounter++;
				this.collisionLogger.log("" + (int) SimEngine.getTime() + ", "  + this.collisionCounter);

				this.stopTransmitting();

				if (this.collisionCounter < this.maxCollisions) {
					int backoff = exponentialBackoff();
					this.backingOff = true;

					send(this, new BackoffTimerEvent(), backoff);

					this.printMsg(
							Color.yellow("COLLISION DETECTED") +
									" no " + this.collisionCounter +
									": seq " + this.currentFrame.seq() +
									", will retry after " + backoff +
									" (" + ((int) SimEngine.getTime() + backoff) + ")"
					);
				} else {
					// Transmission of frame failed
					this.collisionCounter = 0;
					this.droppedFrames++;
					send(this, new TimerEvent(), _timeBetweenSending);
					this.printMsg(Color.red("DISCARDED ") + "frame " + this.currentFrame.seq());
					this.currentFrame = null;
				}

			} else if (this.linkStatus.isIdle() && this.currentFrame != null && !this.backingOff) {
				// Link now seems idle and we have a frame queued up
				this.startTransmitting();
			}

		} else if (ev instanceof Frame) {

			// A frame has been successfully transmitted from somewhere
			Frame frame = (Frame) ev;
			if (frame.destination() == _id)
				this.printMsg(Color.green("RECV ") + "from " + frame.source().toString() + " seq " + frame.seq());

		}
	}

	private void stopTransmitting() {
		this.isTransmitting = false;
		send(_peer, new StopTransmission(), NOW);
	}

	private void startTransmitting() {
		if (this.currentFrame == null) {
			this.printMsg(Color.red("ERROR") + ": Tried to transmit null frame");
			return;
		}

		this.isTransmitting = true;
		send(_peer, new StartTransmission(this.currentFrame), NOW);
		this.printMsg("Sends frame seq " + this.currentFrame.seq());
	}

	private int exponentialBackoff() {
		//return r.nextInt( (int) Math.pow(2, Math.min(this.collisionCounter, this.backoffMax)) ) * (Frame.transmissionDelay * 2);
		return r.nextInt( (int) Math.pow(2, Math.min(this.collisionCounter, this.maxBackoffExponent)) ) * (((CSMACDLink) _peer).propagationDelay * 2);
	}

	public int getDroppedFrames() {
		return this.droppedFrames;
	}

}
