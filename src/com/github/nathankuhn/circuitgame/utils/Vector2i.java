package com.github.nathankuhn.circuitgame.utils;

public class Vector2i {

    public int x;
    public int y;

    public Vector2i() {
        x = 0;
        y = 0;
    }
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Vector2i(Vector3i vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public void scaleSet(float t) {
        x *= t;
        y *= t;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}