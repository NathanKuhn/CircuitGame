package com.github.nathankuhn.graphicsalpha;

import com.github.nathankuhn.graphicsalpha.display.Draw;
import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.engine.Mesh;
import com.github.nathankuhn.graphicsalpha.engine.Renderer;
import com.github.nathankuhn.graphicsalpha.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

public class Main {

    private Window window;

    public Main() {
        window = new Window(500, 500);
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        window.init();

        Vector3f[] pos = {
                new Vector3f(-0.5f,  0.5f, -1.05f),
                new Vector3f(-0.5f,  -0.5f, -1.05f),
                new Vector3f( 0.5f, -0.5f, -1.05f),
                new Vector3f( 0.5f,  0.5f, -1.05f)
        };

        Vector3i[] ind = {
                new Vector3i(0, 1, 3),
                new Vector3i(3, 1, 2)
        };

        Vector3f[] norm = {
                new Vector3f( 1.0f,  0.0f, 0.0f),
                new Vector3f( 0.0f,  1.0f, 0.0f),
                new Vector3f( 0.0f,  0.0f, 1.0f),
                new Vector3f( 1.0f,  1.0f, 0.0f)
        };

        Vector2f[] uvs = {
                new Vector2f( 1.0f,  1.0f),
                new Vector2f(-1.0f,  1.0f),
                new Vector2f(-1.0f,  1.0f),
                new Vector2f( 1.0f, -1.0f),
        };

        Mesh mesh = new Mesh(pos, ind, norm, uvs);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        Renderer renderer = new Renderer(window, mesh);
        renderer.init();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

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
