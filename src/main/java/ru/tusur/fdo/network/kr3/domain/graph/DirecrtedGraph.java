package ru.tusur.fdo.network.kr3.domain.graph;


/**
 * User: oleg
 * Date: 30.11.13
 * Time: 0:43
 */
public class DirecrtedGraph {

    public static void main(String[] args) {

        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");
        Vertex v5 = new Vertex("v5");

        v1.addEdge(new Edge(v2, 10));
        v1.addEdge(new Edge(v3, 30));
        v1.addEdge(new Edge(v4, 50));
        v1.addEdge(new Edge(v5, 10));

        v3.addEdge(new Edge(v5, 10));

        v4.addEdge(new Edge(v2, 40));
        v4.addEdge(new Edge(v3, 20));

        v5.addEdge(new Edge(v1, 10));
        v5.addEdge(new Edge(v3, 10));
        v5.addEdge(new Edge(v4, 30));

        PathFinder dijkstraPath = new Dijkstra(v1);
        System.out.println("Path to v2: " + dijkstraPath.pathTo(v2));
        System.out.println("Path to v3: " + dijkstraPath.pathTo(v3));
        System.out.println("Path to v4: " + dijkstraPath.pathTo(v4));
        System.out.println("Path to v5: " + dijkstraPath.pathTo(v5));

    }

}