package graph.topo;

import graph.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for DFS-based Topological Sort
 */
public class DFSTopologicalSortTest {

    @Test
    public void testSimpleDAG() {
        // Linear DAG: 0->1->2->3
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        DFSTopologicalSort topo = new DFSTopologicalSort(graph);
        TopologicalSortResult result = topo.sort();

        assertFalse(result.hasCycle());
        assertTrue(result.isValid());
        assertEquals(4, result.getOrder().size());
    }

    @Test
    public void testDAGWithBranching() {
        // Diamond: 0->1, 0->2, 1->3, 2->3
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        DFSTopologicalSort topo = new DFSTopologicalSort(graph);
        TopologicalSortResult result = topo.sort();

        assertFalse(result.hasCycle());
        assertEquals(4, result.getOrder().size());

        // Verify topological order: 0 should come before 1,2,3
        int pos0 = result.getOrder().indexOf(0);
        int pos1 = result.getOrder().indexOf(1);
        int pos2 = result.getOrder().indexOf(2);
        int pos3 = result.getOrder().indexOf(3);

        assertTrue(pos0 < pos1);
        assertTrue(pos0 < pos2);
        assertTrue(pos1 < pos3);
        assertTrue(pos2 < pos3);
    }

    @Test
    public void testCycleDetection() {
        // Cycle: 0->1->2->0
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        DFSTopologicalSort topo = new DFSTopologicalSort(graph);
        TopologicalSortResult result = topo.sort();

        assertTrue(result.hasCycle());
        assertFalse(result.isValid());
    }

    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);

        DFSTopologicalSort topo = new DFSTopologicalSort(graph);
        TopologicalSortResult result = topo.sort();

        assertFalse(result.hasCycle());
        assertEquals(1, result.getOrder().size());
        assertEquals(0, result.getOrder().get(0));
    }

    @Test
    public void testDisconnectedDAG() {
        // Two separate chains: 0->1 and 2->3
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        DFSTopologicalSort topo = new DFSTopologicalSort(graph);
        TopologicalSortResult result = topo.sort();

        assertFalse(result.hasCycle());
        assertEquals(4, result.getOrder().size());
    }

    @Test
    public void testMetrics() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        DFSTopologicalSort topo = new DFSTopologicalSort(graph);
        TopologicalSortResult result = topo.sort();

        assertNotNull(result.getMetrics());
        assertEquals(3, result.getMetrics().getDFSVisits());
        assertTrue(result.getMetrics().getStackOperations() > 0);
    }
}