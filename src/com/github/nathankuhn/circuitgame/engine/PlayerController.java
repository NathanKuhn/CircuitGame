package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.display.DisplayManager;
import com.github.nathankuhn.circuitgame.display.InputManager;
import com.github.nathankuhn.circuitgame.hud.*;
import com.github.nathankuhn.circuitgame.rendering.BlockMesh;
import com.github.nathankuhn.circuitgame.rendering.RayHit;
import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.*;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController {

    public static final float MOVE_SPEED = 4.0f;
    public static final float MOUSE_SENSITIVITY = 30.0f;

    private static final float BREAK_COOL_DOWN = 0.5f;
    private static final float PLACE_COOL_DOWN = 0.5f;

    private Player player;
    private World world;
    private RenderObject blockHighlight;

    private Root playerHud;
    private Font hudFont;

    private int selectedBlockIndex;
    private int[] blockList;
    private HudElement[] hudBlocks;
    private Text fpsIndicator;

    private HudElement playerInventory;
    private int[] inventoryBlockList;
    private boolean inventoryOpen;

    private float breakCoolDown;
    private float placeCoolDown;
    private float textUpdateCoolDown;

    public PlayerController(Player player, World world) {
        this.player = player;
        this.world = world;

        breakCoolDown = 0.0f;
        placeCoolDown = 0.0f;
        textUpdateCoolDown = 0.0f;

        blockList = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 10};
        inventoryBlockList = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        try {
            hudFont = new Font("LucidaConsole.fnt");
        } catch(Exception e) {
            e.printStackTrace();
        }

        playerHud = new Root();
        createHud();
        playerInventory = new Panel(playerHud, new Vector2f(), new Vector2f(), new Color(0.0f, 0.0f, 0.0f, 0.0f));
        inventoryOpen = false;
        createInventory();

        blockHighlight = new RenderObject(
                BlockMesh.getRawMesh(),
                Texture.SingleColor(new Color(0.8f, 0.8f, 0.8f, 0.4f))
        );
        blockHighlight.transform.setScale(new Vector3f(1.005f, 1.005f, 1.005f));
        world.addOtherRenderObject(blockHighlight);

    }

    public void focus() {

        playerHud.setEnabled(true);
        InputManager.LockCursor();

        InputManager.SetMouseButtonCallback((windowHandle, button, action, mode) -> {
            if (action == GLFW_PRESS) {
                if (button == GLFW_MOUSE_BUTTON_1) {
                    onLeftClick();
                }
                if (button == GLFW_MOUSE_BUTTON_2) {
                    onRightClick();
                }
            }
        });

        InputManager.SetScrollCallback((windowHandle, xOffset, yOffset) -> {
            onScrollIncrement((float)yOffset);
        });

    }

    public void update(float deltaTime) {

        if (fpsIndicator != null) {
            if (textUpdateCoolDown <= 0.0f) {
                fpsIndicator.setText("FPS: " + (int) (1 / deltaTime));
                textUpdateCoolDown = 0.2f;
            } else {
                textUpdateCoolDown -= deltaTime;
            }
        }

        if (inventoryOpen) {
            blockHighlight.setShouldRender(false);
            return;
        }

        RayHit hit = player.getCamera().castRayFromCenter(world, 5);
        if (hit != null) {
            blockHighlight.setShouldRender(true);
            Vector3f p = VectorMath.Add(hit.getHitPosition(), VectorMath.Scale(hit.getHitNormal(), -0.05f)).toVector3iWorld().toVector3f();
            p.subtractSet(VectorMath.Scale(VectorMath.Subtract(blockHighlight.transform.getScaleVector(), new Vector3f(1.0f, 1.0f, 1.0f)), 0.5f));
            blockHighlight.transform.setPosition(p);
        } else {
            blockHighlight.setShouldRender(false);
        }

        Vector3f playerMovement = new Vector3f();

        if (InputManager.IsKeyPressed(GLFW_KEY_W)) {
            playerMovement.z = -1.0f;
        } else if (InputManager.IsKeyPressed(GLFW_KEY_S)) {
            playerMovement.z = 1.0f;
        }
        if (InputManager.IsKeyPressed(GLFW_KEY_A)) {
            playerMovement.x = -1.0f;
        } else if (InputManager.IsKeyPressed(GLFW_KEY_D)) {
            playerMovement.x = 1.0f;
        }
        if (InputManager.IsKeyPressed(GLFW_KEY_SPACE)) {
            player.jump(8.0f);
        }
        if (InputManager.IsKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            playerMovement.scaleSet(2.0f);
        }
        if (InputManager.IsKeyPressed(GLFW_KEY_R)) {
            player.getPosition().set(world.getSpawn());
        }
        playerMovement.scaleSet(MOVE_SPEED);
        Vector3f inputMovement = player.getCamera().moveVector(playerMovement);

        player.move(inputMovement);

        Vector2f rotVec = InputManager.GetMouseDisplacement();
        rotVec.scaleSet(deltaTime);
        player.getCamera().rotate(-rotVec.x * MOUSE_SENSITIVITY, -rotVec.y * MOUSE_SENSITIVITY, 0.0f);

        player.update(deltaTime);

    }

    public int getSelectedBlock() {
        return blockList[selectedBlockIndex];
    }

    public int[] getBlockList() {
        return blockList;
    }

    private void onLeftClick() {

        if (inventoryOpen) {
            return;
        }

        player.destroyBlock();
    }

    private void onRightClick() {

        if (inventoryOpen) {
            return;
        }

        player.placeBlock(blockList[selectedBlockIndex]);
    }

    private void onScrollIncrement(float offset) {
        selectedBlockIndex = Misc.Mod((int) (selectedBlockIndex - offset), blockList.length);
        updateHudBlocks();
    }

    public void openInventory() {
        playerInventory.setEnabled(true);
        InputManager.UnLockCursor();
        player.move(new Vector3f());
        inventoryOpen = true;
    }

    public void closeInventory() {
        playerInventory.setEnabled(false);
        InputManager.LockCursor();
        inventoryOpen = false;
    }

    public void toggleInventory() {
        if (inventoryOpen) {
            closeInventory();
        } else {
            openInventory();
        }
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
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

    private void createHud(){

        Texture texture = Texture.LoadPNG("Crosshair.png");
        new Image(playerHud, new Vector2f(0, 0), DisplayManager.WindowDimensions(), texture);

        if (hudFont != null) {
            fpsIndicator = new Text(playerHud.getUpperLeftAnchor(), new Vector2f(0.01f, -0.01f), hudFont, "FPS: ");
        }

        Panel panel = new Panel(playerHud.getDownAnchor(), new Vector2f(0.0f, 0.15f), new Vector2f(0.2f, 0.2f), new Color(0.7f, 0.7f, 0.7f, 0.5f));
        hudBlocks = new HudElement[blockList.length];

        for (int i = 0; i < hudBlocks.length; i++) {
            BlockMesh mesh = new BlockMesh(world.getBlockRegistry().getBlock(blockList[i]), world.getTextureAtlas());
            hudBlocks[i] = new OrthoMesh(panel, 0.075f, new Vector2f(0.2f, 0.0f), new Vector3f(25, 45, 0), mesh.getMesh(), world.getTextureAtlas().getTexture());
        }

        updateHudBlocks();
    }

    private void createInventory() {

        Panel panel = new Panel(playerInventory, new Vector2f(0.0f, 0.0f), new Vector2f(1.6f, 1.0f), new Color(0.2f, 0.2f, 0.2f, 0.7f));
        playerInventory.setEnabled(false);

        int index;

        for (int x = -3; x < 5; x++) {
            for (int y = -2; y < 3; y++) {

                index = (x + 3) + (y + 2) * 8;
                if (index < inventoryBlockList.length) {
                    BlockMesh mesh = new BlockMesh(world.getBlockRegistry().getBlock(inventoryBlockList[index]), world.getTextureAtlas());
                    OrthoMesh block = new OrthoMesh(panel, 0.1f, new Vector2f(x * 0.2f - 0.1f, -y * 0.2f), new Vector3f(25f, 45f, 0f), mesh.getMesh(), world.getTextureAtlas().getTexture());
                }
            }
        }
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
