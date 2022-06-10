package com.github.nathankuhn.circuitgame;

import com.github.nathankuhn.circuitgame.client.Client;
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

        System.out.println("Hello! Welcome to CircuitGame alpha v0.0.1");

        //System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        //System.out.println("Press enter to print ups");

        //System.out.println("Initializing window... ");
        timer.update();
        window.init();
        //window.setFullscreen(true); // DON'T USE WHEN DEBUGGING ( ͡° ͜ʖ ͡°)
        timer.update();
        //System.out.println("Finished: " + (int)(timer.deltaTime() * 1000) + "ms elapsed");

        BlockRegistry registry = new BlockRegistry();
        registry.addBlock(new Block("Stone", 1, new BlockTexture(0, 0, 0, 0, 0, 0)));
        registry.addBlock(new Block("Dirt",  2, new BlockTexture(1, 1, 1, 1, 1, 1)));
        registry.addBlock(new Block("Grass",  3, new BlockTexture(2, 2, 2, 2, 3, 1)));
        registry.addBlock(new Block("Wood", 4, new BlockTexture(4, 4, 4, 4, 4, 4)));
        registry.addBlock(new Block("Log", 5, new BlockTexture(8, 8, 8, 8, 7, 7)));
        registry.addBlock(new Block("Tile", 6, new BlockTexture(5, 5, 5, 5, 5, 5)));
        registry.addBlock(new Block("Sand", 7, new BlockTexture(6, 6, 6, 6, 6, 6)));
        registry.addBlock(new Block("Glass", 8, new BlockTexture(9, 9, 9, 9, 9, 9), 0));
        registry.addBlock(new Block("Leaves", 9, new BlockTexture(10, 10, 10, 10, 10, 10)));
        registry.addBlock(new Block("Jack-O-Lantern", 10, new BlockTexture(12, 11, 12, 12, 13, 14)));
        Texture tex = Texture.LoadPNG("TextureAtlas.png");
        TextureAtlas textureAtlas = new TextureAtlas(tex, 16);

        Random random = new Random();
        World world = new World(registry, textureAtlas, 5, 3, 5, random.nextInt());
        System.out.println("Generating world... ");
        timer.update();
        world.generateAll();
        timer.update();
        System.out.println("Finished: " + (int)(timer.deltaTime() * 1000) + "ms elapsed");

        Client client = new Client(window, world);

        client.init();
        client.run();

    }

    public void printFrameRate() {
        System.out.println(1.0f / timer.deltaTime());
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
