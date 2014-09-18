package com.gdd.histories;

import java.util.Collection;
import java.util.HashMap;

import com.gdd.messaging.Operation;
import com.gdd.peers.Peer;

public class HistoryGraph {
	// the hashmap gives quick access to the current operation of the peer by id
	private static HashMap<Integer, HistoryEdge> history = new HashMap<Integer, HistoryEdge>();
	// TO -> FROM x EDGE
	private static HashMap<Operation, HashMap<Operation, HistoryEdge>> OpToEdge = new HashMap<Operation, HashMap<Operation, HistoryEdge>>();

	private static Operation ROOTOPERATION = new Operation(-1, -1);

	/**
	 * Allows getting the head of the desired peer, i.e., its last edge
	 * 
	 * @param p
	 *            the peer to search in the graph
	 * @return the head of the peer in argument
	 */
	public static HistoryEdge getPeer(Peer p) {
		if (HistoryGraph.history.containsKey(p.getS())) {
			return HistoryGraph.history.get(p.getS());
		} else {
			return new HistoryEdge(null, ROOTOPERATION);
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
	public static void addOperation(Peer p, Operation o) {
		// #1 check if a path going to "o" does not already exist
		if (!HistoryGraph.OpToEdge.containsKey(o)) {
			// #1a if it does not exists, creates it
			HistoryGraph.OpToEdge.put(o, new HashMap<Operation, HistoryEdge>());
		}
		// #2 check if the path going to 'o' from the current location of p
		// exists
		if (!HistoryGraph.OpToEdge.get(o).containsKey(
				HistoryGraph.getPeer(p).getTo())) {
			// #2a if not, create it
			HistoryEdge he = new HistoryEdge(HistoryGraph.getPeer(p).getTo(), o);
			HistoryGraph.OpToEdge.get(he.getTo()).put(he.getFrom(), he);
		}
		// #3a add the peer p to the list of peer that went through the edge
		HistoryGraph.OpToEdge.get(o).get(HistoryGraph.getPeer(p).getTo())
				.addPeer(p);
		// #3b change the position of the head of p
		HistoryGraph.history.put(
				p.getS(),
				HistoryGraph.OpToEdge.get(o).get(
						HistoryGraph.getPeer(p).getTo()));
	}

	/**
	 * Return the set of parents of the operation in parameters
	 * 
	 * @param to
	 *            the arrival operation
	 * @return the parents of the arrival operation
	 */
	public static Collection<HistoryEdge> getParentEdges(Operation to) {
		return HistoryGraph.OpToEdge.get(to).values();
	}

	public static int getLength() {
		return HistoryGraph.OpToEdge.size();
	}
}
