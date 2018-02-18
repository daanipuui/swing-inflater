package com.danielpuiu.swing.layout;

public class Bounds {

    int x;
    int y;

    int width;
    int height;

    public Bounds() {
    }

    public Bounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
