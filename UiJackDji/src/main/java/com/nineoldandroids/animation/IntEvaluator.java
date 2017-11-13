package com.nineoldandroids.animation;

/**
 * Created by jack_Xie on 17-11-2.
 */

public class IntEvaluator implements TypeEvaluator<Integer> {
    public IntEvaluator() {
    }

    public Integer evaluate(float var1, Integer var2, Integer var3) {
        int var4 = var2.intValue();
        return Integer.valueOf((int)((float)var4 + var1 * (float)(var3.intValue() - var4)));
    }
}
