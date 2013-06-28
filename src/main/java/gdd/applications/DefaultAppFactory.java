package gdd.applications;

/**
 * Return instances of application that only relay messages notification
 */
public class DefaultAppFactory implements IApplicationFactory {

	public DefaultAppFactory() {
	}

	public DefaultApp get() {
		return new DefaultApp();
	}

}
