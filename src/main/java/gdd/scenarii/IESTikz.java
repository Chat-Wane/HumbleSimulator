package gdd.scenarii;

import gdd.events.EDeliver;
import gdd.events.EReceive;
import gdd.events.ESend;
import gdd.events.MyEvent;
import gdd.network.Peer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.SortedMap;

public class IESTikz implements IExportStrategy {

	public void export() {
		String output = "";
		String outputCircle = "";

		Integer minKey = Scenario.getTimeLine().firstKey();
		Integer maxKey = Scenario.getTimeLine().lastKey();
		String unit = "";
		if (maxKey > 600) {
			unit = "pt";
		}

		// #1 create a line for each process
		for (Entry<Integer, Peer> peerEntry : Scenario.getPeers().entrySet()) {
			output = output + "\n\\draw[->,very thick] (" + peerEntry.getKey()
					+ "," + (minKey - 1) + unit
					+ ") node[anchor=north]{\\textbf{" + peerEntry.getKey()
					+ "}} -- (" + peerEntry.getKey() + "," + (maxKey + 1)
					+ unit + ") node[anchor=south]{"
					+ peerEntry.getValue().getEntry().toString() + "};";
		}
		// #1b print the time every 5 ticks
		for (int i = minKey; i < maxKey; i = i + 5) {
			output = output + "\n\\draw ( -0.5," + i + unit
					+ ") node[anchor=east]{\\textbf{" + i + "}};";
		}

		// #2 following the timeline, create the associated output
		NavigableSet<Integer> keySet = Scenario.getTimeLine().navigableKeySet();
		for (Integer time : keySet) {
			List<MyEvent> eventsAtTime = Scenario.getTimeLine().get(time);
			Iterator<MyEvent> eventIterator = eventsAtTime.iterator();
			HashMap<Integer, HashMap<String, Integer>> agregator = new HashMap<Integer, HashMap<String, Integer>>();
			while (eventIterator.hasNext()) {
				MyEvent e = eventIterator.next();
				if (!agregator.containsKey(e.uid)) {
					agregator.put(e.uid, new HashMap<String, Integer>());
				}
				if (e instanceof ESend) {
					ESend eSend = (ESend) e;
					outputCircle = outputCircle + "\n \\draw[fill=black] ("
							+ eSend.uid + "," + time + unit
							+ ") circle (0.06);";
				}
				if (e instanceof EReceive) {
					EReceive eReceive = (EReceive) e;
					// 2a Search the send point
					ESend eSend = new ESend(Integer.valueOf(eReceive.id
							.split(";")[0]), eReceive.id, null);
					SortedMap<Integer, List<MyEvent>> sortedEvents = Scenario
							.getTimeLine().headMap(time);
					boolean found = false;
					Iterator<Integer> keys = sortedEvents.keySet().iterator();
					Integer timeKey = null;
					while (!found && keys.hasNext()) {
						timeKey = keys.next();
						found = sortedEvents.get(timeKey).contains(
								(MyEvent) eSend);
					}
					if (!found) { // only debug: receive without send
						System.err.println("warning: " + eSend.uid
								+ " did not send " + eSend.id + "yet.");
					} else {
						// 2b make the link between the two events
						output = output + "\n\\draw[->] (" + eSend.uid + ","
								+ timeKey + unit + ") -- (" + eReceive.uid
								+ "," + time + unit + ");";
					}
				}
				if (e instanceof EDeliver) {
					// EDeliver eDeliver = (EDeliver) e;
					if (!agregator.get(e.uid).containsKey("EDeliver")) {
						agregator.get(e.uid).put("EDeliver", 0);
					}
					agregator.get(e.uid).put("EDeliver",
							agregator.get(e.uid).get("EDeliver") + 1);
				}

			}
			// #3 use agregator to summerize some output
			Iterator<Integer> iAtTime = agregator.keySet().iterator();
			while (iAtTime.hasNext()) {
				Integer peerUID = iAtTime.next();

				if (agregator.get(peerUID).containsKey("EDeliver")) {
					outputCircle = outputCircle + "\n \\draw[fill=blue] ("
							+ peerUID + "," + time + unit + ") circle (0.04);";
				}

				if (agregator.get(peerUID).containsKey("EDeliver")
						&& agregator.get(peerUID).get("EDeliver") > 1) {
					outputCircle = outputCircle + "\n \\draw (" + peerUID + ","
							+ time + unit + ") node[anchor=east,color=blue]{"
							+ agregator.get(peerUID).get("EDeliver") + "};";
				}
			}
		}

		// #4 encapsulate the output in a tickz environment
		output = "\\documentclass{article}\n" + "\\usepackage{tikz}\n"
				+ "\\usepackage[graphics,tightpage,active]{preview}\n"
				+ "\\PreviewEnvironment{tikzpicture}\n"
				+ "\\newlength{\\imagewidth}\n" + "\\newlength{\\imagescale}\n"
				+ "\\pagestyle{empty}\n" + "\\thispagestyle{empty}\n"
				+ "\\begin{document}\n" + "\\begin{tikzpicture}\n\\small\n"
				+ output + outputCircle
				+ "\n\n\\end{tikzpicture}\n\\end{document}\n";

		try {
			// Create file
			FileWriter fstream = new FileWriter("target/timeline.tex");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(output);
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
