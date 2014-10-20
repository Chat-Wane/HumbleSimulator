package com.gdd.stats;

import java.util.ArrayList;

public class StatsElement {

	private int lower = 0;

	private ArrayList<StatsAntiEntropy> antientropies = new ArrayList<StatsAntiEntropy>();

	public void incrementLower() {
		this.lower = this.lower + 1;
	}

	public int getLower() {
		return this.lower;
	}

	public void addAntiEntropy(StatsAntiEntropy sae) {
		this.antientropies.add(sae);
	}
}
