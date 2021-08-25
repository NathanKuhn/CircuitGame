package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.*;

public class Mesh {

    protected Vector3f[] vertPositions;
    protected Vector3i[] faceIndices;
    protected Vector3f[] vertNormals;
    protected Vector2f[] vertUVs;

    public Mesh(Vector3f[] vertPositions, Vector3i[] faceIndices, Vector3f[] vertNormals, Vector2f[] vertUVs) {
        this.vertPositions = vertPositions;
        this.faceIndices = faceIndices;
        this.vertNormals = vertNormals;
        this.vertUVs = vertUVs;
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
}
