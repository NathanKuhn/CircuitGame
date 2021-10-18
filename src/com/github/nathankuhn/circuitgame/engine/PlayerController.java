package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.hud.*;
import com.github.nathankuhn.circuitgame.rendering.BlockMesh;
import com.github.nathankuhn.circuitgame.rendering.RayHit;
import com.github.nathankuhn.circuitgame.utils.*;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController {

    public static final float MOVE_SPEED = 4.0f;
    public static final float MOUSE_SENSITIVITY = 10.0f;

    private static float BREAK_COOL_DOWN = 0.5f;
    private static float PLACE_COOL_DOWN = 0.5f;

    private Player player;
    private World world;
    private UserInput userInput;
    private final Window window;

    private Root playerHud;

    private int selectedBlockIndex;
    private int[] blockList;
    private HudElement[] hudBlocks;

    private float breakCoolDown;
    private float placeCoolDown;

    public PlayerController(Player player, World world, UserInput mouseInput, Window window) {
        this.player = player;
        this.world = world;
        this.userInput = mouseInput;
        this.window = window;

        breakCoolDown = 0.0f;
        placeCoolDown = 0.0f;

        blockList = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};

        playerHud = new Root();
        createHud();
    }

    public void focus() {

        playerHud.setEnabled(true);
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
        selectedBlockIndex = Misc.Mod((int) (selectedBlockIndex - offset), blockList.length);
        updateHudBlocks();
    }

    public Root getHudRoot() {
        return playerHud;
    }

    public void setHudEnabled(boolean enabled) {
        playerHud.setEnabled(enabled);
    }

    public boolean isHudEnabled() {
        return playerHud.isEnabled();
    }

    private void createHud() {

        Texture texture = Texture.LoadPNG("Crosshair.png");
        new Image(playerHud, new Vector2f(0, 0), window.getDimensions(), texture);

        Panel panel = new Panel(playerHud.getDownAnchor(), new Vector2f(0.0f, 0.15f), new Vector2f(0.2f, 0.2f), new Color(0.5f, 0.5f, 0.5f, 0.3f));
        hudBlocks = new HudElement[blockList.length];

        for (int i = 0; i < hudBlocks.length; i++) {
            BlockMesh mesh = new BlockMesh(world.getBlockRegistry().getBlock(i + 1), world.getTextureAtlas());
            hudBlocks[i] = new OrthoMesh(panel, 0.075f, new Vector2f(0.2f, 0.0f), new Vector3f(25, 45, 0), mesh.getMesh(), world.getTextureAtlas().getTexture());
        }

        updateHudBlocks();
    }

    private void updateHudBlocks() {
        for (int i = 0; i < blockList.length; i++) {
            hudBlocks[i].setCenter(new Vector2f((i - selectedBlockIndex) * 0.17f, 0.0f));
            if (i == selectedBlockIndex) {
                hudBlocks[i].setDimensions(new Vector2f(0.11f, 0.11f));
            } else {
                hudBlocks[i].setDimensions(new Vector2f(0.075f, 0.075f));
            }
            hudBlocks[i].update();
        }
    }
}
