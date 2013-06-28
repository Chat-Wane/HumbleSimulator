package gdd.graph;

import java.util.HashMap;

public interface IGraph {

	/**
	 * Get the latency graph from each pear to each peer
	 * 
	 * @return a table of size n^2 where n is the number of peers
	 */
	public HashMap<Integer, HashMap<Integer, Integer>> getDistances();

	/**
	 * The minimum distance (or latency) between peer1 and peer2
	 * 
	 * @param peer1
	 *            the uid of one peer
	 * @param peer2
	 *            the uid of another peer
	 * @return the distance between them
	 */
	public Integer getDistance(Integer peer1, Integer peer2);

	/**
	 * Weighted table representing the graph connections
	 * 
	 * @return the table size n^2 where n is the number of peers
	 */
	public HashMap<Integer, HashMap<Integer, Integer>> getGraph();

}
