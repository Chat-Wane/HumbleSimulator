package com.gdd.histories;

import java.util.HashMap;

import com.gdd.messaging.Operation;
import com.gdd.peers.Peer;

public class HistoryGraph {
	// the hashmap gives quick access to the current operation of the peer by id
	private HashMap<Integer, HistoryEdge> graph = new HashMap<Integer, HistoryEdge>();
	private HashMap<Operation, HistoryEdge> OpToEdge = new HashMap<Operation, HistoryEdge>();
	private HistoryEdge root;

	public HistoryGraph() {
		this.root = new HistoryEdge(new Operation(0, 0));
	}

	public HistoryEdge getRoot() {
		return root;
	}

	/**
	 * Allows getting the head of the desired peer, i.e., its last edge
	 * 
	 * @param p
	 *            the peer to search in the graph
	 * @return the head of the peer in argument
	 */
	public HistoryEdge getPeer(Peer p) {
		return this.graph.get(p.getS());
	}

	/**
	 * add the operation and its creator to the graph
	 * 
	 * @param p
	 * @param o
	 */
	public void addOperation(Peer p, Operation o) {
		// #1 check if a path going to "o" does not already exist
		// #2a if it does not exists, creates it
		// #2b add the peer p to the list of peer that went through the edge
	}

}
