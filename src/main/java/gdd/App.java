package gdd;

import gdd.graph.GRandom;
import gdd.graph.IGraph;
import gdd.scenarii.IESAggregate;
import gdd.scenarii.IESTikz;
import gdd.scenarii.Scenario;
import gdd.scenariogenerators.IScenarioGenerator;
import gdd.scenariogenerators.SGGComplete;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {

		System.out.print("Generating graph...");
		Set<Integer> peers = new HashSet<Integer>();
		for (int i = 1; i <= 7; ++i) {
			peers.add(i);
		}
		// IGraph g = new GComplete(peers, 200);
		IGraph g = new GRandom(peers, 50, 0.6f);
		System.out.println(" done.");

		System.out.print("Generating scenario...");
		IScenarioGenerator sg = new SGGComplete(g, 40);
		// IScenarioGenerator sg = new SGGCompleteLSEQ(g, 2, 11, 10);
		// IScenarioGenerator sg = new SGBasic();
		System.out.println(" done.");

		Scenario s = new Scenario(sg, new IESTikz());
		System.out.print("Running scenario...");
		s.run();
		System.out.println(" done.");

		System.out.print("Exporting timeline...");
		s.exportScenario();
		System.out.println(" done.");

		System.out.print("Exporting aggregator...");
		Scenario.setIExportStrategy(new IESAggregate(5));
		s.exportScenario();
		System.out.println(" done.");

	}
}
