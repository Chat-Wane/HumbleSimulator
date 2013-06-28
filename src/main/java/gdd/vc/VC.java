package gdd.vc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Vector Clock
 */
public class VC extends HashMap<Integer, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Function to check if other vc "o" has been overlapped by previous vc's
	 * 
	 * @param o
	 *            the vc potentially <=
	 * @return true if all entries of o are <=
	 */
	public boolean isLeq(VC o) {
		if (this.size() < o.size()) {
			// Cuz their is at least one value which does not exist in "this"
			// Quick version but less precise than below
			return false;
		}
		Set<Integer> keys = o.keySet();
		if (!this.keySet().containsAll(keys)) {
			// o's keys all exist in "this"
			return false;
		}
		boolean lEq = true;
		Iterator<Integer> i = keys.iterator();
		while (lEq && i.hasNext()) { // Check each keyz one by one
			Integer key = i.next();
			if (o.get(key) > this.get(key)) {
				lEq = false;
			}
		}
		return lEq;
	}

	/**
	 * Check if vc "o" with "entry" is ready compare to this vc. In other words,
	 * if all entries of o are <= except "entry" which is <= this -1
	 * 
	 * @param o
	 *            the other vc which ready or not
	 * @param entry
	 *            entry incremented resulting in the vc "o"
	 * @return true if o is ready
	 */
	public boolean isRdy(VC o, Integer entry) {
		boolean rdy = true;
		Iterator<Integer> i = o.keySet().iterator();
		while (rdy && i.hasNext()) {
			Integer key = i.next();
			if (o.get(key) > this.get(key)) {
				if (entry.equals(key)) {
					if (o.get(key) - 1 > this.get(key)) {
						rdy = false;
					}
				} else {
					rdy = false;
				}
			}
		}
		return rdy;
	}

	/**
	 * Increment the entry of this vc and produce a new vc
	 * 
	 * @param entry
	 *            entry to be incremented
	 */
	public static VC inc(VC vc, Integer entry) {
		VC tempVc = (VC) vc.clone();
		tempVc.put(entry, tempVc.get(entry) + 1);
		return tempVc;
	}

	/**
	 * Decrement the entries of vc and produce another vc
	 * 
	 * @param vc
	 * @param entry
	 * @return
	 */
	public static VC dec(VC vc, Integer entry) {
		VC tempVc = (VC) vc.clone();

		tempVc.put(entry, tempVc.get(entry) - 1);
		if (tempVc.get(entry) == 0) {
			tempVc.remove(entry);
		}

		return tempVc;
	}

	/**
	 * Safe get function, return a default 0 value instead of null
	 */
	@Override
	public Integer get(Object key) {
		return super.get(key) == null ? new Integer(0) : super.get(key);
	}

}
