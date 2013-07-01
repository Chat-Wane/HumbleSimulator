package gdd.scenariogenerators;

import gdd.applications.DefaultAppFactory;
import gdd.applications.IApplicationFactory;
import gdd.events.EReceive;
import gdd.events.ESend;
import gdd.events.MyEvent;
import gdd.graph.IGraph;
import gdd.vc.entrychoice.ECConfigurableFactory;
import gdd.vc.entrychoice.IEntryChoiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import cern.jet.random.Poisson;
import cern.jet.random.Uniform;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

/**
 * Generate a random use case using the parameters
 */
public class SGGComplete implements IScenarioGenerator {

	private IGraph g;

	private final Integer nbMsg;

	private final static RandomEngine re = new DRand(123456789);

	public SGGComplete(IGraph g, Integer nbMsg) {
		this.g = g;
		this.nbMsg = nbMsg;
	}

	public TreeMap<Integer, List<MyEvent>> getScenario() {
		TreeMap<Integer, List<MyEvent>> timeline = new TreeMap<Integer, List<MyEvent>>();

		Poisson poisson = new Poisson(100, re);
		Uniform uniform = new Uniform(0, 100, re); // Start uniform between 0
													// and
													// 25
		Integer nbSend = nbMsg; // nb message sent
		HashMap<Integer, HashMap<Integer, Integer>> distances = g
				.getDistances();

		Iterator<Integer> i = distances.keySet().iterator();
		while (i.hasNext()) {
			Integer peer = i.next();
			Integer dateSend = uniform.nextInt();
			for (int sent = 0; sent < nbSend; ++sent) {
				dateSend = dateSend + poisson.nextInt();
				if (!timeline.containsKey(dateSend)) {
					// #1 init the event list
					ArrayList<MyEvent> events = new ArrayList<MyEvent>();
					timeline.put(dateSend, events);
				}
				// #2a create the event
				ESend eSend = new ESend(peer, peer + ";" + (sent + 1), null);
				// #2b put it in the list
				timeline.get(dateSend).add(eSend);

				// #2c process all receive
				Iterator<Integer> j = distances.keySet().iterator();
				while (j.hasNext()) {
					Integer peerReceive = j.next();
					if (!peer.equals(peerReceive)) {
						Integer dateReceive = dateSend
								+ g.getDistance(peer, peerReceive);
						// #2d add receive event
						if (!timeline.containsKey(dateReceive)) {
							// #2e init the event list
							ArrayList<MyEvent> events = new ArrayList<MyEvent>();
							timeline.put(dateReceive, events);
						}
						EReceive eReceive = new EReceive(peerReceive, peer
								+ ";" + (sent + 1), null);
						timeline.get(dateReceive).add(eReceive);
					}
				}
			}
		}

		return timeline;
	}

	// No application
	public IApplicationFactory getApplicationFactory() {
		return new DefaultAppFactory();
	}

	public IEntryChoiceFactory getIEntryChoiceFactory() {
		ArrayList<Integer> entries = new ArrayList<Integer>();
		for (Integer entry : g.getGraph().keySet()) {
			entries.add(entry);
		}
		return new ECConfigurableFactory(entries);
	}

	public List<Integer> getPeerUids() {
		return new ArrayList<Integer>(g.getDistances().keySet());
	}
}
