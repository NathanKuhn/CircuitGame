package com.github.nathankuhn.circuitgame.utils;

public class Transform {

    private Matrix4 translation;
    private Matrix4 scale;
    private Matrix4 rotation;

    private Vector3f positionVector;
    private Vector3f scaleVector;
    private Vector3f rotationVector;

    public Transform(Vector3f position) {
        positionVector = position;
        scaleVector = new Vector3f(1.0f, 1.0f, 1.0f);
        rotationVector = new Vector3f(0.0f, 0.0f, 0.0f);

        translation = Matrix4.Translation(positionVector);
        scale = Matrix4.Scale(scaleVector);
        rotation = Matrix4.RotationXYZ(rotationVector);

    }

    public Matrix4 getTranslationMatrix() {
        return translation;
    }
    public Matrix4 getScaleMatrix() {
        return scale;
    }
    public Matrix4 getRotationMatrix() {
        return rotation;
    }
    public Vector3f getTranslationVector() {
        return positionVector;
    }
    public Vector3f getScaleVector() {
        return scaleVector;
    }
    public Vector3f getRotationVector() {
        return rotationVector;
    }

    private void updateTranslation() {
        translation = Matrix4.Translation(positionVector);
    }
    private void updateScale() {
        scale = Matrix4.Scale(scaleVector);
    }
    private void updateRotation() {
        rotation = Matrix4.RotationXYZ(rotationVector);
    }

    public void setPosition(Vector3f positionVector) {
        this.positionVector = positionVector;
        updateTranslation();
    }
    public void setScale(Vector3f scaleVector) {
        this.scaleVector = scaleVector;
        updateScale();
    }
    public void setRotation(Vector3f rotationVector) {
        this.rotationVector = rotationVector;
        updateRotation();
    }

    public void translate(Vector3f a) {
        this.positionVector.addSet(a);
        updateTranslation();
    }
    public void scale(Vector3f a) {
        this.scaleVector.productSet(a);
        updateScale();
    }
    public void rotate(Vector3f a) {
        this.rotationVector.addSet(a);
        updateRotation();
    }

    public Matrix4 getMatrix() {
        Matrix4 ret = new Matrix4();
        ret = Matrix4.Multiply(ret, translation);
        ret = Matrix4.Multiply(ret, rotation);
        ret = Matrix4.Multiply(ret, scale);

        return ret;
    }

}
