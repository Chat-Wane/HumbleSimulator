package com.gdd.histories;

import java.util.HashMap;

import com.gdd.messaging.Operation;
import com.gdd.peers.Peer;

public class HistoryGraph {
	// the hashmap gives quick access to the current operation of the peer by id
	private HashMap<Integer, HistoryEdge> history = new HashMap<Integer, HistoryEdge>();
	private HashMap<Operation, HashMap<Operation, HistoryEdge>> OpToEdge = new HashMap<Operation, HashMap<Operation, HistoryEdge>>();

	public HistoryGraph() {
	}

	/**
	 * Allows getting the head of the desired peer, i.e., its last edge
	 * 
	 * @param p
	 *            the peer to search in the graph
	 * @return the head of the peer in argument
	 */
	public HistoryEdge getPeer(Peer p) {
		if (this.history.containsKey(p.getS())) {
			return this.history.get(p.getS());
		} else {
			return new HistoryEdge(null, null);
		}
	}

	/**
	 * add an operation and the peer that delivered it
	 * 
	 * @param p
	 *            the peer that delivered the operation
	 * @param o
	 *            the operation delivered
	 */
	public void addOperation(Peer p, Operation o) {
		// #1 check if a path going to "o" does not already exist
		if (!this.OpToEdge.containsKey(o)) {
			// #1a if it does not exists, creates it
			this.OpToEdge.put(o, new HashMap<Operation, HistoryEdge>());
		}
		// #2 check if the path going to 'o' from the current location of p
		// exists
		if (!this.OpToEdge.get(o).containsKey(
				this.history.get(p.getS()).getTo())) {
			// #2a if not, create it
			HistoryEdge he = new HistoryEdge(this.history.get(p).getTo(), o);
			this.OpToEdge.get(he.getTo()).put(he.getFrom(), he);
		}
		// #3a add the peer p to the list of peer that went through the edge
		this.OpToEdge.get(o).get(this.history.get(p).getTo()).addPeer(p);
		// #3b change the position of the head of p
		this.history.put(p.getS(),
				this.OpToEdge.get(o).get(this.history.get(p).getTo()));
	}
}
