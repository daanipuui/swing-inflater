package com.danielpuiu.swing.inflater.layout;

import com.danielpuiu.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCardLayout {

    @Test
    public void testCardLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(StartSwingApplication.class.getClassLoader().getResourceAsStream("card_layout.xml"));
        assertEquals(2, container.getComponentCount());

        List<String> names = Arrays.asList("first", "second");
        for (int i = 0; i < 2; i++) {
            Component component = container.getComponent(i);
            assertTrue(component instanceof JPanel);
            assertEquals(names.get(i), component.getName());
        }

        LayoutManager layoutManager = container.getLayout();
        assertEquals(CardLayout.class, layoutManager.getClass());
    }
}
