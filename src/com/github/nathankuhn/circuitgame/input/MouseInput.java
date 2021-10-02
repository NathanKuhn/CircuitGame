package com.github.nathankuhn.circuitgame.input;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.utils.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private final Window window;
    private final Vector2f currentPos;
    private final Vector2f displaceVec;

    private boolean locked;
    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    public MouseInput(Window window) {
        currentPos = new Vector2f(0, 0);
        displaceVec = new Vector2f(0, 0);
        locked = false;
        inWindow = false;
        leftButtonPressed = false;
        rightButtonPressed = false;
        this.window = window;
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
        });
    }

    public void update() {
        displaceVec.x = 0;
        displaceVec.y = 0;
        if (locked) {
            float deltaX = currentPos.x - window.getWidth() / 2;
            float deltaY = currentPos.y - window.getHeight() / 2;
            if (deltaX != 0) {
                displaceVec.y = deltaX;
            }
            if (deltaY != 0) {
                displaceVec.x = deltaY;
            }
            glfwSetCursorPos(window.getHandle(), window.getWidth() / 2, window.getHeight() / 2);
        }

    }

    public void lockCursor() {
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
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
    public Vector2f getDisplaceVec() {
        return displaceVec;
    }
}
