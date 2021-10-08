package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.engine.World;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Renderer {

    private World world;
    private Camera camera;
    private Window window;
    private ShaderProgram shaderProgram;
    private int textureAtlasID;

    public Renderer(Window window, World world, Camera camera) {
        this.world = world;
        this.window = window;
        this.camera = camera;
        shaderProgram = new ShaderProgram();
    }

    public void init() throws Exception {

        shaderProgram.init();
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("transformMatrix");
        shaderProgram.createUniform("viewMatrix");
        shaderProgram.createUniform("texture_sampler");

        textureAtlasID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureAtlasID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, world.getTextureAtlas().getTexture().getWidth(), world.getTextureAtlas().getTexture().getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, world.getTextureAtlas().getTexture().getBuffer());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glGenerateMipmap(GL_TEXTURE_2D);

        for (int i = 0; i < world.getRenderList().size(); i++) {
            world.getRenderList().get(i).init();
        }

    }
    public void render() {

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        shaderProgram.setUniform("projectionMatrix", window.getProjectionMatrix());
        shaderProgram.setUniform("viewMatrix", camera.getViewMatrix());
        shaderProgram.setUniform("texture_sampler", 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureAtlasID);

        for (int i = 0; i < world.getRenderList().size(); i++) {

            RenderObject renderObject = world.getRenderList().get(i);
            shaderProgram.setUniform("transformMatrix", renderObject.transform.getMatrix());


            glBindVertexArray(renderObject.getVaoID());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            glDrawElements(GL_TRIANGLES, renderObject.getVertexCount(), GL_UNSIGNED_INT, 0);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glBindVertexArray(0);
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        shaderProgram.cleanup();
        for (int i = 0; i < world.getRenderList().size(); i++) {
            world.getRenderList().get(i).cleanup();
        }
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

}
