package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.utils.Matrix4;
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
    private int positionVboID;
    private int normalVboID;
    private int indexVboID;

    public RenderObject(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    public void init() throws Exception {

        shaderProgram = new ShaderProgram();
        shaderProgram.init();
        shaderProgram.createUniform("projectionMatrix");

        // Create Buffers to store data off-heap where it can be accessed by native code

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(mesh.vertPositions.length * 3);
        verticesBuffer.put(mesh.getFlatVertPositions()).flip();

        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(mesh.vertPositions.length * 3);
        normalsBuffer.put(mesh.getFlatNormals()).flip();

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(mesh.faceIndices.length * 3);
        indicesBuffer.put(mesh.getFlatFaceIndices()).flip();

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

    }

    public void render(Window window) {

        shaderProgram.bind();

        shaderProgram.setUniform("projectionMatrix", Matrix4.Perspective((float) Math.toRadians(90), 1, 0.001f, 50));

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shaderProgram.unbind();

    }

    public void cleanup() {

        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(positionVboID);
        glDeleteBuffers(indexVboID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
