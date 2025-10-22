import java.util.*;

public class Graph {
    private Map<String, List<String>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    public void addLocation(String location) {
        adjList.putIfAbsent(location, new ArrayList<>());
    }

    public void removeLocation(String location) {
        // Remove the vertex itself
        adjList.remove(location);

        // Remove all edges pointing to this vertex
        for (List<String> connections : adjList.values()) {
            connections.remove(location);
        }
    }

    public void addRoad(String from, String to) {
        if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
            System.out.println("Error: One or both locations not found.");
            return;
        }

        // Add road (undirected)
        if (!adjList.get(from).contains(to)) {
            adjList.get(from).add(to);
        }
        if (!adjList.get(to).contains(from)) {
            adjList.get(to).add(from);
        }
    }

    public void removeRoad(String from, String to) {
        if (adjList.containsKey(from) && adjList.containsKey(to)) {
            adjList.get(from).remove(to);
            adjList.get(to).remove(from);
        } else {
            System.out.println("Error: One or both locations not found.");
        }
    }

    public void displayConnections() {
        if (adjList.isEmpty()) {
            System.out.println("No locations or connections to display.");
            return;
        }
        for (String loc : adjList.keySet()) {
            System.out.println(loc + " -> " + adjList.get(loc));
        }
    }

    /**
     * REQUIREMENT 4: Uses a Queue for a traversal (BFS) to find a path.
     */
    public void findPath(String start, String end) {
        if (!adjList.containsKey(start) || !adjList.containsKey(end)) {
            System.out.println("Error: One or both locations do not exist.");
            return;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>(); // To reconstruct path

        queue.add(start);
        visited.add(start);
        parentMap.put(start, null);

        boolean found = false;

        while (!queue.isEmpty()) {
            String current = queue.poll(); // Dequeue

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (String neighbor : adjList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current); // Track the path
                    queue.add(neighbor); // Enqueue
                }
            }
        }

        if (found) {
            // Reconstruct path
            List<String> path = new ArrayList<>();
            String current = end;
            while (current != null) {
                path.add(current);
                current = parentMap.get(current);
            }
            Collections.reverse(path);
            System.out.println("Path found: " + path);
        } else {
            System.out.println("No path found between " + start + " and " + end);
        }
    }
}