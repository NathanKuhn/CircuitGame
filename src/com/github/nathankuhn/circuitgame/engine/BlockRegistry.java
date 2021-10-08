package com.github.nathankuhn.circuitgame.engine;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {

    private Map<Integer, Block> registry;

    public BlockRegistry() {
        registry = new HashMap<>();
    }

    public void addBlock(Block block) {
        registry.put(block.getBlockID(), block);
    }

    public Block getBlock(int id) {
        return registry.get(id);
    }

}
