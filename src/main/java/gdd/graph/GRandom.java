package gdd.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

/**
 * Generate a random directed graph
 */
public class GRandom implements IGraph {

	// Matrix of distances between too peers
	private HashMap<Integer, HashMap<Integer, Integer>> distances = new HashMap<Integer, HashMap<Integer, Integer>>();

	// Matrix of adjacent distances
	private HashMap<Integer, HashMap<Integer, Integer>> graph = new HashMap<Integer, HashMap<Integer, Integer>>();

	private final static RandomEngine re = new DRand(123456789);

	public GRandom(Set<Integer> peers, Integer avgDistance, Float pEdge) {
		Uniform uniform = new Uniform(0, 1, re);
		Normal normal = new Normal(avgDistance, 1.0, re);

		// #1 Generate the graphs according to parameters
		Iterator<Integer> iPeer1 = peers.iterator();
		while (iPeer1.hasNext()) {
			Integer peer1 = iPeer1.next();
			HashMap<Integer, Integer> iRow = new HashMap<Integer, Integer>();
			Iterator<Integer> iPeer2 = peers.iterator();
			while (iPeer2.hasNext()) {
				Integer peer2 = iPeer2.next();
				if (peer1.equals(peer2)) { // 0
					// #1a 0
					iRow.put(peer2, 0);
				} else { // random
					Float randomFloat = uniform.nextFloatFromTo(0f, 1f);
					if (randomFloat < pEdge) {
						// #1b add edge
						Integer distance = 0;
						while (distance <= 0) {
							distance = normal.nextInt();
						}
						iRow.put(peer2, distance);
					} else {
						// #1c 0 no edge
						iRow.put(peer2, 0);
					}
				}
			}
			graph.put(peer1, iRow);
		}
		// #2 Process shortest path for each (p1 to p2) and (p2 to p1)
		shortestPath();

		// #3 verify if connected graph
		boolean connected = true;
		for (Integer peer1 : distances.keySet()) {
			for (Integer peer2 : distances.keySet()) {
				if (!peer1.equals(peer2)
						&& distances.get(peer1).get(peer2)
								.equals(Integer.MAX_VALUE)) {
					connected = false;
				}
			}
		}
		if (!connected) {
			System.err
					.println("Graph generation resulted in a not connected graph.");
		}
	}

	public HashMap<Integer, HashMap<Integer, Integer>> getDistances() {
		return distances;
	}

	public Integer getDistance(Integer peer1, Integer peer2) {
		return distances.get(peer1).get(peer2);
	}

	public HashMap<Integer, HashMap<Integer, Integer>> getGraph() {
		return graph;
	}

	@SuppressWarnings("unchecked")
	private void shortestPath() {
		// #1 copy directed weighted graph
		for (Integer key : graph.keySet()) {
			distances.put(key, (HashMap<Integer, Integer>) graph.get(key)
					.clone());
		}

		// #2 set to infinite edges that are non existant except when p1==p2
		for (Integer peer1 : distances.keySet()) {
			for (Integer peer2 : distances.keySet()) {
				if (!peer1.equals(peer2)
						&& distances.get(peer1).get(peer2).equals(0)) {
					distances.get(peer1).put(peer2, Integer.MAX_VALUE);
				}
			}
		}

		// #3 process Floyd-Warshall algorithm
		for (Integer k : distances.keySet()) {
			for (Integer i : distances.keySet()) {
				for (Integer j : distances.keySet()) {
					// infinite + anything = infinite
					Integer addResult;
					if (distances.get(i).get(k).equals(Integer.MAX_VALUE)
							|| distances.get(k).get(j)
									.equals(Integer.MAX_VALUE)) {
						addResult = Integer.MAX_VALUE;
					} else {
						addResult = distances.get(i).get(k)
								+ distances.get(k).get(j);
					}
					if (addResult < distances.get(i).get(j)) {
						Integer newValue = distances.get(i).get(k)
								+ distances.get(k).get(j);
						distances.get(i).put(j, newValue);
					}
				}
			}
		}
	}
}
