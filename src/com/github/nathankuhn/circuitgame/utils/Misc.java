package com.github.nathankuhn.circuitgame.utils;

import java.util.Random;

public class Misc {

    public static int minIndex(float[] values) {
        if (values.length == 0) return 0;
        float min = values[0];
        int ret = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
                ret = i;
            }
        }
        return ret;
    }

    // For computing perlin noise

    private static Vector2f RandomGradient(long x, long y) {
        Random random = new Random((y * 329847298) ^ (x * -1328473828));
        float a = (float) ((random.nextFloat()) * Math.PI * 2);
        return new Vector2f((float)Math.sin(a), (float)Math.cos(a));
    }

    private static float DotGridGradient(int x, int y, float xf, float yf) {
        Vector2f gradient = RandomGradient(x, y);
        float dx = xf - x;
        float dy = yf - y;

        return (dx * gradient.x + dy * gradient.y);
    }

    public static float PerlinNoise(float x, float y) {
        int x0 = (int)x;
        int x1 = x0 + 1;
        int y0 = (int)y;
        int y1 = y0 + 1;

        float sx = x - x0;
        float sy = y - y0;

        float n0, n1, ix0, ix1;
        n0 = DotGridGradient(x0, y0, x, y);
        n1 = DotGridGradient(x1, y0, x, y);
        ix0 = VectorMath.Lerp(n0, n1, sx);

        n0 = DotGridGradient(x0, y1, x, y);
        n1 = DotGridGradient(x1, y1, x, y);
        ix1 = VectorMath.Lerp(n0, n1, sx);

        return VectorMath.Lerp(ix0, ix1, sy);
    }

}
