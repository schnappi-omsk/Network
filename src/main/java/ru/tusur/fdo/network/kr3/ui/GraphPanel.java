package ru.tusur.fdo.network.kr3.ui;

import ru.tusur.fdo.network.kr3.domain.graph.Dijkstra;
import ru.tusur.fdo.network.kr3.domain.graph.Edge;
import ru.tusur.fdo.network.kr3.domain.graph.Vertex;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * User: oleg
 * Date: 03.12.13
 * Time: 15:15
 */
public class GraphPanel extends JPanel {

    public static final int VIEW_MODE = 0;

    public static final int ADD_VERTEX_MODE = 1;

    public static final int ADD_EDGE_MODE = 2;

    public static final int EDIT_VERTEX_MODE = 3;

    public static final int EDIT_EDGE_MODE = 4;

    public static final int REMOVE_VERTEX_MODE = 5;

    public static final int REMOVE_EDGE_MODE = 6;

    public static final int SELECT_FIRST_VERTEX_MODE = 7;

    public static final int SELECT_LAST_VERTEX_MODE = 8;

    public static final int FIND_PATH_MODE = 9;

    public static final String VIEW = "Standard mode";

    public static final String ADD_VERTEX = "Add vertex";

    public static final String ADD_EDGE = "Add edge";

    public static final String EDIT_VERTEX = "Edit vertex";

    public static final String EDIT_EDGE = "Edit edge";

    public static final String REMOVE_VERTEX = "Remove vertex";

    public static final String REMOVE_EDGE = "Remove edge";

    public static final String FIND_PATH = "Shortest path";

    public static final String SELECT_FIRST_VERTEX = "Select first";

    public static final String SELECT_LAST_VERTEX = "Select last";

    private static final double DIAMETER = 30;

    private FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Graph files", "graph");

    private HashMap<Vertex, Ellipse2D> vertices;

    private HashMap<Edge, Line2D> edges;

    private Ellipse2D selectedVertex;

    private Line2D selectedEdge;

    private List<Vertex> selectedPathVertices = new ArrayList<Vertex>();

    private List<Edge> selectPathEdges = new ArrayList<Edge>();

    private JFrame frame;

    private EditPanel editor;

    private int mode;

    private Vertex vertexSource;

    private Vertex vertexTarget;

    private JButton save = new JButton("Сохранить");

    private JButton open = new JButton("Открыть");

    private MouseAdapter mouseListener = new ViewModeMouseHandler();

    public GraphPanel(JFrame frame){
        vertices = new HashMap<Vertex, Ellipse2D>();
        edges = new HashMap<Edge, Line2D>();
        addMouseListener(mouseListener);
        MouseMotionListener mouseMotionListener = new MouseMotionHandler();
        addMouseMotionListener(mouseMotionListener);
        setBackground(Color.WHITE);
        setLayout(null);
        this.frame = frame;
        int buttonW = 120;
        int buttonH = 20;
        open.setBounds(0, 0, buttonW, buttonH);
        save.setBounds(buttonW, 0, buttonW, buttonH);
        open.addActionListener(new GraphOpener());
        save.addActionListener(new GraphSaver());
        add(open);
        add(save);
    }

    public void setEditor(EditPanel editor) {
        this.editor = editor;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if (this.mode == mode) return;
        this.mode = mode;
        selectedVertex = null;
        repaint();
        removeMouseListener(mouseListener);
        if (mode == VIEW_MODE) mouseListener = new ViewModeMouseHandler();
        if (mode == ADD_VERTEX_MODE) mouseListener = new AddVertexMouseHandler();
        if (mode == ADD_EDGE_MODE) mouseListener = new AddEdgeMouseHandler();
        if (mode == REMOVE_VERTEX_MODE) mouseListener = new RemoveVertexMouseHandler();
        if (mode == EDIT_EDGE_MODE) mouseListener = new EditEdgeMouseHandler();
        if (mode == EDIT_VERTEX_MODE) mouseListener = new EditVertexMouseHandler();
        if (mode == REMOVE_EDGE_MODE) mouseListener = new RemoveEdgeMouseHandler();
        if (mode == FIND_PATH_MODE) drawPath();
        if (mode == SELECT_FIRST_VERTEX_MODE) mouseListener = new SelectFirstMouseHandler();
        if (mode == SELECT_LAST_VERTEX_MODE) mouseListener = new SelectLastMouseHandler();
        addMouseListener(mouseListener);
        editor.setMode();
    }

