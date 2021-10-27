package com.github.nathankuhn.circuitgame.utils;

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

    public Vector3f() {
        x = 0;
        y = 0;
        z = 0;
    }
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

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public void addSet(Vector3f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void addSet(Vector3i other) {
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

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Vector3i toVector3i() {
        return new Vector3i((int)x, (int)y, (int)z);
    }

    public Vector3i toVector3iWorld() {
        Vector3i ret = new Vector3i((int)x, (int)y, (int)z);
        if (x < 0) {
            ret.x = (int)x - 1;
        }
        if (y < 0) {
            ret.y = (int)y - 1;
        }
        if (z < 0) {
            ret.z = (int)z - 1;
        }
        return ret;
    }

    public Vector3i floor() {
        return new Vector3i((int)Math.floor((double)x), (int)Math.floor((double)y), (int)Math.floor((double)z));
    }

    public Vector3i ceil() {
        return new Vector3i((int)Math.ceil((double)x), (int)Math.floor((double)y), (int)Math.floor((double)z));
    }

    public Vector3f clone() {
        return new Vector3f(x, y, z);
    }
}
