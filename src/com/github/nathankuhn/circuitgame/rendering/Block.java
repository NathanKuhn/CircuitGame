package com.github.nathankuhn.circuitgame.rendering;

public class Block {

    private String displayName;
    private String blockID;
    private int textureID;

    public Block(String displayName, String blockID, int textureID) {
        this.displayName = displayName;
        this.blockID = blockID;
        this.textureID = textureID;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getBlockID() {
        return blockID;
    }
    public int getTextureID() {
        return textureID;
    }
}
