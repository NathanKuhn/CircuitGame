package com.github.nathankuhn.circuitgame.client;

import com.github.nathankuhn.circuitgame.display.DisplayManager;
import com.github.nathankuhn.circuitgame.display.InputManager;
import com.github.nathankuhn.circuitgame.engine.World;
import com.github.nathankuhn.circuitgame.hud.HudRenderer;
import com.github.nathankuhn.circuitgame.hud.Root;

import static org.lwjgl.opengl.GL11.*;

public class Client {

    private World world;

    private HudRenderer hudRenderer;

    private MainMenu mainMenu;
    private Game game;

    private State state;

    public Client(World world) {

        this.world = world;

        hudRenderer = new HudRenderer(new Root());

        game = new Game(world);

    }

    public void init() {

        try {
            hudRenderer.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputManager.Initialize();
        game.init();

        startGame();
    }

    public void run() {

        while (!DisplayManager.ShouldClose()) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            InputManager.Update();

            switch (state){
                case IN_GAME:
                    game.update();
                case MAIN_MENU:

            }

            hudRenderer.render();
            DisplayManager.Update();

        }

        game.cleanup();
        hudRenderer.cleanup();
        DisplayManager.Close();

    }

    public void startGame() {
        state = State.IN_GAME;
        hudRenderer.setHudRoot(game.getGameHudRoot());
        game.focus();

        try {
            hudRenderer.update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startMenu() {
        state = State.MAIN_MENU;

    }

    private static enum State {
        MAIN_MENU,
        IN_GAME
    }

}
