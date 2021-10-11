package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Color;
import com.github.nathankuhn.circuitgame.utils.Texture;
import com.github.nathankuhn.circuitgame.utils.Vector2f;

public class Panel extends HudElement{

    private Color color;

    public Panel(Vector2f center, Vector2f dimensions, Color color) {
        super(center, dimensions);
        this.color = color;
        setRenderObject( new RenderObject(
                FlatMesh.BuildHudMesh(getRelativeCenter(), getDimensions(), 1, 1),
                Texture.SingleColor(color)
        ));
    }

}
