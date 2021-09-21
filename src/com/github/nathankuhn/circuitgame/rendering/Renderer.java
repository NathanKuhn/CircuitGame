package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.display.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private Scene scene;
    private Window window;
    private ShaderProgram shaderProgram;

    public Renderer(Window window, Scene scene) {
        this.scene = scene;
        this.window = window;
        shaderProgram = new ShaderProgram();
    }

    public void init() throws Exception {

        shaderProgram.init();
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("transformMatrix");
        shaderProgram.createUniform("viewMatrix");
        shaderProgram.createUniform("texture_sampler");

        for (int i = 0; i < scene.numRenderObjects();  i++) {
            scene.getRenderObject(i).init();
        }

    }
    public void render() {

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        shaderProgram.setUniform("projectionMatrix", window.getProjectionMatrix());
        shaderProgram.setUniform("viewMatrix", scene.getMainCamera().getViewMatrix());
        shaderProgram.setUniform("texture_sampler", 0);

        for (int i = 0; i < scene.numRenderObjects(); i++) {

            RenderObject renderObject = scene.getRenderObject(i);
            shaderProgram.setUniform("transformMatrix", renderObject.transform.getMatrix());


            glBindVertexArray(renderObject.getVaoID());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, renderObject.getTextureID());

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
        for (int i = 0; i < scene.numRenderObjects(); i++) {
            scene.getRenderObject(i).cleanup();
        }
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

}
