import graph.*;
import graph.scc.*;
import graph.topo.*;
import graph.dag.*;
import utils.*;
import java.io.*;
import java.util.*;

public class Main {

    private static void printSeparator() {
        System.out.println("================================================================================");
    }

    public static void main(String[] args) {
        // change if you want other file
        String filename = "data/example_tasks.json";

        if (args.length >= 1) {
            filename = args[0];
        }

        printSeparator();
        System.out.println("Smart City Task Scheduler");
        printSeparator();
        System.out.println();

        try {
            // Load graph
            System.out.println("Loading graph from: " + filename);
            GraphLoader.GraphData data = GraphLoader.loadFromFile(filename);
            Graph graph = data.graph;

            System.out.println(data);
            System.out.println();
            System.out.println(graph);
            System.out.println();

            // Step 1 Find SCC
            printSeparator();
            System.out.println("STEP 1: Finding Strongly Connected Components (Tarjan's Algorithm)");
            printSeparator();

            TarjanSCC tarjan = new TarjanSCC(graph);
            SCCResult sccResult = tarjan.findSCCs();
            System.out.println(sccResult);
            System.out.println();

            // Step 2 Build Condensation Graph
            printSeparator();
            System.out.println("STEP 2: Building Condensation Graph (DAG of SCCs)");
            printSeparator();

            CondensationGraph condensation = new CondensationGraph(graph, sccResult);
            Graph dag = condensation.getCondensedGraph();
            System.out.println(condensation);
            System.out.println();

            // Step 3 Topological Sort
            printSeparator();
            System.out.println("STEP 3: Topological Sort of Condensation DAG");
            printSeparator();

            System.out.println("Method 2: DFS-based Topological Sort");
            DFSTopologicalSort dfs = new DFSTopologicalSort(dag);
            TopologicalSortResult dfsResult = dfs.sort();
            System.out.println(dfsResult);
            System.out.println();

            // Step 4 Shortest Paths
            printSeparator();
            System.out.println("STEP 4: Shortest Paths in DAG");
            printSeparator();

            if (sccResult.isDAG()) {
                System.out.println("Graph is a DAG - computing shortest paths from source " + data.source);
                DAGShortestPath sp = new DAGShortestPath(graph);
                PathResult spResult = sp.findShortestPaths(data.source);
                System.out.println(spResult);
                System.out.println();

                int[] dist = spResult.getDistances();
                int[] pred = spResult.getPredecessors();
                System.out.println("Paths from source " + data.source + ":");
                for (int i = 0; i < dist.length; i++) {
                    if (spResult.isReachable(i) && i != data.source) {
                        List<Integer> path = DAGShortestPath.reconstructPath(pred, data.source, i);
                        System.out.println("  To " + i + " (distance=" + dist[i] + "): " + path);
                    }
                }
                System.out.println();
            } else {
                System.out.println("Original graph has cycles - using condensation DAG");
                DAGShortestPath sp = new DAGShortestPath(dag);
                PathResult spResult = sp.findShortestPaths(0);
                System.out.println(spResult);
                System.out.println();
            }

            // Step 5 Longest Path
            printSeparator();
            System.out.println("STEP 5: Longest Path (Critical Path)");
            printSeparator();

            Graph pathGraph = sccResult.isDAG() ? graph : dag;
            DAGLongestPath lp = new DAGLongestPath(pathGraph);
            DAGLongestPath.CriticalPathInfo critical = lp.findCriticalPath();

            if (critical != null) {
                System.out.println(critical);
                PathResult lpResult = lp.findLongestPath();
                System.out.println(lpResult);
            } else {
                System.out.println("Could not compute critical path");
            }

            System.out.println();
            printSeparator();
            System.out.println("Analysis Complete!");
            printSeparator();

        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            System.err.println("Make sure the file exists at: " + filename);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error during analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }
}