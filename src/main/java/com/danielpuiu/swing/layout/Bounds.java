package com.danielpuiu.swing.layout;

import java.awt.*;

public class Bounds {

    int x;
    int y;

    int width;
    int height;

    Bounds() {
    }

    Bounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    Bounds(Component component) {
        this.x = component.getX();
        this.y = component.getY();
        this.width = component.getWidth();
        this.height = component.getHeight();
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
