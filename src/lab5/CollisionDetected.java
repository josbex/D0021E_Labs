package lab5;

import Sim.Event;
import Sim.SimEnt;

/**
 * 
 * @author
 * 
 * This class is in charge of letting the link know if a collision has occurred. When 
 * a node has received a LinkUpdate msg it will see if their packet will have caused a collision
 * and then it should send a CollisionDetected on to the link. 
 *
 */
public class CollisionDetected implements Event {

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

	public String getNameofEvent() {
		// TODO Auto-generated method stub
		return "CollisionDetected";
	}

}
