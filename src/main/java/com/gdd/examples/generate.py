import networkx as nx

CG10 = nx.complete_graph(5)

SPL = nx.shortest_path_length(CG10)

for i in SPL:
    for j in SPL[i]:
        print SPL[i][j],
    print
