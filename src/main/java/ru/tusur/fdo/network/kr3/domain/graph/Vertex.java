package ru.tusur.fdo.network.kr3.domain.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: oleg
 * Date: 30.11.13
 * Time: 14:06
 */
public class Vertex implements Comparable<Vertex>, Serializable {

    private double weight;

    private List<Edge> edges = new ArrayList<Edge>();

    private Vertex previous;

    private String name;

    private double minDistance = Double.POSITIVE_INFINITY;

    public Vertex(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public void addEdge(Edge edge){
        edges.add(edge);
        edge.setSource(this);
    }

    public boolean hasEdgeTo(Vertex vertex){
        if (vertex == null) return false;
        for (Edge edge : edges){
            if (this.equals(edge.getTarget())) return true;
        }
        return false;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(minDistance, o.minDistance);
    }

    @Override
    public String toString() {
        return name;
    }
}
