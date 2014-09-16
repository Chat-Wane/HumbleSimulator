package com.gdd.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.gdd.Global;

/**
 * Decompose the cycle of creation and execution of operations until the export
 */
public class Loop {

	private final Random rng = new Random(Global.SEED);
	private ArrayList<Integer> creationTime = new ArrayList<Integer>();

	/**
	 * function executed before the execution loop is started. It initializes
	 * the objects required.
	 */
	public void before() {
		for (int i = 0; i < Global.OPERATIONS; ++i) {
			this.creationTime.add(rng.nextInt(Global.TOTALTIME));
		}
		Collections.sort(this.creationTime);
	}

	/**
	 * start the execution of the main loop.
	 */
	public void execute() {
		Iterator<Integer> iCreationTime = creationTime.iterator();
		while (iCreationTime.hasNext()) {
			Integer currentTime = iCreationTime.next();
			// #1 choose a peer which will generate the operation
			// #2 increment the vector of the peer
			// #3 
		}
	}

	/**
	 * function executed at the end of the execution to, for instance, export
	 * results.
	 */
	public void after() {

	}
}
