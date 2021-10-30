package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector2i;
import com.github.nathankuhn.circuitgame.utils.Vector3f;

public class Text extends HudElement{

    private Font font;
    private String text;

    public Text(HudElement parent, Vector2f center, Font font, String text) {
        super(parent, center, new Vector2f(1.0f, 1.0f), false);

        this.font = font;
        this.text = text;
        setRenderObject( new RenderObject(
                FlatMesh.BuildTextMesh(new Vector2f(), getCharMap(), font, getZOffset()),
                font.getTexture()
        ));
    }

    public void update(Vector2i windowDimensions) {
        if (getRenderObject() != null) {
            getRenderObject().transform.setScale(new Vector3f(font.getIdealTileSize(windowDimensions), 1.0f));
            getRenderObject().transform.setPosition(new Vector3f(getRelativeCenter(), getZOffset()));
        }
    }

    public void setText(String text) {
        this.text = text;
        getRenderObject().setMesh(FlatMesh.BuildTextMesh(new Vector2f(), getCharMap(), font, getZOffset()));

    }

    private char[][] getCharMap() {

        String[] lines = text.split("\n");

        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength)
                maxLength = line.length();
        }

        char[][] ret = new char[lines.length][maxLength];

        for (int row = 0; row < ret.length; row++) {
            for (int col = 0; col < ret[0].length; col++) {
                if (col < lines[row].length()) {
                    ret[row][col] = lines[row].charAt(col);
                } else {
                    ret[row][col] = ' ';
                }
            }
        }

        return ret;

    }
}
