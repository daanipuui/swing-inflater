package com.danielpuiu.swing.component;

import com.danielpuiu.swing.layout.RelativeLayout;

import java.awt.*;

public class RelativeContainer extends Container {

    private final RelativeLayout relativeLayout = new RelativeLayout();

    public RelativeContainer() {
        super.setLayout(relativeLayout);
    }

    @Override
    public final void setLayout(LayoutManager mgr) {
        throw new UnsupportedOperationException();
    }

    public void above(Component first, Component second) {
        relativeLayout.above(first, second);
    }

    public void alignBottom(Component first, Component second) {
        relativeLayout.alignBottom(first, second);
    }

    public void alignLeft(Component first, Component second) {
        relativeLayout.alignLeft(first, second);
    }

    public void alignParentBottom(Component component) {
        relativeLayout.alignBottom(component, this);
    }

    public void alignParentLeft(Component component) {
        relativeLayout.alignLeft(component, this);
    }

    public void alignParentRight(Component component) {
        relativeLayout.alignRight(component, this);
    }

    public void alignParentTop(Component component) {
        relativeLayout.alignTop(component, this);
    }

    public void alignRight(Component first, Component second) {
        relativeLayout.alignRight(first, second);
    }

    public void alignTop(Component first, Component second) {
        relativeLayout.alignTop(first, second);
    }

    public void below(Component first, Component second) {
        relativeLayout.below(first, second);
    }

    public void centerHorizontal(Component first, Component second) {
        relativeLayout.centerHorizontal(first, second);
    }

    public void centerVertical(Component first, Component second) {
        relativeLayout.centerVertical(first, second);
    }

    public void leftOf(Component first, Component second) {
        relativeLayout.leftOf(first, second);
    }

    public void rightOf(Component first, Component second) {
        relativeLayout.rightOf(first, second);
    }

    public void width(Component component, Double percent) {
        relativeLayout.width(component, percent);
    }

    public void height(Component component, Double percent) {
        relativeLayout.height(component, percent);
    }

    public void setMargins(int top, int left, int bottom, int right) {
        relativeLayout.setMargins(top, left, bottom, right);
    }

    public void setPadding(int top, int left, int bottom, int right) {
        relativeLayout.setPadding(top, left, bottom, right);
    }
}
