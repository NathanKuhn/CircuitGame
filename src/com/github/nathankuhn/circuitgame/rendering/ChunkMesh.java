package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.engine.Block;
import com.github.nathankuhn.circuitgame.engine.BlockRegistry;
import com.github.nathankuhn.circuitgame.engine.Chunk;
import com.github.nathankuhn.circuitgame.utils.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ChunkMesh {

    private Mesh[] layerMeshes;
    private RenderObject[] layerRenderObjects;
    private int layers;
    private TextureAtlas textureAtlas;
    private Chunk chunk;
    private BlockRegistry blockRegistry;

    public ChunkMesh(TextureAtlas textureAtlas, Chunk chunk, BlockRegistry blockRegistry, int layers) {
        this.textureAtlas = textureAtlas;
        this.chunk = chunk;
        this.blockRegistry = blockRegistry;
        this.layers = layers;
        layerMeshes = new Mesh[layers];
        layerRenderObjects = new RenderObject[layers];
    }

    public void init() throws Exception{
        updateMesh();

        for(int i = 0; i < layers; i++) {
            layerRenderObjects[i] = new RenderObject(layerMeshes[i]);
            layerRenderObjects[i].transform.translate(chunk.getPosition());
            layerRenderObjects[i].init();
        }
    }

    public void updateMesh(int layer) {
        List<Mesh> meshes = new ArrayList<>();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                for (int z = 0; z < 32; z++) {

                    int blockID = chunk.getBlockID(x, y, z);
                    if (blockID != 0) {
                        Block block = blockRegistry.getBlock(blockID);
                        if (block.getLayer() == layer) {
                            BlockMesh cube = new BlockMesh(chunk.getSideData(new Vector3i(x, y, z), layer), new Vector3i(x, y, z), block, textureAtlas);
                            meshes.add(cube.getMesh());
                        }
                    }

                }
            }
        }

        layerMeshes[layer] = Mesh.CombineMesh(meshes.toArray(new Mesh[0]));

        if (layerRenderObjects[layer] != null) {
            layerRenderObjects[layer].setMesh(layerMeshes[layer]);
            layerRenderObjects[layer].storeMeshData();
        }

    }

    public void updateMesh() {
        for (int i = 0; i < layers; i++) {
            updateMesh(i);
        }
    }

    public RenderObject getRenderObject(int layer) {
        return layerRenderObjects[layer];
    }

}
