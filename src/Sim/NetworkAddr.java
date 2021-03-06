package Sim;

// This class represent the network address, it consist of a network identity
// "_networkId" represented as an integer (if you want to link this to IP number it can be
// compared to the network part of the IP address like 132.17.9.0). Then _nodeId represent
// the host part.

public class NetworkAddr {
	private int _networkId;
	private int _nodeId;

	public NetworkAddr(int network, int node) {
		_networkId = network;
		_nodeId = node;
	}

	public int networkId() {
		return _networkId;
	}

	public int nodeId() {
		return _nodeId;
	}
	
	@Override
	public boolean equals(Object o) {
		NetworkAddr temp = (NetworkAddr)o;
		return (this._networkId == temp._networkId) && (this._nodeId == temp._nodeId);
	}
	
	@Override
	public int hashCode() {
		int hashcode = 23;
		hashcode = (hashcode * 37) + _networkId;
		hashcode = (hashcode * 37) + _nodeId;
		return hashcode;
	}
	
	public String toString() {
		return _networkId + "." + _nodeId;
	}
	
	public NetworkAddr getNetworkAddr() {
		return new NetworkAddr(this._networkId, 0);
	}

}
