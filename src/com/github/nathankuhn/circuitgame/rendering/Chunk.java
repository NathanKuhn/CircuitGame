package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    protected static int GetIndex(int x, int y, int z) {
        return x + y * 16 + z * 256;
    }
    protected static Vector3i GetLocation(int index) {
        return new Vector3i(index % 16, (index / 16) % 16 , (index / 256));
    }
    private static boolean InBounds(int x, int y, int z) {
        return (x >= 0 && x < 16 && y >= 0 && y < 16 && z >= 0 && z < 16);
    }

    private int[] blockIDs;
    private Mesh mesh;
    private RenderObject renderObject;
    private Vector3i location;
    private TextureAtlas textureAtlas;

    public Chunk(int[] blocks, Vector3i location, TextureAtlas textureAtlas) {
        this.blockIDs = blocks;
        this.location = location;
        this.textureAtlas = textureAtlas;
    }

    public void init() throws Exception{
        update();
        renderObject = new RenderObject(mesh, textureAtlas.getTexture());
        renderObject.init();
    }

    public void update() {
        List<Mesh> meshes = new ArrayList<>();

        for (int b = 0; b < 4096; b++) {
            if (blockIDs[b] != 0) {
                Block block = new Block("Stone", "stone", blockIDs[b]);
                BlockMesh cube = new BlockMesh(getData(GetLocation(b)), VectorMath.Add(GetLocation(b), location), block, textureAtlas);
                meshes.add(cube.getMesh());
            }
        }

        mesh = Mesh.CombineMesh(meshes.toArray(new Mesh[0]));
    }
    private BlockMesh.CubeSideData getData(Vector3i p) {

        boolean north = hasBlock(p.x, p.y, p.z - 1);
        boolean south = hasBlock(p.x, p.y, p.z + 1);
        boolean east = hasBlock(p.x + 1, p.y, p.z);
        boolean west = hasBlock(p.x - 1, p.y, p.z);
        boolean up = hasBlock(p.x, p.y + 1, p.z);
        boolean down = hasBlock(p.x, p.y - 1, p.z);

        return new BlockMesh.CubeSideData(!north, !south, !east, !west, !up, !down);
    }
    public Mesh getMesh() {
        return mesh;
    }
    public RenderObject getRenderObject() {
        return renderObject;
    }
    public boolean hasBlock(int x, int y, int z) {
        if (InBounds(x, y, z)) {
            return blockIDs[GetIndex(x, y, z)] != 0;
        } else {
            return false;
        }
    }
    public int getBlock(int x, int y, int z) {
        return blockIDs[GetIndex(x, y, z)];
    }
    public void placeBlock(int x, int y, int z, int blockID) {
        blockIDs[GetIndex(x, y, z)] = blockID;
    }
    public void breakBlock(int x, int y, int z) {
        blockIDs[GetIndex(x, y, z)] = 0;
    }

}
