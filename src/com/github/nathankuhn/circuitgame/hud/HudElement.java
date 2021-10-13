package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector2i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import java.util.ArrayList;
import java.util.List;

public abstract class HudElement {

    private HudElement parent;
    private List<HudElement> children;
    private Vector2f center;
    private Vector2f dimensions;
    private float zOffset;
    private RenderObject renderObject;

    public HudElement(HudElement parent, Vector2f center, Vector2f dimensions, boolean hasChildren) {
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
        this.center = center;
        this.dimensions = dimensions;
        if (hasChildren) {
            children = new ArrayList<>();
        }
        if (parent != null) {
            zOffset = parent.getZOffset() + 1.0f;
        }
    }

    public HudElement getParent() {
        return parent;
    }

    public List<HudElement> getChildren() {
        return children;
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

    public float getZOffset() {
        return zOffset;
    }

    public boolean getShouldRender() {
        if (renderObject != null) {
            return renderObject.shouldRender();
        }
        return false;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public void setDimensions(Vector2f dimensions) {
        this.dimensions = dimensions;
    }

    private void addChild(HudElement hudElement) {
        children.add(hudElement);
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
