package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.Color;
import com.github.nathankuhn.graphicsalpha.utils.Texture;

import java.nio.ByteBuffer;

public class Material {

    private Color ambient;
    private Color diffuse;
    private Color specular;
    private boolean textured;
    private float reflectance;

    public Material(Color ambient, Color diffuse, Color specular, boolean textured, float reflectance) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.textured = textured;
        this.reflectance = reflectance;
    }

    public Color getAmbient() {
        return ambient;
    }

    public void setAmbient(Color ambient) {
        this.ambient = ambient;
    }

    public Color getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Color diffuse) {
        this.diffuse = diffuse;
    }

    public Color getSpecular() {
        return specular;
    }

    public void setSpecular(Color specular) {
        this.specular = specular;
    }

    public boolean isTextured() {
        return textured;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }
}
