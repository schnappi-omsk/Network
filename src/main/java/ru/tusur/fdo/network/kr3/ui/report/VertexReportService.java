package ru.tusur.fdo.network.kr3.ui.report;

import ru.tusur.fdo.network.kr3.domain.graph.Edge;
import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

/**
 * User: oleg
 * Date: 07.12.13
 * Time: 18:08
 */
public class VertexReportService {


    public static double intensity(Vertex vertex){
        double intensity = 0.0;
        for (Edge edge : vertex.getEdges()){
            intensity += EdgeReportService.intensity(edge);
        }
        return intensity;
    }


}
