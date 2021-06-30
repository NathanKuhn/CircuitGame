package com.github.nathankuhn.graphicsalpha.utils;

public class Vector3 {

    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = 1.0f;
    }
    public Vector3(Vector2 vector, float z) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = z;
    }
    public Vector3(Vector4 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

}
