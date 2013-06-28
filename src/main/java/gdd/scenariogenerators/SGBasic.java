package gdd.scenariogenerators;

import gdd.applications.DefaultAppFactory;
import gdd.applications.IApplicationFactory;
import gdd.events.EReceive;
import gdd.events.ESend;
import gdd.events.MyEvent;
import gdd.vc.entrychoice.ECConfigurableFactory;
import gdd.vc.entrychoice.IEntryChoiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * A very basic use case where two messages are sent,received and delivered on 3
 * peers
 */
public class SGBasic implements IScenarioGenerator {

	public TreeMap<Integer, List<MyEvent>> getScenario() {
		TreeMap<Integer, List<MyEvent>> timeline = new TreeMap<Integer, List<MyEvent>>();

		// date 1
		MyEvent send11 = new ESend(1, "1;1", null);
		MyEvent send21 = new ESend(2, "2;1", null);
		ArrayList<MyEvent> eventsAt1 = new ArrayList<MyEvent>();
		eventsAt1.add(send11);
		eventsAt1.add(send21);
		timeline.put(1, eventsAt1);

		// date 3
		MyEvent receive211 = new EReceive(2, "1;1", null);
		MyEvent receive311 = new EReceive(3, "1;1", null);
		ArrayList<MyEvent> eventsAt3 = new ArrayList<MyEvent>();
		eventsAt3.add(receive211);
		eventsAt3.add(receive311);
		timeline.put(3, eventsAt3);

		// date 4
		MyEvent receive321 = new EReceive(3, "2;1", null);
		ArrayList<MyEvent> eventAt4 = new ArrayList<MyEvent>();
		eventAt4.add(receive321);
		timeline.put(4, eventAt4);

		// date 5
		MyEvent receive121 = new EReceive(1, "2;1", null);
		ArrayList<MyEvent> eventAt5 = new ArrayList<MyEvent>();
		eventAt5.add(receive121);
		timeline.put(5, eventAt5);

		return timeline;
	}

	// No application
	public IApplicationFactory getApplicationFactory() {
		return new DefaultAppFactory();
	}

	public IEntryChoiceFactory getIEntryChoiceFactory() {

		ArrayList<Integer> entries = new ArrayList<Integer>();
		entries.add(1);
		entries.add(2);
		entries.add(3);

		ECConfigurableFactory eccf = new ECConfigurableFactory(entries);
		return eccf;
	}

	public List<Integer> getPeerUids() {
		List<Integer> peerUids = new ArrayList<Integer>();
		peerUids.add(1);
		peerUids.add(2);
		peerUids.add(3);
		return peerUids;
	}
}
