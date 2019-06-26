package com.danielpuiu.swing.inflater.layout;

import com.danielpuiu.swing.inflater.xml.ComponentLoader;

import javax.swing.*;

import java.io.InputStream;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class StartSwingApplication {

    public static void main(String[] args) {
        InputStream inputStream = StartSwingApplication.class.getClassLoader().getResourceAsStream("complex_layout.xml");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

            ComponentLoader loader = new ComponentLoader();
            JPanel container = loader.load(inputStream);
            frame.getContentPane().add(container);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
