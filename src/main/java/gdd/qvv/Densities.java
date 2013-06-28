package gdd.qvv;

import gdd.scenarii.Scenario;

import java.util.HashMap;
import java.util.Iterator;

public class Densities {

	private VC densities;

	private HashMap<Integer, Integer> receivedEntries;

	private Integer ttl; // ttl of entries

	private Integer lastUpdate; //

	public Densities(Integer ttl) {
		this.ttl = ttl;
		lastUpdate = 0;
		this.receivedEntries = new HashMap<Integer, Integer>();
		this.densities = new VC();
	}

	public void addEntry(Integer entry) {
		// #1 update the density vector
		update();

		// #2 increment densities
		if (!receivedEntries.containsKey(entry)) {
			densities = VC.inc(densities, entry);
		}
		receivedEntries.put(entry, ttl);

		// #3 update the date
		lastUpdate = Scenario.getDate();
	}

	public void update() {
		// #1 delete all the entries with ttl < 0 or update ttl
		Iterator<Integer> iEntries = receivedEntries.keySet().iterator();

		while (iEntries.hasNext()) {
			Integer key = iEntries.next();
			if (Scenario.getDate() - lastUpdate >= receivedEntries.get(key)) {
				// #1a delete key
				iEntries.remove();
				densities = VC.dec(densities, key); // decrement
			} else {
				// #1b update ttl
				receivedEntries.put(key,
						receivedEntries.get(key)
								- (Scenario.getDate() - lastUpdate));
			}
		}
	}

	public void reset() {
		densities.clear();
		receivedEntries.clear();
	}

	public VC getDensities() {
		return densities;
	}

	public HashMap<Integer, Integer> getReceivedEntries() {
		return receivedEntries;
	}
}
