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

    public static final String VIEW = "Standard mode";

    public static final String ADD_VERTEX = "Add vertex";

    public static final String ADD_EDGE = "Add edge";

    private HashMap<Integer, JRadioButton> buttons = new HashMap<Integer, JRadioButton>();

    private final JFrame frame;

    public MenuPanel(JFrame frame){
        this.frame = frame;
        setLayout(new GridLayout(0, 1));

        JRadioButton standard = new JRadioButton(VIEW);
        buttons.put(GraphPanel.VIEW_MODE, standard);
        JRadioButton addVertex= new JRadioButton(ADD_VERTEX);
        buttons.put(GraphPanel.ADD_VERTEX_MODE, addVertex);
        JRadioButton addEdge = new JRadioButton(ADD_EDGE);
        buttons.put(GraphPanel.ADD_EDGE_MODE, addEdge);

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

        JPanel bottomPanel = new JPanel();
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
            frame.setTitle(frame.getTitle() + ": " + getMode((JRadioButton) e.getSource()));
        }
    }


}
