package com.github.nathankuhn.circuitgame.rendering;

public class BlockTexture {

    private int northTexture;
    private int southTexture;
    private int eastTexture;
    private int westTexture;
    private int upTexture;
    private int downTexture;

    public BlockTexture(int northTexture, int southTexture, int eastTexture, int westTexture, int upTexture, int downTexture) {
        this.northTexture = northTexture;
        this.southTexture = southTexture;
        this.eastTexture = eastTexture;
        this.westTexture = westTexture;
        this.upTexture = upTexture;
        this.downTexture = downTexture;
    }

    public int getNorthTexture() {
        return northTexture;
    }

    public int getSouthTexture() {
        return southTexture;
    }

    public int getEastTexture() {
        return eastTexture;
    }

    public int getWestTexture() {
        return westTexture;
    }

    public int getUpTexture() {
        return upTexture;
    }

    public int getDownTexture() {
        return downTexture;
    }
}
