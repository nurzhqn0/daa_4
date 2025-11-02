package graph;
import java.util.*;

public class Graph {
    private final int vertices;
    private final List<List<Edge>> adjList;
    private final boolean directed;

    public static class Edge {
        private final int to, weight;

        public Edge(int to, int weight) {
            this.to = to; this.weight = weight;
        }

        public int getTo() {
            return to;
        }

        public int getWeight() {
            return weight;
        }

        public String toString() {
            return "->"+to+"(w="+weight+")";
        }
    }

    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        this.adjList = new ArrayList<>(vertices);

        for (int i = 0; i < vertices; i++) adjList.add(new ArrayList<>());
    }

    public Graph(int vertices) {
        this(vertices, true);
    }

    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(v, weight));

        if (!directed) adjList.get(v).add(new Edge(u, weight));
    }

    public List<Edge> getNeighbors(int v) {
        return adjList.get(v);
    }

    public int getVertices() {
        return vertices;
    }

    public int getEdgeCount() {
        int count = 0;

        for (List<Edge> edges : adjList) count += edges.size();

        return directed ? count : count / 2;
    }

    public boolean isDirected() { return directed; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph: "+vertices+" vertices, "+getEdgeCount()+" edges\n");

        for (int i = 0; i < vertices; i++) {
            sb.append(i+": ");
            for (Edge e : adjList.get(i)) sb.append(e+" ");
            sb.append("\n");
        }

        return sb.toString();
    }
}