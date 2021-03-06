package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.rendering.TextureAtlas;
import com.github.nathankuhn.circuitgame.utils.Misc;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private BlockRegistry blockRegistry;
    private TextureAtlas textureAtlas;
    private Chunk[][][] chunks;
    private int xChunks;
    private int yChunks;
    private int zChunks;
    private List<RenderObject> otherObjects;
    private int layers;
    private int seed;

    public World(BlockRegistry blockRegistry, TextureAtlas textureAtlas, int xChunks, int yChunks, int zChunks, int seed) {
        this.blockRegistry = blockRegistry;
        this.textureAtlas = textureAtlas;
        this.xChunks = xChunks;
        this.yChunks = yChunks;
        this.zChunks = zChunks;
        this.seed = seed;
        chunks = new Chunk[xChunks][yChunks][zChunks];
        otherObjects = new ArrayList<>();
        layers = 2;
    }

    public int[][][] generateChunkMap(int x0, int y0, int z0) {
        int[][][] map = new int[32][32][32];

        int heightValue;

        Random random = new Random(seed);

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                for (int z = 0; z < 32; z++) {
                    float noise = Misc.PerlinNoise((float) (x + x0) / 20.0f, (float) (z + z0) / 20.0f, seed);
                    noise += Misc.PerlinNoise((float) (x + x0) / 100.0f, (float) (z + z0) / 100.0f, ~seed) * 5;
                    heightValue = (int) (noise * 10 + 60);
                    float pumpkinNoise = Misc.PerlinNoise((float) (x + x0) / 20.0f, (float) (z + z0) / 20.0f, seed + 100);

                    if (y + y0 < heightValue - 4) {
                        map[x][y][z] = 1;
                    } else if (y + y0 < heightValue){
                        map[x][y][z] = 2;
                    } else if (y + y0 == heightValue){
                        map[x][y][z] = 3;
                    } else if (random.nextFloat() < (pumpkinNoise - 0.4f) / 0.6f && y + y0 == heightValue + 1) {
                        map[x][y][z] = 10;
                    }
                }
            }
        }

        return map;
    }

    public void generateAll() {
        int[][][] blankMap = new int[32][32][32];

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                for (int z = 0; z < chunks[0][0].length; z++) {
                    chunks[x][y][z] = new Chunk(
                            this,
                            generateChunkMap(x * 32, y * 32, z * 32),
                            new Vector3i((x - xChunks / 2) * 32, y * 32, (z - zChunks / 2) * 32)
                    );
                }
            }
        }

        // initialize chunks

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                for (int z = 0; z < chunks[0][0].length; z++) {
                    try {
                        chunks[x][y][z].getChunkMesh().init();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public List<RenderObject> getRenderList(int layer) {
        List<RenderObject> ret = new ArrayList<>();

        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks[0].length; y++) {
                for (int z = 0; z < chunks[0][0].length; z++) {
                    try {
                        ret.add(chunks[x][y][z].getChunkMesh().getRenderObject(layer));
                    } catch (Exception e) {
                        System.out.println("Failed to load chunk.");
                        e.printStackTrace();
                    }
                }
            }
        }
        return ret;
    }

    public List<RenderObject> getRenderList() {
        List<RenderObject> ret = new ArrayList<>();
        for (int i = 0; i < layers; i++) {
            ret.addAll(getRenderList(i));
        }
        ret.addAll(otherObjects);
        return ret;
    }

    public BlockRegistry getBlockRegistry() {
        return blockRegistry;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
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
            return n / 32;
        }
        return ((n + 1) / 32) - 1;
    }

    private int blockInChunk(int n) {
        if (n >= 0) {
            return n % 32;
        }
        return 31 + ((n + 1) % 32);
    }

    private Chunk getChunk(int x, int y, int z) {
        int xChunk = chunkFromBlock(x) + xChunks / 2;
        int yChunk = chunkFromBlock(y);
        int zChunk = chunkFromBlock(z) + zChunks / 2;
        if (xChunk < 0 || xChunk >= xChunks || yChunk < 0 || yChunk >= yChunks || zChunk < 0 || zChunk >= zChunks) {
            return null;
        }
        return chunks[xChunk][yChunk][zChunk];
    }

    public int getBlock(int x, int y, int z) {
        Chunk chunk = getChunk(x, y, z);
        if (chunk == null) {
            return 0;
        }
        try {
            return chunk.getBlockID(blockInChunk(x), blockInChunk(y), blockInChunk(z));
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

    public boolean hasBlockOfLayer(int x, int y, int z, int layer) {
        int block = getBlock(x, y, z);
        if (block == 0) {
            return false;
        }

        if (blockRegistry.getBlock(block).getLayer() == layer) {
            return true;
        }

        return false;
    }

    public boolean hasBlockOfLayer(Vector3i pos, int layer) {
        return hasBlockOfLayer(pos.x, pos.y, pos.z, layer);
    }

    public void placeBlock(int x, int y, int z, int blockID) {
        Chunk chunk = getChunk(x, y, z);
        if (chunk == null) {
            return;
        }
        chunk.setBlock(blockInChunk(x), blockInChunk(y), blockInChunk(z), blockID);
        chunk.update();

        // If block is on an edge, update the adjacent chunk
        Chunk otherChunk;
        if (blockInChunk(x) == 0) {
            otherChunk = getChunk(x-1, y, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        } else if (blockInChunk(x) == 31) {
            otherChunk = getChunk(x+1, y, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        }
        if (blockInChunk(y) == 0) {
            otherChunk = getChunk(x, y-1, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        } else if (blockInChunk(y) == 31) {
            otherChunk = getChunk(x, y+1, z);
            if (otherChunk != null) {
                otherChunk.update();
            }
        }
        if (blockInChunk(z) == 0) {
            otherChunk = getChunk(x, y, z-1);
            if (otherChunk != null) {
                otherChunk.update();
            }
        } else if (blockInChunk(z) == 31) {
            otherChunk = getChunk(x, y, z+1);
            if (otherChunk != null) {
                otherChunk.update();
            }
        }
    }

    public void placeBlock(Vector3i position, int blockID) {
        placeBlock(position.x, position.y, position.z, blockID);
    }

    public Vector3f getSpawn() {

        int posX = 0;
        int posZ = 0;
        int posY = 0;

        int blockID = 1;
        while(blockID != 0) {
            blockID = getBlock(posX, posY, posZ);
            posY++;
            if (posY > yChunks * 32)
                break;
        }

        return new Vector3f(
                posX + 0.5f,
                posY + 1.0f,
                posZ + 0.5f
        );
    }

    public void addOtherRenderObject(RenderObject other) {
        otherObjects.add(other);
    }

    public int getOtherObjectLength() {
        return otherObjects.size();
    }

    public RenderObject getOtherRenderObject(int index) {
        return otherObjects.get(index);
    }

    public int getLayers() {
        return layers;
    }

}
