package ru.tusur.fdo.network.kr3.domain.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * User: oleg
 * Date: 02.12.13
 * Time: 21:15
 */
public class Dijkstra implements PathFinder {

    private Vertex source;

    public Dijkstra(Vertex source){
        this.source = source;
    }

    public Dijkstra(){}

    public void setSource(Vertex source) {
        this.source = source;
    }

    private void computePaths(){
        source.setPrevious(null);
        source.setMinDistance(0.);
        PriorityQueue<Vertex> vertices = new PriorityQueue<Vertex>();
        vertices.add(source);

        while (!vertices.isEmpty()){
            Vertex w = vertices.poll();
            for (Edge current : w.getEdges()){
                Vertex dest = current.getTarget();
                double distanceSum = w.getMinDistance() + current.getWeight();
                if (distanceSum < dest.getMinDistance()){
                    vertices.remove(dest);
                    dest.setMinDistance(distanceSum);
                    dest.setPrevious(w);
                    vertices.add(dest);
                }
            }
        }

    }

    @Override
    public List<Vertex> pathTo(Vertex target) {
        computePaths();
        List<Vertex> result = new ArrayList<Vertex>();
        for(Vertex vertex = target; vertex != null; vertex = vertex.getPrevious()){
            result.add(vertex);
        }
        Collections.reverse(result);
        return Collections.unmodifiableList(result);
    }
}
