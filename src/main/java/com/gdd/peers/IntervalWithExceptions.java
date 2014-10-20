package com.gdd.peers;

import java.util.ArrayList;

public class IntervalWithExceptions {

	private Integer rightBound = -1;
	private ArrayList<Integer> exceptions = new ArrayList<Integer>();

	public boolean contains(Integer value) {
		return value <= this.rightBound && !this.exceptions.contains(value);
	}

	/**
	 * Increase the vector if the value does not exists
	 * 
	 * @param value
	 *            the new value
	 */
	public void add(Integer value) {
		if (this.exceptions.contains(value)) {
			this.exceptions.remove((Integer) value);
		}
		if (this.rightBound < value) {
			for (Integer i = this.rightBound + 1; i < value; ++i) {
				this.exceptions.add(i);
			}
			this.rightBound = value;
		}
	}

	/**
	 * process the number of differences between the two vectors
	 * 
	 * @param iwe
	 *            the other interval with exceptions
	 * @return the number of differences
	 */
	public int diffNumber(IntervalWithExceptions iwe) {
		int sum = Math.abs(this.rightBound - iwe.rightBound);
		int sumFound = 0;
		for (int i = 0; i < this.exceptions.size(); ++i) {
			boolean found = false;
			int j = 0;
			while (!found && j < iwe.exceptions.size()) {
				if (this.exceptions.get(i) == iwe.exceptions.get(j)) {
					found = true;
				}
				++j;
			}
			if (!found) {
				++sum;
			} else {
				++sumFound;
			}
		}
		sum += iwe.exceptions.size() - sumFound;
		return sum;
	}
}
