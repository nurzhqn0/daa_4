package metrics;
public interface IMetrics {
    void incrementDFSVisits();
    void incrementEdgeTraversals();
    void incrementRelaxations();
    void incrementQueueOperations();
    void incrementStackOperations();
    void startTimer();
    void stopTimer();
    long getDFSVisits();
    long getEdgeTraversals();
    long getRelaxations();
    long getQueueOperations();
    long getStackOperations();
    long getElapsedTimeNanos();
    double getElapsedTimeMillis();
    void reset();
    String report();
}