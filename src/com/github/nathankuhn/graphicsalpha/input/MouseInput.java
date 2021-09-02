package com.github.nathankuhn.graphicsalpha.input;

import com.github.nathankuhn.graphicsalpha.display.Window;
import com.github.nathankuhn.graphicsalpha.utils.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private final Vector2f previousPos;
    private final Vector2f currentPos;

    private final Vector2f displaceVec;

    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    public MouseInput() {
        previousPos = new Vector2f(-1.0f, -1.0f);
        currentPos = new Vector2f(0, 0);
        displaceVec = new Vector2f(0, 0);
        inWindow = false;
        leftButtonPressed = false;
        rightButtonPressed = false;
    }

    public void init(Window window) {
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

    public void input(Window window) {
        displaceVec.x = 0;
        displaceVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            float deltaX = currentPos.x - previousPos.x;
            float deltaY = currentPos.y - previousPos.y;
            if (deltaX != 0) {
                displaceVec.y = deltaX;
            }
            if (deltaY != 0) {
                displaceVec.x = deltaY;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
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
