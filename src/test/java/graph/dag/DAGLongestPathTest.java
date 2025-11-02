package graph.dag;

import graph.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for DAG Longest Path (Critical Path) Algorithm
 */
public class DAGLongestPathTest {

    @Test
    public void testSimplePath() {
        // Linear graph: 0->1->2->3
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 2);

        DAGLongestPath lp = new DAGLongestPath(graph);
        PathResult result = lp.findLongestPath();

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

        DAGLongestPath lp = new DAGLongestPath(graph);
        PathResult result = lp.findLongestPath();

        assertFalse(result.hasCycle());
        assertEquals(8, result.getDistance(3)); // 0->1->3 = 5+3 = 8
    }

    @Test
    public void testCriticalPath() {
        // Project scheduling graph
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);

        DAGLongestPath lp = new DAGLongestPath(graph);
        DAGLongestPath.CriticalPathInfo critical = lp.findCriticalPath();

        assertNotNull(critical);
        assertEquals(9, critical.length); // 0->1->3->4 = 3+4+2
        assertTrue(critical.path.size() > 0);
    }

    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);

        DAGLongestPath lp = new DAGLongestPath(graph);
        PathResult result = lp.findLongestPath();

        assertEquals(0, result.getDistance(0));
    }

    @Test
    public void testDisconnectedGraph() {
        // Two separate paths
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(2, 3, 3);

        DAGLongestPath lp = new DAGLongestPath(graph);
        DAGLongestPath.CriticalPathInfo critical = lp.findCriticalPath();

        assertNotNull(critical);
        assertEquals(5, critical.length); // Longest is 0->1
    }

    @Test
    public void testMetrics() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);

        DAGLongestPath lp = new DAGLongestPath(graph);
        PathResult result = lp.findLongestPath();

        assertNotNull(result.getMetrics());
        assertTrue(result.getMetrics().getEdgeTraversals() > 0);
        assertTrue(result.getMetrics().getRelaxations() > 0);
    }

    @Test
    public void testComplexCriticalPath() {
        // Complex project with multiple paths
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 1);
        graph.addEdge(2, 4, 4);
        graph.addEdge(3, 5, 2);
        graph.addEdge(4, 5, 3);

        DAGLongestPath lp = new DAGLongestPath(graph);
        DAGLongestPath.CriticalPathInfo critical = lp.findCriticalPath();

        assertNotNull(critical);
        assertTrue(critical.length >= 9); // Multiple long paths
        assertEquals(5, critical.end); // Should end at vertex 5
    }
}