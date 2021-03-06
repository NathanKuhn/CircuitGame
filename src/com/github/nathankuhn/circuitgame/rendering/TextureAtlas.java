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
        return VectorMath.Add(VectorMath.Scale(uv, 1.0f / atlasTextureWidth), getUV(textureID));
    }

    private Vector2f getUV(int textureID) {
        int x = (textureID % atlasTextureWidth);
        int y = atlasTextureWidth - (textureID / atlasTextureWidth) - 1;
        return new Vector2f(
                (float) x  / (float) atlasTextureWidth,
                (float) y  / (float) atlasTextureWidth
        );
    }

    public int getNumTexturesWide() {
        return atlasTextureWidth;
    }

    public Texture getTexture() {
        return texture;
    }

}
