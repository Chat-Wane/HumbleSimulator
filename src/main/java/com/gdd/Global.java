package com.gdd;

public class Global {

	public final static long SEED = 123456789;

	// #A Peers and operations
	public final static int PEERS = 100; // number of peers
	public final static int TOTALTIME = 3600; // time of the simulation
												// (arbitrary unit)
	public final static int OPERATIONS = 100; // uniformely ( for now )
												// distributed between peers and
												// among time

	// #B Vector properties
	public final static int R = 10; // the size of the vector

	// #C Network
	public final static String GRAPHFILE = "src/main/java/com/gdd/examples/nwsg100";
	public final static float lagMultiplier = 1.4f;
	public final static float churn = 0f; // constantly a churn percentage of
											// peers down during the experiment
	public final static int churnDuration = 50; // a site is down 50 rounds
	public final static int A = 3; // number of "adjacent" peers to contact to
									// catch up after being down
	public final static int antientropy = 120; // frequency of anti-entropy
												// protocol
	public final static float msgPropagation = 0f; // messages arrives to
													// 'msgPropagation' of
													// the network, the
													// remaining peers must
													// antientropy to
													// eventually retrieve
													// all the operations
}
