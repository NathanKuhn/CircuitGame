package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Texture;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector2i;

public class Image extends HudElement{

    private Texture texture;

    public Image(Vector2f center, Vector2i windowDimensions, Texture texture) {
        super(center, texture.getIdeaDimensions(windowDimensions));
        this.texture = texture;
        setRenderObject( new RenderObject(
                FlatMesh.BuildHudMesh(getCenter(), texture.getIdeaDimensions(windowDimensions), 1, 1),
                texture
        ));
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void update(Vector2i windowDimensions) {
        setRenderObject( new RenderObject(
                FlatMesh.BuildHudMesh(getCenter(), texture.getIdeaDimensions(windowDimensions), 1, 1),
                texture
        ));
    }

}
