package io.github.daanipuui.swing.inflater.layout;

import io.github.daanipuui.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JPanel;
import javax.swing.JTree;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;

import static io.github.daanipuui.swing.inflater.util.TestUtil.showFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.testComponentBounds;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCardLayout {

    @Test
    public void testCardLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(getClass().getClassLoader().getResourceAsStream("card_layout.xml"));
        assertEquals(3, container.getComponentCount());

        List<String> names = Arrays.asList("first", "second", "third");
        for (int i = 0; i < 3; i++) {
            Component component = container.getComponent(i);
            assertTrue(component instanceof JPanel);
            assertEquals(names.get(i), component.getName());
        }

        JPanel third = (JPanel) container.getComponent(2);
        JTree tree = (JTree) third.getComponent(1);
        assertEquals("file-tree", tree.getName());
        assertArrayEquals(new int[]{1, 2, 3}, tree.getSelectionRows());

        LayoutManager layoutManager = container.getLayout();
        assertEquals(CardLayout.class, layoutManager.getClass());
        CardLayout cardLayout = (CardLayout) layoutManager;

        showFrame(container);
        cardLayout.show(container, "FIRST");

        testComponentBounds(container, 0, 0, 205, 82);
        testComponentBounds(container.getComponent(0), 0, 0, 205, 82);
        assertTrue(container.getComponent(0).isVisible());

        testComponentBounds(container.getComponent(1), 0, 0, 205, 82);
        assertFalse(container.getComponent(1).isVisible());

        testComponentBounds(container.getComponent(2), 0, 0, 205, 82);
        assertFalse(container.getComponent(2).isVisible());

        cardLayout.show(container, "SECOND");
        assertFalse(container.getComponent(0).isVisible());
        assertTrue(container.getComponent(1).isVisible());
        assertFalse(container.getComponent(2).isVisible());

        cardLayout.show(container, "THIRD");
        assertFalse(container.getComponent(0).isVisible());
        assertFalse(container.getComponent(1).isVisible());
        assertTrue(container.getComponent(2).isVisible());
    }
}
