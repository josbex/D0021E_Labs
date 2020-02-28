package lab4;

import ANSIColors.Color;
import Sim.Event;
import Sim.IdealLink;
import Sim.Message;
import Sim.NetworkAddr;
import Sim.Node;
import Sim.Router;
import Sim.SimEngine;
import Sim.SimEnt;
import Sim.TimerEvent;
import lab3.SwitchRouterEvent;


public class MIPNode extends Node {
	
	private NetworkAddr HoA;
	private AgentRouter HA;
	private AgentRouter FA;
	int moveAfter;
	

	public MIPNode(int network, int node, AgentRouter HA, AgentRouter FA, int moveAfter) {
		super(network, node);
		this.HoA = null;
		this.HA = HA;
		this.FA = FA;
		this.moveAfter = moveAfter;
		this._identifierString = "MIPNODE " + _id.networkId() + "." + _id.nodeId();
	}
	
	public NetworkAddr getHoA(){
		return HoA;
	}
	
	public void setCoA(NetworkAddr newID){
		this.HoA = _id;
		_id = newID;
		this._identifierString = "MIPNODE " + _id.networkId() + "." + _id.nodeId();
	}
	
	
	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			if (_stopSendingAfter > _sentmsg) {
				_sentmsg++;
				if (HoA == null) {
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 0);
				} else {
					send(
						_peer,
						new WrappedMessage(
							_id,
							HA.getAddr(),
							new Message(this.HoA, new NetworkAddr(_toNetwork, _toHost), _seq)
						),
						0
					);
				}
				send(this, new TimerEvent(), _timeBetweenSending);
				this.printMsg("Sent message with seq: " + _seq + " at time " + SimEngine.getTime());
				_seq++;
				//Move to the new network after set amount of packets sent.
				//Message is sent to the new router warning it of the move.
				if(_sentmsg == moveAfter && moveAfter != 0){
					this.switchRouter(this.FA);
					send(_peer, new BindingUpdate(HoA, _id), 0);
					this.printMsg("NODE (" + HoA.toString() + ") moved to network "+ FA.getNetworkAddr() + ". Care of address is " + _id.toString());
				}
			}
		}
		if (ev instanceof WrappedMessage) {
			Message msg = ((WrappedMessage) ev).getWrapped();
			this.printMsg("Receives message with seq: " + (msg.seq() + " at time " + SimEngine.getTime()));
		}
		if (ev instanceof Message) {
			this.printMsg("Receives message with seq: " + ((Message) ev).seq() + " at time " + SimEngine.getTime());
		}
	}
	
	public void switchRouter(AgentRouter newRouter) {
		IdealLink link = new IdealLink();
		this.setPeer(link);
		this.setCoA(newRouter.getAvailableAddr());
		newRouter.connectInterface(
				newRouter.getAvailableInterface(),
				link,
				this._id
		);
	}

}
