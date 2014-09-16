package com.gdd.messaging;

import com.gdd.Global;
import com.gdd.peers.Peer;

public class Network {

	private static int[] maxDistance;

	public Network() {
		Network.maxDistance = new int[Global.PEERS];
		// #1 process the network
		// #2 process the maxDistance for each peer
	}

	/**
	 * Get the distance from this peer to the farthest peer
	 * 
	 * @param p
	 *            the departure peer
	 * @return the maximal distance
	 */
	public int getMaxDistance(Peer p) {
		return Network.maxDistance[p.getS()];
	}

}
