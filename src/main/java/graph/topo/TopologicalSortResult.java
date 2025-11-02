package graph.topo;

import metrics.Metrics;
import java.util.*;

public class TopologicalSortResult {
    private final List<Integer> order;
    private final boolean hasCycle;
    private final Metrics metrics;
    
    public TopologicalSortResult(List<Integer> order, boolean hasCycle, Metrics metrics) {
        this.order = order;
        this.hasCycle = hasCycle;
        this.metrics = metrics;
    }
    
    public List<Integer> getOrder() {
        return order;
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public boolean isValid() {
        return !hasCycle;
    }

    public Metrics getMetrics() {
        return metrics;
    }
    
    public String toString() {
        return hasCycle ? "Topological Sort Fail (Cycle)\n" + metrics.report() :
            "Topological Order: " + order + "\n" + metrics.report();
    }
}
