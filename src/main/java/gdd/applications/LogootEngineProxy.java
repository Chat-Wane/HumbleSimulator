package gdd.applications;

import gdd.events.EReceive;
import gdd.events.ESend;
import gdd.events.MyEvent;

import java.util.ArrayList;
import java.util.Observable;

import alma.fr.logootenginecomponents.LogootEngine;
import alma.fr.logootenginecomponents.MyDelta;
import alma.fr.logootenginecomponents.MyPatch;
import alma.fr.logootenginecomponents.Replica;
import difflib.Chunk;
import difflib.Delta;
import difflib.InsertDelta;

/**
 * Make the link between the application part and the delivery from peer
 */
public class LogootEngineProxy extends Observable implements IApplication {

	private LogootEngine le;

	private boolean firstcall;

	public LogootEngineProxy(LogootEngine le) {
		this.le = le;
		this.firstcall = false;
	}

	public void update(Observable o, Object arg) {
		if (arg instanceof MyEvent && !this.firstcall) {
			MyEvent e = (MyEvent) arg;
			// #0 set replica
			Replica r = new Replica();
			r.setClock(0);
			r.setId(e.uid);
			le.setReplica(r);
			this.firstcall = true;
		}

		if (arg instanceof EReceive) {
			// #1a cast into a patch
			EReceive e = (EReceive) arg;
			// #1b deliver to the ILogootEngine
			receive(e.payload);
		}

	}

	public void send(Object arg) {
		if (!this.firstcall) {
			Integer uid = (Integer) arg;
			// #0 set replica
			Replica r = new Replica();
			r.setClock(0);
			r.setId(uid);
			le.setReplica(r);
			this.firstcall = true;
		}
		// #1 prepare the send
		ArrayList<Delta> deltas = new ArrayList<Delta>();
		ArrayList<String> insertContent = new ArrayList<String>();
		Integer insertPosition = le.getDoc().size();
		String insertString = le.getReplica().getId().toString();
		insertContent.add(insertString);
		Chunk insertChunk = new Chunk(insertPosition, insertContent);
		ArrayList<String> insertOriginal = new ArrayList<String>();
		Chunk originalChunk = new Chunk(insertPosition, insertOriginal);
		Delta d = new InsertDelta(originalChunk, insertChunk);
		deltas.add(d);

		MyPatch patch = le.generatePatch(deltas);
		le.deliver(patch);
		
		// #2 inform peer layer
		Object payload = patch;
		Integer uid = null;
		String id = null;
		ESend es = new ESend(uid, id, payload);
		setChanged();
		notifyObservers(es);
	}

	public void receive(Object payload) {
		// #1 integrate new patch
		@SuppressWarnings("unchecked")
		ArrayList<MyDelta> deltas = (ArrayList<MyDelta>) payload;
		MyPatch patch =  new MyPatch();
		patch.addAll(deltas);
		le.deliver(patch);
	}

	public LogootEngine getLogootEngine() {
		return le;
	}

}