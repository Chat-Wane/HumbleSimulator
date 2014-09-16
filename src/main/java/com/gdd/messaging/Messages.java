package com.gdd.messaging;

import java.util.HashMap;

import com.gdd.vectors.PlausibleVector;

public class Messages {

	private static HashMap<Operation, PlausibleVector> library = new HashMap<Operation, PlausibleVector>();

	/**
	 * Add an entry to the library of operations
	 * 
	 * @param o
	 *            the unique operation
	 * @param v
	 *            the vector associated with the operation
	 */
	public void addOperation(Operation o, PlausibleVector v) {
		Messages.library.put(o, v);
	}

	/**
	 * Return the plausible vector associated to the operation
	 * 
	 * @param o
	 *            the operation
	 * @return the corresponding plausible vector of the operation
	 */
	public PlausibleVector getPlausibleVector(Operation o) {
		return Messages.library.get(o);
	}

}
