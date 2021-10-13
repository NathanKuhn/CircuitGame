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

    public static Vector2f Add(Vector2f a, Vector2f b) {
        return new Vector2f(a.x + b.x, a.y + b.y);
    }

    public static Vector2f Subtract(Vector2f a, Vector2f b) {
        return new Vector2f(a.x - b.x, a.y - b.y);
    }

    public static Vector3f Subtract(Vector3f a, Vector3f b) {
        return new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3f Subtract(Vector3f a, Vector3i b) {
        return new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3f Scale(Vector3f a, float s) {
        return new Vector3f(a.x * s, a.y * s, a.z * s);
    }

    public static Vector3f Scale(Vector3i a, float s) {
        return new Vector3f(a.x * s, a.y * s, a.z * s);
    }

    public static Vector2f Scale(Vector2f a, float s) {
        return new Vector2f(a.x * s, a.y * s);
    }

    public static Vector3f Normalize(Vector3f a) {
        return Scale(a, 1.0f / a.length());
    }

    public static float Lerp(float a0, float a1, float t) {
        return (a1 - a0) * t + a0;
    }

    public static Vector2f Lerp(Vector2f a0, Vector2f a1, float t) {
        return Add(a0, Scale(Subtract(a1, a0), t));
    }

    public static Vector3f Lerp(Vector3f a0, Vector3f a1, float t) {
        return Add(a0, Scale(Subtract(a1, a0), t)); // a + t * (b - a)
    }

    public static Vector2f Multiply(Vector2f a, Vector2f b) {
        return new Vector2f(a.x * b.x, a.y * b.y);
    }
}
