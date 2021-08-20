package com.github.nathankuhn.graphicsalpha.utils;

public class Vector4f {

    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vector4f(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = 1.0f;
    }
    public Vector4f(Vector3f vector, float w) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = w;
    }

}
