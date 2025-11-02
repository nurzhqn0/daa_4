package graph.scc;

import metrics.Metrics;
import java.util.*;

public class SCCResult {
    private final List<List<Integer>> components;
    private final int componentCount;
    private final Metrics metrics;
    
    public SCCResult(List<List<Integer>> components, int componentCount, Metrics metrics) {
        this.components = components;
        this.componentCount = componentCount;
        this.metrics = metrics;
    }
    
    public List<List<Integer>> getComponents() { return components; }
    public int getComponentCount() { return componentCount; }
    public Metrics getMetrics() { return metrics; }
    
    public int getLargestComponentSize() {
        int max = 0;
        for (List<Integer> scc : components) max = Math.max(max, scc.size());
        return max;
    }
    
    public boolean isDAG() { return getLargestComponentSize() == 1; }
    
    public Map<Integer, Integer> getVertexToComponentMap() {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int vertex : components.get(i)) map.put(vertex, i);
        }
        return map;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SCCs: "+componentCount+", Largest: "+getLargestComponentSize()+", Is DAG: "+isDAG()+"\n");
        for (int i = 0; i < components.size(); i++) {
            sb.append("  SCC"+i+" (size "+components.get(i).size()+"): "+components.get(i)+"\n");
        }
        sb.append(metrics.report());
        return sb.toString();
    }
}
