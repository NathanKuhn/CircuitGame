package com.github.nathankuhn.graphicsalpha;

import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.engine.*;
import com.github.nathankuhn.graphicsalpha.input.MouseInput;
import com.github.nathankuhn.graphicsalpha.utils.*;
import org.lwjgl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    public static final float MOVE_SPEED = 2.0f;
    public static final float MOUSE_SENSITIVITY = 10.0f;

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

        Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

        Vector3f sunDirection = VectorMath.Normalize(new Vector3f(0.5f, -1.0f, -0.5f));
        Color sunColor = new Color(1.0f, 1.0f, 1.0f);
        float sunIntensity = 1.0f;

        Color ambientColor = new Color(0.8f, 0.8f, 1.0f);
        float ambientIntensity = 0.3f;

        EnvironmentLight sun = new EnvironmentLight(sunDirection, sunColor, sunIntensity, ambientColor, ambientIntensity);

        Renderer renderer = new Renderer(window, renderObject, camera, sun);

        MouseInput input = new MouseInput();

        window.init();
        renderer.init();
        input.init(window);

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

        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

//            Vector3f rotationVector = new Vector3f(0.0f, 0.0f, 0.0f);
//            rotationVector.scaleSet(timer.deltaTime());
//            renderObject.transform.rotate(rotationVector);

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

            if (input.isRightButtonPressed()) {
                Vector2f rotVec = input.getDisplaceVec();
                rotVec.scaleSet(timer.deltaTime());
                camera.rotate(-rotVec.x * MOUSE_SENSITIVITY, -rotVec.y * MOUSE_SENSITIVITY, 0.0f);
            }

            renderer.render();
            window.update();
            input.input(window);
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
