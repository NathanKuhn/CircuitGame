package com.github.nathankuhn.circuitgame.client;

import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.engine.UserInput;
import com.github.nathankuhn.circuitgame.engine.World;
import com.github.nathankuhn.circuitgame.hud.HudRenderer;
import com.github.nathankuhn.circuitgame.hud.Root;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Client {

    private Window window;
    private World world;

    private HudRenderer hudRenderer;
    private UserInput userInput;

    private MainMenu mainMenu;
    private Game game;

    private State state;

    public Client(Window window, World world) {

        this.window = window;
        this.world = world;

        hudRenderer = new HudRenderer(window, new Root());
        userInput = new UserInput(window);

        game = new Game(window, userInput, world);

    }

    public void init() {

        try {
            hudRenderer.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        userInput.init();
        game.init();

        startGame();
    }

    public void run() {

        while (!window.shouldClose()) {

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

            userInput.update();

            switch (state){
                case IN_GAME:
                    game.update();
                case MAIN_MENU:

            }

            hudRenderer.render();
            window.update();

        }

        game.cleanup();
        hudRenderer.cleanup();
        window.close();

    }

    public void startGame() {
        state = State.IN_GAME;
        hudRenderer.setHudRoot(game.getGameHudRoot());
        game.focus();
    }

    public void startMenu() {
        state = State.MAIN_MENU;

    }

    private static enum State {
        MAIN_MENU,
        IN_GAME
    }

}
