package com.gdd.peers;

public class DownElement {

	private final Integer uid;
	private final Integer t;

	public DownElement(Integer uid, Integer time) {
		this.uid = uid;
		this.t = time;
	}

	public Integer getT() {
		return t;
	}

	public Integer getUid() {
		return uid;
	}

}
