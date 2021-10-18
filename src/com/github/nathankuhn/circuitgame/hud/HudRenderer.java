package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.rendering.ShaderProgram;
import com.github.nathankuhn.circuitgame.utils.Matrix4;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class HudRenderer {

    private static final String HUD_FRAGMENT_SHADER_PATH = "shaders/hudFragment.shader";
    private static final String HUD_VERTEX_SHADER_PATH = "shaders/hudVertex.shader";

    private final Window window;
    private Root root;
    private final ShaderProgram shaderProgram;

    public HudRenderer(Window window, Root root) {
        this.window = window;
        this.root = root;
        shaderProgram = new ShaderProgram();
    }

    public void init() throws Exception{
        shaderProgram.init(HUD_VERTEX_SHADER_PATH, HUD_FRAGMENT_SHADER_PATH);
        shaderProgram.createUniform("projModelMatrix");
        shaderProgram.createUniform("transformMatrix");
        shaderProgram.createUniform("color");

        for (RenderObject renderObject : root.getRenderObjects()) {
            renderObject.init();
        }
    }

    public void render() {
        shaderProgram.bind();
        shaderProgram.setUniform("projModelMatrix", Matrix4.Orthographic(window.getAspectRatio(), -100.0f, 100.0f));
        shaderProgram.setUniform("color", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));

        glActiveTexture(GL_TEXTURE0);

        for (HudElement hudElement : root.getAllChildren()) {
            RenderObject renderObject = hudElement.getRenderObject();

            if (renderObject == null) {
                continue;
            }

            if (renderObject.shouldRender() && hudElement.isEnabled()) {

                shaderProgram.setUniform("transformMatrix", renderObject.transform.getMatrix());

                glBindTexture(GL_TEXTURE_2D, renderObject.getTextureID());

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
        }

        shaderProgram.unbind();
    }

    public void update() throws Exception{
        root.updateSize(window);
        for (HudElement hudElement : root.getAllChildren()) {
            hudElement.update(window.getDimensions());
            if (hudElement.getRenderObject() != null) {
                hudElement.getRenderObject().init();
            }
        }
    }

    public void cleanup() {
        shaderProgram.cleanup();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        for (RenderObject renderObject : root.getRenderObjects()) {
            renderObject.cleanup();
        }
    }
}
