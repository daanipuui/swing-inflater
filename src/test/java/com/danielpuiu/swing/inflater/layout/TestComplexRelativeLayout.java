package com.danielpuiu.swing.inflater.layout;

import com.danielpuiu.swing.inflater.xml.ComponentLoader;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TestComplexRelativeLayout {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

            ComponentLoader loader = new ComponentLoader();
            JPanel container = loader.load(TestComplexRelativeLayout.class.getClassLoader().getResourceAsStream("complex_layout.xml"));
            frame.getContentPane().add(container);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
