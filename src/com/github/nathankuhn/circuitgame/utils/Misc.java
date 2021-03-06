package com.github.nathankuhn.circuitgame.utils;

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

    private static Vector2f RandomGradient(int x, int y, int seed) {
        int rand = Noise(x, y, seed);
        float randFloat = (float) rand / Integer.MAX_VALUE;
        float a = (float) (randFloat * Math.PI * 2);
        return new Vector2f((float)Math.sin(a), (float)Math.cos(a));
    }

    private static float DotGridGradient(int x, int y, float xf, float yf, int seed) {
        Vector2f gradient = RandomGradient(x, y, seed);
        float dx = xf - x;
        float dy = yf - y;

        return (dx * gradient.x + dy * gradient.y);
    }

    public static float PerlinNoise(float x, float y, int seed) {
        int x0 = (int)x;
        int x1 = x0 + 1;
        int y0 = (int)y;
        int y1 = y0 + 1;

        float sx = x - x0;
        float sy = y - y0;

        float n0, n1, ix0, ix1;
        n0 = DotGridGradient(x0, y0, x, y, seed);
        n1 = DotGridGradient(x1, y0, x, y, seed);
        ix0 = VectorMath.Lerp(n0, n1, sx);

        n0 = DotGridGradient(x0, y1, x, y, seed);
        n1 = DotGridGradient(x1, y1, x, y, seed);
        ix1 = VectorMath.Lerp(n0, n1, sx);

        return VectorMath.Lerp(ix0, ix1, sy);
    }

    public static int Mod(int a, int b) {
        return (a % b + b) % b;
    }

    public static int Hash32Shift(int key)
    {
        key = ~key + (key << 15);
        key = key ^ (key >>> 12);
        key = key + (key << 2);
        key = key ^ (key >>> 4);
        key = key * 2057;
        key = key ^ (key >>> 16);
        return key;
    }

    public static int Noise(int x, int y, int seed)
    {
        return Hash32Shift(seed+ Hash32Shift(x+ Hash32Shift(y)));
    }

    public static String FindValue(String line, String value) {
        int start = line.indexOf(value) + value.length();
        int end = line.indexOf(' ', start);
        if (end == -1) {
            return line.substring(start);
        }
        return line.substring(start, end);
    }

    public static int Floor(float a) {
        if (a >= 0) {
            return (int)a;
        }
        return (int)a - 1;
    }

    public static float Abs(float a) {
        return (a > 0) ? a : -a;
    }
}
