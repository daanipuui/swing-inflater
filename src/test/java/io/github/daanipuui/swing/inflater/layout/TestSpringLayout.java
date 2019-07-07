package io.github.daanipuui.swing.inflater.layout;

import io.github.daanipuui.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;

import static io.github.daanipuui.swing.inflater.util.TestUtil.resizeFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.showFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.testComponentBounds;
import static org.junit.Assert.assertEquals;

public class TestSpringLayout {

    @Test
    public void testSpringLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(getClass().getClassLoader().getResourceAsStream("spring_layout.xml"));
        assertEquals(5, container.getComponentCount());

        JLabel jLabel1 = (JLabel) container.getComponent(0);
        assertEquals("#1", jLabel1.getName());
        System.setProperty("test.color", "0xFF0096");
        assertEquals(Color.getColor("test.color"), jLabel1.getBackground());
        assertEquals("Description", jLabel1.getText());

        JTextField jTextField1 = (JTextField) container.getComponent(1);
        assertEquals("#2", jTextField1.getName());
        System.setProperty("test.color", "0xFF0000");
        assertEquals(Color.getColor("test.color"), jTextField1.getBackground());

        JLabel jLabel2 = (JLabel) container.getComponent(2);
        assertEquals("#3", jLabel2.getName());
        System.setProperty("test.color", "0xFF00FF");
        assertEquals(Color.getColor("test.color"), jLabel2.getBackground());
        assertEquals("Market name", jLabel2.getText());

        JTextField jTextField2 = (JTextField) container.getComponent(3);
        assertEquals("#4", jTextField2.getName());
        System.setProperty("test.color", "0x00FF00");
        assertEquals(Color.getColor("test.color"), jTextField2.getBackground());

        JButton button = (JButton) container.getComponent(4);
        assertEquals("#5", button.getName());
        assertEquals("click me", button.getText());
        assertEquals(0, button.getActionListeners().length);

        JFrame frame = showFrame(container);

        testComponentBounds(container, 0, 0, 148, 0);
        testComponentBounds(jLabel1, 0, 0, 65, 16);
        testComponentBounds(jTextField1, 65, -2, 83, 20);
        testComponentBounds(jLabel2, 0, 16, 75, -32);
        testComponentBounds(jTextField2, 65, -10, -17, 20);
        testComponentBounds(button, -34, 10, 82, 26);

        resizeFrame(frame, 1920, 1080, jTextField2);

        testComponentBounds(container, 0, 0, 1902, 1033);
        testComponentBounds(jLabel1, 0, 0, 65, 16);
        testComponentBounds(jTextField1, 65, -2, 1837, 20);
        testComponentBounds(jLabel2, 0, 16, 75, 1002);
        testComponentBounds(jTextField2, 65, 507, 1737, 20);
        testComponentBounds(button, 1720, 527, 82, 26);
    }
}