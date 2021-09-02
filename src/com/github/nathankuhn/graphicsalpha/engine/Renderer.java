package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.utils.Vector3f;

import static org.lwjgl.opengl.GL11.glViewport;

public class Renderer {

    private Camera camera;
    private RenderObject renderObject;
    private Window window;
    private EnvironmentLight light;

    public Renderer(Window window, RenderObject renderObject, Camera camera, EnvironmentLight environmentLight) {
        this.renderObject = renderObject;
        this.window = window;
        this.camera = camera;
        light = environmentLight;
    }

    public void init(){
        try {
            renderObject.init();
        } catch (Exception e) {
            System.out.println("Error in render init");
            e.printStackTrace();
        }
    }
    public void render() {

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderObject.render(window, camera, light);
    }

    public void cleanup() {
        renderObject.cleanup();
    }

}
