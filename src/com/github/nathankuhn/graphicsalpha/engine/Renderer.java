package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.display.Window;

import static org.lwjgl.opengl.GL11.glViewport;

public class Renderer {

    private RenderObject renderObject;
    private Window window;
    private EnvironmentLight light;

    public Renderer(Window window, RenderObject renderObject, EnvironmentLight environmentLight) {
        this.renderObject = renderObject;
        this.window = window;
        light = environmentLight;
    }

    public void init(){
        try {
            renderObject.init();
        } catch (Exception e) {
            System.out.println("Error in render init");
            System.out.println(e.getStackTrace());
        }
    }
    public void render() {

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderObject.render(window, light);
    }

    public void cleanup() {
        renderObject.cleanup();
    }

}