    public void setVertexName(String name){
        Vertex vertex = findVertex(selectedVertex);
        vertex.setName(name);
    }

    public String getVertexName(){
        if (selectedVertex == null) return "";
        Vertex vertex = findVertex(selectedVertex);
        return vertex.getName();
    }

    public void setEdgeWeight(double weight){
        Edge edge = findEdge(selectedEdge);
        edge.setWeight(weight);
    }

    public double getEdgeWeight(){
        if (selectedEdge == null) return 0.0;
        Edge edge = findEdge(selectedEdge);
        return edge.getWeight();
    }

    public void add(Point2D point, Vertex vertex){
        double x = point.getX();
        double y = point.getY();
        selectedVertex = new Ellipse2D.Double(x, y, DIAMETER, DIAMETER);
        repaint();
    }

    public Ellipse2D findVertexEllipse(Point2D point){
        for (Vertex vertex : vertices.keySet()){
            Ellipse2D ellipse = vertices.get(vertex);
            if (ellipse.getFrame().contains(point)){
                repaint();
                return ellipse;
            }
        }
        return null;
    }

    public boolean lineContains(Line2D line, Point2D point){
        return 5 > Line2D.ptSegDist(
                line.getX1(), line.getY1(),
                line.getX2(), line.getY2(),
                point.getX(), point.getY()
        );
    }

