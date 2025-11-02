package graph.dag;

import graph.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Test suite for DAG Shortest Path Algorithm
 */
public class DAGShortestPathTest {

    @Test
    public void testSimplePath() {
        // Linear graph: 0->1->2->3 with weights
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 2);

        DAGShortestPath sp = new DAGShortestPath(graph);
        PathResult result = sp.findShortestPaths(0);

        assertFalse(result.hasCycle());
        assertEquals(0, result.getDistance(0));
        assertEquals(5, result.getDistance(1));
        assertEquals(8, result.getDistance(2));
        assertEquals(10, result.getDistance(3));
    }

    @Test
    public void testMultiplePaths() {
        // Diamond: 0->1(5), 0->2(2), 1->3(3), 2->3(4)
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 4);

        DAGShortestPath sp = new DAGShortestPath(graph);
        PathResult result = sp.findShortestPaths(0);

        assertFalse(result.hasCycle());
        assertEquals(6, result.getDistance(3)); // 0->2->3 = 2+4 = 6
    }

    @Test
    public void testUnreachableVertices() {
        // 0->1, 2->3 (disconnected)
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(2, 3, 3);

        DAGShortestPath sp = new DAGShortestPath(graph);
        PathResult result = sp.findShortestPaths(0);

        assertTrue(result.isReachable(1));
        assertFalse(result.isReachable(2));
        assertFalse(result.isReachable(3));
    }

    @Test
    public void testPathReconstruction() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 2);

        DAGShortestPath sp = new DAGShortestPath(graph);
        PathResult result = sp.findShortestPaths(0);

        List<Integer> path = DAGShortestPath.reconstructPath(
                result.getPredecessors(), 0, 3);

        assertEquals(4, path.size());
        assertEquals(0, path.get(0));
        assertEquals(1, path.get(1));
        assertEquals(2, path.get(2));
        assertEquals(3, path.get(3));
    }

    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);

        DAGShortestPath sp = new DAGShortestPath(graph);
        PathResult result = sp.findShortestPaths(0);

        assertEquals(0, result.getDistance(0));
    }

    @Test
    public void testMetrics() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);

        DAGShortestPath sp = new DAGShortestPath(graph);
        PathResult result = sp.findShortestPaths(0);

        assertNotNull(result.getMetrics());
        assertTrue(result.getMetrics().getEdgeTraversals() > 0);
        assertTrue(result.getMetrics().getRelaxations() > 0);
    }
}