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
 * Time: 16:17
 */
public class VerticesWeightsTablePanel extends JPanel{

    private DefaultTableModel model = new DefaultTableModel();

    private JTable weightsTable = new JTable(model);

    ReportFrame frame;

    private List<Vertex> vertices;

    public VerticesWeightsTablePanel(ReportFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        vertices = this.frame.getVertices();
        weightsTable.setSize(600, 400);
        addTable();
    }

    private void addTable(){
        for (Vertex vertex : vertices){
            model.addColumn(vertex.getName());
        }
        String[] rowData = new String[vertices.size()];
        for (int col = 0; col < model.getColumnCount(); col++){
            Vertex vertex = vertices.get(col);
            rowData[col] = String.valueOf(vertex.getWeight());
        }
        model.addRow(rowData);
        Container scrollPane = new JScrollPane(weightsTable);
        scrollPane.setSize(600, 400);
        add(scrollPane);
    }
}
