import networkx as nx

CG10 = nx.complete_graph(5)

nx.write_gml(CG10,"gc10")
