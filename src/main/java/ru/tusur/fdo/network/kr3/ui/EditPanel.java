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

    public static final String EDIT_EDGE = "Пропускная способность:";

    public static final String EDIT_VERTEX = "Имя узла:";

    public static final String VERTEX_WEIGHT = "Внутренний трафик:";

    private final JLabel editLabel = new JLabel("editLabel");

    private final JTextField editField = new JTextField();

    private final JLabel secondaryEditLabel = new JLabel(VERTEX_WEIGHT);

    private final JTextField secondaryEditField = new JTextField();

    private final JButton save = new JButton("Сохранить");

    private final JTextArea message = new JTextArea();

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

        add(secondaryEditLabel);
        secondaryEditLabel.setVisible(false);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(secondaryEditField);
        secondaryEditField.setVisible(false);
        add(Box.createRigidArea(new Dimension(0, 5)));

        add(save);
        save.setVisible(false);
        add(Box.createRigidArea(new Dimension(0, 5)));

        message.setEditable(false);
        message.setBackground(null);
        message.setFocusable(false);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setBorder(null);
        add(message);

        add(Box.createRigidArea(new Dimension(0, 300)));

    }

    public void setMessage(String text){
        message.setText(text);
        repaint();
    }

    public void setMode(){
        int mode = frame.getGraphPanel().getMode();
        boolean editMode = mode == GraphPanel.EDIT_EDGE_MODE || mode == GraphPanel.EDIT_VERTEX_MODE;
        boolean vertexEdit = mode == GraphPanel.EDIT_VERTEX_MODE;
        editLabel.setVisible(editMode);
        editField.setVisible(editMode);
        secondaryEditLabel.setVisible(vertexEdit);
        secondaryEditField.setVisible(vertexEdit);
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

    public void setVertexWeight(double weight){
        secondaryEditField.setText(String.valueOf(weight));
    }

    public void setEdgeWeight(double weight){
        editField.setText(Double.toString(weight));
    }

    private class VertexSaver implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (editField.getText().length() > 0)
                frame.getGraphPanel().setVertexName(editField.getText());
            if (secondaryEditField.getText().length() > 0){
                double weight = Double.parseDouble(secondaryEditField.getText());
                frame.getGraphPanel().setVertexWeight(weight);
            }
            frame.getGraphPanel().repaint();
        }
    }

    private class EdgeSaver implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (editField.getText() == null) return;
            double weight = Double.parseDouble(editField.getText());
            frame.getGraphPanel().setEdgeWeight(weight);
            frame.getGraphPanel().repaint();
        }
    }

}
