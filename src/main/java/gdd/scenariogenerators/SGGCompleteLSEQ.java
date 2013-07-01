package gdd.scenariogenerators;

import gdd.applications.IApplicationFactory;
import gdd.applications.LogootEngineFactory;
import gdd.graph.IGraph;

import com.google.inject.Injector;

/**
 * Generate a random use case using the parameters
 */
public class SGGCompleteLSEQ extends SGGComplete {

	private static Injector injector;

	public SGGCompleteLSEQ(IGraph g, Integer nbMsg, Injector injector) {
		super(g, nbMsg);
		SGGCompleteLSEQ.injector = injector;
	}

	// Sequence CRDT application
	@Override
	public IApplicationFactory getApplicationFactory() {
		LogootEngineFactory lef = new LogootEngineFactory();
		lef.setInjector(injector);
		return lef;
	}
}
