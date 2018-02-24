package com.danielpuiu.swing.xml;

import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestLoader {

    @Test
    public void testLoader() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load("src/test/resources/layout.xml");

        assertEquals(4, container.getComponents().length);

        assertTrue(container.getComponent(0) instanceof JLabel);
        JLabel label = (JLabel) container.getComponent(0);
        assertEquals("#1", label.getName());
        assertEquals("Description", label.getText());

        assertTrue(container.getComponent(1) instanceof JTextField);
        JTextField textField = (JTextField) container.getComponent(1);
        assertEquals("#2", textField.getName());

        assertTrue(container.getComponent(2) instanceof JLabel);
        label = (JLabel) container.getComponent(2);
        assertEquals("#3", label.getName());
        assertEquals("Market name", label.getText());

        assertTrue(container.getComponent(3) instanceof JTextField);
        textField = (JTextField) container.getComponent(3);
        assertEquals("#4", textField.getName());
    }

    @Test
    public void testLoader_ComplexLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load("src/test/resources/complex_layout.xml");

        assertEquals(4, container.getComponents().length);
    }
}
