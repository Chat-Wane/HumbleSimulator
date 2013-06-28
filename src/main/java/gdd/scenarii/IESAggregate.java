package gdd.scenarii;

import gdd.events.EDeliver;
import gdd.events.EReceive;
import gdd.events.ESend;
import gdd.events.MyEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;

/**
 * Aggregate data of wanted events
 * 
 * example of use: plot "./aggregate.dat" using 1 title "send" ,
 * "./aggregate.dat" using ($3/10) title "deliver"
 */
public class IESAggregate implements IExportStrategy {

	private ArrayList<HashMap<String, Integer>> aggregates;

	private HashMap<String, Integer> global;

	private Integer step; // How often data are saved

	public IESAggregate(Integer step) {
		this.step = step;
		this.aggregates = new ArrayList<HashMap<String, Integer>>();
		this.global = new HashMap<String, Integer>();
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	@SuppressWarnings("unchecked")
	public void export() {
		// #1 define bounds
		Integer lastSaveTime = Scenario.getTimeLine().firstKey();
		Integer maxKey = Scenario.getTimeLine().lastKey();

		// #2 follow the timeline
		NavigableSet<Integer> keySet = Scenario.getTimeLine().navigableKeySet();
		HashMap<String, Integer> aggregator = new HashMap<String, Integer>();
		for (Integer time : keySet) {
			// System.out.println(lastSaveTime);
			// System.out.println(time);
			if (time - lastSaveTime > step) {
				// save the data
				Integer nbSave = (time - lastSaveTime) / step;
				for (int i = 0; i < nbSave; ++i) {
					aggregates.add(aggregator);
				}
				aggregator = (HashMap<String, Integer>) aggregator.clone();
				lastSaveTime = lastSaveTime + nbSave * step;
			}

			// #2a crawl events
			List<MyEvent> eventsAtTime = Scenario.getTimeLine().get(time);
			Iterator<MyEvent> eventIterator = eventsAtTime.iterator();

			// #2b count
			while (eventIterator.hasNext()) {
				MyEvent e = eventIterator.next();
				String key = "";
				if (e instanceof ESend) {
					key = "send";
				}
				if (e instanceof EReceive) {
					key = "receive";
				}
				if (e instanceof EDeliver) {
					key = "deliver";
				}

				if (key.length() != 0) {
					if (!aggregator.containsKey(key)) {
						aggregator.put(key, 0);
					}
					aggregator.put(key, aggregator.get(key) + 1);
				}
			}
		}

		global.put("peerSize", Scenario.getPeers().size());
		global.put("maxDate", maxKey);

		String output = aggregates.get(aggregates.size() - 1).keySet()
				.toString().replace("[", "").replace("]", "").replace(",", "")
				+ "\n";
		output = output + "# step: " + step + "\n";
		for (int i = 0; i < aggregates.size(); ++i) {
			Set<String> keys = aggregates.get(i).keySet();
			for (String key : keys) {
				output = output + aggregates.get(i).get(key) + " ";
			}
			output = output + "\n";
		}

		try {
			// Create file
			FileWriter fstream = new FileWriter("target/aggregate.dat");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(output);
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
}
