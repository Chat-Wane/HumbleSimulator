package com.gdd.messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.gdd.Global;
import com.gdd.histories.HistoryEdge;
import com.gdd.histories.HistoryGraph;
import com.gdd.peers.Peer;
import com.gdd.peers.Peers;
import com.gdd.vectors.PlausibleVector;
import com.gdd.vectors.Vectors;
import com.gdd.visualization.VisualizationGraph;

/**
 * Decompose the cycle of creation and execution of operations until the export
 */
public class Loop {

	private final Random rng = new Random(Global.SEED);
	private ArrayList<Integer> creationTime = new ArrayList<Integer>();

	/**
	 * function executed before the execution loop is started. It initializes
	 * the objects required.
	 */
	public void before() {
		// #0 create the creation time of all the operations
		for (int i = 0; i < Global.OPERATIONS; ++i) {
			this.creationTime.add(rng.nextInt(Global.TOTALTIME));
		}
		Collections.sort(this.creationTime);

		// #1 create the peers and initialize their vectors
		new Peers();
		new Vectors();
		for (int i = 0; i < Peers.getPeers().size(); ++i) {
			Vectors.setVector(i, new PlausibleVector(Global.R, i % Global.R));
		}

		// #2 create the network
		new Network();

		// #3 create the history of operation
		new HistoryGraph();

	}

	/**
	 * start the execution of the main loop.
	 */
	public void execute() {
		Iterator<Integer> iCreationTime = creationTime.iterator();
		while (iCreationTime.hasNext()) {
			Integer currentTime = iCreationTime.next();
			// #0 process all the message to receive
			// #0a for each peer
			for (int i = 0; i < Peers.getPeers().size(); ++i) {
				Peer p = Peers.getPeer(i);
				// #0b get the messages received by the peer
				ArrayList<Operation> messages = ReceiveMessages.getOperations(
						p, currentTime);
				// #0c deliver the messages
				for (int j = 0; j < messages.size(); ++j) {
					// #0d (TODO) verify if the operation is ready
					HistoryGraph.addOperation(p, messages.get(j));
					p.deliver(messages.get(j));
					Vectors.getVector(p.getS()).incrementFrom(
							Messages.getPlausibleVector(messages.get(j)));
				}
			}
			// #0b remove old messages
			ReceiveMessages.garbageCollect(currentTime);

			// #1 choose a peer which will generate the operation
			Integer pId = rng.nextInt(Global.PEERS);
			// #2 increment the vector of the peer
			Vectors.getVector(pId).increment();
			// #3 put the info in the respective libraries
			Operation op = new Operation(pId, Peer.getC());
			Peer.increment();
			Peers.getPeer(pId).deliver(op);
			// #3a add the message to the messages library
			PlausibleVector pv = new PlausibleVector(Vectors.getVector(pId));
			Messages.addOperation(op, pv, currentTime);
			// #3b add the path to the history
			HistoryGraph.addOperation(Peers.getPeer(pId), op);
		}
	}

	/**
	 * function executed at the end of the execution to, for instance, export
	 * results.
	 */
	public void after() {
		for (int i = 0; i < Peers.getPeers().size(); ++i) {
			HistoryEdge he = HistoryGraph.getPeer(Peers.getPeer(i));
			int pathLength = 0;
			while (he.getFrom().getC() != -1) {
				Collection<HistoryEdge> potentialParentEdge = HistoryGraph
						.getParentEdges(he.getFrom());
				Iterator<HistoryEdge> iPotential = potentialParentEdge
						.iterator();
				while (iPotential.hasNext()) {
					HistoryEdge potential = iPotential.next();
					if (potential.getPeers().get(i)) {
						he = potential;
					}
				}
				++pathLength;
			}
			System.out.println(i + " : "
					+ HistoryGraph.getPeer(Peers.getPeer(i)).getTo().getC()
					+ Arrays.toString(Vectors.getVector(i).v) + "  "
					+ pathLength);
		}

		VisualizationGraph vg = new VisualizationGraph();
		vg.export();
	}
}
