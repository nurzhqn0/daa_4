package graph.dag;

import graph.Graph;
import graph.topo.*;
import metrics.*;
import java.util.*;

public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;
    
    public DAGShortestPath(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }
    
    public PathResult findShortestPaths(int source) {
        int n = graph.getVertices();
        metrics.reset();
        metrics.startTimer();

        DFSTopologicalSort topoSort = new DFSTopologicalSort(graph);
        TopologicalSortResult topoResult = topoSort.sort();
        
        if (topoResult.hasCycle()) {
            metrics.stopTimer();
            return new PathResult(null, null, true, metrics);
        }
        
        List<Integer> topoOrder = topoResult.getOrder();
        int[] dist = new int[n];
        int[] pred = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(pred, -1);
        dist[source] = 0;
        
        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Graph.Edge edge : graph.getNeighbors(u)) {
                    int v = edge.getTo();
                    metrics.incrementEdgeTraversals();
                    if (dist[u] + edge.getWeight() < dist[v]) {
                        dist[v] = dist[u] + edge.getWeight();
                        pred[v] = u;
                        metrics.incrementRelaxations();
                    }
                }
            }
        }
        
        metrics.stopTimer();
        return new PathResult(dist, pred, false, metrics);
    }
    
    public static List<Integer> reconstructPath(int[] pred, int source, int target) {
        if (pred == null || (pred[target] == -1 && target != source)) return Collections.emptyList();
        List<Integer> path = new ArrayList<>();
        int current = target;

        while (current != -1) {
            path.add(current);
            if (current == source) break;
            current = pred[current];
        }

        Collections.reverse(path);
        return path;
    }
}
