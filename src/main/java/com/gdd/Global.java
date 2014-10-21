package com.gdd;

public class Global {

	public final static long SEED = 123456789;

	// #A Peers and operations
	public final static int PEERS = 100; // number of peers
	public final static int TOTALTIME = 3600; // time of the simulation
												// (arbitrary unit)
	public final static int OPERATIONS = 600; // uniformely ( for now )
												// distributed between peers and
												// among time

	// #B Vector properties
	public final static int R = 50; // the size of the vector

	// #C Network
	public final static String GRAPHFILE = "src/main/java/com/gdd/examples/nwsg100";
	public final static float lagMultiplier = 1.4f;
	public final static float churn = 0.1f; // constantly a churn percentage of
											// peers down during the experiment
	public final static int churnDuration = 50; // a site is down 50 rounds
	public final static int A = 1; // number of "adjacent" peers to contact to
									// catch up after being down
	public final static int antientropy = 50; // frequency of anti-entropy
												// protocol
}