    public Line2D findEdgeLine(Point2D point){
        for (Edge edge : edges.keySet()){
            Line2D line = edges.get(edge);
            boolean isPointOnLine = lineContains(line, point);
            if (isPointOnLine){
                repaint();
                return line;
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

    public Edge findEdge(Line2D line){
        for (Edge current : edges.keySet()){
            if (edges.get(current) == line){
                return current;
            }
        }
        return null;
    }

    public void removeVertex(Vertex vertex){
        if (vertex == null) return;
        if (vertices.get(vertex) == selectedVertex) selectedVertex = null;
        removeVertexEdges(vertex);
        vertices.remove(vertex);
        repaint();
    }

    public void removeEdge(Edge edge){
        if (edge == null) return;
        if (edges.get(edge) == selectedEdge) selectedEdge = null;
        edges.remove(edge);
        repaint();
    }

    private void removeVertexEdges(Vertex vertex){
        List<Edge> edgeList = vertex.getEdges();
        for (Edge edge : edgeList){
            edges.remove(edge);
        }
        List<Edge> toDelete = new ArrayList<Edge>();
        for (Edge edge : edges.keySet()){
            if (edge.getTarget() == vertex){
                toDelete.add(edge);
            }
        }
        for (Edge edge : toDelete){
            edges.remove(edge);
        }

    }

    public void drawEdge(Vertex from, Vertex to){
        if (from == null) return;
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

    public void drawPath(){
        selectedPathVertices = null;
        Dijkstra pathFinder = new Dijkstra();
        if (vertexSource != null && vertexTarget != null){
            setInitialDistances();
            pathFinder.setSource(vertexSource);
            selectedPathVertices = pathFinder.pathTo(vertexTarget);
            selectPathEdges.clear();
            for (Vertex current : selectedPathVertices){
                if (current.getPrevious() == null) continue;
                List<Edge> edgeList = current.getPrevious().getEdges();
                for (Edge edge : edgeList){
                    if (edge.getTarget() == current) selectPathEdges.add(edge);
                }
            }
            repaint();
            vertexSource = null;
            vertexTarget = null;
            pathFinder = null;
        }
    }

    private void setInitialDistances(){
        for (Vertex vertex : vertices.keySet()){
            vertex.setMinDistance(Double.MAX_VALUE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.LIGHT_GRAY);
        drawBackground(g2d);
        Font font = new Font("Serif", Font.PLAIN, 10);
        FontMetrics metrics = g2d.getFontMetrics(font);
        g2d.setFont(font);
        for (Edge edge : edges.keySet()){
            Line2D line = edges.get(edge);
            g2d.setStroke(new BasicStroke(1));
            Color color;
            if (selectPathEdges.contains(edge)){
                color = Colors.PATH_COLOR;
            } else color = line == selectedEdge ? Colors.SELECTION_COLOR : Colors.DEFAULT_VERTEX_COLOR;
            g2d.setPaint(color);
            g2d.draw(line);
            String text = Double.toString(edge.getWeight());
            int centerX = (int) (line.getX1() + ((line.getX2() - line.getX1()) / 2));
            int centerY = (int) (line.getY1() + ((line.getY2() - line.getY1()) / 2));
            double deg = Math.toDegrees(
                    Math.atan2(centerY - line.getY2(), centerX - line.getX2()) + Math.PI
            );
            if ( deg > 90 && deg < 270 ) deg += 180;
            double angle = Math.toRadians(deg);
            int textWidth = metrics.stringWidth(text);
            g2d.rotate(angle, centerX, centerY);
            g2d.setPaint(Color.BLACK);
            g2d.drawString(text, centerX - (textWidth / 2), centerY - 5);
            g2d.rotate(-angle, centerX, centerY);
        }
        for (Vertex vertex : vertices.keySet()){
            Ellipse2D ellipse = vertices.get(vertex);
            Color color;
            if (selectedPathVertices != null && selectedPathVertices.contains(vertex)
                    || vertex == vertexSource || vertex == vertexTarget){
                color = Colors.PATH_COLOR;
            } else color = ellipse == selectedVertex ? Colors.SELECTION_COLOR : Colors.DEFAULT_VERTEX_COLOR;
            g2d.setPaint(color);
            g2d.draw(ellipse);
            g2d.fill(ellipse);
            g2d.setPaint(Color.BLACK);
            g2d.drawString(vertex.getName(),
                (int) ellipse.getCenterX() - (int) DIAMETER,
                (int) ellipse.getCenterY() - (int) DIAMETER / 2
            );
        }
    }

    private void drawBackground(Graphics2D g){
        int squareSide = 10;
        int horizontal = getWidth() * 2 / squareSide;
        int vertical = getHeight() * 2 / squareSide;
        int x = 0;
        int y = 20;
        for (int horizontalCounter = 0; horizontalCounter < horizontal; horizontalCounter++ ){
            for (int verticalCounter = 0; verticalCounter < vertical; verticalCounter++){
                g.drawRect(x, y, squareSide, squareSide);
                x += squareSide;
            }
            y += squareSide;
            x = 0;
        }
    }

    private void onVertexSelect(){
        editor.setVertexName(getVertexName());
    }

    private void onEdgeSelect(){
        editor.setEdgeWeight(getEdgeWeight());
    }

    private void clearPath(){
        vertexSource = null;
        vertexTarget = null;
        selectedPathVertices = null;
        selectPathEdges.clear();
    }

    private class GraphSaver implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.setFileFilter(fileFilter);
            int ret = fileDialog.showDialog(null, "Save graph");
            if (ret == JFileChooser.APPROVE_OPTION){
                File file = fileDialog.getSelectedFile();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    ObjectOutputStream mapOut = new ObjectOutputStream(out);
                    mapOut.writeObject(vertices);
                    mapOut.writeObject(edges);
                    mapOut.close();
                    out.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private class GraphOpener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.setFileFilter(fileFilter);
            int ret = fileDialog.showDialog(null, "Open graph");
            if (ret == JFileChooser.APPROVE_OPTION){
                File file = fileDialog.getSelectedFile();
                try {
                    FileInputStream in = new FileInputStream(file);
                    ObjectInputStream mapIn = new ObjectInputStream(in);
                    vertices = (HashMap<Vertex, Ellipse2D>) mapIn.readObject();
                    edges = (HashMap<Edge, Line2D>) mapIn.readObject();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                repaint();
            }
        }
    }

    private class SelectFirstMouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            selectedVertex = findVertexEllipse(e.getPoint());
            if (selectedVertex != null){
                clearPath();
                vertexSource = findVertex(selectedVertex);
            }
            else vertexSource = null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class SelectLastMouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            selectedVertex = findVertexEllipse(e.getPoint());
            if (selectedVertex != null) vertexTarget = findVertex(selectedVertex);
            else vertexTarget = null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class AddVertexMouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            selectedVertex = findVertexEllipse(e.getPoint());
            if (selectedVertex == null){
                Vertex vertex = new Vertex("x=" + e.getX() + "y=" + e.getY());
                add(e.getPoint(), vertex);
            }
            onVertexSelect();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }

    }

    private class AddEdgeMouseHandler extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            Vertex vertexFrom = null;
            Vertex vertexTo = null;
            if (selectedVertex != null){
                vertexFrom = findVertex(selectedVertex);
            }
            selectedVertex = findVertexEllipse(e.getPoint());
            if (selectedVertex != null && selectedVertex != vertices.get(vertexFrom)
                    && e.getButton() == MouseEvent.BUTTON3){
                vertexTo = findVertex(selectedVertex);
            }
            if (vertexFrom != null && vertexTo != null){
                drawEdge(vertexFrom, vertexTo);
            }
            onVertexSelect();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class ViewModeMouseHandler extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            selectedVertex = findVertexEllipse(e.getPoint());
            if (selectedVertex == null) {
                selectedEdge = findEdgeLine(e.getPoint());
            } else {
                selectedEdge = null;
            }
            onVertexSelect();
            onEdgeSelect();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class EditEdgeMouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            selectedEdge = findEdgeLine(e.getPoint());
            if (selectedEdge != null){
                clearPath();
                onEdgeSelect();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class EditVertexMouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            selectedVertex = findVertexEllipse(e.getPoint());
            if (selectedVertex != null){
                clearPath();
                onVertexSelect();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class RemoveVertexMouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3){
                selectedVertex = findVertexEllipse(e.getPoint());
                if (selectedVertex != null){
                    if (selectedPathVertices.contains(findVertex(selectedVertex))) clearPath();
                    Vertex vertex = findVertex(selectedVertex);
                    removeVertex(vertex);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class RemoveEdgeMouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3){
                selectedEdge = findEdgeLine(e.getPoint());
                if (selectedEdge != null){
                    if (selectPathEdges.contains(findEdge(selectedEdge))) clearPath();
                    Edge edge = findEdge(selectedEdge);
                    removeEdge(edge);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
    }

    private class MouseMotionHandler implements MouseMotionListener{

        int x;

        int y;

        private void dragAssociated(java.util.List<Edge> edgeList){
            for (Edge current : edgeList){
                Vertex associated = current.getTarget();
                Ellipse2D associatedEllipse = vertices.get(associated);
                double xTo = associatedEllipse.getCenterX();
                double yTo = associatedEllipse.getCenterY();
                Line2D wire = edges.get(current);
                wire.setLine(x, y, xTo, yTo);
            }
        }

        private void dragAssociatedToThis(Vertex vertex){
            for (Edge current : edges.keySet()){
                if (current.getTarget() == vertex){
                    Vertex source = current.getSource();
                    Ellipse2D ellipse = vertices.get(source);
                    double xTo = ellipse.getCenterX();
                    double yTo = ellipse.getCenterY();
                    Line2D wire = edges.get(current);
                    wire.setLine(x, y, xTo, yTo);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (mode != VIEW_MODE) return;
            if (selectedVertex != null){
                x = e.getX();
                y = e.getY();
                selectedVertex.setFrame(x - DIAMETER / 2, y - DIAMETER / 2, DIAMETER, DIAMETER);
                Vertex vertex = findVertex(selectedVertex);
                java.util.List<Edge> edgeList = vertex.getEdges();
                dragAssociated(edgeList);
                dragAssociatedToThis(vertex);
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

}
