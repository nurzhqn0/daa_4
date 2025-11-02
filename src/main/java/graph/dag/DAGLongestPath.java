package graph.dag;

import graph.Graph;
import graph.topo.*;
import metrics.*;
import java.util.*;

public class DAGLongestPath {
    private final Graph graph;
    private final Metrics metrics;
    
    public DAGLongestPath(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }
    
    public PathResult findLongestPath() {
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
        Arrays.fill(dist, Integer.MIN_VALUE);
        Arrays.fill(pred, -1);
        
        for (int v : topoOrder) {
            if (dist[v] == Integer.MIN_VALUE) dist[v] = 0;
            for (Graph.Edge edge : graph.getNeighbors(v)) {
                int u = edge.getTo();
                metrics.incrementEdgeTraversals();
                if (dist[v] != Integer.MIN_VALUE && dist[v] + edge.getWeight() > dist[u]) {
                    dist[u] = dist[v] + edge.getWeight();
                    pred[u] = v;
                    metrics.incrementRelaxations();
                }
            }
        }
        
        metrics.stopTimer();
        return new PathResult(dist, pred, false, metrics);
    }
    
    public CriticalPathInfo findCriticalPath() {
        PathResult result = findLongestPath();
        if (result.hasCycle()) return null;
        
        int[] dist = result.getDistances();
        int[] pred = result.getPredecessors();
        int maxDist = Integer.MIN_VALUE, endVertex = -1;
        
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] > maxDist) {
                maxDist = dist[i];
                endVertex = i;
            }
        }
        
        List<Integer> path = reconstructPath(pred, endVertex);
        return new CriticalPathInfo(path.isEmpty() ? -1 : path.get(0), endVertex, maxDist, path);
    }
    
    private List<Integer> reconstructPath(int[] pred, int end) {
        List<Integer> path = new ArrayList<>();
        int current = end;

        while (current != -1) {
            path.add(current);
            current = pred[current];
        }

        Collections.reverse(path);
        return path;
    }
    
    public static class CriticalPathInfo {
        public final int start, end, length;
        public final List<Integer> path;

        public CriticalPathInfo(int start, int end, int length, List<Integer> path) {
            this.start = start;
            this.end = end;
            this.length = length;
            this.path = path;
        }

        public String toString() {
            return "Critical Path: "+path+" (length="+length+", from "+start+" to "+end+")";
        }
    }
}
