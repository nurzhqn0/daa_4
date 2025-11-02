package graph.scc;

import graph.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Tarjan's SCC Algorithm
 */
public class TarjanSCCTest {

    @Test
    public void testSimpleCycle() {
        // Create graph with one cycle: 0->1->2->0
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult result = tarjan.findSCCs();

        assertEquals(1, result.getComponentCount());
        assertEquals(3, result.getLargestComponentSize());
        assertFalse(result.isDAG());
    }

    @Test
    public void testPureDAG() {
        // Create DAG: 0->1->2->3
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult result = tarjan.findSCCs();

        assertEquals(4, result.getComponentCount());
        assertEquals(1, result.getLargestComponentSize());
        assertTrue(result.isDAG());
    }

    @Test
    public void testMultipleSCCs() {
        // Two cycles: 0->1->0 and 2->3->2
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult result = tarjan.findSCCs();

        assertEquals(2, result.getComponentCount());
        assertEquals(2, result.getLargestComponentSize());
    }

    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);

        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult result = tarjan.findSCCs();

        assertEquals(1, result.getComponentCount());
        assertTrue(result.isDAG());
    }

    @Test
    public void testComplexGraph() {
        // Graph from example_tasks.json structure
        Graph graph = new Graph(8, true);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 1, 1); // Creates cycle 1->2->3->1
        graph.addEdge(4, 5, 2);
        graph.addEdge(5, 6, 5);
        graph.addEdge(6, 7, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult result = tarjan.findSCCs();

        assertEquals(6, result.getComponentCount());
        assertEquals(3, result.getLargestComponentSize());
        assertFalse(result.isDAG());
    }

    @Test
    public void testMetricsTracking() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        SCCResult result = tarjan.findSCCs();

        assertNotNull(result.getMetrics());
        assertTrue(result.getMetrics().getDFSVisits() > 0);
        assertTrue(result.getMetrics().getEdgeTraversals() > 0);
    }
}