package com.gdd.messaging;

import java.util.ArrayList;

import com.gdd.Global;
import com.gdd.peers.Peer;
import com.gdd.peers.Peers;
import com.gdd.util.NetworkParser;

public class Network {

	private static int[] maxDistance;
	private static ArrayList<ArrayList<Integer>> distanceMatrix;

	public Network() {
		Network.maxDistance = new int[Global.PEERS];
		// #1 process the network
		Network.distanceMatrix = NetworkParser.parse(Global.GRAPHFILE);
		// #2 process the maxDistance for each peer
		for (int i = 0; i < Network.distanceMatrix.size(); ++i) {
			Network.maxDistance[i] = 0;
			for (int j = 0; j < Network.distanceMatrix.get(i).size(); ++j) {
				Network.maxDistance[i] = Math.max(Network.maxDistance[i],
						Network.distanceMatrix.get(i).get(j));
			}
		}
	}

	/**
	 * Get the distance from this peer to the farthest peer
	 * 
	 * @param p
	 *            the departure peer
	 * @return the maximal distance
	 */
	public static int getMaxDistance(Peer p) {
		return Network.maxDistance[p.getS()];
	}

	/**
	 * Get the shortest distance between two peers
	 * 
	 * @param from
	 *            the departure peer
	 * @param to
	 *            the arrival peer
	 * @return
	 */
	public static int getDistance(Peer from, Peer to) {
		return Network.distanceMatrix.get(from.getS()).get(to.getS());
	}

	public static float getAvgDistance() {
		int sum = 0;
		for (int i = 0; i < Global.PEERS; ++i) {
			for (int j = 0; j < Global.PEERS; ++j) {
				if (i != j) {
					sum += Network.getDistance(Peers.getPeer(i),
							Peers.getPeer(j));
				}
			}
		}
		return (float) (sum / (Math.pow((double) Global.PEERS - 1, 2.0)));
	}
}
