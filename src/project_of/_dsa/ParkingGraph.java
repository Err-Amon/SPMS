
package project_of._dsa;

import java.util.*;

public class ParkingGraph {
    private int vertices;
    private List<List<Edge>> adjacencyList;
    private Map<Integer, String> slotLabels;

    public static class Edge {
        int source;
        int destination;
        int weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public int getDestination() {
            return destination;
        }

        public int getSource() {
            return source;
        }
    }

    public ParkingGraph(int vertices) {
        this.vertices = vertices;
        adjacencyList = new ArrayList<>();
        slotLabels = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
            slotLabels.put(i, "Slot " + (i + 1));
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjacencyList.get(source).add(new Edge(source, destination, weight));
        adjacencyList.get(destination).add(new Edge(destination, source, weight));
    }

    public void setSlotLabel(int slotNumber, String label) {
        slotLabels.put(slotNumber - 1, label);
    }

    public String getSlotLabel(int slotNumber) {
        return slotLabels.getOrDefault(slotNumber - 1, "Slot " + slotNumber);
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getAdjacencyList(int vertex) {
        return adjacencyList.get(vertex);
    }

    public List<Integer> bfs(int startVertex) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        visited[startVertex] = true;
        queue.add(startVertex);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);
            for (Edge edge : adjacencyList.get(current)) {
                if (!visited[edge.getDestination()]) {
                    visited[edge.getDestination()] = true;
                    queue.add(edge.getDestination());
                }
            }
        }
        return result;
    }

    public List<Integer> dfs(int startVertex) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        dfsTraversal(startVertex, visited, result);
        return result;
    }

    private void dfsTraversal(int vertex, boolean[] visited, List<Integer> result) {
        visited[vertex] = true;
        result.add(vertex);
        for (Edge edge : adjacencyList.get(vertex)) {
            if (!visited[edge.getDestination()]) {
                dfsTraversal(edge.getDestination(), visited, result);
            }
        }
    }

    public Map<Integer, Integer> dijkstraShortestPath(int startVertex) {
        Map<Integer, Integer> distances = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        for (int i = 0; i < vertices; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(startVertex, 0);
        pq.add(new int[]{startVertex, 0});
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int dist = current[1];
            if (dist > distances.get(u)) {
                continue;
            }
            for (Edge edge : adjacencyList.get(u)) {
                int newDist = dist + edge.getWeight();
                if (newDist < distances.get(edge.getDestination())) {
                    distances.put(edge.getDestination(), newDist);
                    pq.add(new int[]{edge.getDestination(), newDist});
                }
            }
        }
        return distances;
    }

    public List<List<Integer>> getAllPaths(int source, int destination) {
        List<List<Integer>> paths = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        List<Integer> currentPath = new ArrayList<>();
        currentPath.add(source);
        findAllPaths(source, destination, visited, currentPath, paths);
        return paths;
    }

    private void findAllPaths(int current, int destination, boolean[] visited,
                              List<Integer> currentPath, List<List<Integer>> paths) {
        if (current == destination) {
            paths.add(new ArrayList<>(currentPath));
            return;
        }
        visited[current] = true;
        for (Edge edge : adjacencyList.get(current)) {
            if (!visited[edge.getDestination()]) {
                currentPath.add(edge.getDestination());
                findAllPaths(edge.getDestination(), destination, visited, currentPath, paths);
                currentPath.remove(currentPath.size() - 1);
            }
        }
        visited[current] = false;
    }

    public boolean isConnected() {
        return bfs(0).size() == vertices;
    }

    public int getMinimumSpanningTreeWeight() {
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        int totalWeight = 0;
        pq.add(new Edge(-1, 0, 0));
        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            if (visited[edge.getDestination()]) {
                continue;
            }
            visited[edge.getDestination()] = true;
            totalWeight += edge.getWeight();
            for (Edge next : adjacencyList.get(edge.getDestination())) {
                if (!visited[next.getDestination()]) {
                    pq.add(next);
                }
            }
        }
        return totalWeight;
    }
}
