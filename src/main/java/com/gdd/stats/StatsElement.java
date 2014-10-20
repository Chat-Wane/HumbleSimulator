package com.gdd.stats;

public class StatsElement {

	private int lower = 0;

	public void incrementLower() {
		this.lower = this.lower + 1;
	}

	public int getLower() {
		return this.lower;
	}
}
