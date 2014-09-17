package com.gdd.messaging;

public class Operation implements Comparable<Operation> {
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

	public int compareTo(Operation o) {
		if (this.c < o.c) {
			return -1;
		}
		if (this.c > o.c) {
			return 1;
		}
		return 1;
	}
}
