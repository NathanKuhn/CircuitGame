package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.display.Window;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class RenderObject {

    private Mesh mesh;
    private Material material;
    private ShaderProgram shaderProgram;

    private int vaoID;
    private int posVboID;
    private int idxVboID;

    public RenderObject(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    public void init() throws Exception {

        shaderProgram = new ShaderProgram();
        shaderProgram.init();

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(mesh.getVertexCount() * 3);
        verticesBuffer.put(mesh.getFlatVertPositions()).flip();

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(mesh.faceIndices.length * 3);
        indicesBuffer.put(mesh.getFlatFaceIndices()).flip();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        posVboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, posVboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        memFree(verticesBuffer);

        idxVboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        memFree(indicesBuffer);

    }

    public void render(Window window) {

        shaderProgram.bind();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();

    }

    public void cleanup() {

        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboID);
        glDeleteBuffers(idxVboID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
