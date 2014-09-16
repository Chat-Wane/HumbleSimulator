package com.gdd.histories;

import java.util.BitSet;

import com.gdd.Global;
import com.gdd.messaging.Operation;
import com.gdd.peers.Peer;

public class HistoryEdge {
	private final Operation from; // the departure node
	private final Operation to; // the arrival node
	private BitSet peers = new BitSet(Global.PEERS);

	/**
	 * Constructor
	 * 
	 * @param from
	 *            the departure node of the edge
	 * @param to
	 *            the arrival node through this edge
	 */
	public HistoryEdge(Operation from, Operation to) {
		this.from = from;
		this.to = to;
	}

	public BitSet getPeers() {
		return peers;
	}

	/**
	 * Add the peer in argument to the list of peers that travelled through this
	 * edge
	 * 
	 * @param p
	 *            the peer that used this edge
	 */
	public void addPeer(Peer p) {
		this.peers.set(p.getS());
	}

	public Operation getFrom() {
		return from;
	}

	public Operation getTo() {
		return to;
	}
}
