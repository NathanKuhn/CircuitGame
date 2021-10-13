package com.github.nathankuhn.circuitgame;

import com.github.nathankuhn.circuitgame.display.Draw;
import com.github.nathankuhn.circuitgame.display.Window;
import com.github.nathankuhn.circuitgame.engine.Block;
import com.github.nathankuhn.circuitgame.engine.BlockRegistry;
import com.github.nathankuhn.circuitgame.engine.Player;
import com.github.nathankuhn.circuitgame.engine.World;
import com.github.nathankuhn.circuitgame.hud.*;
import com.github.nathankuhn.circuitgame.rendering.*;
import com.github.nathankuhn.circuitgame.input.MouseInput;
import com.github.nathankuhn.circuitgame.utils.*;
import org.lwjgl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.glEnable;

public class Main {

    public static final float MOVE_SPEED = 4.0f;
    public static final float MOUSE_SENSITIVITY = 10.0f;

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
        System.out.println("[Done]");

        BlockRegistry registry = new BlockRegistry();
        registry.addBlock(new Block("Stone", 1, new BlockTexture(0, 0, 0, 0, 0, 0)));
        registry.addBlock(new Block("Dirt",  2, new BlockTexture(1, 1, 1, 1, 1, 1)));
        registry.addBlock(new Block("Grass",  3, new BlockTexture(2, 2, 2, 2, 3, 1)));
        registry.addBlock(new Block("Wood", 4, new BlockTexture(4, 4, 4, 4, 4, 4)));
        registry.addBlock(new Block("Log", 5, new BlockTexture(8, 8, 8, 8, 7, 7)));
        registry.addBlock(new Block("Tile", 6, new BlockTexture(5, 5, 5, 5, 5, 5)));
        registry.addBlock(new Block("sand", 7, new BlockTexture(6, 6, 6, 6, 6, 6)));
        Texture tex = Texture.LoadPNG("TextureAtlas.png");
        TextureAtlas textureAtlas = new TextureAtlas(tex, 16);

        World world = new World(registry, textureAtlas, 10, 3, 10);
        System.out.print("Generating world... ");
        world.generateAll();
        System.out.println("[Done]");

        //Mesh testMesh = MeshImporter.LoadFromOBJ("teapot.obj");
        //Mesh testMesh = FlatMesh.BuildHudMesh(1f, 0.5f, 2, 4);
        //Texture testTexture = Texture.LoadPNG("Cube_1mx1m.png");
        //RenderObject test = new RenderObject(testMesh, testTexture);
        //test.transform.translate(world.getSpawn());

        //world.addOtherRenderObject(test);

        Camera camera = new Camera(new Vector3f(0.0f, 5.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
        Player player = new Player(world, camera);

        // Hud

        Root root = new Root();

        Texture texture = Texture.LoadPNG("Crosshair.png");
        new Image(root, new Vector2f(0, 0), window.getDimensions(), texture);

        Panel panel = new Panel(root.getRightAnchor(), new Vector2f(-0.2f, 0.0f), new Vector2f(0.2f, 0.2f), new Color(0.5f, 0.5f, 0.5f, 0.6f));


        int selectedBlock = 0;
        HudElement[] hudBlocks = new HudElement[7];

        for (int i = 0; i < hudBlocks.length; i++) {
            BlockMesh mesh = new BlockMesh(registry.getBlock(i + 1), textureAtlas);
            hudBlocks[i] = new OrthoMesh(panel, 0.1f, new Vector2f(0.0f, i * 0.2f), new Vector3f(25, 45, 0), mesh.getMesh(), textureAtlas.getTexture());
        }

        // Setting up renderer

        Renderer renderer = new Renderer(window, world, camera);
        HudRenderer hudRenderer = new HudRenderer(window, root);
        MouseInput input = new MouseInput(window);
        input.lockCursor();

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
            if (key == GLFW_KEY_M && action == GLFW_PRESS)
                swapRenderModes();
            if (key == GLFW_KEY_F11 && action == GLFW_PRESS)
                this.window.toggleFullscreen();
        });

        // Set the clear color
        glClearColor(0.7f, 0.9f, 1.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        Vector3f playerMovement = new Vector3f();
        float breakCoolDown = 0.0f;
        float placeCoolDown = 0.0f;

        while ( !window.shouldClose() ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            //Color white = new Color(1.0f, 1.0f, 1.0f);

            //Draw.Rectangle(white, new Vector2f(-0.005f, -0.005f), new Vector2f(0.01f, 0.01f));

            // cast rays and break blocks

            if (input.isLeftButtonPressed()) {
                if (breakCoolDown <= 0) {
                    RayHit hit = camera.castRayFromCenter(world, 5);
                    if (hit != null) {
                        world.placeBlock(
                                VectorMath.Add(hit.getHitPosition(), VectorMath.Scale(hit.getHitNormal(), -0.01f)).toVector3i(),
                                0
                        );
                    }
                    breakCoolDown += 1.0f;
                }
                breakCoolDown -= timer.deltaTime();
            } else {
                breakCoolDown = 0.0f;
            }

            if (input.isRightButtonPressed()) {
                if (placeCoolDown <= 0) {
                    RayHit hit = camera.castRayFromCenter(world, 5);
                    if (hit != null) {
                        world.placeBlock(
                                VectorMath.Add(hit.getHitPosition(), VectorMath.Scale(hit.getHitNormal(), 0.01f)).toVector3i(),
                                selectedBlock + 1
                        );
                    }
                    placeCoolDown += 1.0f;
                }
                placeCoolDown -= timer.deltaTime();
            } else {
                placeCoolDown = 0.0f;
            }

            if (selectedBlock != Misc.Mod((int)input.getScrollOffset(), hudBlocks.length)) {

                selectedBlock = Misc.Mod((int)input.getScrollOffset(), hudBlocks.length);

                for (int i = 0; i < hudBlocks.length; i++) {
                    hudBlocks[i].setCenter(new Vector2f(0.0f, (i - selectedBlock) * 0.2f));
                    hudBlocks[i].update();
                }

            }

            playerMovement.set(0,0,0);

            if (window.isKeyPressed(GLFW_KEY_W)) {
                playerMovement.z = -1.0f;
            } else if (window.isKeyPressed(GLFW_KEY_S)) {
                playerMovement.z = 1.0f;
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                playerMovement.x = -1.0f;
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                playerMovement.x = 1.0f;
            }
            if (window.isKeyPressed(GLFW_KEY_SPACE)) {
                player.jump(6.0f);
            }
            if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
                playerMovement.scaleSet(2.0f);
            }
            if (window.isKeyPressed(GLFW_KEY_R)) {
                player.getPosition().set(world.getSpawn());
            }
            playerMovement.scaleSet(MOVE_SPEED);
            Vector3f inputMovement = camera.moveVector(playerMovement);

            player.move(inputMovement);

            Vector2f rotVec = input.getDisplaceVec();
            camera.rotate(-rotVec.x * MOUSE_SENSITIVITY * timer.deltaTime(), -rotVec.y * MOUSE_SENSITIVITY * timer.deltaTime(), 0.0f);

            Vector3f rot = camera.getRotation();

            if (rot.x > 90) {
                rot.x = 90;
            } else if (rot.x < -90) {
                rot.x = -90;
            }

            if (window.isResized()) {
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
                try {
                    hudRenderer.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
