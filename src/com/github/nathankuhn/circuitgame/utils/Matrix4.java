package com.github.nathankuhn.circuitgame.utils;

public class Matrix4 {

    private float[] values;

    public Matrix4() {
        values = new float[] {
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };
    }

    public float get(int row, int col) {
        return values[row * 4 + col];
    }

    public float[] getArray() {

        float[] temp = new float[16];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                temp[col * 4 + row] = values[row * 4 + col];
            }
        }

        return temp;
    }

    public void set(int row, int col, float value) {
        values[row * 4 + col] = value;
    }

    public void set(int row, int col, double value) {
        values[row * 4 + col] = (float) value;
    }

    public float[][] toArray() {

        float[][] out = new float[4][4];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                out[row][col] = values[row *4 + col];
            }
        }

        return out;

    }

    public String toString() {

        String out = "";

        for (int row = 0; row < 4; row++) {
            if (row != 0)
                out += "\n";
            for (int col = 0; col < 4; col++) {
                if (col != 0)
                    out += ", ";
                out += get(row, col);
            }
        }

        return out;

    }

    public static Matrix4 Multiply(Matrix4 a, Matrix4 b) {

        Matrix4 out = new Matrix4();

        float[][] af = a.toArray();
        float[][] bf = b.toArray();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                out.set(row, col, af[row][0] * bf[0][col] + af[row][1] * bf[1][col] + af[row][2] * bf[2][col] + af[row][3] * bf[3][col]);

            }
        }


        return out;
    }

    public static Vector4f Multiply(Matrix4 matrix, Vector4f vector) {
        Vector4f ret = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
        ret.x = vector.x * matrix.get(0, 0) + vector.y * matrix.get(0, 1) + vector.z * matrix.get(0, 2) + vector.w * matrix.get(0, 3);
        ret.y = vector.x * matrix.get(1, 0) + vector.y * matrix.get(1, 1) + vector.z * matrix.get(1, 2) + vector.w * matrix.get(1, 3);
        ret.z = vector.x * matrix.get(2, 0) + vector.y * matrix.get(2, 1) + vector.z * matrix.get(2, 2) + vector.w * matrix.get(2, 3);
        ret.w = vector.x * matrix.get(3, 0) + vector.y * matrix.get(3, 1) + vector.z * matrix.get(3, 2) + vector.w * matrix.get(3, 3);
        return ret;
    }

    public static Matrix4 Perspective(float FOV, float aspectRatio, float Z_NEAR, float Z_FAR) {

        Matrix4 out = new Matrix4();

        float zm = Z_FAR - Z_NEAR;
        float zp = Z_FAR + Z_NEAR;

        out.set(0, 0, 1.0f / (float) Math.tan(FOV / 2.0f) / aspectRatio);
        out.set(1, 1, 1.0f / (float) Math.tan(FOV / 2.0f));
        out.set(2, 2, -zp / zm);
        out.set(2, 3, -(2 * Z_FAR * Z_NEAR) / zm);
        out.set(3, 2, -1.0f);
        out.set(3, 3, 0.0f);

        return out;

    }

    public static Matrix4 Translation(float x, float y, float z) {

        Matrix4 out = new Matrix4();

        out.set(0, 3, x);
        out.set(1, 3, y);
        out.set(2, 3, z);

        return out;

    }

    public static Matrix4 Translation(Vector3f vector) {
        return Translation(vector.x, vector.y, vector.z);
    }

    public static Matrix4 Scale(float x, float y, float z) {

        Matrix4 out = new Matrix4();

        out.set(0, 0, x);
        out.set(1, 1, y);
        out.set(2, 2, z);

        return out;
    }

    public static Matrix4 Scale(Vector3f vector) {
        return Scale(vector.x, vector.y, vector.z);
    }

    public static Matrix4 RotationX(float t) {

        t = (float) Math.toRadians(t);

        Matrix4 out = new Matrix4();

        out.set(1, 1, Math.cos(t));
        out.set(1, 2, -Math.sin(t));
        out.set(2, 1, Math.sin(t));
        out.set(2, 2, Math.cos(t));

        return out;
    }

    public static Matrix4 RotationY(float t) {

        t = (float) Math.toRadians(t);

        Matrix4 out = new Matrix4();

        out.set(0, 0, Math.cos(t));
        out.set(0, 2, Math.sin(t));
        out.set(2, 0, -Math.sin(t));
        out.set(2, 2, Math.cos(t));

        return out;
    }

    public static Matrix4 RotationZ(float t) {

        t = (float) Math.toRadians(t);

        Matrix4 out = new Matrix4();

        out.set(0, 0, Math.cos(t));
        out.set(0, 1, -Math.sin(t));
        out.set(1, 0, Math.sin(t));
        out.set(1, 1, Math.cos(t));

        return out;
    }

    public static Matrix4 RotationXYZ(float x, float y, float z) {
        Matrix4 rx = RotationX(x);
        Matrix4 ry = RotationY(y);
        Matrix4 rz = RotationZ(z);

        Matrix4 ret = new Matrix4();
        ret = Multiply(ret, rx);
        ret = Multiply(ret, ry);
        ret = Multiply(ret, rz);

        return ret;
    }

    public static Matrix4 RotationXYZ(Vector3f vector) {
        return RotationXYZ(vector.x, vector.y, vector.z);
    }

    public static Matrix4 InverseRotationXYZ(float x, float y, float z) {

        Matrix4 rx = RotationX(-x);
        Matrix4 ry = RotationY(-y);
        Matrix4 rz = RotationZ(-z);

        Matrix4 ret = new Matrix4();
        ret = Multiply(ret, rz);
        ret = Multiply(ret, ry);
        ret = Multiply(ret, rx);

        return ret;
    }

    public static Matrix4 InverseRotationXYZ(Vector3f vector) {
        return InverseRotationXYZ(vector.x, vector.y, vector.z);
    }

}
