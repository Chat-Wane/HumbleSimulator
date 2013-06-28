package gdd;

import gdd.applications.LogootEngineProxy;
import gdd.graph.GComplete;
import gdd.graph.IGraph;
import gdd.scenarii.IESTikz;
import gdd.scenarii.Scenario;
import gdd.scenariogenerators.IScenarioGenerator;
import gdd.scenariogenerators.SGGCompleteLSEQ;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import alma.fr.RunWiki;
import alma.fr.data.Positions;
import alma.fr.logootenginecomponents.LogootEngine;
import alma.fr.modules.GreedRandDoubleModule;

import com.google.inject.Guice;

/**
 * Hello world!
 * 
 */
public class LseqConcurrency {
	public static void main(String[] args) {

		Integer nbMsgs = 200;
		Integer nbPeers = 1;

		System.out.print("Generating graph...");
		Set<Integer> peers = new HashSet<Integer>();
		for (int i = 1; i <= nbPeers; ++i) {
			peers.add(i);
		}
		IGraph g = new GComplete(peers, 5);
		// IGraph g = new GRandom(peers, 50, 0.6f);
		System.out.println(" done.");

		System.out.print("Generating scenario...");
		// IScenarioGenerator sg = new SGGComplete(g, 2, 10, 40);
		IScenarioGenerator sg = new SGGCompleteLSEQ(g, 2, 11, nbMsgs / nbPeers,
				Guice.createInjector(new GreedRandDoubleModule()));
		// IScenarioGenerator sg = new SGBasic();
		System.out.println(" done.");

		Scenario s = new Scenario(sg, new IESTikz());
		System.out.print("Running scenario...");
		s.run();
		System.out.println(" done.");

		System.out.print("Exporting timeline...");
		// s.exportScenario();
		System.out.println(" done.");

		LogootEngine le = ((LogootEngineProxy) Scenario.getApplications()
				.get(1)).getLogootEngine();
		System.out.println(le.getDoc());
		System.out.println("size =" + le.getDoc().size());
		Float[] avgAndMax = RunWiki.avgAndMaxBitSize(le.getIdTable());
		System.out.println("avg bitLength=" + avgAndMax[0]);
		System.out.println("max bitLength=" + avgAndMax[1]);
		
		/// /  / / / / /// // / /// / // // // / // // // / / / 
		// / #2 second run to compare
		System.out.println("========================");
		System.out.println("========================");

		nbPeers = 10;
		System.out.print("Generating graph...");
		peers = new HashSet<Integer>();
		for (int i = 1; i <= nbPeers; ++i) {
			peers.add(i);
		}
		g = new GComplete(peers, 5);
		// IGraph g = new GRandom(peers, 50, 0.6f);
		System.out.println(" done.");
		
		
		System.out.print("Generating scenario...");
		IScenarioGenerator sg2 = new SGGCompleteLSEQ(g, 2, 11,
				nbMsgs / nbPeers,
				Guice.createInjector(new GreedRandDoubleModule()));
		System.out.println(" done.");

		Scenario s2 = new Scenario(sg2, new IESTikz());
		System.out.print("Running scenario...");
		s2.run();
		System.out.println(" done.");

		LogootEngine le2 = ((LogootEngineProxy) Scenario.getApplications().get(
				1)).getLogootEngine();
		System.out.println(le2.getDoc());
		System.out.println("size =" + le2.getDoc().size());
		Float[] avgAndMax2 = RunWiki.avgAndMaxBitSize(le2.getIdTable());
		System.out.println("avg bitLength=" + avgAndMax2[0]);
		System.out.println("max bitLength=" + avgAndMax2[1]);

		String results = "";
		for (int i = 1; i < le.getIdTable().size() - 1; ++i) {
			Positions p = le.getIdTable().get(i);
			results = results
					+ le.getStrategyChoice().getSpectrum().get(p).getDate()
					+ " " + p.getC().size() + " " + p.getD().bitLength() + " "
					+ " " + le.getIdTable().get(i).getC().size() + " "
					+ le2.getIdTable().get(i).getD().bitLength() + "\n";
		}

		try {
			// Create file
			FileWriter fstream = new FileWriter("target/spectrum.dat");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(results);
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
}
