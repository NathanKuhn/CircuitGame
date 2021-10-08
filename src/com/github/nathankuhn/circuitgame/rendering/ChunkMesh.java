package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.engine.Block;
import com.github.nathankuhn.circuitgame.engine.Chunk;
import com.github.nathankuhn.circuitgame.utils.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ChunkMesh {

    private Mesh mesh;
    private RenderObject renderObject;
    private TextureAtlas textureAtlas;
    private Chunk chunk;

    public ChunkMesh(TextureAtlas textureAtlas, Chunk chunk) {
        this.textureAtlas = textureAtlas;
        this.chunk = chunk;
    }

    public void init() throws Exception{
        updateMesh();
        renderObject = new RenderObject(mesh);
        renderObject.transform.translate(chunk.getPosition());
        renderObject.init();
    }

    public void updateMesh() {
        List<Mesh> meshes = new ArrayList<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (chunk.getBlockID(x, y, z) != 0) {
                        BlockMesh cube = new BlockMesh(chunk.getSideData(new Vector3i(x, y, z)), new Vector3i(x, y, z), chunk.getBlock(x, y, z), textureAtlas);
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

    public RenderObject getRenderObject() {
        return renderObject;
    }

}
