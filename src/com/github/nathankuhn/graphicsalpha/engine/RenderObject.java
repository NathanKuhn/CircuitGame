package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.utils.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class RenderObject {

    public Transform transform;

    private Mesh mesh;
    private Material material;
    private ShaderProgram shaderProgram;

    private int vaoID;
    private int positionVboID;
    private int normalVboID;
    private int indexVboID;
    private int uvsVboID;
    private int textureID;

    public RenderObject(Mesh mesh, Material material) {
        this.transform = new Transform(new Vector3f(0.0f, 0.0f, 0.0f));
        this.mesh = mesh;
        this.material = material;
        shaderProgram = new ShaderProgram();
    }

    public void init() throws Exception {

        shaderProgram.init();
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("transformMatrix");
        shaderProgram.createUniform("rotationMatrix");
        shaderProgram.createUniform("viewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("lightDirection");
        shaderProgram.createUniform("lightColor");
        shaderProgram.createUniform("lightIntensity");
        shaderProgram.createUniform("ambientColor");
        shaderProgram.createUniform("ambientIntensity");

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

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, material.getTextureWidth(), material.getTextureHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, material.getTextureBuffer());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void render(Window window, Camera camera, EnvironmentLight light) {

        shaderProgram.bind();

        shaderProgram.setUniform("projectionMatrix", window.getProjectionMatrix());
        shaderProgram.setUniform("transformMatrix", transform.getMatrix());
        shaderProgram.setUniform("rotationMatrix", transform.getRotationMatrix());
        shaderProgram.setUniform("viewMatrix", camera.getViewMatrix());
        shaderProgram.setUniform("texture_sampler", 0);
        shaderProgram.setUniform("lightDirection", light.getSunDirection());
        shaderProgram.setUniform("lightColor", new Vector3f(light.getSunColor()));
        shaderProgram.setUniform("lightIntensity", light.getSunIntensity());
        shaderProgram.setUniform("ambientColor", new Vector3f(light.getAmbientColor()));
        shaderProgram.setUniform("ambientIntensity", light.getAmbientIntensity());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    public void cleanup() {

        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(positionVboID);
        glDeleteBuffers(indexVboID);
        glDeleteBuffers(uvsVboID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
