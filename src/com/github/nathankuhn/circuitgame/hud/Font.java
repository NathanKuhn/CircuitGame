package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.utils.Misc;
import com.github.nathankuhn.circuitgame.utils.Texture;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector2i;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Font {

    private static final String FONT_FILE_PATH = "res/fonts/";

    private Texture fontTexture;

    private int[] charIDs;
    private Vector2f[] charPos;
    private Vector2i characterPixelSize;
    private Vector2f characterSize;

    public Font(String fontFilePath) throws Exception{

        File fontFile = new File(FONT_FILE_PATH + fontFilePath);
        BufferedReader br = new BufferedReader(new FileReader(fontFile));

        String info = br.readLine();
        String common = br.readLine();

        String page = br.readLine();

        String texturePath = Misc.FindValue(page, "file=");
        fontTexture = Texture.LoadPNG(texturePath);

        String chars = br.readLine();

        int count = Integer.parseInt(Misc.FindValue(chars, "count="));

        charIDs = new int[count];
        charPos = new Vector2f[count];

        String line = "";

        for (int i = 0; i < count; i++) {
            line = br.readLine();
            charIDs[i] = Integer.parseInt(Misc.FindValue(line, "id="));
            charPos[i] = new Vector2f(
                    (float) Integer.parseInt(Misc.FindValue(line, "x=")) / fontTexture.getWidth(),
                    (float) Integer.parseInt(Misc.FindValue(line, "y=")) / fontTexture.getHeight()
            );
        }
        characterPixelSize = new Vector2i(
                Integer.parseInt(Misc.FindValue(line, "width=")),
                Integer.parseInt(Misc.FindValue(line, "height="))
        );

        characterSize = new Vector2f(
                (float) characterPixelSize.x / fontTexture.getWidth(),
                (float) characterPixelSize.y / fontTexture.getHeight()
        );

    }

    public Vector2f[] getUVCoords(char ch) {

        int index = Arrays.binarySearch(charIDs, ch);

        if (index == -1) {
            return getUVCoords(new Vector2f(0, 0));
        }

        return getUVCoords(charPos[index]);
    }

    public Texture getTexture() {
        return fontTexture;
    }

    private Vector2f[] getUVCoords(Vector2f pos) {
        return new Vector2f[] {
                new Vector2f(pos.x, pos.y),
                new Vector2f(pos.x + characterSize.x, pos.y),
                new Vector2f(pos.x + characterSize.x, pos.y + characterSize.y),
                new Vector2f(pos.x, pos.y + characterSize.y)
        };
    }

}
