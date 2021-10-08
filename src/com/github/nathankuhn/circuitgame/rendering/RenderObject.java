package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class RenderObject {

    public Transform transform;

    private Mesh mesh;

    private int vaoID;
    private int positionVboID;
    private int normalVboID;
    private int indexVboID;
    private int uvsVboID;

    public RenderObject(Mesh mesh) {
        this.transform = new Transform(new Vector3f(0.0f, 0.0f, 0.0f));
        this.mesh = mesh;
    }

    protected int getVaoID() {
        return vaoID;
    }
    protected int getVertexCount() {
        return mesh.getVertexCount();
    }
    protected Mesh getMesh() {
        return mesh;
    }
    protected void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    protected void init() throws Exception {

        // Create Buffers to store data off-heap where it can be accessed by native code

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(mesh.vertPositions.length * 3);
        verticesBuffer.put(mesh.getFlatVertPositions()).flip();

        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(mesh.vertPositions.length * 3);
        normalsBuffer.put(mesh.getFlatNormals()).flip();

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(mesh.faceIndices.length * 3);
        indicesBuffer.put(mesh.getFlatFaceIndices()).flip();

        FloatBuffer uvsBuffer = MemoryUtil.memAllocFloat(mesh.vertUVs.length * 2);
        uvsBuffer.put(mesh.getFlatUVs()).flip();

        // Generate vertex array object and the several vertex buffer objects associated with it

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        positionVboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, positionVboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        memFree(verticesBuffer);

        normalVboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, normalVboID);
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        memFree(normalsBuffer);

        indexVboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        memFree(indicesBuffer);

        uvsVboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvsVboID);
        glBufferData(GL_ARRAY_BUFFER,uvsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        memFree(uvsBuffer);

    }

    protected void storeMeshData() {

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(mesh.vertPositions.length * 3);
        verticesBuffer.put(mesh.getFlatVertPositions()).flip();

        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(mesh.vertPositions.length * 3);
        normalsBuffer.put(mesh.getFlatNormals()).flip();

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(mesh.faceIndices.length * 3);
        indicesBuffer.put(mesh.getFlatFaceIndices()).flip();

        FloatBuffer uvsBuffer = MemoryUtil.memAllocFloat(mesh.vertUVs.length * 2);
        uvsBuffer.put(mesh.getFlatUVs()).flip();

        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, positionVboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        memFree(verticesBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, normalVboID);
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
        memFree(normalsBuffer);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        memFree(indicesBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, uvsVboID);
        glBufferData(GL_ARRAY_BUFFER,uvsBuffer, GL_STATIC_DRAW);
        memFree(uvsBuffer);

    }

    protected void cleanup() {

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(positionVboID);
        glDeleteBuffers(indexVboID);
        glDeleteBuffers(uvsVboID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
