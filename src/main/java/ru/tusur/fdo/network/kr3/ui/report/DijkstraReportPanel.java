package ru.tusur.fdo.network.kr3.ui.report;

import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User: oleg
 * Date: 07.12.13
 * Time: 20:01
 */
public class DijkstraReportPanel extends JPanel{

    private DefaultTableModel model = new DefaultTableModel();

    private JTable dijkstraTable = new JTable(model);

    ReportFrame frame;

    private List<Vertex> vertices;

    public DijkstraReportPanel(ReportFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        vertices = this.frame.getPath();
        dijkstraTable.setSize(600, 400);
        if (vertices == null){
            JLabel message = new JLabel("Сначала вычислите путь");
            Container contentPane = getRootPane();
            contentPane.add(message);
        } else addTable();
    }

    private void addTable(){
        if (vertices == null) return;
        for (Vertex vertex : vertices){
            model.addColumn(vertex.getName());
        }
        String[] rowData = new String[vertices.size()];
        for (int col = 0; col < model.getColumnCount(); col++){
            Vertex vertex = vertices.get(col);
            rowData[col] = String.valueOf(vertex.getMinDistance());
        }
        model.addRow(rowData);
        Container scrollPane = new JScrollPane(dijkstraTable);
        scrollPane.setSize(600, 400);
        add(scrollPane);
    }

}
