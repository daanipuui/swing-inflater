package com.danielpuiu.swing.layout;

import com.danielpuiu.swing.component.RelativeContainer;
import com.danielpuiu.swing.xml.ComponentLoader;

import javax.swing.*;

public class TestRelativeLayout {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ComponentLoader loader = new ComponentLoader();
            RelativeContainer container = loader.load("src/test/resources/layout.xml");
            frame.getContentPane().add(container);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
