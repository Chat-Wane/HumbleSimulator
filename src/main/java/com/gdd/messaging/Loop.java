package com.gdd.messaging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;

import com.gdd.Global;
import com.gdd.histories.HistoryEdge;
import com.gdd.histories.HistoryGraph;
import com.gdd.peers.Peer;
import com.gdd.peers.Peers;
import com.gdd.peers.Stats;
import com.gdd.vectors.PlausibleVector;
import com.gdd.vectors.Vectors;
import com.gdd.visualization.VisualizationGraph;

/**
 * Decompose the cycle of creation and execution of operations until the export
 */
public class Loop {

	private final static Random rng = new Random(Global.SEED);
	private ArrayList<Integer> creationTime = new ArrayList<Integer>();
	private final static Normal normal = new Normal(Global.TOTALTIME / 2,
			Global.TOTALTIME / 2 * 0.33, new DRand((int) Global.SEED));

	/**
	 * function executed before the execution loop is started. It initializes
	 * the objects required.
	 */
	public void before() {
		// #0 create the creation time of all the operations
		for (int i = 0; i < Global.OPERATIONS; ++i) {
			Integer random = normal.nextInt();
			while (random < 0 || random > Global.TOTALTIME) {
				random = normal.nextInt();
			}
			this.creationTime.add(random); // rng.nextInt(Global.TOTALTIME));
		}
		Collections.sort(this.creationTime);

		// #1 create the peers and initialize their vectors
		new Peers();
		new Vectors();

		// #2 create the network
		new Network();

		// #3 create the history of operation and buffers of messages
		new HistoryGraph();
		new Buffers();

		// #4 some stats glean during execution
		new Stats();

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
				Buffers.addBufferedOperations(p, messages);

				// #0c deliver the messages that are ready
				ArrayList<Integer> buffer = Buffers.getBuffer(p);
				int j = 0;
				while (j < buffer.size()) {
					Operation o = Messages.getOperation(buffer.get(j));
					if (Vectors.getVector(p).isLeq( // ( for the stats only
							Messages.getPlausibleVector(o))) {
						Stats.incrementLower(p);
						Stats.addLower(currentTime);
					}
					// #0d if ready, deliver and remove it from the buffer
					if (Vectors.getVector(p).isReady(
							Messages.getPlausibleVector(o))
							|| Vectors.getVector(p).isLeq(
									Messages.getPlausibleVector(o))) {
						HistoryGraph.addOperation(p, o);
						p.deliver(o);
						Vectors.getVector(p.getS()).incrementFrom(
								Messages.getPlausibleVector(o));
						Buffers.removeBufferedOperation(p, o);
						j = 0; // restart examining messages
					} else {
						++j;
					}
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

		// #1 measuring size of the history for each peer
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

			int sum = 0;
			for (int j = 0; j < Global.R; ++j) {
				sum += Vectors.getVector(i).get(j);
			}

			System.out.println(i + " : [Sum " + sum + "][Low "
					+ Stats.getLower(Peers.getPeer(i)) + "][Max "
					+ HistoryGraph.getPeer(Peers.getPeer(i)).getTo().getC()
					+ "][PTH " + pathLength + "] "
					+ Arrays.toString(Vectors.getVector(i).v));
		}

		// #2 export the history to a .dot file
		VisualizationGraph vg = new VisualizationGraph();
		vg.export();

		// #3 export the creation of operations
		PrintWriter writer;
		try {
			writer = new PrintWriter("operations.txt", "UTF-8");
			Iterator<Integer> iCreationTime = creationTime.iterator();
			int sum = 0;
			Integer previous = 0;
			while (iCreationTime.hasNext()) {
				Integer creation = iCreationTime.next();
				// if (!creation.equals(previous)) {
				// writer.println(previous + " " + sum);
				// sum = 1;
				// previous = creation;
				// } else {
				// sum += 1;
				// }
				writer.println(creation);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			writer = new PrintWriter("lowers.txt", "UTF-8");
			Iterator<Integer> iLowers = Stats.lowers.iterator();
			int sum = 0;
			Integer previous = 0;
			while (iLowers.hasNext()) {
				Integer creation = iLowers.next();
				writer.println(creation);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("AVG SIZE OF PATHS= " + Network.getAvgDistance());

		if (Stats.pathLengths.size() > 0) {
			int sum = 0;
			int max = 0;
			for (int i = 0; i < Stats.pathLengths.size(); ++i) {
				sum += Stats.pathLengths.get(i);
				max = Math.max(max, Stats.pathLengths.get(i));
			}
			System.out.println("AVG UPPER SIZE OF RECOVERY= " + sum
					/ (float) Stats.pathLengths.size());
			System.out.println("MAX UPPER SIZE OF RECOVERY= " + max);
		}

		try {
			writer = new PrintWriter("paths.txt", "UTF-8");
			for (int i = 0; i < Stats.pathLengths.size(); ++i) {
				writer.println(Stats.pathLengths.get(i));
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
