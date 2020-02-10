package Sim;
import lab1.LossyLink;
import lab2.CBR_Genrator;
import lab2.Sink;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links
 		Link link1 = new Link();
		Link link2 = new Link();
		
 		//Link link1 = new LossyLink(20, 3, 0.7);
		//Link link2 = new LossyLink(20, 3, 0.7);
		
		// Create two end hosts that will be
		// communicating via the router
		//Node host1 = new Node(1,1);
		//Node host2 = new Node(2,1);
		
		CBR_Genrator host1 = new CBR_Genrator(1,1);
		Sink host2 = new Sink(2,1);
		
		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(2);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);
		
		// Generate some traffic
		// host1 will send 3 messages with time interval 5 to network 2, node 1. Sequence starts with number 1
		//host1.StartSending(2, 2, 100, 5, 1); 
		
		//CBR_generator sends 100 packets in the span of 10 seconds
		host1.StartSending(2, 2, 100, 10); 
		
		// host2 will send 2 messages with time interval 10 to network 1, node 1. Sequence starts with number 10
		//host2.StartSending(1, 1, 2, 10, 10); 
		
		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
	
		t.start();
		try
		{
			t.join();
			
			//System.out.println("Link 1: " + ((LossyLink) link1).averageDelay());
			//System.out.println("Link 2: " + ((LossyLink) link2).averageDelay());
		}
		catch (Exception e)
		{
			System.out.println("The motor seems to have a problem, time for service?");
		}		



	}
}
