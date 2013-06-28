package gdd.applications;

import gdd.events.ESend;

import java.util.Observable;

/**
 * Make the link between the application part and the delivery from peer Simple
 * relay the send message to layer of peer
 */
public class DefaultApp extends Observable implements IApplication {

	public DefaultApp() {
	}

	public void update(Observable o, Object arg) {
		// Nothing
	}

	public void send(Object arg) {
		// #1 prepare the send
		Object payload = null;
		Integer uid = null;
		String id = null;
		// #2 inform peer layer
		ESend es = new ESend(uid, id, payload);
		setChanged();
		notifyObservers(es);
	}

	public void receive(Object payload) {
		// nothing
	}

}