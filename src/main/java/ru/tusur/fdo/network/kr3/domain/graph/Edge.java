package ru.tusur.fdo.network.kr3.domain.graph;

/**
 * User: oleg
 * Date: 30.11.13
 * Time: 14:06
 */
public class Edge {

    private double weight;

    private Vertex target;

    private Vertex source;

    public Edge(final Vertex target, final double weight) {
        this.target = target;
        this.weight = weight;
    }

    public Vertex getTarget() {
        return target;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source){
        this.source = source;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
