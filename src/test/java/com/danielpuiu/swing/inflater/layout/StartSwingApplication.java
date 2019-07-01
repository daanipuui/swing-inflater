package com.danielpuiu.swing.inflater.layout;

import com.danielpuiu.swing.inflater.xml.ComponentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.io.InputStream;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class StartSwingApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartSwingApplication.class);

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

    @SuppressWarnings("unused")
    public static ActionListener onClick() {
        return e -> {
            LOGGER.info("Exit the application");
            System.exit(-1);
        };
    }
}
