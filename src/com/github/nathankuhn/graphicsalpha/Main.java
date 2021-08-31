package com.github.nathankuhn.graphicsalpha;

import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.engine.*;
import com.github.nathankuhn.graphicsalpha.utils.*;
import org.lwjgl.*;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    private final Window window;
    private final Timer timer;

    public Main() {
        window = new Window(500, 500);
        timer = new Timer();
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        System.out.println("Press enter to print ups");

        Mesh obj = MeshImporter.LoadFromOBJ("cube.obj");
        Texture tex = Texture.LoadPNG("cube_1mx1m.png");
        Material mat = new Material(tex);

        RenderObject renderObject = new RenderObject(obj, mat);
        renderObject.transform.setPosition(new Vector3f(0.0f, 0.0f, -3.0f));

        Vector3f sunDirection = VectorMath.Normalize(new Vector3f(0.5f, -1.0f, -0.5f));
        Color sunColor = new Color(1.0f, 1.0f, 1.0f);
        float sunIntensity = 1.0f;

        Color ambientColor = new Color(0.8f, 0.8f, 1.0f);
        float ambientIntensity = 0.3f;

        EnvironmentLight sun = new EnvironmentLight(sunDirection, sunColor, sunIntensity, ambientColor, ambientIntensity);

        Renderer renderer = new Renderer(window, renderObject, sun);

        window.init();
        renderer.init();

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window.getHandle(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            if (key == GLFW_KEY_ENTER && action == GLFW_PRESS) {
                printFrameRate();
            }
        });

        // Set the clear color
        glClearColor(0.1f, 0.1f, 0.15f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            Vector3f rotationVector = new Vector3f(0.0f, 0.4f, 0.0f);
            rotationVector.scaleSet(timer.deltaTime());
            renderObject.transform.rotate(rotationVector);

            renderer.render();
            window.update();
            timer.update();
        }

        renderer.cleanup();
        window.close();

    }

    public void printFrameRate() {
        System.out.println(1.0f / timer.deltaTime());
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
