package com.danielpuiu.swing.inflater.layout;

import com.danielpuiu.swing.inflater.xml.ComponentLoader;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class StartSwingApplication {

    public static void main(String[] args) {
        String resourceName = "complex_layout.xml";

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

            ComponentLoader loader = new ComponentLoader();
            JPanel container = loader.load(StartSwingApplication.class.getClassLoader().getResourceAsStream(resourceName));
            frame.getContentPane().add(container);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
