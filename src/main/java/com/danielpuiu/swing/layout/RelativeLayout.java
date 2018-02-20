package com.danielpuiu.swing.layout;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RelativeLayout implements LayoutManager2 {

    private static final Function<Component, Set<Component>> SET_INITIALIZER = key -> new HashSet<>();

    private final Map<Component, Set<Component>> leftToRight = new HashMap<>();
    private final Map<Component, Set<Component>> topToBottom = new HashMap<>();

    private final Map<Component, Set<Component>> alignBottom = new HashMap<>();
    private final Map<Component, Set<Component>> alignLeft = new HashMap<>();
    private final Map<Component, Set<Component>> alignRight = new HashMap<>();
    private final Map<Component, Set<Component>> alignTop = new HashMap<>();

    private final Map<Component, Set<Component>> centerHorizontal = new HashMap<>();
    private final Map<Component, Set<Component>> centerVertical = new HashMap<>();

    private final Map<Component, Bounds> componentToBounds = new HashMap<>();

    private final Map<Component, Double> componentToWidthWeight = new HashMap<>();
    private final Map<Component, Double> componentToHeightWeight = new HashMap<>();

    private final List<Integer> widths = new ArrayList<>();
    private final List<Integer> heights = new ArrayList<>();

    private Insets margins = new Insets(0, 0, 0, 0);
    private Insets padding = new Insets(0, 0, 0, 0);

    private float alignmentX = 0.5f;
    private float alignmentY = 0.5f;

    private Function<Component, Dimension> componentDimension;

    @Override
    public void addLayoutComponent(Component component, Object constraints) {
    }

    @Override
    public void addLayoutComponent(String name, Component component) {
    }

    @Override
    public void removeLayoutComponent(Component component) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        componentDimension = Component::getPreferredSize;
        return getLayoutSize(parent);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        componentDimension = Component::getMinimumSize;
        return getLayoutSize(parent);
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        componentDimension = Component::getMinimumSize;
        return getLayoutSize(parent);
    }

    private Dimension getLayoutSize(Container parent) {
        Set<Component> components = getParentComponents(parent);

        int width = computePreferredDimensionsLeftToRight(parent, components);
        int height = computePreferredDimensionsTopToBottom(parent, components);

        return new Dimension(width, height);
    }

    private int computePreferredDimensionsLeftToRight(Container parent, Set<Component> components) {
        for (Component component: alignLeft.getOrDefault(parent, Collections.emptySet())) {
            leftToRight.computeIfAbsent(component, SET_INITIALIZER);
        }
        computePreferredLeftToRight(getLeftMostComponents(components), margins.left);
        return widths.stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    private void computePreferredLeftToRight(Set<Component> components, int left) {
        if (isEmpty(components)) {
            widths.add(left + margins.right);
            return;
        }

        for (Component component: components) {
            int x = component.isVisible()? left + padding.left: 0;
            int width = component.isVisible()? componentDimension.apply(component).width + padding.right: 0;
            computePreferredLeftToRight(leftToRight.get(component), x + width);
        }
    }

    private int computePreferredDimensionsTopToBottom(Container parent, Set<Component> components) {
        for (Component component: alignTop.getOrDefault(parent, Collections.emptySet())) {
            topToBottom.computeIfAbsent(component, SET_INITIALIZER);
        }
        computePreferredTopToBottom(getTopMostComponents(components), margins.top);
        return heights.stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    private void computePreferredTopToBottom(Set<Component> components, int top) {
        if (isEmpty(components)) {
            heights.add(top + margins.bottom);
            return;
        }

        for (Component component: components) {
            int y = component.isVisible()? top + padding.top: 0;
            int height = component.isVisible()? componentDimension.apply(component).height + padding.bottom: 0;
            computePreferredTopToBottom(topToBottom.get(component), y + height);
        }
    }

    private Set<Component> getParentComponents(Container parent) {
        return Stream.of(parent.getComponents()).collect(Collectors.toSet());
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return alignmentX;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return alignmentY;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Set<Component> components = getParentComponents(parent);

            layoutLeftToRight(parent, components);
            layoutTopToBottom(parent, components);

            setDimensions();
        }
    }

    private void layoutLeftToRight(Container parent, Set<Component> components) {
        Set<Component> leftMostComponents = getLeftMostComponents(components);
        computeLeftToRight(leftMostComponents, margins.left);

        computeLeftToRightAlignments(leftMostComponents);
        alignToRight(parent);

        centerOnComponentHorizontally(parent, new Bounds(parent));
        centerHorizontal(leftMostComponents);
    }

    private void layoutTopToBottom(Container parent, Set<Component> components) {
        Set<Component> topMostComponents = getTopMostComponents(components);
        computeTopToBottom(topMostComponents, margins.top);

        computeTopToBottomAlignments(topMostComponents);
        alignToBottom(parent);

        centerOnComponentVertically(parent, new Bounds(parent));
        centerVertical(topMostComponents);
    }

    private int getParentWidth(Container parent) {
        return parent.getWidth() - (margins.left + margins.right);
    }

    private int getParentHeight(Container parent) {
        return parent.getHeight() - (margins.top + margins.bottom);
    }

    private void computeLeftToRight(Set<Component> components, int left) {
        if (isEmpty(components)) {
            return;
        }

        for (Component component: components) {
            Bounds bounds = componentToBounds.computeIfAbsent(component, key -> new Bounds());
            bounds.x = left + padding.left;
            bounds.width = getComponentWidth(component);

            computeLeftToRight(leftToRight.get(component), bounds.x + bounds.width + padding.right);
        }
    }

    private int getComponentWidth(Component component) {
        if (!component.isVisible()) {
            return 0;
        }

        if (componentToWidthWeight.containsKey(component)) {
            double percent = componentToWidthWeight.get(component);
            return (int) (getParentWidth(component.getParent()) * percent) - (padding.left + padding.right);
        }

        return componentDimension.apply(component).width;
    }

    private Set<Component> getLeftMostComponents(Set<Component> components) {
        Set<Component> rightComponents = leftToRight.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        return components.stream().filter(component -> !rightComponents.contains(component)).collect(Collectors.toSet());
    }

    private void computeTopToBottom(Set<Component> components, int top) {
        if (isEmpty(components)) {
            return;
        }

        for (Component component: components) {
            Bounds bounds = componentToBounds.computeIfAbsent(component, key -> new Bounds());
            bounds.y = top + padding.top;
            bounds.height = getComponentHeight(component);

            computeTopToBottom(topToBottom.get(component), bounds.y + bounds.height + padding.bottom);
        }
    }

    private int getComponentHeight(Component component) {
        if (!component.isVisible()) {
            return 0;
        }

        if (componentToHeightWeight.containsKey(component)) {
            double percent = componentToHeightWeight.get(component);
            return (int) (getParentHeight(component.getParent()) * percent) - (padding.top + padding.bottom);
        }

        return componentDimension.apply(component).height;
    }

    private Set<Component> getTopMostComponents(Set<Component> components) {
        Set<Component> bottomComponents = topToBottom.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        return components.stream().filter(component -> !bottomComponents.contains(component)).collect(Collectors.toSet());
    }

    private void alignToRight(Container parent) {
        int width = margins.left + getParentWidth(parent) - padding.right;
        for (Component component: alignRight.getOrDefault(parent, Collections.emptySet())) {
            Bounds bounds = componentToBounds.get(component);
            bounds.width = component.isVisible()? width - bounds.x: 0;
        }
    }

    private void computeLeftToRightAlignments(Set<Component> components) {
        if (isEmpty(components)) {
            return;
        }

        for (Component reference: components) {
            Bounds referenceBounds = componentToBounds.get(reference);

            for (Component component: alignLeft.getOrDefault(reference, Collections.emptySet())) {
                Bounds bounds = componentToBounds.get(component);
                bounds.x = referenceBounds.x;
            }

            for (Component component: alignRight.getOrDefault(reference, Collections.emptySet())) {
                Bounds bounds = componentToBounds.get(component);
                int oldWidth = bounds.width;
                bounds.width = component.isVisible()? referenceBounds.x + referenceBounds.width - bounds.x: 0;

                if (componentToWidthWeight.containsKey(component)) {
                    bounds.x += bounds.width - oldWidth;
                }
            }

            computeLeftToRightAlignments(leftToRight.get(reference));
        }
    }

    private void alignToBottom(Container parent) {
        int height = margins.top + getParentHeight(parent) - padding.bottom;
        for (Component component: alignBottom.getOrDefault(parent, Collections.emptySet())) {
            Bounds bounds = componentToBounds.get(component);
            bounds.height = component.isVisible()? height - bounds.y: 0;
        }
    }

    private void computeTopToBottomAlignments(Set<Component> components) {
        if (isEmpty(components)) {
            return;
        }

        for (Component reference: components) {
            Bounds referenceBounds = componentToBounds.get(reference);

            for (Component component: alignTop.getOrDefault(reference, Collections.emptySet())) {
                Bounds bounds = componentToBounds.get(component);
                bounds.y = referenceBounds.y;
            }

            for (Component component: alignBottom.getOrDefault(reference, Collections.emptySet())) {
                Bounds bounds = componentToBounds.get(component);
                int oldHeight = bounds.height;
                bounds.height = component.isVisible()? referenceBounds.y + referenceBounds.height - bounds.y: 0;

                if (componentToHeightWeight.containsKey(component)) {
                    bounds.y += bounds.height - oldHeight;
                }
            }

            computeTopToBottomAlignments(topToBottom.get(reference));
        }
    }

    private void centerHorizontal(Set<Component> components) {
        if (isEmpty(components)) {
            return;
        }

        for (Component reference: components) {
            centerOnComponentHorizontally(reference, componentToBounds.get(reference));
            centerHorizontal(leftToRight.get(reference));
        }
    }

    private void centerOnComponentHorizontally(Component reference, Bounds referenceBounds) {
        int referenceMiddle = referenceBounds.x + referenceBounds.width/2;

        for (Component component: centerHorizontal.getOrDefault(reference, Collections.emptySet())) {
            Bounds bounds = componentToBounds.get(component);
            bounds.x -= bounds.x + bounds.width/2 - referenceMiddle;
        }
    }

    private void centerVertical(Set<Component> components) {
        if (isEmpty(components)) {
            return;
        }

        for (Component reference: components) {
            centerOnComponentVertically(reference, componentToBounds.get(reference));
            centerVertical(topToBottom.get(reference));
        }
    }

    private void centerOnComponentVertically(Component reference, Bounds referenceBounds) {
        int referenceMiddle = referenceBounds.y + referenceBounds.height/2;

        for (Component component: centerVertical.getOrDefault(reference, Collections.emptySet())) {
            Bounds bounds = componentToBounds.get(component);
            bounds.y -= bounds.y + bounds.height/2 - referenceMiddle;
        }
    }


    private boolean isEmpty(Set<Component> components) {
        return Objects.isNull(components) || components.isEmpty();
    }

    private void setDimensions() {
        for (Map.Entry<Component, Bounds> entry: componentToBounds.entrySet()) {
            Bounds bounds = entry.getValue();

            Component component = entry.getKey();
            component.setSize(bounds.width, bounds.height);
            component.setLocation(bounds.x, bounds.y);
        }
    }

    public void setMargins(int top, int left, int bottom, int right) {
        margins = new Insets(top, left, bottom, right);
    }

    public void setPadding(int top, int left, int bottom, int right) {
        padding = new Insets(top, left, bottom, right);
    }

    public void setAlignmentX(float alignmentX) {
        this.alignmentX = alignmentX;
    }

    public void setAlignmentY(float alignmentY) {
        this.alignmentY = alignmentY;
    }

    public void above(Component first, Component second) {
        topToBottom.computeIfAbsent(first, SET_INITIALIZER).add(second);
    }

    public void alignBottom(Component first, Component second) {
        alignBottom.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void alignLeft(Component first, Component second) {
        alignLeft.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void alignRight(Component first, Component second) {
        alignRight.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void alignTop(Component first, Component second) {
        alignTop.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void below(Component first, Component second) {
        topToBottom.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void centerHorizontal(Component first, Component second) {
        centerHorizontal.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void centerVertical(Component first, Component second) {
        centerVertical.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void leftOf(Component first, Component second) {
        leftToRight.computeIfAbsent(first, SET_INITIALIZER).add(second);
    }

    public void rightOf(Component first, Component second) {
        leftToRight.computeIfAbsent(second, SET_INITIALIZER).add(first);
    }

    public void width(Component component, Double percent) {
        componentToWidthWeight.put(component, percent);
    }

    public void height(Component component, Double percent) {
        componentToHeightWeight.put(component, percent);
    }
}
