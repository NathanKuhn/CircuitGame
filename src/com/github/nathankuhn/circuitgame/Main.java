package com.github.nathankuhn.circuitgame;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.engine.*;
import com.github.nathankuhn.circuitgame.hud.*;
import com.github.nathankuhn.circuitgame.rendering.*;
import com.github.nathankuhn.circuitgame.utils.*;
import org.lwjgl.*;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.glEnable;

public class Main {

    private final Window window;
    private final Timer timer;
    private boolean polygon;

    public Main() {
        window = new Window(1000, 1000);
        timer = new Timer();
        polygon = true;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        System.out.println("Press enter to print ups");

        System.out.print("Initializing window... ");
        window.init();
        //window.setFullscreen(true); // DON'T USE WHEN DEBUGGING ( ͡° ͜ʖ ͡°)
        System.out.println("[Done]");

        BlockRegistry registry = new BlockRegistry();
        registry.addBlock(new Block("Stone", 1, new BlockTexture(0, 0, 0, 0, 0, 0)));
        registry.addBlock(new Block("Dirt",  2, new BlockTexture(1, 1, 1, 1, 1, 1)));
        registry.addBlock(new Block("Grass",  3, new BlockTexture(2, 2, 2, 2, 3, 1)));
        registry.addBlock(new Block("Wood", 4, new BlockTexture(4, 4, 4, 4, 4, 4)));
        registry.addBlock(new Block("Log", 5, new BlockTexture(8, 8, 8, 8, 7, 7)));
        registry.addBlock(new Block("Tile", 6, new BlockTexture(5, 5, 5, 5, 5, 5)));
        registry.addBlock(new Block("sand", 7, new BlockTexture(6, 6, 6, 6, 6, 6)));
        registry.addBlock(new Block("glass", 8, new BlockTexture(9, 9, 9, 9, 9, 9), 0));
        registry.addBlock(new Block("leaves", 9, new BlockTexture(10, 10, 10, 10, 10, 10)));
        Texture tex = Texture.LoadPNG("TextureAtlas.png");
        TextureAtlas textureAtlas = new TextureAtlas(tex, 16);

        Random random = new Random();
        World world = new World(registry, textureAtlas, 10, 7, 10, random.nextInt());
        System.out.print("Generating world... ");
        world.generateAll();
        System.out.println("[Done]");

        Camera camera = new Camera(new Vector3f(0.0f, 5.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
        Player player = new Player(world, camera);
        UserInput input = new UserInput(window);

        PlayerController playerController = new PlayerController(player, world, input, window);
        playerController.focus();

        // Setting up renderer

        Renderer renderer = new Renderer(window, world, camera);
        HudRenderer hudRenderer = new HudRenderer(window, playerController.getHudRoot());

        try {
            renderer.init();
        } catch (Exception e) {
            System.out.println("Error in render init");
            e.printStackTrace();
        }

        try {
            hudRenderer.init();
        } catch(Exception e) {
            System.out.println("Error in hud render init");
            e.printStackTrace();
        }
        input.init();

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window.getHandle(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            if (key == GLFW_KEY_ENTER && action == GLFW_PRESS)
                printFrameRate();
            if (key == GLFW_KEY_M && action == GLFW_PRESS) {
                swapRenderModes();
                playerController.setHudEnabled(!playerController.isHudEnabled());
            }
            if (key == GLFW_KEY_F11 && action == GLFW_PRESS)
                this.window.toggleFullscreen();
        });

        // Set the clear color
        glClearColor(0.7f, 0.9f, 1.0f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (window.isResized()) {
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
                try {
                    hudRenderer.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            playerController.update(timer.deltaTime());
            player.update(timer.deltaTime());

            renderer.render();
            hudRenderer.render();
            window.update();
            input.update();
            timer.update();
        }

        renderer.cleanup();
        hudRenderer.cleanup();
        window.close();

    }

    public void printFrameRate() {
        System.out.println(1.0f / timer.deltaTime());
    }
    public void swapRenderModes() {
        if (polygon) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glDisable(GL_CULL_FACE);
            polygon = false;
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glEnable(GL_CULL_FACE);
            polygon = true;
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
