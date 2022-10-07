package com.github.nathankuhn.circuitgame.client;

import com.github.nathankuhn.circuitgame.display.DisplayManager;
import com.github.nathankuhn.circuitgame.engine.Player;
import com.github.nathankuhn.circuitgame.engine.PlayerController;
import com.github.nathankuhn.circuitgame.engine.UserInput;
import com.github.nathankuhn.circuitgame.engine.World;
import com.github.nathankuhn.circuitgame.hud.Root;
import com.github.nathankuhn.circuitgame.rendering.Camera;
import com.github.nathankuhn.circuitgame.rendering.Renderer;
import com.github.nathankuhn.circuitgame.utils.Timer;
import com.github.nathankuhn.circuitgame.utils.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.glEnable;

public class Game {

    private UserInput userInput;
    private World world;

    private PlayerController playerController;
    private Renderer renderer;
    private Timer timer;

    private State state;

    private boolean wireframeMode;

    public Game(UserInput userInput, World world) {
        this.userInput = userInput;
        this.world = world;

        Camera camera = new Camera(new Vector3f(), new Vector3f());
        Player player = new Player(world, camera);
        playerController = new PlayerController(player, world, userInput);
        renderer = new Renderer(world, player.getCamera());
        timer = new Timer();

        state = State.PLAY;

        wireframeMode = false;
    }

    public void init() {

        try {
            renderer.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void focus() {
        glClearColor(0.7f, 0.9f, 1.0f, 1.0f);
        glfwSetKeyCallback(DisplayManager.WindowHandle(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            if (key == GLFW_KEY_M && action == GLFW_PRESS) {
                swapRenderModes();
                playerController.setHudEnabled(!playerController.isHudEnabled());
            }
            if (key == GLFW_KEY_TAB && action == GLFW_PRESS) {
                playerController.toggleInventory();
            }
        });

        timer.reset();
        playerController.focus();

    }

    public void update() {

        timer.update();

        playerController.update(timer.deltaTime());

        renderer.render();

    }

    public void cleanup() {
        renderer.cleanup();
    }

    public void swapRenderModes() {
        if (!wireframeMode) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glDisable(GL_CULL_FACE);
            wireframeMode = true;
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glEnable(GL_CULL_FACE);
            wireframeMode = false;
        }
    }

    protected Root getGameHudRoot() {
        return playerController.getHudRoot();
    }

    private static enum State {
        PLAY,
        MENU,
        INVENTORY
    }
}
