package ru.tusur.fdo.network.kr3.ui.report;

import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User: oleg
 * Date: 07.12.13
 * Time: 18:06
 */
public class IntensitiesPanel extends JPanel {

    private DefaultTableModel model = new DefaultTableModel();

    private JTable intensitiesTable = new JTable(model);

    ReportFrame frame;

    private List<Vertex> vertices;

    public IntensitiesPanel(ReportFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        vertices = this.frame.getVertices();
        addTable();
    }

    private void addTable(){
        for (Vertex vertex : vertices){
            model.addColumn(vertex.getName());
        }
        String[] rowData = new String[vertices.size()];
        for (int col = 0; col < model.getColumnCount(); col++){
            Vertex vertex = vertices.get(col);
            rowData[col] = String.format("%.2f", VertexReportService.intensity(vertex));
        }
        model.addRow(rowData);
        Container scrollPane = new JScrollPane(intensitiesTable);
        scrollPane.setSize(600, 400);
        add(scrollPane);
    }

}
