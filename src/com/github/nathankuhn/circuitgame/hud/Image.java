package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Texture;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector2i;
import com.github.nathankuhn.circuitgame.utils.Vector3f;

public class Image extends HudElement{

    private Texture texture;

    public Image(HudElement parent, Vector2f center, Vector2i windowDimensions, Texture texture) {
        super(parent, center, texture.getIdealDimensions(windowDimensions), false);
        this.texture = texture;
        setRenderObject( new RenderObject(
                FlatMesh.BuildHudMesh(getRelativeCenter(), new Vector2f(1.0f, 1.0f), 1, 1, 0.0f),
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
        getRenderObject().transform.setScale(new Vector3f(texture.getIdealDimensions(windowDimensions), 1.0f));
        getRenderObject().transform.setPosition(new Vector3f(getRelativeCenter(), getZOffset()));
    }

}
