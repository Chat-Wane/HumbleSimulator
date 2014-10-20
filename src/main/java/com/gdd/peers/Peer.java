package com.gdd.peers;

import com.gdd.messaging.Operation;

public class Peer {
	private int s; // source id
	private static int c = 0; // monotonically increasing counter. static
								// because of the sequential execution
								// that guarantees the uniqueness in itself
	// the operations received by the peer
	private IntervalWithExceptions iwe = new IntervalWithExceptions();

	/**
	 * Constructor
	 * 
	 * @param s
	 */
	public Peer(int s) {
		this.s = s;
	}

	/**
	 * Increment the counter of the peers
	 */
	public static void increment() {
		Peer.c = Peer.c + 1;
	}

	public int getS() {
		return s;
	}

	public static int getC() {
		return c;
	}

	public IntervalWithExceptions getIwe() {
		return iwe;
	}

	public void deliver(Operation o) {
		this.iwe.add(o.getC());
	}
	
}
