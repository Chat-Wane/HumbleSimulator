package com.gdd.peers;

import java.util.ArrayList;

import com.gdd.Global;

public class Stats {

	private static ArrayList<StatsElement> stats = new ArrayList<StatsElement>();

	public Stats() {
		for (int i = 0; i < Global.PEERS; ++i) {
			Stats.stats.add(new StatsElement());
		}
	}

	public static void incrementLower(Peer p) {
		Stats.stats.get(p.getS()).incrementLower();
	}

	public static int getLower(Peer p) {
		return Stats.stats.get(p.getS()).getLower();
	}
}
