import java.util.*;

public class KruskalMST {
    public static List<Edge> kruskalMST(List<Edge> edges, int numVertices) {
        // Sort the edges in ascending order of weight.
        Collections.sort(edges);

        List<Edge> minimumSpanningTree = new ArrayList<>();
        DisjointSet disjointSet = new DisjointSet(numVertices);
        // Start measuring space usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Start measuring time
        long startTime = System.nanoTime();

        // Iterate through sorted edges and add to MST if it doesn't form a cycle.
        for (Edge edge : edges) {
            int srcRoot = disjointSet.find(edge.src);
            int destRoot = disjointSet.find(edge.dest);

            if (srcRoot != destRoot) {
                minimumSpanningTree.add(edge);
                disjointSet.union(srcRoot, destRoot);
            }
        }

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

        System.out.println("Enter the number of vertices:");
        int numVertices = scanner.nextInt(); // User specifies the number of vertices.

        List<Edge> edges = generateGraph(graphType, representation, numVertices);

        long startTime = System.currentTimeMillis();

        List<Edge> minimumSpanningTree = kruskalMST(edges, numVertices);

        long endTime = System.currentTimeMillis();

        // Calculate space efficiency.
        long spaceUsed = calculateSpaceUsage(graphType, representation, numVertices, edges.size());

        // Calculate time efficiency.
        long executionTime = endTime - startTime;

        // Display results to the user.
        displayResults(minimumSpanningTree);

        // Close the scanner when you're done.
        scanner.close();
    }

    private static List<Edge> generateGraph(int graphType, int representation, int numVertices) {
        List<Edge> edges = new ArrayList<>();

        Random rand = new Random();

        if (graphType == 1) { // Dense Graph
            if (representation == 1) { // Adjacency Matrix
                // Generate a dense graph using an adjacency matrix.
                for (int i = 0; i < numVertices; i++) {
                    for (int j = i + 1; j < numVertices; j++) {
                        int weight = rand.nextInt(100); // Adjust the range as needed.
                        edges.add(new Edge(i, j, weight));
                    }
                }
            } else if (representation == 2) { // Adjacency List
                // Generate a dense graph using an adjacency list.
                for (int i = 0; i < numVertices; i++) {
                    for (int j = i + 1; j < numVertices; j++) {
                        int weight = rand.nextInt(100); // Adjust the range as needed.
                        edges.add(new Edge(i, j, weight));
                        edges.add(new Edge(j, i, weight)); // Add the reverse edge for undirected graph.
                    }
                }
            }
        } else if (graphType == 2) { // Sparse Graph
            if (representation == 1) { // Adjacency Matrix
                for (int i = 0; i < numVertices; i++) {
                    for (int j = i + 1; j < numVertices; j++) {
                        if (rand.nextDouble() <= 0.2) {
                            int weight = rand.nextInt(100); // Adjust the weight range as needed.
                            edges.add(new Edge(i, j, weight));
                            edges.add(new Edge(j, i, weight)); // Add the reverse edge for undirected graph.
                        }
                    }
                }
            } else if (representation == 2) { // Adjacency List
                List<List<Edge>> adjacencyList = generateSparseGraphList(numVertices, 5); // Adjust maxNeighbors as needed.

                // Convert the adjacency list to a list of edges for further processing if needed.
                edges = convertAdjacencyListToEdges(adjacencyList);
            }
        }

        return edges;
    }
    private static List<Edge> convertAdjacencyListToEdges(List<List<Edge>> adjacencyList) {
        List<Edge> edges = new ArrayList<>();

        for (List<Edge> neighbors : adjacencyList) {
            edges.addAll(neighbors);
        }

        return edges;
    }

    private static List<List<Edge>> generateSparseGraphList(int numVertices, int maxNeighbors) {
        List<List<Edge>> adjacencyList = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numVertices; i++) {
            List<Edge> neighbors = new ArrayList<>();
            int numNeighbors = rand.nextInt(maxNeighbors + 1); // Random number of neighbors.

            for (int j = 0; j < numNeighbors; j++) {
                int neighbor = rand.nextInt(numVertices); // Random neighbor vertex.
                int weight = rand.nextInt(100); // Adjust the weight range as needed.
                neighbors.add(new Edge(i, neighbor, weight));
            }

            adjacencyList.add(neighbors);
        }

        return adjacencyList;
    }

    private static long calculateSpaceUsage(int graphType, int representation, int numVertices, int numEdges) {
        long spaceUsed = 0;

        if (representation == 1) { // Adjacency Matrix
            if (graphType == 1) { // Dense Graph
                // Calculate space for a dense graph using an adjacency matrix.
                // Assuming each element takes 4 bytes (int).
                spaceUsed = (long) numVertices * numVertices * 4;
            } else if (graphType == 2) { // Sparse Graph
                // Calculate space for a sparse graph using an adjacency matrix.
                // Assuming each element takes 4 bytes (int).
                // You may need to customize this for your specific sparse graph implementation.
                spaceUsed = (long) numVertices * numVertices * 4;
            }
        } else if (representation == 2) { // Adjacency List
            if (graphType == 1) { // Dense Graph
                // Calculate space for a dense graph using an adjacency list.
                // Assuming each edge takes 12 bytes (int + int + int).
                spaceUsed = (long) numEdges * 12;
            } else if (graphType == 2) { // Sparse Graph
                // Calculate space for a sparse graph using an adjacency list.
                // Assuming each edge takes 12 bytes (int + int + int).
                // You may need to customize this for your specific sparse graph implementation.
                spaceUsed = (long) numEdges * 12;
            }
        }

        return spaceUsed;
    }

    private static void displayResults(List<Edge> minimumSpanningTree) {
        System.out.println("Minimum Spanning Tree:");
        for (Edge edge : minimumSpanningTree) {
            System.out.println("Edge: " + edge.src + " - " + edge.dest);
        }

    }
}
