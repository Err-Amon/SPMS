
package project_of._dsa;

import java.util.*;

public class GreedyAllocator {

    private ParkingGraph graph;
    private int totalSlots;

    public GreedyAllocator(int totalSlots) {
        this.totalSlots = totalSlots;
        this.graph = new ParkingGraph(totalSlots);
        initializeParkingGraph();
    }

    private void initializeParkingGraph() {
        for (int i = 0; i < totalSlots - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }
    }

    public int allocateBestSlot(Vehicle vehicle) {
        int entrance = 0;
        Map<Integer, Integer> distances = graph.dijkstraShortestPath(entrance);

        List<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < totalSlots; i++) {
            if (!DataStructureManager.slotManager.isOccupied(i + 1)) {
                candidates.add(i);
            }
        }
        if (candidates.isEmpty()) {
            return -1;
        }
        int bestSlot = -1;
        int bestDistance = Integer.MAX_VALUE;

        for (int candidate : candidates) {
            int distance = distances.get(candidate);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestSlot = candidate;
            }
        }
        if (bestSlot == -1) {
            bestSlot = candidates.get(0);
        }
        return bestSlot + 1;
    }

    public List<Integer> findAllocateOrder() {
        List<Integer> order = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();
        boolean[] visitedArr = new boolean[totalSlots];
        dfsAllocationOrder(0, visitedArr, visited);
        return visited;
    }

    private void dfsAllocationOrder(int slot, boolean[] visited, List<Integer> order) {
        visited[slot] = true;
        order.add(slot);
        for (ParkingGraph.Edge edge : graph.getAdjacencyList(slot)) {
            if (!visited[edge.getDestination()]) {
                dfsAllocationOrder(edge.getDestination(), visited, order);
            }
        }
    }

    public List<Integer> bfsAllocationOrder() {
        return graph.bfs(0);
    }

    public Map<Integer, Integer> getShortestDistancesFromEntrance() {
        return graph.dijkstraShortestPath(0);
    }

    public ParkingGraph getParkingGraph() {
        return graph;
    }
}
