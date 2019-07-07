package io.github.daanipuui.swing.inflater.layout;

import io.github.daanipuui.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import static io.github.daanipuui.swing.inflater.util.ObjectUtil.cast;
import static io.github.daanipuui.swing.inflater.util.TestUtil.showFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.testComponentBounds;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBorderLayout {

    @SuppressWarnings("WeakerAccess")
    public static ActionListener onWestClick() {
        return e -> System.exit(-1);
    }

    @Test
    public void testBorderLayout() {
        ComponentLoader loader = new ComponentLoader();
        JPanel container = loader.load(getClass().getClassLoader().getResourceAsStream("border_layout.xml"));
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

        JButton button = (JButton) borderLayout.getLayoutComponent(container, WEST);
        ActionListener[] actionListeners = button.getActionListeners();
        assertEquals(1, actionListeners.length);
        assertEquals(onWestClick(), actionListeners[0]);

        showFrame(container);

        testComponentBounds(container, 0, 0, 192, 78);
        testComponentBounds(container.getComponent(0), 0, 0, 192, 26);
        testComponentBounds(container.getComponent(1), 0, 52, 192, 26);
        testComponentBounds(container.getComponent(2), 62, 26, 71, 26);
        testComponentBounds(container.getComponent(3), 133, 26, 59, 26);
        testComponentBounds(container.getComponent(4), 0, 26, 62, 26);
    }
}