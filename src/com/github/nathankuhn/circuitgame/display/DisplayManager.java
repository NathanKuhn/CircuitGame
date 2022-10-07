package com.github.nathankuhn.circuitgame.display;

import com.github.nathankuhn.circuitgame.utils.Vector2i;
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

public class DisplayManager {

    private static final int DEFAULT_WIDTH = 1366;
    private static final int DEFAULT_HEIGHT = 786;
    private static long windowHandle;
    private static int width;
    private static int height;

    public static void Initialize() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be resizable

        // Create the window
        windowHandle = glfwCreateWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT, "Circuit Game", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> { // TODO add lambda handler for this, until then resizing disabled
            DisplayManager.width = width;
            DisplayManager.height = height;
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // 1 is vsync on, 0 is vsync off
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(windowHandle);

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

        CenterWindow();
    }

    public static void Update() {
        glfwSwapBuffers(windowHandle); // swap the color buffers
        glfwPollEvents(); // Key callbacks are called here
    }

    public static int WindowHeight() {
        return height;
    }

    public static int WindowWidth() {
        return width;
    }

    public static float WindowAspectRatio() {
        return (float) width / (float) height;
    }

    public static long WindowHandle() {
        return windowHandle;
    }

    public static void Fullscreen(boolean fullscreen) {

        if (fullscreen) {
            // get resolution of monitor
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = videoMode.width();
            height = videoMode.height();

            glfwSetWindowMonitor(windowHandle, glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
            glfwSwapInterval(1);
        } else {
            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
            glfwSetWindowMonitor(windowHandle, NULL, 0, 0, width, height, 0);
        }

    }

    public static boolean ShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public static boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public static void Close() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static Vector2i WindowDimensions() {
        return new Vector2i(width, height);
    }

    private static void CenterWindow() {
        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            // Center the window
            glfwSetWindowPos(
                    windowHandle,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
    }
}
