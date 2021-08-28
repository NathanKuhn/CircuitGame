package com.github.nathankuhn.graphicsalpha.utils;

public class Timer {

    private final float UNITS = 1_000_000_000.0f;

    private long lastTime;
    private float deltaTime;

    public Timer() {
        lastTime = getCurrentTime();
    }

    public float deltaTime() {
        return deltaTime;
    }

    public void update() {
        long temp = lastTime;
        lastTime = getCurrentTime();
        deltaTime = (lastTime - temp) / UNITS;
    }

    private long getCurrentTime() {
        return System.nanoTime();
    }


}
