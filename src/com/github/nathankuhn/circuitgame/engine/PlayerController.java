package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.hud.Root;
import com.github.nathankuhn.circuitgame.rendering.RayHit;
import com.github.nathankuhn.circuitgame.utils.Misc;
import com.github.nathankuhn.circuitgame.utils.Vector2f;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController {

    public static final float MOVE_SPEED = 4.0f;
    public static final float MOUSE_SENSITIVITY = 10.0f;

    private static float BREAK_COOL_DOWN = 0.5f;
    private static float PLACE_COOL_DOWN = 0.5f;

    private Player player;
    private World world;
    private UserInput userInput;

    private Root playerHud;

    private int selectedBlockIndex;
    private int[] blockList;

    private float breakCoolDown;
    private float placeCoolDown;

    public PlayerController(Player player, World world, UserInput mouseInput) {
        this.player = player;
        this.world = world;
        this.userInput = mouseInput;

        breakCoolDown = 0.0f;
        placeCoolDown = 0.0f;

        blockList = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
    }

    public void focus() {

        userInput.lockCursor();

        userInput.setMouseButtonCallback((windowHandle, button, action, mode) -> {
            if (action == GLFW_PRESS) {
                if (button == GLFW_MOUSE_BUTTON_1) {
                    onLeftClick();
                }
                if (button == GLFW_MOUSE_BUTTON_2) {
                    onRightClick();
                }
            }
        });

        userInput.setScrollCallback((windowHandle, xOffset, yOffset) -> {
            onScrollIncrement((float)yOffset);
        });

    }

    public void update(float deltaTime) {

        Vector3f playerMovement = new Vector3f();

        if (userInput.isKeyPressed(GLFW_KEY_W)) {
            playerMovement.z = -1.0f;
        } else if (userInput.isKeyPressed(GLFW_KEY_S)) {
            playerMovement.z = 1.0f;
        }
        if (userInput.isKeyPressed(GLFW_KEY_A)) {
            playerMovement.x = -1.0f;
        } else if (userInput.isKeyPressed(GLFW_KEY_D)) {
            playerMovement.x = 1.0f;
        }
        if (userInput.isKeyPressed(GLFW_KEY_SPACE)) {
            player.jump(6.0f);
        }
        if (userInput.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            playerMovement.scaleSet(2.0f);
        }
        if (userInput.isKeyPressed(GLFW_KEY_R)) {
            player.getPosition().set(world.getSpawn());
        }
        playerMovement.scaleSet(MOVE_SPEED);
        Vector3f inputMovement = player.getCamera().moveVector(playerMovement);

        player.move(inputMovement);

        Vector2f rotVec = userInput.getDisplaceVec();
        player.getCamera().rotate(-rotVec.x * MOUSE_SENSITIVITY * deltaTime, -rotVec.y * MOUSE_SENSITIVITY * deltaTime, 0.0f);

    }

    public int getSelectedBlock() {
        return blockList[selectedBlockIndex];
    }

    public int[] getBlockList() {
        return blockList;
    }

    private void onLeftClick() {
        RayHit hit = player.getCamera().castRayFromCenter(world, 5);
        if (hit != null) {
            world.placeBlock(
                    VectorMath.Add(hit.getHitPosition(), VectorMath.Scale(hit.getHitNormal(), -0.01f)).toVector3i(),
                    0
            );
        }
    }

    private void onRightClick() {
        RayHit hit = player.getCamera().castRayFromCenter(world, 5);
        if (hit != null) {
            world.placeBlock(
                    VectorMath.Add(hit.getHitPosition(), VectorMath.Scale(hit.getHitNormal(), 0.01f)).toVector3i(),
                    blockList[selectedBlockIndex]
            );
        }
    }

    private void onScrollIncrement(float offset) {
        selectedBlockIndex = Misc.Mod((int) (selectedBlockIndex + offset), blockList.length);
    }

}
