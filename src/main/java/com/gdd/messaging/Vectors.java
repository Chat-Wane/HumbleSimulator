package com.gdd.messaging;

import java.util.HashMap;

import com.gdd.peers.Peer;
import com.gdd.vectors.PlausibleVector;

public class Vectors {

	private static HashMap<Integer, PlausibleVector> vectors = new HashMap<Integer, PlausibleVector>();

	/**
	 * Get the current vector of the peer
	 * 
	 * @param p
	 *            the peer
	 * @return the current vector of the peer in argument
	 */
	public PlausibleVector getVector(Peer p) {
		return Vectors.vectors.get(p.getS());
	}

	/**
	 * Change the current vector of the peer in argument
	 * @param p the peer
	 * @param v the new vector
	 */
	public void setVector(Peer p, PlausibleVector v) {
		Vectors.vectors.put(p.getS(), v);
	}

}
