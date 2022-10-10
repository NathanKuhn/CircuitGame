package com.github.nathankuhn.circuitgame.display;

import com.github.nathankuhn.circuitgame.utils.Vector2f;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {
    private static final Vector2f mousePosition = new Vector2f();
    private static final Vector2f mouseDisplacement = new Vector2f();
    private static boolean locked = false;
    private static boolean inWindow = false;
    private static boolean leftButtonPressed = false;
    private static boolean rightButtonPressed = false;
    private static int scrollOffset = 0;

    private static GLFWMouseButtonCallbackI mouseButtonCallback = (windowHandle, button, action, mode) -> {};
    private static GLFWScrollCallbackI scrollCallback = (windowHandle, xOffset, yOffset) -> {};
    public static void Initialize() {
        glfwSetCursorPosCallback(DisplayManager.WindowHandle(), (windowHandle, xPos, yPos) -> {
            mousePosition.x = (float) xPos;
            mousePosition.y = (float) yPos;
        });
        glfwSetCursorEnterCallback(DisplayManager.WindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(DisplayManager.WindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            mouseButtonCallback.invoke(windowHandle, button, action, mode);
        });
        glfwSetScrollCallback(DisplayManager.WindowHandle(), (windowHandle, xOffset, yOffset) -> {
            scrollOffset += yOffset;
            scrollCallback.invoke(windowHandle, xOffset, yOffset);
        });
    }

    public static void Update() {
        mouseDisplacement.x = 0;
        mouseDisplacement.y = 0;
        if (locked) {
            float deltaX = mousePosition.x - DisplayManager.WindowWidth() / 2.0f;
            float deltaY = mousePosition.y - DisplayManager.WindowHeight() / 2.0f;
            if (deltaX != 0) {
                mouseDisplacement.y = deltaX;
            }
            if (deltaY != 0) {
                mouseDisplacement.x = deltaY;
            }
            glfwSetCursorPos(DisplayManager.WindowHandle(), DisplayManager.WindowWidth() / 2.0, DisplayManager.WindowHeight() / 2.0);
        }

    }

    public static void SetMouseButtonCallback(GLFWMouseButtonCallbackI mouseButtonCallback) {
        InputManager.mouseButtonCallback = mouseButtonCallback;
    }

    public static void SetScrollCallback(GLFWScrollCallbackI scrollCallback) {
        InputManager.scrollCallback = scrollCallback;
    }

    public static void LockCursor() {
        glfwSetInputMode(DisplayManager.WindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        SetCursorPos(DisplayManager.WindowWidth() / 2.0, DisplayManager.WindowHeight() / 2.0);
        //glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        locked = true;
    }
    public static void UnLockCursor() {
        glfwSetInputMode(DisplayManager.WindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        locked = false;
    }

    public static void ToggleCursorLock() {
        if (locked) {
            UnLockCursor();
        } else {
            LockCursor();
        }
    }

    public static void SetCursorPos(double x, double y) {
        glfwSetCursorPos(DisplayManager.WindowHandle(), x, y);
        mousePosition.x = (float)x;
        mousePosition.y = (float)y;
    }

    public static boolean IsLocked() {
        return locked;
    }

    public static boolean IsInWindow() {
        return inWindow;
    }

    public static boolean IsLeftButtonPressed() {
        return leftButtonPressed;
    }

    public static boolean IsRightButtonPressed() {
        return rightButtonPressed;
    }

    public static float GetScrollOffset() {
        return scrollOffset;
    }

    public static Vector2f GetMouseDisplacement() {
        return mouseDisplacement;
    }

    public static boolean IsKeyPressed(int keycode) {
        return DisplayManager.isKeyPressed(keycode);
    }
}
