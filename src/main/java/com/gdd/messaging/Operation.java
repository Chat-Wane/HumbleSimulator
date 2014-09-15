package com.gdd.messaging;

public class Operation {
	private final int s; // pointer to the origin
	private final int c; // unique identifier

	public Operation(int s, int c) {
		this.s = s;
		this.c = c;
	}

	public int getC() {
		return c;
	}

	public int getS() {
		return s;
	}
}
