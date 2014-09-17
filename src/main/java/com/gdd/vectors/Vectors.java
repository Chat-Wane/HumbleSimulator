package com.gdd.vectors;

import java.util.HashMap;

import com.gdd.peers.Peer;

public class Vectors {

	private static HashMap<Integer, PlausibleVector> vectors = new HashMap<Integer, PlausibleVector>();

	/**
	 * Get the current vector of the peer
	 * 
	 * @param p
	 *            the peer
	 * @return the current vector of the peer in argument
	 */
	public static PlausibleVector getVector(Peer p) {
		return Vectors.vectors.get(p.getS());
	}

	public static PlausibleVector getVector(Integer pId) {
		return Vectors.vectors.get(pId);
	}

	/**
	 * Change the current vector of the peer in argument
	 * 
	 * @param p
	 *            the peer
	 * @param v
	 *            the new vector
	 */
	public static void setVector(Peer p, PlausibleVector v) {
		Vectors.vectors.put(p.getS(), v);
	}

	public static void setVector(Integer pId, PlausibleVector v) {
		Vectors.vectors.put(pId, v);
	}

}
