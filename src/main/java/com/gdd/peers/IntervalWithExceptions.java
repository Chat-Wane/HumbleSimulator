package com.gdd.peers;

import java.util.ArrayList;

import com.gdd.messaging.Messages;
import com.gdd.messaging.Operation;

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

	/**
	 * process the number of missing operation of interval passed in argument,
	 * i.e., it ignores when iwe in argument is higher than this
	 * 
	 * @param pv
	 *            the vector that misses some operation
	 * @return the number of operations that this must transmit to the peer
	 *         holding the IWE in argument
	 */
	public int diffFromToNumber(IntervalWithExceptions iwe) {
		int sum = 0;
		int diff = this.rightBound - iwe.rightBound;
		if (diff > 0) {
			sum = diff;
		}
		for (int i = 0; i < iwe.exceptions.size(); ++i) {
			if (iwe.exceptions.get(i) < Math.min(this.rightBound,
					iwe.rightBound)
					&& (!this.exceptions.contains(iwe.exceptions.get(i)))) {
				++sum;
			}
		}
		return sum;
	}

	/**
	 * get the operations that are missing at the iwe peer
	 * 
	 * @param iwe
	 *            the interval with exception of the requesting peer
	 * @return the list of operation missing at the remote peer
	 */
	public ArrayList<Operation> antiEntropyFromTo(IntervalWithExceptions iwe) {
		ArrayList<Operation> missingOperations = new ArrayList<Operation>();
		int diff = this.rightBound - iwe.rightBound;
		if (diff > 0) {
			for (int i = this.rightBound; i > iwe.rightBound; --i) {
				missingOperations.add(Messages.getOperation(i));
			}
		}
		for (int i = 0; i < iwe.exceptions.size(); ++i) {
			if (iwe.exceptions.get(i) < Math.min(this.rightBound,
					iwe.rightBound)
					&& (!this.exceptions.contains(iwe.exceptions.get(i)))) {
				missingOperations.add(Messages.getOperation(iwe.exceptions
						.get(i)));
			}
		}
		return missingOperations;
	}

}
