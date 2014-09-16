import networkx as nx

CG10 = nx.complete_graph(5)

nx.write_gml(CG10,"gc10")

nx.write_weighted_edgelist(CG10,"gc10edge")

print(nx.shortest_path_length(CG10))
