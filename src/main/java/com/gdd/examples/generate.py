import networkx as nx
import matplotlib.pyplot as plt

#CG100 = nx.complete_graph(100)
NWSG10000 = nx.newman_watts_strogatz_graph(10000,4,0.1);

SPL = nx.shortest_path_length(NWSG10000)

#nx.draw(NWSG10000)
#plt.show();

for i in SPL:
    for j in SPL[i]:
        print SPL[i][j],
    print
