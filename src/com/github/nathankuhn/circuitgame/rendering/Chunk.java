package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private static int GetIndex(int x, int y, int z) {
        return x + y * 16 + z * 256;
    }
    private static Vector3i GetLocation(int index) {
        return new Vector3i(index % 16, (index / 16) % 16 , (index / 256));
    }
    private static boolean InBounds(int x, int y, int z) {
        return (x >= 0 && x < 16 && y >= 0 && y < 16 && z >= 0 && z < 16);
    }

    private boolean[] blocks;
    private Mesh mesh;

    public Chunk(boolean[] blocks) {
        this.blocks = blocks;
    }

    public void update() {
        List<Mesh> meshes = new ArrayList<>();

        for (int b = 0; b < 4096; b++) {
            if (blocks[b]) {
                System.out.println(GetLocation(b).x);
                Cube cube = new Cube(getData(GetLocation(b)), GetLocation(b));
                meshes.add(cube.getMesh());
            }
        }

        mesh = Mesh.CombineMesh(meshes.toArray(new Mesh[0]));
    }
    private Cube.CubeSideData getData(Vector3i p) {

        boolean north = hasBlock(p.x, p.y, p.z - 1);
        boolean south = hasBlock(p.x, p.y, p.z + 1);
        boolean east = hasBlock(p.x + 1, p.y, p.z);
        boolean west = hasBlock(p.x - 1, p.y, p.z);
        boolean up = hasBlock(p.x, p.y + 1, p.z);
        boolean down = hasBlock(p.x, p.y - 1, p.z);

        return new Cube.CubeSideData(!north, !south, !east, !west, !up, !down);
    }
    public Mesh getMesh() {
        return mesh;
    }
    public boolean hasBlock(int x, int y, int z) {
        if (InBounds(x, y, z)) {
            return blocks[GetIndex(x, y, z)];
        } else {
            return false;
        }
    }
    public void placeBlock(int x, int y, int z) {
        blocks[GetIndex(x, y, z)] = true;
    }
    public void breakBlock(int x, int y, int z) {
        blocks[GetIndex(x, y, z)] = false;
    }

}
