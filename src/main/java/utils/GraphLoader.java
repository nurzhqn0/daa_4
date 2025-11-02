package utils;

import graph.Graph;
import java.io.*;

public class GraphLoader {
    
    public static GraphData loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(" ");
        }
        reader.close();
        return parseJSON(content.toString());
    }
    
    private static GraphData parseJSON(String json) {
        // Simple JSON parser for our specific format
        json = json.replaceAll("[\\r\\n\\s]+", " ").trim();
        
        // Extract values
        int n = extractInt(json, "\"n\"");
        boolean directed = json.contains("\"directed\": true");
        int source = extractInt(json, "\"source\"");
        if (source == -1) source = 0;
        
        String name = extractString(json, "\"name\"");
        if (name == null) name = "Unnamed";
        
        String description = extractString(json, "\"description\"");
        if (description == null) description = "";
        
        String weightModel = extractString(json, "\"weight_model\"");
        if (weightModel == null) weightModel = "edge";
        
        Graph graph = new Graph(n, directed);
        
        // Extract edges array
        int edgesStart = json.indexOf("\"edges\"");
        if (edgesStart != -1) {
            int arrayStart = json.indexOf('[', edgesStart);
            int arrayEnd = json.indexOf(']', arrayStart);
            if (arrayStart != -1 && arrayEnd != -1) {
                String edgesArray = json.substring(arrayStart + 1, arrayEnd);
                String[] edges = edgesArray.split("\\},\\s*\\{");
                
                for (String edge : edges) {
                    edge = edge.replace("{", "").replace("}", "");
                    int u = extractInt(edge, "\"u\"");
                    int v = extractInt(edge, "\"v\"");
                    int w = extractInt(edge, "\"w\"");
                    if (w == -1) w = 1;
                    
                    if (u >= 0 && v >= 0) {
                        graph.addEdge(u, v, w);
                    }
                }
            }
        }
        
        return new GraphData(graph, name, description, source, weightModel);
    }
    
    private static int extractInt(String json, String key) {
        int pos = json.indexOf(key);
        if (pos == -1) return -1;
        
        int colonPos = json.indexOf(':', pos);
        if (colonPos == -1) return -1;
        
        int start = colonPos + 1;
        while (start < json.length() && !Character.isDigit(json.charAt(start)) && json.charAt(start) != '-') start++;
        
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        
        if (start < end) {
            try {
                return Integer.parseInt(json.substring(start, end).trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
    
    private static String extractString(String json, String key) {
        int pos = json.indexOf(key);
        if (pos == -1) return null;
        
        int colonPos = json.indexOf(':', pos);
        if (colonPos == -1) return null;
        
        int quoteStart = json.indexOf('"', colonPos);
        if (quoteStart == -1) return null;
        
        int quoteEnd = json.indexOf('"', quoteStart + 1);
        if (quoteEnd == -1) return null;
        
        return json.substring(quoteStart + 1, quoteEnd);
    }
    
    public static class GraphData {
        public final Graph graph;
        public final String name, description, weightModel;
        public final int source;
        
        public GraphData(Graph graph, String name, String description, int source, String weightModel) {
            this.graph = graph;
            this.name = name;
            this.description = description;
            this.source = source;
            this.weightModel = weightModel;
        }
        
        public String toString() {
            return String.format("%s: %d vertices, %d edges, source=%d, model=%s",
                name, graph.getVertices(), graph.getEdgeCount(), source, weightModel);
        }
    }
}
