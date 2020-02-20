package lab4;

import Sim.NetworkAddr;
import Sim.Node;


public class MIPNode extends Node {
	
	private NetworkAddr homeAddress;
	private HomeAgentRouter homeAgent;

	public MIPNode(int network, int node) {
		super(network, node);
		homeAddress = _id;
	}
	
	public void moveTo(HomeAgentRouter newRouter) {
		_id = new NetworkAddr(newRouter.getNetworkAddr(), _id.nodeId());
		//send(homeAgent, new BindingUpdate(_id), 0);
	}

}
