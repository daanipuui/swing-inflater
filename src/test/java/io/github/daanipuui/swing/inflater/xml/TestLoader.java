package io.github.daanipuui.swing.inflater.xml;

import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;

import static org.junit.Assert.assertEquals;

public class TestLoader {

    @Test
    public void testLoader() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(getClass().getClassLoader().getResourceAsStream("layout.xml"));

        assertEquals(5, container.getComponents().length);

        Component component1 = container.getComponent(0);
        assertEquals(JLabel.class, component1.getClass());
        assertEquals("#1", component1.getName());
        assertEquals("Description", ((JLabel) component1).getText());

        Component component2 = container.getComponent(1);
        assertEquals(JTextField.class, component2.getClass());
        assertEquals("#2", component2.getName());

        Component component3 = container.getComponent(2);
        assertEquals(JLabel.class, component3.getClass());
        assertEquals("#3", component3.getName());
        assertEquals("Market name", ((JLabel) component3).getText());

        Component component4 = container.getComponent(3);
        assertEquals(JTextField.class, component4.getClass());
        assertEquals("#4", component4.getName());

        Component component5 = container.getComponent(4);
        assertEquals(JButton.class, component5.getClass());
        assertEquals("#5", component5.getName());
        assertEquals("click me", ((JButton) component5).getText());
    }

    @Test
    public void testLoader_ComplexLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(getClass().getClassLoader().getResourceAsStream("complex_layout.xml"));

        assertEquals(4, container.getComponents().length);
    }
}
