package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.*;

public class EnvironmentLight {

    private Vector3f sunDirection;
    private Color sunColor;
    private float sunIntensity;
    private Color ambientColor;
    private float ambientIntensity;

    public EnvironmentLight(Vector3f sunDirection, Color sunColor, float sunIntensity, Color ambientColor, float ambientIntensity) {
        this.sunDirection = sunDirection;
        this.sunColor = sunColor;
        this.sunIntensity = sunIntensity;
        this.ambientColor = ambientColor;
        this.ambientIntensity = ambientIntensity;
    }

    public Vector3f getSunDirection() {
        return sunDirection;
    }
    public void setSunDirection(Vector3f sunDirection) {
        this.sunDirection = sunDirection;
    }
    public Color getSunColor() {
        return sunColor;
    }
    public void setSunColor(Color sunColor) {
        this.sunColor = sunColor;
    }
    public float getSunIntensity() {
        return sunIntensity;
    }
    public void setSunIntensity(float sunIntensity) {
        this.sunIntensity = sunIntensity;
    }
    public Color getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Color ambientColor) {
        this.ambientColor = ambientColor;
    }
    public float getAmbientIntensity() {
        return ambientIntensity;
    }
    public void setAmbientIntensity(float ambientIntensity) {
        this.ambientIntensity = ambientIntensity;
    }
}
