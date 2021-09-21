package com.github.nathankuhn.circuitgame;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.rendering.*;
import com.github.nathankuhn.circuitgame.input.MouseInput;
import com.github.nathankuhn.circuitgame.utils.*;
import org.lwjgl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    public static final float MOVE_SPEED = 2.0f;
    public static final float MOUSE_SENSITIVITY = 50.0f;

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

        RenderObject cube01 = new RenderObject(obj, tex);
        cube01.transform.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
        cube01.transform.setScale(new Vector3f(0.5f, 0.5f, 0.5f));

        RenderObject cube02 = new RenderObject(obj, tex);
        cube02.transform.setPosition(new Vector3f(1.0f, 0.0f, 0.0f));
        cube02.transform.setScale(new Vector3f(0.5f, 0.5f, 0.5f));

        Scene scene = new Scene();
        scene.addRenderObject(cube01);
        scene.addRenderObject(cube02);

        Renderer renderer = new Renderer(window, scene);

        MouseInput input = new MouseInput(window);

        window.init();
        try {
            renderer.init();
        } catch (Exception e) {
            System.out.println("Error in render init");
            e.printStackTrace();
        }
        input.init();

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

        Vector3f cameraMovement = new Vector3f(0.0f, 0.0f, 0.0f);

        Camera camera = scene.getMainCamera();

        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            cameraMovement.set(0,0,0);

            if (window.isKeyPressed(GLFW_KEY_W)) {
                cameraMovement.z = -1.0f;
            } else if (window.isKeyPressed(GLFW_KEY_S)) {
                cameraMovement.z = 1.0f;
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                cameraMovement.x = -1.0f;
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                cameraMovement.x = 1.0f;
            }
            if (window.isKeyPressed(GLFW_KEY_Z)) {
                cameraMovement.y = -1.0f;
            } else if (window.isKeyPressed(GLFW_KEY_X)) {
                cameraMovement.y = 1.0f;
            }
            cameraMovement.scaleSet(MOVE_SPEED * timer.deltaTime());
            camera.move(cameraMovement);

            Vector2f rotVec = input.getDisplaceVec();
            rotVec.scaleSet(timer.deltaTime());
            camera.rotate(-rotVec.x * MOUSE_SENSITIVITY, -rotVec.y * MOUSE_SENSITIVITY, 0.0f);

            Vector3f rot = camera.getRotation();

            if (rot.x > 90) {
                rot.x = 90;
            } else if (rot.x < -90) {
                rot.x = -90;
            }

            renderer.render();
            window.update();
            input.update();
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
