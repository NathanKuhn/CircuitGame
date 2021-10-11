package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.Mesh;
import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.*;

public class OrthoMesh extends HudElement{

    private Mesh mesh;
    private Texture texture;

    public OrthoMesh(Vector2f scale, Vector2f center, Vector3f rotation, Mesh mesh, Texture texture) {
        super(scale, center);
        this.mesh = mesh;
        this.texture = texture;
        Vector2f origin = VectorMath.Subtract(center, VectorMath.Scale(scale, 0.5f));
        setRenderObject(new RenderObject(mesh, texture));
        getRenderObject().transform.setScale(new Vector3f(scale, 0.0f));
        getRenderObject().transform.setPosition(new Vector3f(origin, 0.0f));
        getRenderObject().transform.setRotation(rotation);
    }

}
