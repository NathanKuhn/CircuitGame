package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.rendering.Camera;
import com.github.nathankuhn.circuitgame.rendering.RayHit;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

import java.awt.font.TextMeasurer;

public class Player {

    private static final float PLAYER_HEIGHT = 1.85f;
    private static final float EYE_HEIGHT = 1.75f;
    private static final float PLAYER_WIDTH = 0.5f;
    private static final float PLAYER_GRAVITY = 18.0f;
    private static final float TERMINAL_VELOCITY = 30.0f;
    private static final float WALK_LERP_FACTOR = 20.0f;

    private Vector3f position;
    private Vector3f previousPosition;
    private Vector3f velocity;
    private Vector3f move;
    private World world;
    private Camera camera;
    private BoundingBox boundingBox;
    private boolean grounded;

    public Player(World world, Camera camera) {
        this.position = world.getSpawn();
        this.world = world;
        this.camera = camera;
        this.move = new Vector3f();
        this.boundingBox = new BoundingBox(getBoundingBoxCenter(), new Vector3f(PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_WIDTH));
        velocity = new Vector3f(0.0f,0.0f,0.0f);
        grounded = false;
    }

    public void update(float deltaTime) {
        camera.setPosition(position);

        position = VectorMath.Add(position, VectorMath.Scale(velocity, deltaTime));
        boundingBox.setCenter(getBoundingBoxCenter());

        Vector3f velocityXZ = VectorMath.Lerp(velocity, move, WALK_LERP_FACTOR * deltaTime * (grounded ? 1 : 0.1f));
        if (VectorMath.Subtract(velocity, move).length() < 0.01f) {
            velocityXZ.set(move);
        }
        velocity.x = velocityXZ.x;
        velocity.z = velocityXZ.z;

        Vector3f correction = boundingBox.worldCollision(world, position.toVector3i());
        if (correction.y != 0) {
            position.y += correction.y;
            velocity.y = 0;
            if (correction.y > 0) {
                grounded = true;
            }
        } else {
            if (velocity.y > - TERMINAL_VELOCITY) {
                velocity.y -= PLAYER_GRAVITY * deltaTime;
            } else {
                velocity.y = -TERMINAL_VELOCITY;
            }
            grounded = false;
        }
        if (correction.x != 0) {
            position.x += correction.x;
            velocity.x = 0;
        }
        if (correction.z != 0) {
            position.z += correction.z;
            velocity.z = 0;
        }
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void move(Vector3f move) {
        this.move = move;
    }

    public void jump(float speed) {
        if (isGrounded()) {
            velocity.y += speed;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    private Vector3f getBoundingBoxCenter() {
        //return VectorMath.Subtract(new Vector3f(position.x, position.y - PLAYER_HEIGHT / 2, position.z), position.toVector3i());
        return new Vector3f(position.x, position.y - EYE_HEIGHT + PLAYER_HEIGHT / 2, position.z);
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Camera getCamera() {
        return camera;
    }

    public void destroyBlock() {

        RayHit hit = camera.castRayFromCenter(world, 5);
        if (hit != null) {
            world.placeBlock(
                    VectorMath.Add(hit.getHitPosition(), VectorMath.Scale(hit.getHitNormal(), -0.5f)).toVector3iWorld(),
                    0
            );
        }

    }

    public void placeBlock(int blockID) {

        RayHit hit = camera.castRayFromCenter(world, 5);
        if (hit != null) {
            Vector3i pos = VectorMath.Add(hit.getHitPosition(), VectorMath.Scale(hit.getHitNormal(), 0.05f)).toVector3iWorld();
            if (!boundingBox.isColliding(pos)) {
                world.placeBlock(pos, blockID);
            }
        }

    }
}
