package com.github.nathankuhn.circuitgame;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.rendering.*;
import com.github.nathankuhn.circuitgame.input.MouseInput;
import com.github.nathankuhn.circuitgame.utils.*;
import org.lwjgl.*;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.glEnable;

public class Main {

    public static final float MOVE_SPEED = 2.0f;
    public static final float MOUSE_SENSITIVITY = 50.0f;

    private final Window window;
    private final Timer timer;
    private boolean polygon;

    public Main() {
        window = new Window(500, 500);
        timer = new Timer();
        polygon = true;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        System.out.println("Press enter to print ups");

        //Mesh obj = MeshImporter.LoadFromOBJ("cube.obj");
        boolean[] map = new boolean[4096];
        map[0] = true;
        Chunk chunk = new Chunk(map, new Vector3i(0,0,0));

        // Basic terrain generation
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 16; z++) {
                    chunk.placeBlock(x, y, z);
                }
            }
        }

        chunk.update();
        Mesh obj = chunk.getMesh();

        Texture tex = Texture.LoadPNG("MissingTexture.png");

        RenderObject chunk01 = new RenderObject(obj, tex);
        Camera camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
        Scene scene = new Scene(camera);
        scene.addRenderObject(chunk01);

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
            if (key == GLFW_KEY_ENTER && action == GLFW_PRESS)
                printFrameRate();
            if (key == GLFW_KEY_M && action == GLFW_PRESS)
                swapRenderModes();
        });

        // Set the clear color
        glClearColor(0.1f, 0.1f, 0.15f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        Vector3f cameraMovement = new Vector3f(0.0f, 0.0f, 0.0f);

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
    public void swapRenderModes() {
        if (polygon) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glDisable(GL_CULL_FACE);
            polygon = false;
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glEnable(GL_CULL_FACE);
            polygon = true;
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
