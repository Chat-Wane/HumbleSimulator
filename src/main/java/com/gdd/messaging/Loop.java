package com.gdd.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.gdd.Global;
import com.gdd.histories.HistoryGraph;
import com.gdd.peers.Peer;
import com.gdd.peers.Peers;
import com.gdd.vectors.PlausibleVector;
import com.gdd.vectors.Vectors;

/**
 * Decompose the cycle of creation and execution of operations until the export
 */
public class Loop {

	private final Random rng = new Random(Global.SEED);
	private ArrayList<Integer> creationTime = new ArrayList<Integer>();

	/**
	 * function executed before the execution loop is started. It initializes
	 * the objects required.
	 */
	public void before() {
		for (int i = 0; i < Global.OPERATIONS; ++i) {
			this.creationTime.add(rng.nextInt(Global.TOTALTIME));
		}
		Collections.sort(this.creationTime);
	}

	/**
	 * start the execution of the main loop.
	 */
	public void execute() {
		Iterator<Integer> iCreationTime = creationTime.iterator();
		while (iCreationTime.hasNext()) {
			Integer currentTime = iCreationTime.next();
			// #0 process all the message to receive
			// #0a deliver the message
			for (int i = 0; i < Peers.getPeers().size(); ++i) {
				Peer p = Peers.getPeer(i);
				ArrayList<Operation> messages = ReceiveMessages.getOperations(
						p, currentTime);
				for (int j = 0; j < messages.size(); ++j) {
					p.deliver(messages.get(j));
				}
			}
			// #0b remove old messages
			ReceiveMessages.garbageCollect(currentTime);

			// #1 choose a peer which will generate the operation
			Integer pId = rng.nextInt(Global.PEERS);
			// #2 increment the vector of the peer
			Vectors.getVector(pId).increment();
			// #3 put the info in the respective libraries
			Peer.increment();
			Operation op = new Operation(pId, Peer.getC());
			// #3a add the message to the messages library
			PlausibleVector pv = new PlausibleVector(Vectors.getVector(pId));
			Messages.addOperation(pv);
			// #3b add the path to the history
			HistoryGraph.addOperation(Peers.getPeer(pId), op);
		}
	}

	/**
	 * function executed at the end of the execution to, for instance, export
	 * results.
	 */
	public void after() {

	}
}
