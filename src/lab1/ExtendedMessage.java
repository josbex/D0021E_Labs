package lab1;

import Sim.Message;
import Sim.NetworkAddr;

import java.util.ArrayList;

public class ExtendedMessage extends Message {

	static ArrayList<ExtendedMessage> msgs = new ArrayList<ExtendedMessage>();

	//private NetworkAddr _source;
	//private NetworkAddr _destination;
	//private int _seq=0;
	public double delay;
	private double timestamp;
	private double jitter;

	public ExtendedMessage(NetworkAddr from, NetworkAddr to, int seq, double timestamp) {
		super(from, to, seq);
		this.timestamp = timestamp;
		this.delay = 0.0;
		msgs.add(this);
	}

	public static double calcJitter() {
		//ExtendedMessage last = null;
		double sum = 0.0;
		for (ExtendedMessage msg : msgs) {
			sum += msg.delay;
			//if (last == null){
			//	last = msg;
			//	continue;
			//}
			//msg.jitter =

		}
		return sum / msgs.size();
	}

	public double getAvgJitter() {
		return calcJitter();
	}

	public double getTimestamp() {
		return timestamp;
	}


}
