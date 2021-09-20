package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.utils.Matrix4;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector4f;

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
    private PointLight pointLight;
    private ShaderProgram shaderProgram;

    public Renderer(Window window, Scene scene, PointLight pointLight) {
        this.scene = scene;
        this.window = window;
        this.pointLight = pointLight;
        shaderProgram = new ShaderProgram();
    }

    public void init() throws Exception {

        shaderProgram.init();
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("transformMatrix");
        shaderProgram.createUniform("viewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createUniform("specularPower");
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createPointLightUniform("pointLight");
        //shaderProgram.createUniform("camera_pos");

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
        shaderProgram.setUniform("ambientLight", new Vector3f(0.2f, 0.2f, 0.2f));
        //shaderProgram.setUniform("camera_pos", scene.getMainCamera().getPosition());

        PointLight worldPosLight = new PointLight(pointLight);
        worldPosLight.setPosition(Matrix4.Multiply(scene.getMainCamera().getViewMatrix(), new Vector4f(worldPosLight.getPosition(), 1.0f)).toVector3f());
        shaderProgram.setUniform("pointLight", worldPosLight);

        shaderProgram.setUniform("specularPower", 10.0f);

        for (int i = 0; i < scene.numRenderObjects(); i++) {

            RenderObject renderObject = scene.getRenderObject(i);
            shaderProgram.setUniform("transformMatrix", renderObject.transform.getMatrix());
            shaderProgram.setUniform("material", renderObject.getMaterial());


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
