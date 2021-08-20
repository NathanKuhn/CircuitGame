package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.Color;

public class Material {

    private Color[][] albedo;
    private Color[][] normal;
    private float[][] specular;

    public Material(Color[][] albedo, Color[][] normal, float[][] specular) {
        this.albedo = albedo;
        this.normal = normal;
        this.specular = specular;
    }

    public Color[][] getAlbedo() {
        return albedo;
    }

    public Color[][] getNormal() {
        return normal;
    }

    public float[][] getSpecular() {
        return specular;
    }
}
