package com.github.nathankuhn.graphicsalpha.utils;

public class Color {

    public float r;
    public float b;
    public float g;
    public float a;

    public Color(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0f;
    }
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Vector4f toVector() {
        return new Vector4f(r, g, b, a);
    }

}
