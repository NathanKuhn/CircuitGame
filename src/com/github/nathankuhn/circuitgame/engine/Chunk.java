package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.rendering.BlockMesh;
import com.github.nathankuhn.circuitgame.rendering.ChunkMesh;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class Chunk {

    private static boolean InBounds(int x, int y, int z) {
        return (x >= 0 && x < 32 && y >= 0 && y < 32 && z >= 0 && z < 32);
    }

    private final World world;
    private int[][][] blockIDs;
    private Vector3i position;
    private ChunkMesh chunkMesh;

    public Chunk(World world, int[][][] blocks, Vector3i position) {
        if (blocks.length != 32 || blocks[0].length != 32 || blocks[0][0].length != 32) {
            // outside of chunk
        }
        this.world = world;
        this.blockIDs = blocks;
        this.position = position;
        chunkMesh = new ChunkMesh(world.getTextureAtlas(), this, world.getBlockRegistry(), world.getLayers());
    }

    public BlockMesh.CubeSideData getSideData(Vector3i a, int layer) {

        Vector3i p = VectorMath.Add(a, position);

        boolean north = world.hasBlockOfLayer(p.x, p.y, p.z - 1, layer);
        boolean south = world.hasBlockOfLayer(p.x, p.y, p.z + 1, layer);
        boolean east = world.hasBlockOfLayer(p.x + 1, p.y, p.z, layer);
        boolean west = world.hasBlockOfLayer(p.x - 1, p.y, p.z, layer);
        boolean up = world.hasBlockOfLayer(p.x, p.y + 1, p.z, layer);
        boolean down = world.hasBlockOfLayer(p.x, p.y - 1, p.z, layer);

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
