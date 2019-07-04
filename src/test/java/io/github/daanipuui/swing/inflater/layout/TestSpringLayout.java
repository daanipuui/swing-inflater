package io.github.daanipuui.swing.inflater.layout;

import io.github.daanipuui.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JPanel;

import static org.junit.Assert.assertEquals;

public class TestSpringLayout {

    @Test
    public void testSpringLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(StartSwingApplication.class.getClassLoader().getResourceAsStream("spring_layout.xml"));
        assertEquals(5, container.getComponentCount());
    }
}