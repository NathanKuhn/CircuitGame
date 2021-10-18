package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class UserInput {

    private final Window window;
    private final Vector2f currentPos;
    private final Vector2f displaceVec;

    private boolean locked;
    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;
    private int scrollOffset;

    private GLFWMouseButtonCallbackI mouseButtonCallback;
    private GLFWScrollCallbackI scrollCallback;

    public UserInput(Window window) {
        this.window = window;

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
        glfwSetCursorPosCallback(window.getHandle(), (windowHandle, xPos, yPos) -> {
            currentPos.x = (float) xPos;
            currentPos.y = (float) yPos;
        });
        glfwSetCursorEnterCallback(window.getHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            mouseButtonCallback.invoke(windowHandle, button, action, mode);
        });
        glfwSetScrollCallback(window.getHandle(), (windowHandle, xOffset, yOffset) -> {
            scrollOffset += yOffset;
            scrollCallback.invoke(windowHandle, xOffset, yOffset);
        });
    }

    public void update() {
        displaceVec.x = 0;
        displaceVec.y = 0;
        if (locked) {
            float deltaX = currentPos.x - window.getWidth() / 2.0f;
            float deltaY = currentPos.y - window.getHeight() / 2.0f;
            if (deltaX != 0) {
                displaceVec.y = deltaX;
            }
            if (deltaY != 0) {
                displaceVec.x = deltaY;
            }
            glfwSetCursorPos(window.getHandle(), window.getWidth() / 2.0, window.getHeight() / 2.0);
        }

    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI mouseButtonCallback) {
        this.mouseButtonCallback = mouseButtonCallback;
    }

    public void setScrollCallback(GLFWScrollCallbackI scrollCallback) {
        this.scrollCallback = scrollCallback;
    }

    public void lockCursor() {
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwSetCursorPos(window.getHandle(), window.getWidth() / 2.0, window.getHeight() / 2.0);
        //glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        locked = true;
    }
    public void unLockCursor() {
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        locked = false;
    }

    public void toggleCursorLock() {
        if (locked) {
            unLockCursor();
        } else {
            lockCursor();
        }
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
        return window.isKeyPressed(keycode);
    }
}
