package graph.scc;

import graph.Graph;
import java.util.*;

public class CondensationGraph {
    private final Graph condensed;
    private final Map<Integer, Integer> vertexToComponent;
    private final List<List<Integer>> components;
    
    public CondensationGraph(Graph original, SCCResult sccResult) {
        this.components = sccResult.getComponents();
        this.vertexToComponent = sccResult.getVertexToComponentMap();
        this.condensed = new Graph(sccResult.getComponentCount(), true);

        buildCondensationGraph(original);
    }
    
    private void buildCondensationGraph(Graph original) {
        Set<String> addedEdges = new HashSet<>();

        for (int u = 0; u < original.getVertices(); u++) {
            int compU = vertexToComponent.get(u);
            for (Graph.Edge edge : original.getNeighbors(u)) {
                int v = edge.getTo();
                int compV = vertexToComponent.get(v);

                if (compU != compV) {
                    String edgeKey = compU + "->" + compV;

                    if (!addedEdges.contains(edgeKey)) {
                        condensed.addEdge(compU, compV, edge.getWeight());
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
    }
    
    public Graph getCondensedGraph() {
        return condensed;
    }
    public int getComponentId(int vertex) {
        return vertexToComponent.get(vertex);
    }

    public List<Integer> getComponentVertices(int componentId) {
        return components.get(componentId);
    }

    public int getComponentCount() {
        return components.size();
    }

    public List<List<Integer>> getComponents() {
        return components;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Condensation Graph: "+condensed.getVertices()+" components\n");
        sb.append(condensed.toString());

        return sb.toString();
    }
}
