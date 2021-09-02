package com.github.nathankuhn.graphicsalpha.utils;

public class Vector2f {

    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2f(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public void scaleSet(float t) {
        x *= t;
        y *= t;
    }

}
