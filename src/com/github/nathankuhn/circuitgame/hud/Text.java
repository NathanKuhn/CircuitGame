package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector2i;

public class Text extends HudElement{

    private Font font;
    private String text;

    public Text(HudElement parent, Vector2f center, Font font, String text) {
        super(parent, center, new Vector2f(0.1f, 0.1f), false);

        this.font = font;
//        setRenderObject( new RenderObject(
//                FlatMesh.BuildTextMesh(new Vector2f(), )
//        ));


    }

    public void updateText() {

        String[] lines = text.split("\n");

        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength)
                maxLength = line.length();
        }



    }

    private char[][] getCharMap() {

        String[] lines = text.split("\n");

        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength)
                maxLength = line.length();
        }

        char[][] ret = new char[lines.length][maxLength];

        return ret;

    }
}
