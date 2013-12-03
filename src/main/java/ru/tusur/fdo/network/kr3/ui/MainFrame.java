package ru.tusur.fdo.network.kr3.ui;

import javax.swing.*;
import java.awt.*;

/**
 * User: oleg
 * Date: 02.12.13
 * Time: 23:56
 */
public class MainFrame extends JFrame {

    public static final int WIDTH = 800;

    public static final int HEIGHT = 600;

    public static final int GRAPH_WIDTH = 600;

    public static final int MENU_WIDTH = WIDTH - GRAPH_WIDTH;

    public MainFrame(){
        super("Network");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        GraphPanel graphPanel = new GraphPanel(this);
        graphPanel.setPreferredSize(new Dimension(GRAPH_WIDTH, HEIGHT));
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Container contentPane = getContentPane();
        contentPane.add(graphPanel, BorderLayout.CENTER);

        MenuPanel menu = new MenuPanel(this);
        menu.setPreferredSize(new Dimension(MENU_WIDTH, HEIGHT));
        contentPane.add(menu, BorderLayout.EAST);
    }



}
