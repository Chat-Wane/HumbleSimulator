package gdd.scenariogenerators;

import gdd.applications.IApplicationFactory;
import gdd.events.MyEvent;
import gdd.vc.entrychoice.IEntryChoiceFactory;

import java.util.List;
import java.util.TreeMap;

public interface IScenarioGenerator {

	/**
	 * Generate the timeline of events send and receive
	 * 
	 * @return the timeline ordered by date
	 */
	public TreeMap<Integer, List<MyEvent>> getScenario();

	/**
	 * Get the a factory of entry choices strategy employed by each peer
	 * 
	 * @return IEntryChoice factory
	 */
	public IEntryChoiceFactory getIEntryChoiceFactory();

	/**
	 * Get the application dependant of the delivery of messages
	 * 
	 * @return application factory
	 */
	public IApplicationFactory getApplicationFactory();

	/**
	 * Return the peer uids involved in the scenario
	 */
	public List<Integer> getPeerUids();
}
