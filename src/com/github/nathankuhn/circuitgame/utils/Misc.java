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

}
