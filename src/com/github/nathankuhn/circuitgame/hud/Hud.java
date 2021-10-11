package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Texture;
import com.github.nathankuhn.circuitgame.utils.Vector2i;
import com.github.nathankuhn.circuitgame.utils.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Hud {

    private List<HudElement> hudElements;

    public Hud() {
        hudElements = new ArrayList<>();
    }

    public List<RenderObject> getRenderObjects() {
        List<RenderObject> ret = new ArrayList<>();

        for (HudElement hudElement : hudElements) {
            ret.add(hudElement.getRenderObject());
        }

        return ret;
    }

    public void addHudElement(HudElement hudElement) {
        hudElements.add(hudElement);
    }

    protected void updateHudElementResize(Vector2i windowDimensions) {
        for (HudElement hudElement : hudElements) {
            hudElement.update(windowDimensions);
        }
    }
}
