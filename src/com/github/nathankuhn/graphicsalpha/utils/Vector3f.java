package com.github.nathankuhn.graphicsalpha.utils;

public class Vector3f {

    public float x;
    public float y;
    public float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3f(Vector2f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = 1.0f;
    }
    public Vector3f(Vector2f vector, float z) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = z;
    }
    public Vector3f(Vector4f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

}
