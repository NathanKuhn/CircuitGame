package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.rendering.BlockTexture;

public class Block {

    private String displayName;
    private int blockID;
    private BlockTexture texture;

    public Block(String displayName, int blockID, BlockTexture texture) {
        this.displayName = displayName;
        this.blockID = blockID;
        this.texture = texture;
    }

    public String getDisplayName() {
        return displayName;
    }
    public int getBlockID() {
        return blockID;
    }
    public BlockTexture getBlockTexture() {
        return texture;
    }
}
