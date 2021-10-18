package com.github.nathankuhn.circuitgame.utils;

import com.github.nathankuhn.circuitgame.display.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {

    public static final String IMAGE_PATH = "res/textures/";
    public static final String MISSING_TEXTURE = "MissingTexture.png";

    private int width;
    private int height;
    private byte[] data;

    public Texture(int width, int height, byte[] data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new byte[width * height * 4];
    }

    public ByteBuffer getBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2f getIdealDimensions(Vector2i windowDimensions) {
        return new Vector2f(
                (float) width / (float) windowDimensions.y * 2,
                (float) height / (float) windowDimensions.y * 2
        );
    }

    public static Texture LoadPNG(String filename) {
        File image = new File(IMAGE_PATH + filename);
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(image);
        } catch (IOException e) {
            image = new File(IMAGE_PATH + MISSING_TEXTURE);
            try {
                bufferedImage = ImageIO.read(image);
            } catch (IOException mte) {
                System.err.println("Could not find missing texture image");
                mte.printStackTrace();
                return new Texture(1, 1, new byte[] {0,0,0,0});
            }
        }

        int[] argb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        byte[] rgba = intARGBtoByteRGBA(argb);

        return new Texture(bufferedImage.getWidth(), bufferedImage.getHeight(), rgba);
    }

    public static Texture SingleColor(Color color) {
        byte r = (byte) (color.r * 255);
        byte g = (byte) (color.g * 255);
        byte b = (byte) (color.b * 255);
        byte a = (byte) (color.a * 255);
        return new Texture(1, 1, new byte[] {r, g, b, a});
    }

    public static byte[] intARGBtoByteRGBA(int[] argb) {
        byte[] rgba = new byte[argb.length * 4];

        for (int i = 0; i < argb.length; i++) {
            rgba[4 * i] = (byte) ((argb[i] >> 16) & 0xff);
            rgba[4 * i + 1] = (byte) ((argb[i] >> 8) & 0xff);
            rgba[4 * i + 2] = (byte) ((argb[i]) & 0xff);
            rgba[4 * i + 3] = (byte) ((argb[i] >> 24) & 0xff);
        }

        return rgba;
    }

}
