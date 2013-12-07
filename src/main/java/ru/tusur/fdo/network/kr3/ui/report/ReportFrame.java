package ru.tusur.fdo.network.kr3.ui.report;

import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * User: oleg
 * Date: 07.12.13
 * Time: 16:05
 */
public class ReportFrame extends JFrame{

    public static final String PLACEHOLDER = "---";

    public static final int WIDTH = 640;

    public static final int HEIGHT = 480;

    private JTabbedPane tabbedPane = new JTabbedPane();

    private java.util.List<Vertex> vertices;

    private java.util.List<Vertex> path;

    public ReportFrame(java.util.List<Vertex> vertices, List<Vertex> path) {
        this.vertices = vertices;
        this.path = path;
        setSize(WIDTH, HEIGHT);
        tabbedPane.addTab("Трафик", new WeightsTablePanel(this));
        tabbedPane.addTab("Каналы", new VerticesWeightsTablePanel(this));
        tabbedPane.addTab("Интенсивность в узлах", new IntensitiesPanel(this));
        tabbedPane.addTab("Задержки", new TimeOutPanel(this));
        tabbedPane.addTab("Суммарный путь", new DijkstraReportPanel(this));
        Container contentPane = getContentPane();
        contentPane.add(tabbedPane);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Vertex> getPath() {
        return Collections.unmodifiableList(path);
    }
}
