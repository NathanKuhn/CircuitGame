package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class Player {

    private static final float PLAYER_HEIGHT = 1.8f;
    private static final float PLAYER_GRAVITY = 20.0f;
    private static final float WALK_LERP_FACTOR = 20.0f;

    private Vector3f position;
    private Vector3f velocity;
    private Vector3f move;
    private World world;
    private Camera camera;

    public Player(Vector3f position, World world, Camera camera) {
        this.position = position;
        this.world = world;
        this.camera = camera;
        this.move = new Vector3f();
        velocity = new Vector3f(0.0f,0.0f,0.0f);
    }

    public void update(float deltaTime) {
        camera.setPosition(position);

        position = VectorMath.Add(position, VectorMath.Scale(velocity, deltaTime));

        if (isGrounded()) {
            velocity.y = 0.0f;
            move.y = 0.0f;
            velocity = VectorMath.Lerp(velocity, move, WALK_LERP_FACTOR * deltaTime);
            if (VectorMath.Subtract(velocity, move).length() < 0.01f) {
                velocity.set(move);
            }
        } else {
            velocity.y -= PLAYER_GRAVITY * deltaTime;
        }
    }

    public boolean isGrounded() {
        Vector3i check = new Vector3i((int)position.x, (int)(position.y - PLAYER_HEIGHT), (int)position.z);
        return (world.getBlock(check) != 0);
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


}
