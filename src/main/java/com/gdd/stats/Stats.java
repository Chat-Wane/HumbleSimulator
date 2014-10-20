package com.gdd.stats;

import java.util.ArrayList;

import com.gdd.Global;
import com.gdd.peers.Peer;

public class Stats {

	// stats per peer
	private static ArrayList<StatsElement> stats = new ArrayList<StatsElement>();

	// stats over time
	public static ArrayList<Integer> lowers = new ArrayList<Integer>();
	public static ArrayList<Integer> pathLengths = new ArrayList<Integer>();

	public Stats() {
		for (int i = 0; i < Global.PEERS; ++i) {
			Stats.stats.add(new StatsElement());
		}
	}

	public static void addLower(Integer t) {
		Stats.lowers.add((Integer) t);
	}

	public static void incrementLower(Peer p) {
		Stats.stats.get(p.getS()).incrementLower();
	}

	public static int getLower(Peer p) {
		return Stats.stats.get(p.getS()).getLower();
	}

	public static void addStatsAntiEntropy(Peer p, Integer time,
			Integer optimal, Integer actual) {
		Stats.stats.get(p.getS()).addAntiEntropy(
				new StatsAntiEntropy(time, optimal, actual));
	}
}
