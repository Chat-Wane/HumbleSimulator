package gdd.vc;

import gdd.vc.entrychoice.IEntryChoice;

import java.util.Set;

/**
 * Hold causality data and metadata on each peer.
 */
public class CausalityHandler {

	private VC vc;

	private Integer entry;

	private Densities densities;

	private IEntryChoice ec;

	public CausalityHandler(IEntryChoice ec) {
		this.densities = new Densities(10000); // 10000= infinite
		this.vc = new VC();
		this.ec = ec;
		this.entry = this.ec.init();
	}

	// May add a ttl to received entries
	/**
	 * When message is received, its entries are stored on the peer
	 * 
	 * @param receivedEntry
	 */
	public void addReceivedEntry(Integer receivedEntry) {
		densities.addEntry(receivedEntry);
	}

	public Integer getEntry() {
		return entry;
	}

	public Set<Integer> getReceivedEntries() {
		return densities.getReceivedEntries().keySet();
	}

	public void inc() {
		vc = VC.inc(vc, entry);
	}

	public void inc(Integer entry) {
		vc = VC.inc(vc, entry);
	}

	public Densities getDensities() {
		return densities;
	}

	public VC getVc() {
		return vc;
	}
}
