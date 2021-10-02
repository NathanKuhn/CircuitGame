package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private static boolean InBounds(int x, int y, int z) {
        return (x >= 0 && x < 16 && y >= 0 && y < 16 && z >= 0 && z < 16);
    }

    private World world;
    private int[][][] blockIDs;
    private Mesh mesh;
    private RenderObject renderObject;
    private Vector3i location;
    private TextureAtlas textureAtlas;

    public Chunk(World world, int[][][] blocks, Vector3i location, TextureAtlas textureAtlas) {
        if (blocks.length != 16 || blocks[0].length != 16 || blocks[0][0].length != 16) {
            System.out.println("you are a dodo head");
        }
        this.world = world;
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

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (blockIDs[x][y][z] != 0) {
                        Block block = new Block("Stone", "stone", blockIDs[x][y][z]);
                        BlockMesh cube = new BlockMesh(getData(new Vector3i(x, y, z)), VectorMath.Add(new Vector3i(x, y, z), location), block, textureAtlas);
                        meshes.add(cube.getMesh());
                    }
                }
            }
        }

        mesh = Mesh.CombineMesh(meshes.toArray(new Mesh[0]));
        if (renderObject != null) {
            renderObject.setMesh(mesh);
            renderObject.storeMeshData();
        }

    }
    private BlockMesh.CubeSideData getData(Vector3i a) {

        Vector3i p = VectorMath.Add(a, location);

        boolean north = world.getBlock(p.x, p.y, p.z - 1) != 0;
        boolean south = world.getBlock(p.x, p.y, p.z + 1) != 0;
        boolean east = world.getBlock(p.x + 1, p.y, p.z) != 0;
        boolean west = world.getBlock(p.x - 1, p.y, p.z) != 0;
        boolean up = world.getBlock(p.x, p.y + 1, p.z) != 0;
        boolean down = world.getBlock(p.x, p.y - 1, p.z) != 0;

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
            return blockIDs[x][y][z] != 0;
        } else {
            return false;
        }
    }
    public int getBlock(int x, int y, int z) {
        return blockIDs[x][y][z];
    }
    public void placeBlock(int x, int y, int z, int blockID) {
        blockIDs[x][y][z] = blockID;
    }
    public void breakBlock(int x, int y, int z) {
        blockIDs[x][y][z] = 0;
    }

}
