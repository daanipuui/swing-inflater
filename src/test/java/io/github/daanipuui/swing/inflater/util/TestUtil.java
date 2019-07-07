package io.github.daanipuui.swing.inflater.util;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestUtil {

    public static void testComponentBounds(Component component, int x, int y, int width, int height) {
        assertEquals(x, component.getX());
        assertEquals(y, component.getY());
        assertEquals(width, component.getWidth());
        assertEquals(height, component.getHeight());
    }

    public static JFrame showFrame(JComponent component) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.getContentPane().add(component);
        frame.pack();
        frame.setVisible(true);

        return frame;
    }

    public static void resizeFrame(JFrame frame, int width, int height, JComponent lastUpdated) {
        AtomicBoolean resized = new AtomicBoolean();
        lastUpdated.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                resized.set(true);
            }
        });

        frame.setSize(new Dimension(width, height));

        while (!Thread.currentThread().isInterrupted() && !resized.get());
    }
}
