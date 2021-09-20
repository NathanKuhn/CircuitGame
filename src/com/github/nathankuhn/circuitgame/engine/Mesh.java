package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.utils.*;

public class Mesh {

    protected Vector3f[] vertPositions;
    protected Vector3f[] vertNormals;
    protected Vector2f[] vertUVs;
    protected Vector3i[] faceIndices;

    public Mesh(Vector3f[] vertPositions, Vector3f[] vertNormals, Vector2f[] vertUVs, Vector3i[] faceIndices) {
        this.vertPositions = vertPositions;
        this.faceIndices = faceIndices;
        this.vertNormals = vertNormals;
        this.vertUVs = vertUVs;
    }

    public Mesh(float[] positions, float[] normals, float[] uvs, int[] indices) {
        int verts = positions.length / 3;
        int faces = indices.length / 3;
        vertPositions = new Vector3f[verts];
        faceIndices = new Vector3i[faces];
        vertNormals = new Vector3f[verts];
        vertUVs = new Vector2f[verts];

        for (int vert = 0; vert < verts; vert++) {
            vertPositions[vert] = new Vector3f(
                positions[vert * 3],
                positions[vert * 3 + 1],
                positions[vert * 3 + 2]
            );
            vertNormals[vert] = new Vector3f(
                    normals[vert * 3],
                    normals[vert * 3 + 1],
                    normals[vert * 3 + 2]
            );
            vertUVs[vert] = new Vector2f(
                    uvs[vert * 2],
                    uvs[vert * 2 + 1]
            );
        }

        for (int face = 0; face < faces; face++) {
            faceIndices[face] = new Vector3i(
                    indices[face * 3],
                    indices[face * 3 + 1],
                    indices[face * 3 + 2]
            );
        }
    }

    public Vector3f getVertPosition(int vert) {
        return vertPositions[vert];
    }

    public Vector3f getVertNormal(int vert) {
        return vertNormals[vert];
    }

    public Vector2f getVertUVs(int vert) {
        return vertUVs[vert];
    }

    public int getVertexCount() {
        return faceIndices.length * 3;
    }

    public float[] getFlatVertPositions() {

        float[] ret = new float[vertPositions.length * 3];

        for (int vert = 0; vert < vertPositions.length; vert++) {
            ret[vert * 3] = vertPositions[vert].x;
            ret[vert * 3 + 1] = vertPositions[vert].y;
            ret[vert * 3 + 2] = vertPositions[vert].z;
        }

        return ret;
    }

    public float[] getFlatNormals() {

        float[] ret = new float[vertNormals.length * 3];

        for (int vert = 0; vert < vertNormals.length; vert++) {
            ret[vert * 3] = vertNormals[vert].x;
            ret[vert * 3 + 1] = vertNormals[vert].y;
            ret[vert * 3 + 2] = vertNormals[vert].z;
        }

        return ret;
    }

    public int[] getFlatFaceIndices() {

        int[] ret = new int[faceIndices.length * 3];

        for (int vert = 0; vert < faceIndices.length; vert++) {
            ret[vert * 3] = faceIndices[vert].x;
            ret[vert * 3 + 1] = faceIndices[vert].y;
            ret[vert * 3 + 2] = faceIndices[vert].z;
        }

        return ret;
    }

    public float[] getFlatUVs() {

        float[] ret = new float[vertUVs.length * 2];

        for (int vert = 0; vert < vertUVs.length; vert++) {
            ret[vert * 2] = vertUVs[vert].x;
            ret[vert * 2 + 1] = 1.0f - vertUVs[vert].y; // for some reason in openGL, the v coordinate is flipped
        }

        return ret;
    }
}
