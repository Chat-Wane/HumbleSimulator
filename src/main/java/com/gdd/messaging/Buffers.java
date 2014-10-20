package com.gdd.messaging;

import java.util.ArrayList;

import com.gdd.Global;
import com.gdd.peers.Peer;

public class Buffers {
	// peerID -> List< opID >
	private static ArrayList<ArrayList<Integer>> buffers = new ArrayList<ArrayList<Integer>>();

	public Buffers() {
		for (Integer i = 0; i < Global.PEERS; ++i) {
			Buffers.buffers.add(new ArrayList<Integer>());
		}
	}

	/**
	 * Get the list of operations that were not ready to be integrated at the
	 * peer yet
	 * 
	 * @param p
	 *            the peer
	 * @return a list of integer which are the unique id of operations in the
	 *         buffer
	 */
	public static ArrayList<Integer> getBuffer(Peer p) {
		return Buffers.buffers.get(p.getS());
	}

	/**
	 * Add an operation that is received but not ready to be integrated yet
	 * 
	 * @param p
	 *            the peer which delays the operation
	 * @param o
	 *            the operation to delay
	 */
	public static void addBufferedOperation(Peer p, Operation o) {
		Buffers.buffers.get(p.getS()).add(o.getC());
	}

	/**
	 * Add the list of operations to the peer's buffer
	 * 
	 * @param p
	 *            the peer which add the list of operations
	 * @param operations
	 *            the operations to add to the buffer
	 */
	public static void addBufferedOperations(Peer p,
			ArrayList<Operation> operations) {
		for (int i = 0; i < operations.size(); ++i) {
			if (!Buffers.buffers.get(p.getS()).contains(
					operations.get(i).getC())) {
				Buffers.buffers.get(p.getS()).add(operations.get(i).getC());
			}
		}
	}

	/**
	 * Remove the operation from the peer's buffer
	 * 
	 * @param p
	 *            the peer which removes the operation
	 * @param o
	 *            the operation to remove
	 */
	public static void removeBufferedOperation(Peer p, Operation o) {
		Buffers.buffers.get(p.getS()).remove((Integer) o.getC());
	}

}
