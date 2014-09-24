import networkx as nx

#CG100 = nx.complete_graph(100)
NWSG100 = nx.newman_watts_strogatz_graph(100,4,0.1);

SPL = nx.shortest_path_length(NWSG100)

for i in SPL:
    for j in SPL[i]:
        print SPL[i][j],
    print
