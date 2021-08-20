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

    public int getVertNum() {
        return vertPositions.length;
    }

    protected float[] getFlatVertPositions() {

        float[] ret = new float[vertPositions.length * 3];

        for (int vert = 0; vert < vertPositions.length; vert++) {
            ret[0] = vertPositions[vert].x;
            ret[1] = vertPositions[vert].y;
            ret[2] = vertPositions[vert].z;
        }

        return ret;

    }
}
