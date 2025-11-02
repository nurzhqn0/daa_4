package metrics;
public class Metrics implements IMetrics {
    private long dfsVisits, edgeTraversals, relaxations, queueOperations, stackOperations, startTime, endTime;

    public Metrics() {
        reset();
    }

    public void incrementDFSVisits() {
        dfsVisits++;
    }

    public void incrementEdgeTraversals() {
        edgeTraversals++;
    }

    public void incrementRelaxations() {
        relaxations++;
    }

    public void incrementQueueOperations() {
        queueOperations++;
    }

    public void incrementStackOperations() {
        stackOperations++;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getDFSVisits() {
        return dfsVisits;
    }

    public long getEdgeTraversals() {
        return edgeTraversals;
    }

    public long getRelaxations() {
        return relaxations;
    }

    public long getQueueOperations() {
        return queueOperations;
    }

    public long getStackOperations() {
        return stackOperations;
    }

    public long getElapsedTimeNanos() {
        return endTime - startTime;
    }

    public double getElapsedTimeMillis() {
        return getElapsedTimeNanos() / 1_000_000.0;
    }

    public void reset() {
        dfsVisits = edgeTraversals = relaxations = queueOperations = stackOperations = startTime = endTime = 0;
    }

    public String report() {
        return String.format("Metrics: DFS=%d, Edges=%d, Relax=%d, Queue=%d, Stack=%d, Time=%.3fms\n",
                dfsVisits, edgeTraversals, relaxations, queueOperations, stackOperations, getElapsedTimeMillis());
    }
}