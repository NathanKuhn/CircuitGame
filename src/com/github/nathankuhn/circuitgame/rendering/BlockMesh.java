package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.engine.Block;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockMesh {

    private static final Vector3f[] NORTH_FACE = new Vector3f[] {
            new Vector3f(1,0,0),
            new Vector3f(0,0,0),
            new Vector3f(0,1,0),
            new Vector3f(1,1,0)
    };
    private static final Vector3f[] SOUTH_FACE = new Vector3f[] {
            new Vector3f(0,0,1),
            new Vector3f(1,0,1),
            new Vector3f(1,1,1),
            new Vector3f(0,1,1)
    };
    private static final Vector3f[] EAST_FACE = new Vector3f[] {
            new Vector3f(1,0,1),
            new Vector3f(1,0,0),
            new Vector3f(1,1,0),
            new Vector3f(1,1,1)
    };
    private static final Vector3f[] WEST_FACE = new Vector3f[] {
            new Vector3f(0,0,0),
            new Vector3f(0,0,1),
            new Vector3f(0,1,1),
            new Vector3f(0,1,0)
    };
    private static final Vector3f[] UP_FACE = new Vector3f[] {
            new Vector3f(0,1,1),
            new Vector3f(1,1,1),
            new Vector3f(1,1,0),
            new Vector3f(0,1,0)
    };
    private static final Vector3f[] DOWN_FACE = new Vector3f[] {
            new Vector3f(1,0,1),
            new Vector3f(0,0,1),
            new Vector3f(0,0,0),
            new Vector3f(1,0,0)
    };
    private static final Vector3f[] NORTH_NORMALS = new Vector3f[]{
            new Vector3f(0, 0, -1),
            new Vector3f(0, 0, -1),
            new Vector3f(0, 0, -1),
            new Vector3f(0, 0, -1)
    };
    private static final Vector3f[] SOUTH_NORMALS = new Vector3f[]{
            new Vector3f(0, 0, 1),
            new Vector3f(0, 0, 1),
            new Vector3f(0, 0, 1),
            new Vector3f(0, 0, 1),
    };
    private static final Vector3f[] EAST_NORMALS =  new Vector3f[]{
            new Vector3f(1, 0, 0),
            new Vector3f(1, 0, 0),
            new Vector3f(1, 0, 0),
            new Vector3f(1, 0, 0),
    };
    private static final Vector3f[] WEST_NORMALS =  new Vector3f[]{
            new Vector3f(-1, 0, 0),
            new Vector3f(-1, 0, 0),
            new Vector3f(-1, 0, 0),
            new Vector3f(-1, 0, 0),
    };
    private static final Vector3f[] UP_NORMALS =    new Vector3f[]{
            new Vector3f(0, 1, 0),
            new Vector3f(0, 1, 0),
            new Vector3f(0, 1, 0),
            new Vector3f(0, 1, 0),
    };
    private static final Vector3f[] DOWN_NORMALS =  new Vector3f[]{
            new Vector3f(0, -1, 0),
            new Vector3f(0, -1, 0),
            new Vector3f(0, -1, 0),
            new Vector3f(0, -1, 0),
    };

    private static final Vector2f[] FACE_UVS = new Vector2f[] {
            new Vector2f(0,0),
            new Vector2f(1,0),
            new Vector2f(1,1),
            new Vector2f(0,1)
    };
    private static final Vector3i[] FACE_INDICES = new Vector3i[] {
            new Vector3i(0,1,2),
            new Vector3i(0,2,3)
    };

    private static Vector3i[] AddIndex(Vector3i[] arr, int num) {
        Vector3i[] ret = new Vector3i[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ret[i] = VectorMath.Add(arr[i], new Vector3i(num, num, num));
        }
        return ret;
    }

    private CubeSideData sides;
    private Vector3i location;
    private Block block;
    private Mesh mesh;
    private TextureAtlas textureAtlas;

    public BlockMesh(CubeSideData sides, Vector3i location, Block block, TextureAtlas textureAtlas) {
        this.sides = sides;
        this.location = location;
        this.block = block;
        this.textureAtlas = textureAtlas;
        mesh = new Mesh(new Vector3f[0], new Vector3f[0], new Vector2f[0], new Vector3i[0]);
        updateMesh();
    }

    private void updateMesh() {
        List<Vector3f> positions = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>(); // TODO refactor to simple normal identifier
        List<Vector2f> uvs = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        int prevVerts = 0;

        if (sides.north()) {
            positions.addAll(factorVerts(NORTH_FACE));
            normals.addAll(Arrays.asList(NORTH_NORMALS));
            uvs.addAll(factorUVs(FACE_UVS, block.getBlockTexture().getNorthTexture()));
            faces.addAll(Arrays.asList(FACE_INDICES));
            prevVerts += 4;
        }
        if (sides.south()) {
            positions.addAll(factorVerts(SOUTH_FACE));
            normals.addAll(Arrays.asList(SOUTH_NORMALS));
            uvs.addAll(factorUVs(FACE_UVS, block.getBlockTexture().getSouthTexture()));
            faces.addAll(Arrays.asList(AddIndex(FACE_INDICES, prevVerts)));
            prevVerts += 4;
        }
        if (sides.east()) {
            positions.addAll(factorVerts(EAST_FACE));
            normals.addAll(Arrays.asList(EAST_NORMALS));
            uvs.addAll(factorUVs(FACE_UVS, block.getBlockTexture().getEastTexture()));
            faces.addAll(Arrays.asList(AddIndex(FACE_INDICES, prevVerts)));
            prevVerts += 4;
        }
        if (sides.west()) {
            positions.addAll(factorVerts(WEST_FACE));
            normals.addAll(Arrays.asList(WEST_NORMALS));
            uvs.addAll(factorUVs(FACE_UVS, block.getBlockTexture().getWestTexture()));
            faces.addAll(Arrays.asList(AddIndex(FACE_INDICES, prevVerts)));
            prevVerts += 4;
        }
        if (sides.up()) {
            positions.addAll(factorVerts(UP_FACE));
            normals.addAll(Arrays.asList(UP_NORMALS));
            uvs.addAll(factorUVs(FACE_UVS, block.getBlockTexture().getUpTexture()));
            faces.addAll(Arrays.asList(AddIndex(FACE_INDICES, prevVerts)));
            prevVerts += 4;
        }
        if (sides.down()) {
            positions.addAll(factorVerts(DOWN_FACE));
            normals.addAll(Arrays.asList(DOWN_NORMALS));
            uvs.addAll(factorUVs(FACE_UVS, block.getBlockTexture().getDownTexture()));
            faces.addAll(Arrays.asList(AddIndex(FACE_INDICES, prevVerts)));
        }

        Vector3f[] posArr = positions.toArray(new Vector3f[0]);
        Vector3f[] nrmArr = normals.toArray(new Vector3f[0]);
        Vector2f[] uvsArr = uvs.toArray(new Vector2f[0]);
        Vector3i[] idxArr = faces.toArray(new Vector3i[0]);

        mesh = new Mesh(posArr, nrmArr, uvsArr, idxArr);
    }
    public Mesh getMesh() {
        return mesh;
    }

    private List<Vector3f> factorVerts(Vector3f[] verts) {
        List<Vector3f> ret = new ArrayList<>();
        for (int vert = 0; vert < verts.length; vert++) {
            ret.add(VectorMath.Add(verts[vert], location));
        }
        return ret;
    }

    private List<Vector2f> factorUVs(Vector2f[] uvs, int textureID) {
        List<Vector2f> ret = new ArrayList<>();
        for (int uv = 0; uv < uvs.length; uv++) {
            ret.add(textureAtlas.factorUV(uvs[uv], textureID));
        }
        return ret;
    }

    public static class CubeSideData {

        private int filledSides;
        private boolean north;
        private boolean south;
        private boolean east;
        private boolean west;
        private boolean up;
        private boolean down;

        public CubeSideData(boolean north, boolean south, boolean east, boolean west, boolean up, boolean down) {

            this.north = north;
            this.south = south;
            this.east = east;
            this.west = west;
            this.up = up;
            this.down = down;

            filledSides = 0;
            if (north)
                filledSides++;
            if (south)
                filledSides++;
            if(east)
                filledSides++;
            if(west)
                filledSides++;
            if(up)
                filledSides++;
            if(down)
                filledSides++;
        }

        private boolean north() {
            return north;
        }
        private boolean south() {
            return south;
        }
        private boolean east() {
            return east;
        }
        private boolean west() {
            return west;
        }
        private boolean up() {
            return up;
        }
        private boolean down() {
            return down;
        }
        private int filledSides() {
            return filledSides;
        }

    }

}
