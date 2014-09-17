package com.gdd.peers;

import java.util.ArrayList;

public class IntervalWithExceptions {

	private Integer rightBound = 0;
	private ArrayList<Integer> exceptions = new ArrayList<Integer>();

	public boolean contains(Integer value) {
		return value < this.rightBound && !this.exceptions.contains(value);
	}

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

}
