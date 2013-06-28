package gdd.network;

import gdd.vc.VC;

/**
 * Immutable message containing the metadata required to preserve causality
 * Assume: only used by peer class
 */
public class Msg {

	private final String uid;
	private final VC vc;
	private final Integer entry;
	private final Object payload;

	public Msg(String uid, VC vc, Integer entry, Object payload) {
		// #0 copy uid;
		this.uid = new String(uid);
		// #1 copy of qvv
		this.vc = (VC) vc.clone();
		// #2 copy of entries
		this.entry = new Integer(entry);
		this.payload = payload;
	}

	public Integer getEntry() {
		return entry;
	}

	public VC getVc() {
		return vc;
	}

	public Object getPayload() {
		return payload;
	}

	public String getUid() {
		return uid;
	}
}
