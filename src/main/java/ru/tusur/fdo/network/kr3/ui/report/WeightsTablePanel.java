package ru.tusur.fdo.network.kr3.ui.report;

import ru.tusur.fdo.network.kr3.domain.graph.Edge;
import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * User: oleg
 * Date: 07.12.13
 * Time: 16:11
 */
public class WeightsTablePanel extends JPanel {

    private DefaultTableModel model = new DefaultTableModel();

    private JTable weightsTable = new JTable(model);

    ReportFrame frame;

    private List<Vertex> vertices;

    public WeightsTablePanel(ReportFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.frame = frame;
        vertices = this.frame.getVertices();
        weightsTable.setSize(600, 400);
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
                    if (edge.getTarget() == innerVertex) rowData[col] = String.valueOf(edge.getWeight());
                }
            }
            model.addRow(rowData);
        }
        Container scrollPane = new JScrollPane(weightsTable);
        scrollPane.setSize(600, 400);
        add(scrollPane);
    }

}
