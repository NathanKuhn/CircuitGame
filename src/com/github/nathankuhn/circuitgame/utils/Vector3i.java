package com.github.nathankuhn.circuitgame.utils;

public class Vector3i {

    public int x;
    public int y;
    public int z;

    public Vector3i() {
        x = 0;
        y = 0;
        z = 0;
    }
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3i(Vector3f vector) {
        this.x = (int)vector.x;
        this.y = (int)vector.y;
        this.z = (int)vector.z;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
