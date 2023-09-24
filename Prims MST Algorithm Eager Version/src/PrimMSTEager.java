import java.util.*;

public class PrimMSTEager {
    public static List<Edge> primMST(List<List<Edge>> adjList) {
        int numVertices = adjList.size();
        boolean[] visited = new boolean[numVertices];
        List<Edge> minimumSpanningTree = new ArrayList<>();
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));

        // Start measuring space usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Start measuring time
        long startTime = System.nanoTime();

        // Initialize with edges connected to the first vertex (vertex 0)
        for (Edge edge : adjList.get(0)) {
            minHeap.add(edge);
        }

        visited[0] = true; // Mark the first vertex as visited

        while (!minHeap.isEmpty()) {
            Edge currentEdge = minHeap.poll();
            int src = currentEdge.src;
            int dest = currentEdge.dest;

            if (visited[src] && visited[dest]) {
                continue; // Skip edges that connect visited vertices
            }

            visited[src] = true;
            visited[dest] = true;
            minimumSpanningTree.add(currentEdge);

            // Add neighboring edges to the minHeap if the neighboring vertex is not visited
            for (Edge neighbor : adjList.get(visited[src] ? dest : src)) {
                if (!visited[neighbor.dest]) {
                    minHeap.add(neighbor);
                }
            }
        }

        // Stop measuring time
        long endTime = System.nanoTime();
        double executionTimeMilliseconds = (endTime - startTime) / 1e6; // 1e9 nanoseconds in a second, 1000 milliseconds in a second

        // Stop measuring space usage (use max memory instead of total memory)
        long maxMemory = runtime.maxMemory();
        long spaceUsageBytes = usedMemoryBefore - runtime.freeMemory() + (maxMemory - usedMemoryBefore);
        double spaceUsageKB = spaceUsageBytes / (1024.0 * 1024.0); // Convert bytes to megabytes

        System.out.println("");
        System.out.println("");
        System.out.println("");

        System.out.println("Space Usage: " + spaceUsageKB + " MB");
        System.out.println("Execution Time: " + executionTimeMilliseconds + " milliseconds");

        return minimumSpanningTree;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose the type of graph:");
        System.out.println("1. Dense Graph");
        System.out.println("2. Sparse Graph");
        int graphType = scanner.nextInt();

        System.out.println("Choose the representation:");
        System.out.println("1. Adjacency Matrix");
        System.out.println("2. Adjacency List");
        int representation = scanner.nextInt();

        // Create the graph based on user input (dense or sparse) and representation.
        List<List<Edge>> adjList = generateGraph(graphType, representation);
        int numVertices = adjList.size();

        // Apply Prim's algorithm to find the MST.
        List<Edge> minimumSpanningTree = primMST(adjList);

        // Display results to the user.
        System.out.println("Minimum Spanning Tree Edges:");
        for (Edge edge : minimumSpanningTree) {
            System.out.println(edge.src + " - " + edge.dest );
        }

        // Close the scanner when you're done.
        scanner.close();
    }

    // Generate a random graph or create one based on user input.
    private static List<List<Edge>> generateGraph(int graphType, int representation) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of vertices: ");
        int numVertices = scanner.nextInt();

        if (representation == 1) {
            // Generate an adjacency matrix
            int[][] adjacencyMatrix = new int[numVertices][numVertices];

            Random random = new Random();
            double edgeProbability;

            if (graphType == 1) {
                // Dense graph (higher edge probability)
                edgeProbability = 0.6;
            } else {
                // Sparse graph (lower edge probability)
                edgeProbability = 0.2;
            }

            for (int src = 0; src < numVertices; src++) {
                for (int dest = src + 1; dest < numVertices; dest++) {
                    if (random.nextDouble() < edgeProbability) {
                        int weight = random.nextInt(100); // Random weight for the edge
                        adjacencyMatrix[src][dest] = weight;
                        adjacencyMatrix[dest][src] = weight; // Undirected graph
                    }
                }
            }

            // Convert the adjacency matrix to an adjacency list
            List<List<Edge>> adjList = new ArrayList<>(numVertices);
            for (int i = 0; i < numVertices; i++) {
                adjList.add(new ArrayList<>());
            }

            for (int src = 0; src < numVertices; src++) {
                for (int dest = src + 1; dest < numVertices; dest++) {
                    if (adjacencyMatrix[src][dest] > 0) {
                        adjList.get(src).add(new Edge(src, dest, adjacencyMatrix[src][dest]));
                        adjList.get(dest).add(new Edge(dest, src, adjacencyMatrix[src][dest])); // Undirected graph
                    }
                }
            }

            return adjList;
        } else if (representation == 2) {
            // Generate an adjacency list
            List<List<Edge>> adjList = new ArrayList<>(numVertices);

            for (int i = 0; i < numVertices; i++) {
                adjList.add(new ArrayList<>());
            }

            Random random = new Random();
            double edgeProbability;

            if (graphType == 1) {
                // Dense graph (higher edge probability)
                edgeProbability = 0.6;
            } else {
                // Sparse graph (lower edge probability)
                edgeProbability = 0.2;
            }

            for (int src = 0; src < numVertices; src++) {
                for (int dest = src + 1; dest < numVertices; dest++) {
                    if (random.nextDouble() < edgeProbability) {
                        int weight = random.nextInt(100); // Random weight for the edge
                        adjList.get(src).add(new Edge(src, dest, weight));
                        adjList.get(dest).add(new Edge(dest, src, weight)); // Undirected graph
                    }
                }
            }
            return adjList;
        } else {
            System.out.println("Invalid representation choice.");
            System.exit(1);
            return null; // This is to satisfy the compiler; it won't be reached.
        }
    }
}
