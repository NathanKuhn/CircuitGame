package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.Color;
import com.github.nathankuhn.graphicsalpha.utils.Texture;

import java.nio.ByteBuffer;

public class Material {

    private Texture albedo;

    public Material(Texture albedo) {
        this.albedo = albedo;
    }

    public Texture getAlbedo() {
        return albedo;
    }
    public int getTextureWidth() {
        return albedo.getWidth();
    }
    public int getTextureHeight() {
        return albedo.getHeight();
    }
    public ByteBuffer getTextureBuffer() {
        return albedo.getBuffer();
    }
}
