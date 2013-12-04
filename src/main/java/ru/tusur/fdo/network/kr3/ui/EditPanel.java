package ru.tusur.fdo.network.kr3.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: oleg
 * Date: 04.12.13
 * Time: 14:53
 */
public class EditPanel extends JPanel {

    private MainFrame frame;

    private final JLabel vertexLabel = new JLabel("Vertex name:");

    private final JTextField vertexName = new JTextField();

    private final JButton saveVertex = new JButton("Save");

    private final JLabel edgeLabel = new JLabel("Edge weight:");

    private final JTextField edgeWeight = new JTextField();

    private final JButton saveEdge = new JButton("Save");

    public EditPanel(MainFrame frame){
        this.frame = frame;
        this.frame.getGraphPanel().setEditor(this);

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setAlignmentX(LEFT_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 5)));

        add(vertexLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(vertexName);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(saveVertex);
        add(Box.createRigidArea(new Dimension(0, 5)));
        saveVertex.addActionListener(new VertexSaver());

        add(edgeLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(edgeWeight);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(saveEdge);
        add(Box.createRigidArea(new Dimension(0, 300)));
        saveEdge.addActionListener(new EdgeSaver());

    }

    public void setVertexName(String name){
        vertexName.setText(name);
    }

    public void setEdgeWeight(double weight){
        edgeWeight.setText(Double.toString(weight));
    }

    public void showVertexEditor(){
        vertexName.setVisible(true);
        vertexLabel.setVisible(true);
        saveVertex.setVisible(true);
        edgeLabel.setVisible(false);
        edgeWeight.setVisible(false);
        saveEdge.setVisible(false);
    }

    public void showEdgeEditor(){
        vertexName.setVisible(false);
        vertexLabel.setVisible(false);
        saveVertex.setVisible(false);
        edgeLabel.setVisible(true);
        edgeWeight.setVisible(true);
        saveEdge.setVisible(true);
    }

    private class VertexSaver implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (vertexName.getText().length() > 0)
                frame.getGraphPanel().setVertexName(vertexName.getText());
        }
    }

    private class EdgeSaver implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            double weight = Double.parseDouble(edgeWeight.getText());
            frame.getGraphPanel().setEdgeWeight(weight);
        }
    }

}
