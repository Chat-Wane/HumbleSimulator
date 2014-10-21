package com.gdd.histories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.gdd.messaging.Messages;
import com.gdd.messaging.Operation;
import com.gdd.peers.Peer;
import com.gdd.stats.Stats;
import com.gdd.vectors.PlausibleVector;
import com.gdd.vectors.Vectors;

public class HistoryGraph {
	// the hashmap gives quick access to the current operation of the peer by id
	private static HashMap<Integer, HistoryEdge> history = new HashMap<Integer, HistoryEdge>();
	// TO -> FROM x EDGE
	private static HashMap<Operation, HashMap<Operation, HistoryEdge>> opToEdge = new HashMap<Operation, HashMap<Operation, HistoryEdge>>();

	private static Operation ROOTOPERATION = new Operation(-1, -1);

	/**
	 * Allows getting the head of the desired peer, i.e., its last edge
	 * 
	 * @param p
	 *            the peer to search in the graph
	 * @return the head of the peer in argument
	 */
	public static HistoryEdge getPeer(Peer p) {
		if (HistoryGraph.history.containsKey(p.getS())) {
			return HistoryGraph.history.get(p.getS());
		} else {
			return new HistoryEdge(null, ROOTOPERATION);
		}
	}

	private static void addLowerOperation(Peer p, Operation o) {
		// #1 if lower, go backward to find the proper location in the
		// history
		o.addPeer(p);
		HistoryEdge he = HistoryGraph.getPeer(p);
		PlausibleVector pv = new PlausibleVector(Vectors.getVector(p));
		PlausibleVector addedOppv = Messages.getPlausibleVector(o);
		int pathLength = 0;
		boolean found = false;

		while (he.getFrom().getC() != -1 && !found) {
			++pathLength;
			// #2 rebuild the vector as it was before the previous operation
			Operation lastOperation = Messages.getOperation(he.getTo().getC());
			PlausibleVector oppv = Messages.getPlausibleVector(lastOperation);
			// copy
			if (!lastOperation.isMarked(p)) {
				pv.decrementFrom(oppv);
			}

			// #3 if the plausible vector is lower now, backward iteration
			// must be stopped
			if (!pv.isLeq(addedOppv)) {
				found = true;
			} else {
				// #4 find the previous history edge used by peer p
				Collection<HistoryEdge> potentialParentEdge = HistoryGraph
						.getParentEdges(he.getFrom());
				Iterator<HistoryEdge> iPotential = potentialParentEdge
						.iterator();
				while (iPotential.hasNext()) {
					HistoryEdge potential = iPotential.next();
					if (potential.getPeers().get(p.getS())) {
						he = potential;
					}
				}
			}
		}

		// #5 add the operation in the found place
		he.delPeer(p);
		if (!HistoryGraph.opToEdge.get(o).containsKey(he.getFrom())) {
			HistoryGraph.opToEdge.get(o).put(he.getFrom(),
					new HistoryEdge(he.getFrom(), o));
		}
		if (!HistoryGraph.opToEdge.get(he.getTo()).containsKey(o)) {
			HistoryGraph.opToEdge.get(he.getTo()).put(o,
					new HistoryEdge(o, he.getTo()));
		}
		HistoryGraph.opToEdge.get(he.getTo()).get(o).addPeer(p);
		HistoryGraph.opToEdge.get(o).get(he.getFrom()).addPeer(p);
		// #6 notify the statistic table
		Stats.pathLengths.add((Integer) pathLength);
	}

	/**
	 * add an operation and the peer that delivered it
	 * 
	 * @param p
	 *            the peer that delivered the operation
	 * @param o
	 *            the operation delivered
	 */
	public static void addOperation(Peer p, Operation o) {
		// #0 check if the operation is lower
		if (p.getS() != o.getS()
				&& Vectors.getVector(p).isLeq(Messages.getPlausibleVector(o))) {
			HistoryGraph.addLowerOperation(p, o);
		} else {
			// #1 check if a path going to "o" does not already exist
			if (!HistoryGraph.opToEdge.containsKey(o)) {
				// #1a if it does not exists, creates it
				HistoryGraph.opToEdge.put(o,
						new HashMap<Operation, HistoryEdge>());
			}
			// #2 check if the path going to 'o' from the current location of p
			// exists
			if (!HistoryGraph.opToEdge.get(o).containsKey(
					HistoryGraph.getPeer(p).getTo())) {
				// #2a if not, create it
				HistoryEdge he = new HistoryEdge(HistoryGraph.getPeer(p)
						.getTo(), o);
				HistoryGraph.opToEdge.get(he.getTo()).put(he.getFrom(), he);
			}
			// #3a add the peer p to the list of peer that went through the edge
			HistoryGraph.opToEdge.get(o).get(HistoryGraph.getPeer(p).getTo())
					.addPeer(p);
			// #3b change the position of the head of p
			HistoryGraph.history.put(p.getS(), HistoryGraph.opToEdge.get(o)
					.get(HistoryGraph.getPeer(p).getTo()));
		}
	}

	/**
	 * Return the set of parents of the operation in parameters
	 * 
	 * @param to
	 *            the arrival operation
	 * @return the parents of the arrival operation
	 */
	public static Collection<HistoryEdge> getParentEdges(Operation to) {
		return HistoryGraph.opToEdge.get(to).values();
	}

	public static int getLength() {
		return HistoryGraph.opToEdge.size();
	}
}
