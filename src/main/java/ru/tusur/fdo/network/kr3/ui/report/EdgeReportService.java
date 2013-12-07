package ru.tusur.fdo.network.kr3.ui.report;

import ru.tusur.fdo.network.kr3.domain.graph.Edge;
import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

/**
 * User: oleg
 * Date: 07.12.13
 * Time: 18:29
 */
public class EdgeReportService {

    public static double intensity(Edge edge){
        return edge.getTarget().getWeight() * probability(edge);
    }

    public static double probability(Edge edge){
        double sum = 0.0;
        Vertex vertex = edge.getSource();
        for (Edge current : vertex.getEdges()){
            sum += current.getWeight();
        }
        return edge.getWeight() / sum;
    }

}
