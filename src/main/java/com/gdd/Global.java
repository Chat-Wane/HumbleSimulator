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
	public final static int R = 10; // the size of the vector

	// #C Network
	public final static String GRAPHFILE = "src/main/java/com/gdd/examples/nwsg100";
	public final static float lagMultiplier = 1.4f;
}
