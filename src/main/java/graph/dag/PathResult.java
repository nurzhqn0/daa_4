package graph.dag;

import metrics.Metrics;

public class PathResult {
    private final int[] distances;
    private final int[] predecessors;
    private final boolean hasCycle;
    private final Metrics metrics;
    
    public PathResult(int[] distances, int[] predecessors, boolean hasCycle, Metrics metrics) {
        this.distances = distances;
        this.predecessors = predecessors;
        this.hasCycle = hasCycle;
        this.metrics = metrics;
    }
    
    public int[] getDistances() {
        return distances;
    }

    public int[] getPredecessors() {
        return predecessors;
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public int getDistance(int vertex) {
        return distances[vertex];
    }

    public boolean isReachable(int vertex) { 
        return !hasCycle && distances != null && 
            distances[vertex] != Integer.MAX_VALUE && distances[vertex] != Integer.MIN_VALUE;
    }
    
    public String toString() {
        if (hasCycle) return "Path Fail (Cycle)\n" + metrics.report();
        StringBuilder sb = new StringBuilder("Distances: [");
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] == Integer.MAX_VALUE) sb.append("∞");
            else if (distances[i] == Integer.MIN_VALUE) sb.append("-∞");
            else sb.append(distances[i]);
            if (i < distances.length-1) sb.append(", ");
        }
        sb.append("]\n").append(metrics.report());
        return sb.toString();
    }
}
