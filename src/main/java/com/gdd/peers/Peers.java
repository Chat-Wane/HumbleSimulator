package com.gdd.peers;

import java.util.ArrayList;
import java.util.BitSet;

import com.gdd.Global;

public class Peers {

	// pid -> Peer
	private static ArrayList<Peer> peers = new ArrayList<Peer>();

	public Peers() {
		for (int i = 0; i < Global.PEERS; ++i) {
			Peers.peers.add(new Peer(i));
		}
	}

	public static ArrayList<Peer> getPeers() {
		return Peers.peers;
	}

	public static Peer getPeer(Integer pId) {
		return Peers.peers.get(pId);
	}

}
