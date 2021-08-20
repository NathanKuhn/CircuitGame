package com.github.nathankuhn.graphicsalpha.engine;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memFree;

public class EngineObject {

    private Mesh mesh;
    private Material material;

    private int vaoID;
    private int vboID;

    public EngineObject(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    public void init() {

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(mesh.getVertNum() * 3);
        verticesBuffer.put(mesh.getFlatVertPositions()).flip();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        memFree(verticesBuffer);

    }
}
