package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.display.DisplayManager;
import com.github.nathankuhn.circuitgame.engine.World;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private World world;
    private Camera camera;
    private ShaderProgram shaderProgram;
    private int textureAtlasID;

    public Renderer(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
        shaderProgram = new ShaderProgram();
    }

    public void init() throws Exception {

        shaderProgram.init();
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

        for (RenderObject renderObject : world.getRenderList()) {
            renderObject.init();
        }

    }
    public void render() {

        shaderProgram.bind();

        shaderProgram.setUniform("viewMatrix", camera.getViewMatrix());
        shaderProgram.setUniform("texture_sampler", 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureAtlasID);

        for (RenderObject renderObject : world.getRenderList()) {

            if (!renderObject.shouldRender()) {
                continue;
            }

            if (renderObject.hasSeparateTexture()) {
                glBindTexture(GL_TEXTURE_2D, renderObject.getTextureID());
            }

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

            if (renderObject.hasSeparateTexture()) {
                glBindTexture(GL_TEXTURE_2D, textureAtlasID);
            }
        }

        shaderProgram.unbind();
    }

    public void render(RenderTexture renderTexture) {

        glBindFramebuffer(GL_FRAMEBUFFER, renderTexture.getFboID());
        glViewport(0, 0, renderTexture.getWidth(), renderTexture.getHeight());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        render();

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, DisplayManager.WindowWidth(), DisplayManager.WindowHeight());
    }

    public void cleanup() {
        shaderProgram.cleanup();
        for (RenderObject renderObject : world.getRenderList()) {
            renderObject.cleanup();
        }
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDeleteTextures(textureAtlasID);
    }

}
