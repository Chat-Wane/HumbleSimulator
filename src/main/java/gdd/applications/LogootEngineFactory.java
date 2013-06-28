package gdd.applications;

import alma.fr.logootenginecomponents.LogootEngine;
import alma.fr.modules.GreedDoubleModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class LogootEngineFactory implements IApplicationFactory {

	private static Injector injector;

	public LogootEngineFactory() {
		// DEFAULT
		// injector = Guice.createInjector(new WeissModule());
		// injector = Guice.createInjector(new GreedModule());
		// injector = Guice.createInjector(new DoubleModule());
		injector = Guice.createInjector(new GreedDoubleModule());
		// injector = Guice.createInjector(new GreedRandDoubleModule());
	}

	public void setInjector(Injector injector) {
		LogootEngineFactory.injector = injector;
	}

	public LogootEngineProxy get() {
		LogootEngine le = injector.getInstance(LogootEngine.class);
		return new LogootEngineProxy(le);
	}

}
