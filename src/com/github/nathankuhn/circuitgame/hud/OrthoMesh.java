package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.Mesh;
import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.*;

public class OrthoMesh extends HudElement{

    private Mesh mesh;
    private Texture texture;

    public OrthoMesh(HudElement parent, float scale, Vector2f center, Vector3f rotation, Mesh mesh, Texture texture) {
        super(parent, center, new Vector2f(scale, scale), false);
        this.mesh = mesh;
        this.texture = texture;
        setRenderObject(new RenderObject(mesh, texture));
        getRenderObject().transform.setCorrection(new Vector3f(-0.5f, -0.5f, -0.5f));
        getRenderObject().transform.setScale(new Vector3f(scale, scale, scale));
        getRenderObject().transform.setPosition(new Vector3f(getRelativeCenter(), getZOffset()));
        getRenderObject().transform.setRotation(rotation);
    }

    public void update() {
        getRenderObject().transform.setPosition(new Vector3f(getRelativeCenter(), getZOffset()));
    }

}
