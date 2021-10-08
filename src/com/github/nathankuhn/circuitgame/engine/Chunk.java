package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.rendering.BlockMesh;
import com.github.nathankuhn.circuitgame.rendering.ChunkMesh;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class Chunk {

    private static boolean InBounds(int x, int y, int z) {
        return (x >= 0 && x < 16 && y >= 0 && y < 16 && z >= 0 && z < 16);
    }

    private World world;
    private int[][][] blockIDs;
    private Vector3i position;
    private ChunkMesh chunkMesh;

    public Chunk(World world, int[][][] blocks, Vector3i position) {
        if (blocks.length != 16 || blocks[0].length != 16 || blocks[0][0].length != 16) {
            // outside of chunk
        }
        this.world = world;
        this.blockIDs = blocks;
        this.position = position;
        chunkMesh = new ChunkMesh(world.getTextureAtlas(), this);
    }

    public BlockMesh.CubeSideData getSideData(Vector3i a) {

        Vector3i p = VectorMath.Add(a, position);

        boolean north = world.getBlock(p.x, p.y, p.z - 1) != 0;
        boolean south = world.getBlock(p.x, p.y, p.z + 1) != 0;
        boolean east = world.getBlock(p.x + 1, p.y, p.z) != 0;
        boolean west = world.getBlock(p.x - 1, p.y, p.z) != 0;
        boolean up = world.getBlock(p.x, p.y + 1, p.z) != 0;
        boolean down = world.getBlock(p.x, p.y - 1, p.z) != 0;

        return new BlockMesh.CubeSideData(!north, !south, !east, !west, !up, !down);
    }

    public ChunkMesh getChunkMesh() {
        return chunkMesh;
    }

    public void update() {
        chunkMesh.updateMesh();
    }

    public Vector3i getPosition() {
        return position;
    }

    public Block getBlock(int x, int y, int z) {
        return world.getBlockRegistry().getBlock(blockIDs[x][y][z]);
    }

    public int getBlockID(int x, int y, int z) {
        return blockIDs[x][y][z];
    }

    public void setBlock(int x, int y, int z, int blockID) {
        blockIDs[x][y][z] = blockID;
    }

}
