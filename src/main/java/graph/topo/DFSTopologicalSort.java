package graph.topo;

import graph.Graph;
import metrics.*;
import java.util.*;

public class DFSTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;
    private boolean[] visited, onPath;
    private Stack<Integer> stack;
    private boolean hasCycle;
    
    public DFSTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }
    
    public TopologicalSortResult sort() {
        int n = graph.getVertices();
        visited = new boolean[n];
        onPath = new boolean[n];
        stack = new Stack<>();
        hasCycle = false;
        
        metrics.reset();
        metrics.startTimer();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfs(i);
        }
        metrics.stopTimer();
        
        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) {
            order.add(stack.pop());
            metrics.incrementStackOperations();
        }
        
        return new TopologicalSortResult(order, hasCycle, metrics);
    }
    
    private void dfs(int u) {
        metrics.incrementDFSVisits();
        visited[u] = true;
        onPath[u] = true;
        
        for (Graph.Edge edge : graph.getNeighbors(u)) {
            int v = edge.getTo();
            metrics.incrementEdgeTraversals();
            if (onPath[v]) hasCycle = true;
            else if (!visited[v]) dfs(v);
        }
        
        onPath[u] = false;
        stack.push(u);
        metrics.incrementStackOperations();
    }
}
