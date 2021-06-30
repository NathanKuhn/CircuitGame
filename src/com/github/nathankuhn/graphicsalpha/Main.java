package com.github.nathankuhn.graphicsalpha;

import com.github.nathankuhn.graphicsalpha.display.Draw;
import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.utils.Color;
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

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer



            window.update();
        }

        window.close();

    }

    public static void main(String[] args) {
        new Main().run();
    }

}
