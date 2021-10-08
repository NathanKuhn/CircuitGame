package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.engine.World;
import com.github.nathankuhn.circuitgame.utils.*;

public class Camera {

    private Vector3f position;
    private Vector3f rotation;

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void translate(Vector3f translationVector) {
        position.addSet(translationVector);
    }

    public void rotate(float x, float y, float z) {
        rotation.addSet(new Vector3f(x, y, z));
    }

    public void rotate(Vector3f rotationVector) {
        rotation.addSet(rotationVector);
    }

    public Vector3f moveVector(float x, float y, float z) {
        Vector3f ret = new Vector3f();
        if (z != 0) {
            ret.x += (float)Math.sin(Math.toRadians(rotation.y)) * z;
            ret.z += (float)Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if (x != 0) {
            ret.x += (float)Math.sin(Math.toRadians(rotation.y + 90)) * x;
            ret.z += (float)Math.cos(Math.toRadians(rotation.y + 90)) * x;
        }
        ret.y = y;
        return ret;
    }

    public Vector3f moveVector(Vector3f moveVector) {
        return moveVector(moveVector.x, moveVector.y, moveVector.z);
    }

    public Matrix4 getViewMatrix() {
        Matrix4 ret = new Matrix4();
        ret = Matrix4.Multiply(Matrix4.Translation(VectorMath.Scale(position, -1)), ret);
        ret = Matrix4.Multiply(Matrix4.RotationXYZ(VectorMath.Scale(rotation, -1)), ret);
        return ret;
    }

    public RayHit castRayFromCenter(World world, float maxDistance) {
        Vector3f direction = new Vector3f(0, 0, -1.0f);
        direction = Matrix4.Multiply(
                Matrix4.InverseRotationXYZ(VectorMath.Scale(rotation, -1)),
                new Vector4f(direction, 0.0f)
        ).toVector3f();
        Ray ray = new Ray(position, VectorMath.Normalize(direction));

        return Ray.FindVoxelIntersection(ray, world, maxDistance);
    }
}
