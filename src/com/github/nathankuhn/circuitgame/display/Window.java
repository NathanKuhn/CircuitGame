package com.github.nathankuhn.circuitgame.display;

import com.github.nathankuhn.circuitgame.utils.Matrix4;
import com.github.nathankuhn.circuitgame.utils.Vector2i;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long window;
    private int width;
    private int height;
    private int oldWidth;
    private int oldHeight;
    private boolean resized;
    private boolean fullscreen;
    private float fov;
    private float nearPlane;
    private float farPlane;
    private Matrix4 projectionMatrix;

    public Window(int width, int height) {
        this.width = width;
        this.height = height;
        oldWidth = width;
        oldHeight = height;
        resized = false;
        fullscreen = false;

        fov = (float) Math.toRadians(90);
        nearPlane = 0.05f;
        farPlane = 500.0f;
        projectionMatrix = Matrix4.Perspective(fov, (float)width / (float) height, nearPlane, farPlane);
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "Circuit Game", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            Window.this.width = width;
            Window.this.height = height;
            Window.this.setResized(true);
        });

        centerWindow();


        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // 1 is vsync on, 0 is vsync off
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //setFullscreen(true);
    }

    public void update() {

        glfwSwapBuffers(window); // swap the color buffers
        glfwPollEvents(); // Key callbacks are called here

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getAspectRatio() {
        return (float) width / (float) height;
    }

    public long getHandle() {
        return window;
    }

    public void setResized(boolean resized) {
        if (resized) {
            projectionMatrix = Matrix4.Perspective(fov, (float)width / (float)height, nearPlane, farPlane);
        }
        this.resized = resized;
    }

    public boolean isResized() {
        return resized;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;

        if (fullscreen) {
            oldWidth = width;
            oldHeight = height;
            // get resolution of monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = vidmode.width();
            height = vidmode.height();

            glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
            glfwSwapInterval(1);
            resized = true;
        } else {
            width = oldWidth;
            height = oldHeight;
            glfwSetWindowMonitor(window, NULL, 0, 0, width, height, 0);
            centerWindow();
        }

    }

    public void toggleFullscreen() {
        setFullscreen(!fullscreen);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void close() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public Vector2i getDimensions() {
        return new Vector2i(width, height);
    }

    private void centerWindow() {
        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
    }
}
