package com.gdd.messaging;

import java.util.ArrayList;

import com.gdd.vectors.PlausibleVector;

public class Messages {

	private static ArrayList<MessagesElement> library = new ArrayList<MessagesElement>();

	/**
	 * Add an entry to the library of operations
	 * 
	 * @param v
	 *            the vector associated with the operation
	 */
	public static void addOperation(Operation o, PlausibleVector v, int t) {
		Messages.library.add(o.getC(), new MessagesElement(o, v, t));
	}

	/**
	 * Return the plausible vector associated to the operation
	 * 
	 * @param o
	 *            the operation
	 * @return the corresponding plausible vector of the operation
	 */
	public static PlausibleVector getPlausibleVector(Operation o) {
		return Messages.library.get(o.getC()).getV();
	}

	/**
	 * Return the operation at the designated index, normally, o.getC is equal
	 * to index
	 * 
	 * @param index
	 *            the index of the operation
	 * @return the operation
	 */
	public static Operation getOperation(int index) {
		return Messages.library.get(index).getO();
	}

	/**
	 * Return the creation date of the operation
	 * 
	 * @param o
	 *            the operation
	 */
	public static int getCreationTime(Operation o) {
		return Messages.library.get(o.getC()).getT();
	}

	public static int size() {
		return Messages.library.size();
	}

}
