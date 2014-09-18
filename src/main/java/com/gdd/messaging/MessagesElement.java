package com.gdd.messaging;

import com.gdd.vectors.PlausibleVector;

public class MessagesElement {
	private final Operation o; // the operation
	private final PlausibleVector v; // the vector of the operation
	private final int t; // the creation date of the operation

	public MessagesElement(Operation o, PlausibleVector v, int t) {
		this.o = o;
		this.v = v;
		this.t = t;
	}

	public Operation getO() {
		return o;
	}

	public int getT() {
		return t;
	}

	public PlausibleVector getV() {
		return v;
	}

}
