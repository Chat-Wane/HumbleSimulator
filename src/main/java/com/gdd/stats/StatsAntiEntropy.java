package com.gdd.stats;

public class StatsAntiEntropy {

	private final Integer t;
	private final Integer optimal;
	private final Integer actual;

	public StatsAntiEntropy(Integer time, Integer optimal, Integer actual) {
		this.t = time;
		this.optimal = optimal;
		this.actual = actual;
	}

	public Integer getActual() {
		return actual;
	}

	public Integer getOptimal() {
		return optimal;
	}

	public Integer getT() {
		return t;
	}

}
