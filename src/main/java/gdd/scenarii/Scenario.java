package gdd.scenarii;

import gdd.applications.IApplication;
import gdd.applications.IApplicationFactory;
import gdd.events.EDeliver;
import gdd.events.EReceive;
import gdd.events.ESend;
import gdd.events.MyEvent;
import gdd.network.Peer;
import gdd.network.Repository;
import gdd.scenariogenerators.IScenarioGenerator;
import gdd.vc.entrychoice.IEntryChoiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

/**
 * Scenario using a scenario generator in entry to call and produces all events.
 * Export is made in a latex file using tickz. Timeline of each site is
 * prompted.
 */
public class Scenario implements Observer {

	private static IScenarioGenerator sg;

	private static TreeMap<Integer, List<MyEvent>> timeLine;

	private static HashMap<Integer, Peer> peers;
	private static HashMap<Integer, IApplication> applications;

	private static Integer date;

	private static IExportStrategy ies;

	public Scenario(IScenarioGenerator sg, IExportStrategy ies) {
		Scenario.sg = sg;
		Scenario.peers = new HashMap<Integer, Peer>();
		Scenario.applications = new HashMap<Integer, IApplication>();
		Scenario.ies = ies;
	}

	/**
	 * generate the timeline with all the events generate from the Scenario
	 * loaded and the peers
	 */
	public void run() {
		// #1a get the initial state
		Scenario.timeLine = sg.getScenario();
		IEntryChoiceFactory iecf = sg.getIEntryChoiceFactory();
		IApplicationFactory af = sg.getApplicationFactory();
		// #1b init the peers and the applications
		if (!Scenario.timeLine.containsKey(0)) {
			Scenario.timeLine.put(0, new ArrayList<MyEvent>());
		}
		for (Integer peerKey : sg.getPeerUids()) {
			Peer peer = new Peer(peerKey, iecf.get());
			peer.addObserver(this);
			peers.put(peerKey, peer);
			IApplication application = af.get();
			peer.addObserver(application);
			((Observable) application).addObserver(peer);
			applications.put(peerKey, application);
		}

		// #1c init the repository
		Repository.setNbPeers(peers.size());

		// #2 run the timeline
		NavigableSet<Integer> keySet = timeLine.navigableKeySet();
		for (Integer time : keySet) {
			Scenario.date = time;
			// 2a apply events of the date
			Object[] eventAtDate = timeLine.get(time).toArray();
			for (int i = 0; i < eventAtDate.length; ++i) {
				// execute event
				MyEvent e = (MyEvent) eventAtDate[i];
				if (e instanceof ESend) {
					// 2b send the new message
					ESend eSend = (ESend) e;
					applications.get(eSend.uid).send(e.uid); // inform the uid
				}
				if (e instanceof EReceive) {
					// 2c receive the new message
					EReceive eReceive = (EReceive) e;
					peers.get(eReceive.uid).receive(eReceive.id);
				}
			}
		}
	}

	/**
	 * Integrate peers events
	 */
	public void update(Observable o, Object arg) {
		// #2 listen to events send from peers
		// #3 add dem to timeline
		if (arg instanceof EDeliver) {
			EDeliver e = (EDeliver) arg;
			timeLine.get(date).add(e);
		}
	}

	/**
	 * export in a file includable in tex documents with tikz
	 */
	public void exportScenario() {
		ies.export();
	}

	public static HashMap<Integer, Peer> getPeers() {
		return peers;
	}

	public static HashMap<Integer, IApplication> getApplications() {
		return applications;
	}

	public static Integer getDate() {
		return date;
	}

	public static IScenarioGenerator getSg() {
		return sg;
	}

	public static TreeMap<Integer, List<MyEvent>> getTimeLine() {
		return timeLine;
	}

	public static IExportStrategy getIExportStrategy() {
		return ies;
	}

	public static void setIExportStrategy(IExportStrategy ies) {
		Scenario.ies = ies;
	}

}
