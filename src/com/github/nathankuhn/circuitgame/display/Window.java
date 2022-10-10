package com.github.nathankuhn.circuitgame.display;

class Window {

    private long handle;
    private int width;
    private int height;
    private boolean fullscreen;

    public Window(long handle, int width, int height) {
        this.handle = handle;
        this.width = width;
        this.height = height;
        this.fullscreen = false;
    }

    public long getHandle() {
        return handle;
    }

    public void setHandle(long handle) {
        this.handle = handle;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
