package io.github.daanipuui.swing.inflater.layout;

import io.github.daanipuui.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;

import static io.github.daanipuui.swing.inflater.util.TestUtil.resizeFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.showFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.testComponentBounds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGridBagLayout {

    @Test
    public void testGridBagLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(getClass().getClassLoader().getResourceAsStream("grid_bag_layout.xml"));
        assertEquals(5, container.getComponentCount());

        List<String> names = Arrays.asList("Button 1", "Button 2", "Button 3", "Button 4", "Button 5");
        for (int i = 0; i < 5; i++) {
            Component component = container.getComponent(i);
            assertTrue(component instanceof JButton);
            assertEquals(names.get(i), component.getName());
        }

        LayoutManager layoutManager = container.getLayout();
        assertEquals(GridBagLayout.class, layoutManager.getClass());

        JFrame frame = showFrame(container);

        testComponentBounds(container, 0, 0, 243, 128);
        testComponentBounds(container.getComponent(0), 0, 0, 81, 26);
        testComponentBounds(container.getComponent(1), 81, 0, 81, 26);
        testComponentBounds(container.getComponent(2), 162, 0, 81, 26);
        testComponentBounds(container.getComponent(3), 0, 26, 243, 66);
        testComponentBounds(container.getComponent(4), 81, 102, 162, 26);

        resizeFrame(frame, 1000, 1000, (JComponent) container.getComponent(4));

        testComponentBounds(container, 0, 0, 982, 953);
        testComponentBounds(container.getComponent(0), 0, 0, 327, 26);
        testComponentBounds(container.getComponent(1), 327, 0, 327, 26);
        testComponentBounds(container.getComponent(2), 654, 0, 327, 26);
        testComponentBounds(container.getComponent(3), 0, 26, 981, 66);
        testComponentBounds(container.getComponent(4), 327, 927, 654, 26);
    }
}