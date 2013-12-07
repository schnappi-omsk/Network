package ru.tusur.fdo.network.kr3.ui.report;

import ru.tusur.fdo.network.kr3.domain.graph.Edge;
import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User: oleg
 * Date: 07.12.13
 * Time: 19:39
 */
public class TimeOutPanel extends JPanel {

    private DefaultTableModel model = new DefaultTableModel();

    private JTable timeOutTable = new JTable(model);

    ReportFrame frame;

    private List<Vertex> vertices;

    public TimeOutPanel(ReportFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        vertices = frame.getVertices();
        addTable();
    }

    private void addTable(){
        model.addColumn("");
        for (Vertex vertex : vertices){
            model.addColumn(vertex.getName());
        }
        for (Vertex vertex : vertices){
            String[] rowData = new String[vertices.size() + 1];
            rowData[0] = vertex.getName();
            for (int col = 1; col < model.getColumnCount(); col++){
                Vertex innerVertex = vertices.get(col - 1);
                if (innerVertex == vertex){
                    rowData[col] = ReportFrame.PLACEHOLDER;
                    continue;
                }
                for (Edge edge : vertex.getEdges()){
                    if (edge.getTarget() == innerVertex){
                        double timeOut =
                                (1 / (VertexReportService.intensity(innerVertex) + vertex.getWeight())
                                * (EdgeReportService.intensity(edge) / edge.getWeight() - EdgeReportService.intensity(edge))
                        );
                        timeOut = Math.abs(timeOut);
                        rowData[col] = String.format("%.2f", timeOut);
                    }
                }
            }
            model.addRow(rowData);
        }
        Container scrollPane = new JScrollPane(timeOutTable);
        add(scrollPane);
    }

}
