package com.gdd.messaging;

import java.util.ArrayList;

import com.gdd.peers.Peer;
import com.gdd.peers.Peers;

public class ReceiveMessages {
	private static int minIndex = 0; // before this index, messages are
										// delivered to every peers

	/**
	 * Return the list of operation that are ready to be delivered by p
	 * 
	 * @param p
	 *            the peer
	 * @param t
	 *            the current time of the main loop
	 * @return the list of operation to deliver
	 */
	public static ArrayList<Operation> getOperations(Peer p, int t) {
		int index = ReceiveMessages.minIndex;
		ArrayList<Operation> result = new ArrayList<Operation>();
		while (index < Messages.size()) {
			Operation o = Messages.getOperation(index);
			if (Messages.getCreationTime(o)
					+ Network.getDistance(Peers.getPeer(o.getS()), p) <= t
					&& !p.getIwe().contains(o.getC())) {
				result.add(o);
			}
			++index;
		}
		return result;
	}

	/**
	 * Remove operations which are supposedly delivered to all peers
	 * 
	 * @param t
	 *            the current time of the main loop
	 */
	public static void garbageCollect(int t) {
		while (ReceiveMessages.minIndex < Messages.size()
				&& Messages.getCreationTime(Messages
						.getOperation(ReceiveMessages.minIndex))
						+ Network
								.getMaxDistance(Peers.getPeer(Messages
										.getOperation(ReceiveMessages.minIndex)
										.getS())) <= t) {
			ReceiveMessages.minIndex = ReceiveMessages.minIndex + 1;
		}
	}
}
