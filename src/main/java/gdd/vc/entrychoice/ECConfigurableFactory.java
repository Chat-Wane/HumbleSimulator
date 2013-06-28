package gdd.vc.entrychoice;

import java.util.ArrayList;

public class ECConfigurableFactory implements IEntryChoiceFactory {

	private ArrayList<Integer> entries;

	public ECConfigurableFactory(ArrayList<Integer> entries) {
		this.entries = entries;
	}

	public IEntryChoice get() {
		return new ECConfigurable(entries);
	}

}
