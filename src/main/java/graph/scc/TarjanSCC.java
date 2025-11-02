package graph.scc;

import graph.Graph;
import metrics.*;
import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    private int[] ids, low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int id, sccCount;
    private List<List<Integer>> sccs;
    
    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }
    
    public SCCResult findSCCs() {
        int n = graph.getVertices();
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sccs = new ArrayList<>();
        Arrays.fill(ids, -1);
        id = 0;
        sccCount = 0;
        
        metrics.reset();
        metrics.startTimer();
        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) dfs(i);
        }
        metrics.stopTimer();
        return new SCCResult(sccs, sccCount, metrics);
    }
    
    private void dfs(int at) {
        metrics.incrementDFSVisits();
        ids[at] = low[at] = id++;
        stack.push(at);
        metrics.incrementStackOperations();
        onStack[at] = true;
        
        for (Graph.Edge edge : graph.getNeighbors(at)) {
            int to = edge.getTo();
            metrics.incrementEdgeTraversals();
            if (ids[to] == -1) {
                dfs(to);
                low[at] = Math.min(low[at], low[to]);
            } else if (onStack[to]) {
                low[at] = Math.min(low[at], ids[to]);
            }
        }
        
        if (ids[at] == low[at]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                metrics.incrementStackOperations();
                onStack[node] = false;
                scc.add(node);
                if (node == at) break;
            }
            sccs.add(scc);
            sccCount++;
        }
    }
}
