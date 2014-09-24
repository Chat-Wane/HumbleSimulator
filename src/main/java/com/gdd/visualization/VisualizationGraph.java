package com.gdd.visualization;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.gdd.histories.HistoryEdge;
import com.gdd.histories.HistoryGraph;
import com.gdd.peers.Peers;

public class VisualizationGraph {

	private DirectedGraph<Integer, DefaultEdge> graph = new DefaultDirectedGraph<Integer, DefaultEdge>(
			DefaultEdge.class);

	public VisualizationGraph() {
		for (int i = 0; i < Peers.getPeers().size(); ++i) {
			HistoryEdge he = HistoryGraph.getPeer(Peers.getPeer(i));
			while (he.getFrom().getC() != -1) {
				// System.out.println(he.getFrom().getC());
				Collection<HistoryEdge> potentialParentEdge = HistoryGraph
						.getParentEdges(he.getFrom());
				Iterator<HistoryEdge> iPotential = potentialParentEdge
						.iterator();
				while (iPotential.hasNext()) {
					HistoryEdge potential = iPotential.next();
					if (potential.getPeers().get(i)) {
						he = potential;
					}
				}
				if (he.getFrom().getC() != -1) {
					this.graph.addVertex(he.getFrom().getC());
					this.graph.addVertex(he.getTo().getC());
					this.graph.addEdge(he.getFrom().getC(), he.getTo().getC());
				}
			}
		}
	}

	public void export() {
		// Warning, the vertex are renamed ( I think ... )
		DOTExporter<Integer, DefaultEdge> exporter = new DOTExporter<Integer, DefaultEdge>(
				new StringNameProvider<Integer>(),
				new StringNameProvider<Integer>(), null);

		try {
			exporter.export(new FileWriter("graph.dot"), this.graph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JGraphModelAdapter<Integer, DefaultEdge> getAdapter() {
		return new JGraphModelAdapter<Integer, DefaultEdge>(this.graph);
	}
}
