package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3f;
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
        int[][][] map = new int[16][16][16];

        // Basic terrain generation

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 16; z++) {
                    if (y >= 3) {
                        map[x][y][z] = 4;
                    } else {
                        map[x][y][z] = 1;
                    }
                }
            }
        }

        // Copy map into every chunk

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                for (int z = 0; z < chunks[0][0].length; z++) {
                    chunks[x][y][z] = new Chunk(this, copyMap(map), new Vector3i(x * 16, y * 16, z * 16), textureAtlas);
                }
            }
        }

        // initialize chunks

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                for (int z = 0; z < chunks[0][0].length; z++) {
                    try {
                        chunks[x][y][z].init();
                    } catch (Exception e) {
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

    private static int[][][] copyMap(int[][][] input) {
        int[][][] ret = new int[input.length][input[0].length][input[0][0].length];

        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input[0].length; y++) {
                ret[x][y] = input[x][y].clone();
            }
        }

        return ret;
    }

    private int chunkFromBlock(int n) {
        if (n >= 0) {
            return n / 16;
        } else {
            return (n / 16) - 1;
        }
    }
    private Chunk getChunk(int x, int y, int z) {
        int xChunk = chunkFromBlock(x);
        int yChunk = chunkFromBlock(y);
        int zChunk = chunkFromBlock(z);
        if (xChunk < 0 || xChunk >= xChunks || yChunk < 0 || yChunk >= yChunks || zChunk < 0 || zChunk >= zChunks) {
            return null;
        }
        return chunks[xChunk][yChunk][zChunk];
    }

    public int getBlock(int x, int y, int z) { // TODO use getChunk
        int xChunk = chunkFromBlock(x);
        int yChunk = chunkFromBlock(y);
        int zChunk = chunkFromBlock(z);
        if (xChunk < 0 || xChunk >= xChunks || yChunk < 0 || yChunk >= yChunks || zChunk < 0 || zChunk >= zChunks) {
            return 0;
        }
        try {
            return chunks[xChunk][yChunk][zChunk].getBlock(x % 16, y % 16, z % 16);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
    public int getBlock(Vector3i pos) {
        return getBlock(pos.x, pos.y, pos.z);
    }
    public int getBlock(float x, float y, float z) {
        return getBlock((int)x, (int)y, (int)z);
    }
    public void placeBlock(int x, int y, int z, int blockID) {
        Chunk chunk = getChunk(x, y, z);
        chunk.placeBlock(x % 16, y % 16, z % 16, blockID);
        chunk.update();

        // If block is on an edge, update the adjacent chunk
        Chunk otherChunk;
        if (x % 16 == 0) {
            otherChunk = getChunk(x-1, y, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        } else if (x % 16 == 15) {
            otherChunk = getChunk(x+1, y, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        }
        if (y % 16 == 0) {
            otherChunk = getChunk(x, y-1, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        } else if (y % 16 == 15) {
            otherChunk = getChunk(x, y+1, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        }
        if (z % 16 == 0) {
            otherChunk = getChunk(x, y, z-1);
            if (otherChunk != null) {
                otherChunk.update();
            }
        } else if (z % 16 == 15) {
            otherChunk = getChunk(x, y, z+1);
            if (otherChunk != null) {
                otherChunk.update();
            }
        }
    }
    public void placeBlock(Vector3i position, int blockID) {
        placeBlock(position.x, position.y, position.z, blockID);
    }
}
