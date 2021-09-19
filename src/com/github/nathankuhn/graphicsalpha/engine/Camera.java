package com.github.nathankuhn.graphicsalpha.engine;

import com.github.nathankuhn.graphicsalpha.utils.Matrix4;
import com.github.nathankuhn.graphicsalpha.utils.Vector3f;
import com.github.nathankuhn.graphicsalpha.utils.VectorMath;

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
    public void move(float x, float y, float z) {
        if (z != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * z;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if (x != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y + 90)) * x;
            position.z += (float)Math.cos(Math.toRadians(rotation.y + 90)) * x;
        }
        position.y += y;
    }
    public void move(Vector3f moveVector) {
        move(moveVector.x, moveVector.y, moveVector.z);
    }

    public Matrix4 getViewMatrix() {

        Matrix4 ret = new Matrix4();
        ret = Matrix4.Multiply(Matrix4.Translation(VectorMath.Scale(position, -1)), ret);
        ret = Matrix4.Multiply(Matrix4.RotationXYZ(VectorMath.Scale(rotation, -1)), ret);
        return ret;

    }
}
