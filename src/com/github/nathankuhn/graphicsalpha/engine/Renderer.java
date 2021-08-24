package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.display.Window;

import static org.lwjgl.opengl.GL11.glViewport;

public class Renderer {

    private RenderObject renderObject;
    private Window window;

    public Renderer(Window window, Mesh mesh) {
        renderObject = new RenderObject(mesh, null);
        this.window = window;
    }

    public void init(){
        try {
            renderObject.init();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
    public void render() {

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderObject.render(window);
    }

    public void cleanup() {
        renderObject.cleanup();
    }

}
