package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.rendering.BlockTexture;

public class Block {

    private final String displayName;
    private final int blockID;
    private BlockTexture texture;
    private final int layer;

    public Block(String displayName, int blockID, BlockTexture texture) {
        this.displayName = displayName;
        this.blockID = blockID;
        this.texture = texture;
        layer = 1;
    }

    public Block(String displayName, int blockID, BlockTexture texture, int layer) {
        this.displayName = displayName;
        this.blockID = blockID;
        this.texture = texture;
        this.layer = layer;
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

    public int getLayer() {
        return layer;
    }
}
