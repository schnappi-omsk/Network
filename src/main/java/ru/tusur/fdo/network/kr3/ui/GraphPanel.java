package ru.tusur.fdo.network.kr3.ui;

import ru.tusur.fdo.network.kr3.domain.graph.Edge;
import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;

/**
 * User: oleg
 * Date: 03.12.13
 * Time: 15:15
 */
public class GraphPanel extends JPanel {

    public static final int VIEW_MODE = 0;

    public static final int ADD_VERTEX_MODE = 1;

    public static final int ADD_EDGE_MODE = 2;

    private static final double DIAMETER = 30;

    private HashMap<Vertex, Ellipse2D> vertices;

    private HashMap<Edge, Line2D> edges;

    private Ellipse2D selected;

    private JFrame frame;

    private int mode;

    public GraphPanel(JFrame frame){
        vertices = new HashMap<Vertex, Ellipse2D>();
        edges = new HashMap<Edge, Line2D>();
        addMouseListener(new ViewModeMouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
        this.frame = frame;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void add(Point2D point, Vertex vertex){
        double x = point.getX();
        double y = point.getY();

        selected = new Ellipse2D.Double(x, y, DIAMETER, DIAMETER);
        vertices.put(vertex, selected);
        repaint();
    }

    public Ellipse2D find(Point2D point){
        for (Vertex vertex : vertices.keySet()){
            Ellipse2D ellipse = vertices.get(vertex);
            if (ellipse.getFrame().contains(point)){
                repaint();
                return ellipse;
            }
        }
        return null;
    }

    public Vertex findVertex(Ellipse2D ellipse){
        for (Vertex current : vertices.keySet()){
            if (vertices.get(current) == ellipse){
                return current;
            }
        }
        return null;
    }

    public void remove(Vertex vertex){
        if (vertex == null) return;
        if (vertices.get(vertex) == selected) selected = null;
        vertices.remove(vertex);
        repaint();
    }

    public void drawEdge(Vertex from, Vertex to){
        if (from.hasEdgeTo(to)) return;
        Ellipse2D ellipseFrom = vertices.get(from);
        if (ellipseFrom == null) return;
        Ellipse2D ellipseTo = vertices.get(to);
        if (ellipseTo == null) return;
        Line2D wire = new Line2D.Double(ellipseFrom.getCenterX(), ellipseFrom.getCenterY(),
                ellipseTo.getCenterX(), ellipseTo.getCenterY());
        Edge edge = new Edge(to, 0);
        from.addEdge(edge);
        edges.put(edge, wire);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Vertex vertex : vertices.keySet()){
            Ellipse2D ellipse = vertices.get(vertex);
            g2d.setPaint(ellipse == selected ? Colors.SELECTION_COLOR : Colors.DEFAULT_VERTEX_COLOR);
            g2d.draw(ellipse);
            g2d.fill(ellipse);
        }
        for (Edge edge : edges.keySet()){
            Line2D line = edges.get(edge);
            g2d.setPaint(Colors.DEFAULT_VERTEX_COLOR);
            g2d.draw(line);
        }
    }

    private class ViewModeMouseHandler extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            Vertex vertexTo;
            Vertex vertexFrom = null;
            if (selected != null && e.getButton() == MouseEvent.BUTTON3){
                vertexFrom = findVertex(selected);
            }
            selected = find(e.getPoint());
            if (selected == null){
                vertexTo = new Vertex("x=" + e.getX() + "y=" + e.getY());
                add(e.getPoint(), vertexTo);
            } else {
                vertexTo = findVertex(selected);
            }
            if (vertexFrom != null  && e.getButton() == MouseEvent.BUTTON3){
                drawEdge(vertexFrom, vertexTo);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class MouseMotionHandler implements MouseMotionListener{
        @Override
        public void mouseDragged(MouseEvent e) {
            if (selected != null){
                int x = e.getX();
                int y = e.getY();
                selected.setFrame(x - DIAMETER / 2, y - DIAMETER / 2, DIAMETER, DIAMETER);
                Vertex vertex = findVertex(selected);
                java.util.List<Edge> edgeList = vertex.getEdges();
                for (Edge current : edgeList){
                    Vertex associated = current.getTarget();
                    Ellipse2D associatedEllipse = vertices.get(associated);
                    double xTo = associatedEllipse.getCenterX();
                    double yTo = associatedEllipse.getCenterY();
                    Line2D wire = edges.get(current);
                    wire.setLine(x,y, xTo, yTo);
                }
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

}
