package gdd.network;

import gdd.applications.IApplication;
import gdd.events.EDeliver;
import gdd.events.EReceive;
import gdd.events.ESend;
import gdd.vc.CausalityHandler;
import gdd.vc.entrychoice.IEntryChoice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

public class Peer extends Observable implements IApplication {

	private CausalityHandler causalityHandler;

	private Integer uid;
	private Integer counter;

	private HashMap<String, Msg> buffer;

	public Peer(Integer uid, IEntryChoice iec) {
		this.causalityHandler = new CausalityHandler(iec);
		this.uid = uid;
		this.counter = new Integer(0);
		this.buffer = new HashMap<String, Msg>();
	}

	public Integer getUid() {
		return uid;
	}

	public Integer getEntry() {
		return causalityHandler.getEntry();
	}

	public CausalityHandler getCausalityHandler() {
		return causalityHandler;
	}

	public void receive(Object arg) {
		String id = (String) arg;
		Msg m = Repository.get(id);
		// #1 look over buffer if any vc is ready
		Iterator<String> i = buffer.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			Msg mBuf = buffer.get(key);
			if (causalityHandler.getVc().isRdy(mBuf.getVc(), mBuf.getEntry())) {
				// #1a deliver it
				this.deliver(key, mBuf);
				// #1b remove it from buffer
				i.remove();
			} // #1c else next
		}

		// #2 see if msg in argument is deliverable
		if (causalityHandler.getVc().isRdy(m.getVc(), m.getEntry())) {
			// #2a true: deliver
			deliver(m.getUid(), m);
		} else {
			// #2b false: add to the buffer
			buffer.put(m.getUid(), m);
		}
	}

	private void deliver(String id, Msg m) {
		// #1a notify delivery
		setChanged();
		EDeliver ed = new EDeliver(uid, id);
		notifyObservers(ed);

		// #2 inc entry
		causalityHandler.inc(m.getEntry());
		setChanged();

		// #3 send message to application layer
		EReceive er = new EReceive(this.uid, id, m.getPayload());
		setChanged();
		notifyObservers(er);

		// #3b (optionnal) report any event

	}

	public void update(Observable o, Object arg) {
		// #1 receive from Scenario the receive notif'
		if (arg instanceof EReceive) {
			EReceive e = (EReceive) arg;
			receive(e.id);
			// #1b re emit for applications
			setChanged();
			notifyObservers(e);
		}

		// #2 send from Application, route it
		if (arg instanceof ESend) {
			ESend e = (ESend) arg;
			send(e.payload);
		}
	}

	public void send(Object payload) {
		// #1 inc all entries and counter
		this.counter = this.counter + 1;
		causalityHandler.inc();
		// qvvHandler.addReceivedEntries(qvvHandler.getEntries());
		// #2 create the message
		Msg m = new Msg(this.uid.toString() + ";" + this.counter.toString(),
				causalityHandler.getVc(), getEntry(), payload);
		// #2 add it in the general repo
		Repository.put(this.uid.toString() + ";" + this.counter.toString(), m);
	}
}
