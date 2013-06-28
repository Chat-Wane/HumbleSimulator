package gdd.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Generate complete graph, or clique
 */
public class GComplete implements IGraph {

	// Matrix of distance, also the matrix of graph (since complete graph)
	private HashMap<Integer, HashMap<Integer, Integer>> distances = new HashMap<Integer, HashMap<Integer, Integer>>();

	public GComplete(Set<Integer> peers, Integer distance) {
		// #1 Generate the graphs according to parameters
		Iterator<Integer> i = peers.iterator();
		while (i.hasNext()) {
			Integer peer1 = i.next();
			HashMap<Integer, Integer> idRow = new HashMap<Integer, Integer>();
			Iterator<Integer> j = peers.iterator();
			while (j.hasNext()) {
				Integer peer2 = j.next();
				if (peer1.equals(peer2)) {
					idRow.put(peer2, 0);
				} else {
					idRow.put(peer2, distance);
				}
			}
			distances.put(peer1, idRow);
		}

	}

	public HashMap<Integer, HashMap<Integer, Integer>> getDistances() {
		return distances;
	}

	public Integer getDistance(Integer peer1, Integer peer2) {
		return distances.get(peer1).get(peer2);
	}

	public HashMap<Integer, HashMap<Integer, Integer>> getGraph() {
		return distances;
	}

}
