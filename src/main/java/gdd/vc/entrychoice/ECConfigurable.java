package gdd.vc.entrychoice;

import java.util.ArrayList;

public class ECConfigurable implements IEntryChoice {

	// Pseudo random entries
	private static ArrayList<Integer> entries;
	private static Integer numberOfRandom = 0;

	public ECConfigurable(ArrayList<Integer> entries) {
		ECConfigurable.entries = entries;
	}

	public Integer init() {
		Integer result = entries.get(numberOfRandom);
		ECConfigurable.numberOfRandom = (ECConfigurable.numberOfRandom + 1)
				% entries.size();
		return result;
	}

	public Integer get() {
		Integer result = entries.get(numberOfRandom);
		ECConfigurable.numberOfRandom = (ECConfigurable.numberOfRandom + 1)
				% entries.size();
		return result;
	}

}
