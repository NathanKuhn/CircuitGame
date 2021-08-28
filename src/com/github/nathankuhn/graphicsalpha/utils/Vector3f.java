package com.github.nathankuhn.graphicsalpha.utils;

public class Vector3f {

    public static final Vector3f RIGHT = new Vector3f(1.0f, 0.0f, 0.0f);
    public static final Vector3f LEFT = new Vector3f(-1.0f, 0.0f, 0.0f);
    public static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);
    public static final Vector3f DOWN = new Vector3f(0.0f, -1.0f, 0.0f);
    public static final Vector3f BACK = new Vector3f(0.0f, 0.0f, 1.0f);
    public static final Vector3f FRONT = new Vector3f(0.0f, 0.0f, -1.0f);

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
    public Vector3f(Color color) {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    public float[] getArray() {
        return new float[] {x, y, z};
    }

    public void addSet(Vector3f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }
    public void subtractSet(Vector3f other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }
    public void scaleSet(float other) {
        this.x *= other;
        this.y *= other;
        this.z *= other;
    }
    public void productSet(Vector3f other) {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
    }

}
