package com.github.nathankuhn.circuitgame.utils;

import org.lwjgl.glfw.GLFW;

public class Timer {

    private double lastTime;
    private float deltaTime;

    public Timer() {
        lastTime = getCurrentTime();
    }

    public float deltaTime() {
        return deltaTime;
    }

    public void update() {
        double temp = lastTime;
        lastTime = getCurrentTime();
        deltaTime = (float) (lastTime - temp);
    }

    public void reset() {
        lastTime = getCurrentTime();
    }

    private double getCurrentTime() {
        return (GLFW.glfwGetTime());
    }


}
