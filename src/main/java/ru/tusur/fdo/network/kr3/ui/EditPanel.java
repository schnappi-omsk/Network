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

    public static final String EDIT_EDGE = "Edge weight:";

    public static final String EDIT_VERTEX = "Vertex name:";

    private final JLabel editLabel = new JLabel("editLabel");

    private final JTextField editField = new JTextField();

    private final JButton save = new JButton("Save");

    private ActionListener listener;

    public EditPanel(MainFrame frame){
        this.frame = frame;
        this.frame.getGraphPanel().setEditor(this);

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setAlignmentX(LEFT_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 5)));

        add(editLabel);
        editLabel.setVisible(false);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(editField);
        editField.setVisible(false);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(save);
        save.setVisible(false);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(Box.createRigidArea(new Dimension(0, 300)));

    }

    public void setMode(){
        int mode = frame.getGraphPanel().getMode();
        boolean editMode = mode == GraphPanel.EDIT_EDGE_MODE || mode == GraphPanel.EDIT_VERTEX_MODE;
        editLabel.setVisible(editMode);
        editField.setVisible(editMode);
        save.setVisible(editMode);
        save.removeActionListener(listener);
        if (mode == GraphPanel.EDIT_VERTEX_MODE){
            editLabel.setText(EDIT_VERTEX);
            listener = new VertexSaver();
        }
        else if (mode == GraphPanel.EDIT_EDGE_MODE) {
            editLabel.setText(EDIT_EDGE);
            listener = new EdgeSaver();
        }
        save.addActionListener(listener);
    }

    public void setVertexName(String name){
        editField.setText(name);
    }

    public void setEdgeWeight(double weight){
        editField.setText(Double.toString(weight));
    }

    public void showVertexEditor(){
    }

    public void showEdgeEditor(){
    }

    private class VertexSaver implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (editField.getText().length() > 0)
                frame.getGraphPanel().setVertexName(editField.getText());
        }
    }

    private class EdgeSaver implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (editField.getText() == null) return;
            double weight = Double.parseDouble(editField.getText());
            frame.getGraphPanel().setEdgeWeight(weight);
        }
    }

}
