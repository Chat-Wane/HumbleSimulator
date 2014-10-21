package com.gdd.peers;

import java.util.ArrayList;
import java.util.Random;

import com.gdd.Global;
import com.gdd.messaging.Buffers;
import com.gdd.stats.Stats;
import com.gdd.vectors.Vectors;

public class Down {

	// list of the peers which are down
	private static ArrayList<DownElement> downPeers = new ArrayList<DownElement>();

	private static Random r = new Random(Global.SEED);

	public static void addPeer(Peer p, Integer currentTime) {
		Down.downPeers.add(new DownElement(p.getS(), currentTime));
	}

	public static Integer size() {
		return Down.downPeers.size();
	}

	/**
	 * get if the peer p is down, e.g. offline.
	 * 
	 * @param p
	 *            the peer p
	 * @return true if the peer p is not accessible, false otherwise
	 */
	public static boolean isDown(Peer p) {
		boolean found = false;
		int i = 0;
		while (!found && i < Down.downPeers.size()) {
			if (p.getS() == Down.downPeers.get(i).getUid()) {
				found = true;
			}
			++i;
		}
		return found;
	}

	/**
	 * wake up the peers that where asleep for too long and proceed to catch up
	 * 
	 * @param currentTime
	 *            the current time of the main loop (arbitrary unit)
	 */
	public static void wakeUp(Integer currentTime) {
		// #1 get if the peer must wake up
		while (Down.downPeers.size() > 0
				&& currentTime >= Down.downPeers.get(0).getT()
						+ Global.churnDuration) {
			// #2 contact A adjacent peers randomly if they are awake and
			// proceed to anti-entropy
			Integer j = 0;
			while (j < Global.A) {
				Integer uid = r.nextInt(Global.PEERS);
				if (!Down.isDown(Peers.getPeer(uid))) {
					Down.antientropy(
							Peers.getPeer(Down.downPeers.get(0).getUid()),
							Peers.getPeer(uid), currentTime);
					++j;
				}
			}
			// #3 remove the peer from the list of down peers
			Down.downPeers.remove((int) 0);
		}
	}

	public static void goToSleep(Integer currentTime) {
		while (Global.churn > (Down.downPeers.size() / (float) Global.PEERS)) {
			Integer uid = r.nextInt(Global.PEERS);
			if (!Down.isDown(Peers.getPeer(uid))) {
				Down.addPeer(Peers.getPeer(uid), currentTime);
			}
		}
	}

	/**
	 * Perform an antientropy between two peers allowing a peer to catch up with
	 * one another
	 * 
	 * @param origin
	 *            the peer that initiates the anti entropy
	 * @param receiver
	 *            the peer that receives the anti entropy request
	 * @param currentTime
	 *            the time of the main loop
	 */
	public static void antientropy(Peer origin, Peer receiver,
			Integer currentTime) {
		// #1 count the number of optimal operation to retrieved 4 Stats !
		int optimal = receiver.getIwe().diffFromToNumber(origin.getIwe());
		// #2 count the number of messages transfered from one to the other
		int actual = Vectors.getVector(receiver).diffFromToNumber(
				Vectors.getVector(origin))
				+ Stats.getLower(receiver); // (TODO) change the diff style
		Stats.addStatsAntiEntropy(origin, currentTime, optimal, actual);
		Buffers.addBufferedOperations(origin, receiver.getIwe()
				.antiEntropyFromTo(origin.getIwe()));
	}
}
