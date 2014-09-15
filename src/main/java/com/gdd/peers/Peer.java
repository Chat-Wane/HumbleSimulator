package com.gdd.peers;

public class Peer {
	private int s; // source id
	private static int c = 0; // monotonically increasing counter. static
								// because of the sequential execution
								// that guarantees the uniqueness in itself

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
}
