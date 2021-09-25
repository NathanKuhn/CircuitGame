package com.github.nathankuhn.circuitgame.utils;

public class VectorMath {

    public static Vector3f Add(Vector3f a, Vector3f b) {
        return new Vector3f(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    public static Vector3i Add(Vector3i a, Vector3i b) {
        return new Vector3i(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    public static Vector3f Add(Vector3f a, Vector3i b) {
        return new Vector3f(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    public static Vector3f Subtract(Vector3f a, Vector3f b) {
        return new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    public static Vector3f Scale(Vector3f a, float s) {
        return new Vector3f(a.x * s, a.y * s, a.z * s);
    }
    public static Vector3f Normalize(Vector3f a) {
        return Scale(a, 1.0f / a.length());
    }
}
