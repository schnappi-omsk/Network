package ru.tusur.fdo.network.kr3.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * User: oleg
 * Date: 03.12.13
 * Time: 21:13
 */
public class MenuPanel extends JPanel {

    public static final int MENU_WIDTH = 200;

    public static final int MENU_HEIGHT = 400;

    private HashMap<Integer, JRadioButton> buttons = new HashMap<Integer, JRadioButton>();

    private final MainFrame frame;

    private GraphPanel listener;

    public void setListener(GraphPanel listener) {
        this.listener = listener;
    }

    public MenuPanel(MainFrame frame){
        this.frame = frame;
        setLayout(new GridLayout(0, 1));

        JRadioButton standard = new JRadioButton(GraphPanel.VIEW);
        buttons.put(GraphPanel.VIEW_MODE, standard);
        JRadioButton addVertex= new JRadioButton(GraphPanel.ADD_VERTEX);
        buttons.put(GraphPanel.ADD_VERTEX_MODE, addVertex);
        JRadioButton addEdge = new JRadioButton(GraphPanel.ADD_EDGE);
        buttons.put(GraphPanel.ADD_EDGE_MODE, addEdge);
        JRadioButton editVertex = new JRadioButton(GraphPanel.EDIT_VERTEX);
        buttons.put(GraphPanel.EDIT_VERTEX_MODE, editVertex);
        JRadioButton editEdge = new JRadioButton(GraphPanel.EDIT_EDGE);
        buttons.put(GraphPanel.EDIT_EDGE_MODE, editEdge);
        JRadioButton removeVertex = new JRadioButton(GraphPanel.REMOVE_VERTEX);
        buttons.put(GraphPanel.REMOVE_VERTEX_MODE, removeVertex);
        JRadioButton removeEdge = new JRadioButton(GraphPanel.REMOVE_EDGE);
        buttons.put(GraphPanel.REMOVE_EDGE_MODE, removeEdge);

        ActionListener listener = new MenuItemHandler();
        JPanel radioPanel = new JPanel(new GridLayout(0,1));
        radioPanel.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        radioPanel.setSize(MENU_WIDTH, MENU_HEIGHT);
        radioPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ButtonGroup menu = new ButtonGroup();
        for (int item : buttons.keySet()){
            JRadioButton button = buttons.get(item);
            button.addActionListener(listener);
            menu.add(button);
            radioPanel.add(button);
        }
        add(radioPanel);

        JPanel bottomPanel = new EditPanel(frame);
        bottomPanel.setPreferredSize(new Dimension(MENU_WIDTH, 200));

        add(bottomPanel);

    }

    private int getMode(JRadioButton button){
        if (buttons.containsValue(button)){
            for (int key : buttons.keySet()){
                if (buttons.get(key) == button) return key;
            }
        }
        return 0;
    }

    private class MenuItemHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            listener.setMode(getMode((JRadioButton) e.getSource()));
        }
    }


}
