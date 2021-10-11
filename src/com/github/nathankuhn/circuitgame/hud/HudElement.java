package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector2i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import java.util.List;

public abstract class HudElement {

    private HudElement parent;
    private Vector2f center;
    private Vector2f dimensions;
    private RenderObject renderObject;

    public HudElement(Vector2f center, Vector2f dimensions) {
        this.center = center;
        this.dimensions = dimensions;
    }

    public HudElement getParent() {
        return parent;
    }

    public Vector2f getCenter() {
        return center;
    }

    public Vector2f getRelativeCenter() {
        if (parent == null) {
            return center;
        }
        return VectorMath.Add(center, parent.getCenter());
    }

    public Vector2f getDimensions() {
        return dimensions;
    }

    public boolean getShouldRender() {
        if (renderObject != null) {
            return renderObject.shouldRender();
        }
        return false;
    }

    public void setParent(HudElement parent) {
        this.parent = parent;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public void setDimensions(Vector2f dimensions) {
        this.dimensions = dimensions;
    }

    void setRenderObject(RenderObject renderObject) {
        this.renderObject = renderObject;
    }

    public void setShouldRender(boolean shouldRender) {
        if (renderObject != null) {
            renderObject.setShouldRender(shouldRender);
        }
    }

    public RenderObject getRenderObject() {
        return renderObject;
    }

    public void update(Vector2i windowDimensions) {}

}
