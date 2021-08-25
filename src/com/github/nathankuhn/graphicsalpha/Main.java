package com.github.nathankuhn.graphicsalpha;

import com.github.nathankuhn.graphicsalpha.display.Draw;
import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.engine.Mesh;
import com.github.nathankuhn.graphicsalpha.engine.RenderObject;
import com.github.nathankuhn.graphicsalpha.engine.Renderer;
import com.github.nathankuhn.graphicsalpha.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.glEnable;

public class Main {

    private Window window;

    public Main() {
        window = new Window(500, 500);
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        Vector3f[] pos = {
                new Vector3f(0.5f,-0.5f,0.5f),
                new Vector3f(0.5f,-0.5f,-0.5f),
                new Vector3f(0.5f,0.5f,-0.5f),
                new Vector3f(0.5f,0.5f,0.5f),
                new Vector3f(-0.5f,-0.5f,0.5f),
                new Vector3f(-0.5f,-0.5f,-0.5f),
                new Vector3f(-0.5f,0.5f,-0.5f),
                new Vector3f(-0.5f,0.5f,0.5f),
        };

        Vector3i[] ind = {
                new Vector3i(4, 0, 3),
                new Vector3i(4, 3, 7),
                new Vector3i(0, 1, 2),
                new Vector3i(0, 2, 3),
                new Vector3i(1, 5, 6),
                new Vector3i(1, 6, 2),
                new Vector3i(5, 4, 7),
                new Vector3i(5, 7, 6),
                new Vector3i(7, 3, 2),
                new Vector3i(7, 2, 6),
                new Vector3i(0, 5, 1),
                new Vector3i(0, 4, 5),
        };

        Vector3f[] norm = {
                new Vector3f( 1.0f,  0.0f, 0.0f),
                new Vector3f( 0.0f,  1.0f, 0.0f),
                new Vector3f( 0.0f,  0.0f, 1.0f),
                new Vector3f( 1.0f,  1.0f, 0.0f),
                new Vector3f( 0.0f,  0.0f, 1.0f),
                new Vector3f( 1.0f,  1.0f, 0.0f),
                new Vector3f( 1.0f,  0.0f, 0.0f),
                new Vector3f( 0.0f,  1.0f, 0.0f)
        };

        Vector2f[] uvs = {
                new Vector2f( 1.0f,  1.0f),
                new Vector2f(-1.0f,  1.0f),
                new Vector2f(-1.0f,  1.0f),
                new Vector2f( 1.0f, -1.0f),
                new Vector2f( 1.0f,  1.0f),
                new Vector2f(-1.0f,  1.0f),
                new Vector2f(-1.0f,  1.0f),
                new Vector2f( 1.0f, -1.0f),
        };

        Mesh mesh = new Mesh(pos, ind, norm, uvs);
        RenderObject renderObject = new RenderObject(mesh, null);
        renderObject.transform.setPosition(new Vector3f(0.0f, 0.0f, -1.5f));

        window.init();

        Renderer renderer = new Renderer(window, renderObject);
        renderer.init();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        Vector3f r = new Vector3f(0.0f, 0.0f, 0.0f);
        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            r.x += 0.01;
            r.y += 0.003;
            renderObject.transform.setRotation(r);

            renderer.render();
            window.update();
        }

        renderer.cleanup();
        window.close();

    }

    public static void main(String[] args) {
        new Main().run();
    }

}
