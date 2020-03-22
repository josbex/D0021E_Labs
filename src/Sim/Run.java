package Sim;
//import lab5.BroadcastRouter;
import lab5.CSMACDLink;
import lab5.CSMACDNode;
import lab5.Frame;

// An example of how to build a topology and starting the simulation engine

public class Run {

	public static void main(String[] args) {
		//Creates two links
		//Link link1 = new IdealLink();
		//Link link2 = new IdealLink();
		//Link link3 = new IdealLink();

		Frame.transmissionDelay = 300;

		Link collisionLink = new CSMACDLink(100);
		//Link Link1 = new IdealLink();
		//Link Link2 = new IdealLink();
		//Link Link3 = new IdealLink();
		
		CSMACDNode host1 = new CSMACDNode(1,1);
		CSMACDNode host2 = new CSMACDNode(1,2);
		CSMACDNode host3 = new CSMACDNode(1,3);
		CSMACDNode host4 = new CSMACDNode(1,4);
		
		
		//Link link1 = new Link();
		//Link link2 = new Link();

		//Link link1 = new LossyLink(20, 3, 0.7);
		//Link link2 = new LossyLink(20, 3, 0.7);

		// Create two end hosts that will be
		// communicating via the router
		//Node host1 = new Node(1,1);
		//Node host2 = new Node(2,1);
		
		
		//PoissonGenerator host1 = new PoissonGenerator(1,1);
		//GaussianGenerator host1 = new GaussianGenerator(1,1, 10, 5);
		//Sink host2 = new Sink(2,1);
		
		//MobileNode host1 = new MobileNode(1, 1, 2, 4);
		//MobileNode host2 = new MobileNode(2, 1, 3, 2);
		
		//AgentRouter R1 = new AgentRouter(5,1,0);
		//AgentRouter R2 = new AgentRouter(6,2,0);
		
		//Host (1.1)'s HA is set to R1 and moves to R2 after sending 3 packets. Set packet number to 0 to not move.
		//MIPNode host1 = new MIPNode(1,1,R1, R2, 3);
		//MIPNode host2 = new MIPNode(1,2,R1, R2, 0);
		

		//CBRGenrator host1 = new CBRGenrator(1, 1);
		//Sink host2 = new Sink(2, 1);

		//Connect links to hosts
		host1.setPeer(collisionLink);
		host2.setPeer(collisionLink);
		host3.setPeer(collisionLink);
		host4.setPeer(collisionLink);

		// Creates as router and connect
		// links to it. Information about
		// the host connected to the other
		// side of the link is also provided
		
		// Note. A switch is created in same way using the Switch class
		//BroadcastRouter routeNode = new BroadcastRouter(4);
		
		//Sets to 5 available interfaces in the router
		//MovableRouter routeNode = new MovableRouter(5);
		//routeNode.connectInterface(0, Link1, host1);
		//routeNode.connectInterface(1, Link2, host2);
		//routeNode.connectInterface(2, Link3, host3);
		//routeNode.connectInterface(3, collisionLink, host4);
		//routeNode.connectInterface(1, link2, host2);
		//R1.connectInterface(1, link1, host1.getAddr());
		//R1.connectInterface(2, link2, host2.getAddr());
		
		//R1.connectInterface(0, link3, R2.getAddr());
		//R2.connectInterface(0, link3, R1.getAddr());
		
		// Generate some traffic
		// host1 will send 30 messages with time interval 1 to host3. Sequence starts with number 101
		host1.StartSending(host3.getAddr(), 30, 1, 1, 10);
		
		// host2 will send 10 messages with time interval 400 to host4. Sequence starts with number 201
		host2.StartSending(host4.getAddr(), 10, 1, 101, 10);

		// host3 will send 20 messages with time interval 100 to host1. Sequence starts with number 301
		host3.StartSending(host1.getAddr(), 20, 1, 201, 10);
		

		//CBRGenerator sends 10 packets with a timeinterval of 5 seconds
		//host1.StartSending(2, 2, 10, 5); 
		
		//PoissonGenerator has runtime of 10 packets and lambda value of 5
		//host1.StartSending(2, 2, 5.0, 10);

		//Gaussian test
		//host1.StartSending(2, 2, 10);





		// Start the simulation engine and of we go!
		Thread t = new Thread(SimEngine.instance());

		t.start();
		try {
			t.join();

			//System.out.println("\n" + "---- SUMMARY ----");
			//((LossyLink) link1).printSummary();
			//((LossyLink) link2).printSummary();
		} catch (Exception e) {
			System.out.println("The motor seems to have a problem, time for service?");
		}

	}
}
