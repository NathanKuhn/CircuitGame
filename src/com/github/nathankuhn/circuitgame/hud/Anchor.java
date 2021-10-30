package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class Anchor extends HudElement{

    public Anchor(HudElement parent, Vector2f offset) {
        super(parent, offset, new Vector2f(), true);
    }

    @Override
    public Vector2f getCenter() {
        return VectorMath.Multiply(super.getCenter(), getParent().getDimensions());
    }

}
