package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.display.DisplayManager;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class UserInput {
    private final Vector2f currentPos;
    private final Vector2f displaceVec;

    private boolean locked;
    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;
    private int scrollOffset;

    private GLFWMouseButtonCallbackI mouseButtonCallback;
    private GLFWScrollCallbackI scrollCallback;

    public UserInput() {

        currentPos = new Vector2f(0, 0);
        displaceVec = new Vector2f(0, 0);

        locked = false;
        inWindow = false;
        leftButtonPressed = false;
        rightButtonPressed = false;
        scrollOffset = 0;

        mouseButtonCallback = (windowHandle, button, action, mode) -> {};
        scrollCallback = (windowHandle, xOffset, yOffset) -> {};

    }

    public void init() {
        glfwSetCursorPosCallback(DisplayManager.WindowHandle(), (windowHandle, xPos, yPos) -> {
            currentPos.x = (float) xPos;
            currentPos.y = (float) yPos;
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

    public void update() {
        displaceVec.x = 0;
        displaceVec.y = 0;
        if (locked) {
            float deltaX = currentPos.x - DisplayManager.WindowWidth() / 2.0f;
            float deltaY = currentPos.y - DisplayManager.WindowHeight() / 2.0f;
            if (deltaX != 0) {
                displaceVec.y = deltaX;
            }
            if (deltaY != 0) {
                displaceVec.x = deltaY;
            }
            glfwSetCursorPos(DisplayManager.WindowHandle(), DisplayManager.WindowWidth() / 2.0, DisplayManager.WindowHeight() / 2.0);
        }

    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI mouseButtonCallback) {
        this.mouseButtonCallback = mouseButtonCallback;
    }

    public void setScrollCallback(GLFWScrollCallbackI scrollCallback) {
        this.scrollCallback = scrollCallback;
    }

    public void lockCursor() {
        glfwSetInputMode(DisplayManager.WindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        setCursorPos(DisplayManager.WindowWidth() / 2.0, DisplayManager.WindowHeight() / 2.0);
        //glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        locked = true;
    }
    public void unLockCursor() {
        glfwSetInputMode(DisplayManager.WindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        locked = false;
    }

    public void toggleCursorLock() {
        if (locked) {
            unLockCursor();
        } else {
            lockCursor();
        }
    }

    public void setCursorPos(double x, double y) {
        glfwSetCursorPos(DisplayManager.WindowHandle(), x, y);
        currentPos.x = (float)x;
        currentPos.y = (float)y;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isInWindow() {
        return inWindow;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public float getScrollOffset() {
        return scrollOffset;
    }

    public Vector2f getDisplaceVec() {
        return displaceVec;
    }

    public boolean isKeyPressed(int keycode) {
        return DisplayManager.isKeyPressed(keycode);
    }
}
