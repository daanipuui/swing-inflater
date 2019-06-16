package com.danielpuiu.swing.inflater.layout;

import com.danielpuiu.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;

import static com.danielpuiu.swing.inflater.util.ObjectUtil.cast;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBorderLayout {

    @Test
    public void testBorderLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(StartSwingApplication.class.getClassLoader().getResourceAsStream("border_layout.xml"));
        assertEquals(5, container.getComponentCount());

        List<String> names = Arrays.asList("north", "south", "center", "east", "west");
        for (int i = 0; i < 5; i++) {
            Component component = container.getComponent(i);
            assertTrue(component instanceof JButton);
            assertEquals(names.get(i), component.getName());
        }

        LayoutManager layoutManager = container.getLayout();
        assertEquals(BorderLayout.class, layoutManager.getClass());

        BorderLayout borderLayout = cast(layoutManager);
        List<String> positions = Arrays.asList(NORTH, SOUTH, CENTER, EAST, WEST);
        for (int i = 0; i < 5; i++) {
            assertEquals("Component #" + i, loader.getComponent(names.get(i)), borderLayout.getLayoutComponent(positions.get(i)));
        }
    }
}