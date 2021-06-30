package com.github.nathankuhn.graphicsalpha.utils;

public class Vector2 {

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2(Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

}
