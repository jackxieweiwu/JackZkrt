package com.nineoldandroids.animation;

/**
 * Created by jack_Xie on 17-11-2.
 */

public class ArgbEvaluator implements TypeEvaluator {
    public ArgbEvaluator() {
    }

    public Object evaluate(float var1, Object var2, Object var3) {
        int var4 = ((Integer)var2).intValue();
        int var5 = var4 >> 24;
        int var6 = var4 >> 16 & 255;
        int var7 = var4 >> 8 & 255;
        int var8 = var4 & 255;
        int var9 = ((Integer)var3).intValue();
        int var10 = var9 >> 24;
        int var11 = var9 >> 16 & 255;
        int var12 = var9 >> 8 & 255;
        int var13 = var9 & 255;
        return Integer.valueOf(var5 + (int)(var1 * (float)(var10 - var5)) << 24 | var6 + (int)(var1 * (float)(var11 - var6)) << 16 | var7 + (int)(var1 * (float)(var12 - var7)) << 8 | var8 + (int)(var1 * (float)(var13 - var8)));
    }
}