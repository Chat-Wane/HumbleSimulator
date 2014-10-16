package com.gdd.messaging;

import java.util.BitSet;

import com.gdd.Global;
import com.gdd.peers.Peer;

public class Operation implements Comparable<Operation> {
	private final int s; // pointer to the origin
	private final int c; // unique identifier
	private final BitSet marked; // peer x has marked the operation

	public Operation(int s, int c) {
		this.s = s;
		this.c = c;
		this.marked = new BitSet(Global.PEERS);
	}

	public int getC() {
		return c;
	}

	public int getS() {
		return s;
	}

	public int compareTo(Operation o) {
		if (this.c < o.c) {
			return -1;
		}
		if (this.c > o.c) {
			return 1;
		}
		return 1;
	}

	/**
	 * The peer sees the operation as lower or equal and marks it
	 * 
	 * @param p
	 *            the peer which marks the operation
	 */
	public void addPeer(Peer p) {
		this.marked.set(p.getS());
	}

	/**
	 * get if the peer p marked the operation
	 * 
	 * @param p
	 *            the peer
	 * @return true if p marked the operation, false otherwise
	 */
	public boolean isMarked(Peer p) {
		return this.marked.get(p.getS());
	}
}
