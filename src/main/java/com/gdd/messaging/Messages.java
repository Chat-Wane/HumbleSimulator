package com.gdd.messaging;

import java.util.ArrayList;

import com.gdd.vectors.PlausibleVector;

public class Messages {

	private static ArrayList<PlausibleVector> library = new ArrayList<PlausibleVector>();

	/**
	 * Add an entry to the library of operations
	 * 
	 * @param v
	 *            the vector associated with the operation
	 */
	public static void addOperation(PlausibleVector v) {
		Messages.library.add(v);
	}

	/**
	 * Return the plausible vector associated to the operation
	 * 
	 * @param o
	 *            the operation
	 * @return the corresponding plausible vector of the operation
	 */
	public static PlausibleVector getPlausibleVector(Operation o) {
		return Messages.library.get(o.getC());
	}

}
