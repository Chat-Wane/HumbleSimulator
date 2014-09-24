package com.gdd.vectors;

import java.util.ArrayList;

import com.gdd.Global;
import com.gdd.peers.Peer;

public class Vectors {

	// pid -> Peer's vector
	private static ArrayList<PlausibleVector> vectors = new ArrayList<PlausibleVector>();

	public Vectors() {
		for (int i = 0; i < Global.PEERS; ++i) {
			Vectors.vectors.add(new PlausibleVector(Global.R, i % Global.R));
		}
	}

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
		Vectors.vectors.set(p.getS(), v);
	}

	public static void setVector(Integer pId, PlausibleVector v) {
		Vectors.vectors.set(pId, v);
	}

}
