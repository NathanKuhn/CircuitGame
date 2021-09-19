package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.Color;
import com.github.nathankuhn.graphicsalpha.utils.Vector3f;

public class PointLight {

    private Color color;
    private Vector3f position;
    private float intensity;
    private Attenuation att;

    public PointLight(Color color, Vector3f position, float intensity, Attenuation att) {
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.att = att;
    }
    public PointLight(PointLight pointLight) {
        this.color = pointLight.getColor();
        this.position = pointLight.getPosition();
        this.intensity = pointLight.getIntensity();
        this.att = pointLight.getAtt();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Attenuation getAtt() {
        return att;
    }

    public static class Attenuation {

        private float constant;
        private float linear;
        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public float getLinear() {
            return linear;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }

        public float getExponent() {
            return exponent;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }

}
