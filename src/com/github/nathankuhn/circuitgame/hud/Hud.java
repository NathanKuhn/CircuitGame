package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Texture;
import com.github.nathankuhn.circuitgame.utils.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Hud {

    private List<RenderObject> renderObjects;

    public Hud() {
        renderObjects = new ArrayList<>();
    }

    public List<RenderObject> getRenderObjects() {
        return renderObjects;
    }

    public void addRenderObject(RenderObject renderObject) {
        renderObjects.add(renderObject);
    }
}
