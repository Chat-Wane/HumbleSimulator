package gdd.applications;


/**
 * All the factory that returns Observer. i.e. applications receiving messages
 * from the system ( aka the "peer" class)
 */
public interface IApplicationFactory {

	IApplication get();
}
