package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class World {

    private Chunk[][][] chunks;
    private TextureAtlas textureAtlas;
    private int xChunks;
    private int yChunks;
    private int zChunks;

    public World(TextureAtlas textureAtlas, int xChunks, int yChunks, int zChunks) {
        this.textureAtlas = textureAtlas;
        this.xChunks = xChunks;
        this.yChunks = yChunks;
        this.zChunks = zChunks;
        chunks = new Chunk[xChunks][yChunks][zChunks];
    }

    public void generateAll() {
        int[] map = new int[4096];

        // Basic terrain generation

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 16; z++) {
                    if (y >= 3) {
                        map[Chunk.GetIndex(x, y, z)] = 4;
                    } else {
                        map[Chunk.GetIndex(x, y, z)] = 1;
                    }
                }
            }
        }

        // Copy map into every chunk

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                for (int z = 0; z < chunks[0][0].length; z++) {
                    chunks[x][y][z] = new Chunk(map.clone(), new Vector3i(x * 16, y * 16, z * 16), textureAtlas);
                    try {
                        chunks[x][y][z].init();
                    } catch (Exception e) {
                        System.out.println("Could not load chunk.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public List<RenderObject> getRenderList() {
        List<RenderObject> ret = new ArrayList<>();

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                for (int z = 0; z < chunks[0][0].length; z++) {
                    try {
                        ret.add(chunks[x][y][z].getRenderObject());
                    } catch (Exception e) {
                        System.out.println("Failed to load chunk.");
                        e.printStackTrace();
                    }
                }
            }
        }

        return ret;
    }

    public int getChunks() {
        return xChunks * yChunks * zChunks;
    }
}
