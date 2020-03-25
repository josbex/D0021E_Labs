package Sim;
//import lab5.BroadcastRouter;
import lab5.CSMACDLink;
import lab5.CSMACDNode;
import lab5.Frame;
import lab5.Logger;

// An example of how to build a topology and starting the simulation engine

public class Run {

	public static void main(String[] args) {

		Frame.transmissionDelay = 300;

		Link collisionLink = new CSMACDLink(100);

		CSMACDNode host1 = new CSMACDNode(1,1);
		CSMACDNode host2 = new CSMACDNode(1,2);
		CSMACDNode host3 = new CSMACDNode(1,3);
		CSMACDNode host4 = new CSMACDNode(1,4);
		
		//Connect link to hosts
		host1.setPeer(collisionLink);
		host2.setPeer(collisionLink);
		host3.setPeer(collisionLink);
		host4.setPeer(collisionLink);

		// Simulation 1
		// All hosts will send 100 frames as fast as they can (basically no delay between frames)
		host1.StartSending(host3.getAddr(), 10000, 0, 0, 10, 16);
		host2.StartSending(host4.getAddr(), 10000, 0, 0, 10, 16);
		host3.StartSending(host1.getAddr(), 10000, 0, 0, 10, 16);
		host4.StartSending(host2.getAddr(), 10000, 0, 0, 10, 16);

		// Simulation 2
		// These hosts will send 100 messages with time interval 1000.
		/*
		host1.StartSending(host3.getAddr(), 10000, 1000, 0, 10, 16);
		host2.StartSending(host4.getAddr(), 10000, 1000, 0, 10, 16);
		// These hosts will send 50 messages with time interval 2000.
		host3.StartSending(host1.getAddr(), 5000, 2000, 0, 10, 16);
		host4.StartSending(host2.getAddr(), 5000, 2000, 0, 10, 16);
		*/

		// Start the simulation engine and of we go!
		Thread t = new Thread(SimEngine.instance());

		t.start();
		try {
			t.join();

			host1.printMsg("Total dropped frames: " + host1.getDroppedFrames());
			host2.printMsg("Total dropped frames: " + host2.getDroppedFrames());
			host3.printMsg("Total dropped frames: " + host3.getDroppedFrames());
			host4.printMsg("Total dropped frames: " + host4.getDroppedFrames());

			// Write simulation data to files.
			Logger.writeAllLoggers();

		} catch (Exception e) {
			System.out.println("The motor seems to have a problem, time for service?");
		}

	}
}
