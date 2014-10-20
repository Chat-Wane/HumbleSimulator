package com.gdd.vectors;

public class PlausibleVector {

	public int[] v; // vector
	public final int e; // entry

	public PlausibleVector(Integer r, Integer e) {
		this.v = new int[r];
		for (int i = 0; i < r; ++i) {
			this.v[i] = 0;
		}
		this.e = e;
	};

	public PlausibleVector(PlausibleVector pv) {
		this.v = new int[pv.v.length];
		System.arraycopy(pv.v, 0, this.v, 0, pv.v.length);
		this.e = pv.e;
	}

	/**
	 * access to the value contained in the vector at the given index
	 * 
	 * @param index
	 *            the index of the vector
	 * @return the value at the index
	 */
	public Integer get(Integer index) {
		return this.v[index];
	};

	/**
	 * increment the entry of the plausible vector
	 */
	public void increment() {
		this.v[this.e] = this.v[this.e] + 1;
	};

	/**
	 * increment the entry given another plausible vector supposedly ready
	 * 
	 * @param pv
	 *            the other plausible vector
	 */
	public void incrementFrom(PlausibleVector pv) {
		this.v[pv.e] = Math.max(this.v[pv.e], pv.v[pv.e]);
	};

	/**
	 * decrement the entry given another plausible vector
	 * 
	 * @param pv
	 *            the other plausible vector
	 */
	public void decrementFrom(PlausibleVector pv) {
		this.v[pv.e] = this.v[pv.e] - 1;
	}

	/**
	 * Check if the given plausible vector is ready regarding the current values
	 * of "this" vector
	 * 
	 * @param pv
	 *            the other plausible vector to check
	 * @return true if the plausible vector is ready, false otherwise
	 */
	public boolean isReady(PlausibleVector pv) {
		boolean ready = true;
		int i = 0;
		while (ready && i < this.v.length) {
			if ((i != pv.e) && (i != this.e) && (this.v[i] < pv.v[i])) {
				ready = false;
			}
			i = i + 1;
		}
		return (ready //
		&& (((this.e != pv.e) && (this.v[this.e] >= pv.v[this.e]) && ((this.v[pv.e] + 1) == pv.v[pv.e])) //
		|| ((this.e == pv.e) && ((this.v[this.e] + 1) == pv.v[this.e]))));
	};

	/**
	 * Check if the given plausible vector all its values lower or equal
	 * compared to "this" vector
	 * 
	 * @param pv
	 *            the plausible vector to check
	 * @return true if the plausible vector is lower or equal, false otherwise
	 */
	public boolean isLeq(PlausibleVector pv) {
		boolean higher = true;
		int i = 0;
		while (higher && i < this.v.length) {
			if (this.v[i] < pv.v[i]) {
				higher = false;
			}
			i = i + 1;
		}
		return higher;
	};

	@Override
	public boolean equals(Object obj) {
		boolean equals = true;
		PlausibleVector cast = null;
		if (obj instanceof PlausibleVector) {
			cast = (PlausibleVector) obj;
			equals = ((cast.v.length == this.v.length) && (cast.e == this.e));
		} else {
			equals = false;
		}

		Integer i = 0;
		while (equals && i < this.v.length) {
			if (this.v[i] != cast.v[i]) {
				equals = false;
			}
			i = i + 1;
		}
		return equals;
	}

	/**
	 * process the number of differences between the two vectors
	 * 
	 * @param pv
	 *            the other plausible vector
	 * @return the number of differences
	 */
	public int diffNumber(PlausibleVector pv) {
		int sum = 0;
		for (int i = 0; i < this.v.length; ++i) {
			sum += Math.abs(this.v[i] - pv.v[i]);
		}
		return sum;
	}
}
