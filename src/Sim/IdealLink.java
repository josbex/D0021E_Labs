package Sim;

// This class implements a link without any loss, jitter or delay

import lab3.SwitchRouterEvent;
import lab4.BindingUpdate;

public class IdealLink extends Link {
	final private int _now = 0;

	public IdealLink() {
		super();
	}

	// Called when a message enters the link

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof Message || ev instanceof SwitchRouterEvent || ev instanceof BindingUpdate) {
			this.printMsg("Link recv msg, passes it through");
			if (src == _connectorA) {
				send(_connectorB, ev, _now);
			} else {
				send(_connectorA, ev, _now);
			}
		}
	}


}