package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Texture;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class TextureAtlas {

    private int singleTextureWidth;
    private int atlasTextureWidth;
    private Texture texture;

    public TextureAtlas(Texture texture, int singleTextureWidth){
        this.singleTextureWidth = singleTextureWidth;
        this.texture = texture;
        if (texture.getWidth() % singleTextureWidth != 0 || texture.getWidth() != texture.getHeight()) {
            System.err.println("Could not create texture atlas.");
        }

        atlasTextureWidth = texture.getWidth() / singleTextureWidth;
    }

    public Vector2f factorUV(Vector2f uv, int textureID) {
        return VectorMath.Add(VectorMath.Scale(uv, atlasTextureWidth), getUV(textureID));
    }

    private Vector2f getUV(int textureID) {
        int x = (textureID + 1) % atlasTextureWidth;
        int y = (textureID + 1) / atlasTextureWidth;
        return new Vector2f(
                x * 1.0f / atlasTextureWidth,
                y * 1.0f / atlasTextureWidth
        );
    }

    public int getNumTexturesWide() {
        return atlasTextureWidth;
    }

}
