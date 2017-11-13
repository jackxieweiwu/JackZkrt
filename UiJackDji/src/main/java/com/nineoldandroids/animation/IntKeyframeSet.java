package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

import java.util.ArrayList;

/**
 * Created by jack_Xie on 17-11-2.
 */

class IntKeyframeSet extends KeyframeSet {
    private int firstValue;
    private int lastValue;
    private int deltaValue;
    private boolean firstTime = true;

    public IntKeyframeSet(Keyframe.IntKeyframe... var1) {
        super(var1);
    }

    public Object getValue(float var1) {
        return Integer.valueOf(this.getIntValue(var1));
    }

    public IntKeyframeSet clone() {
        ArrayList var1 = this.mKeyframes;
        int var2 = this.mKeyframes.size();
        Keyframe.IntKeyframe[] var3 = new Keyframe.IntKeyframe[var2];

        for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = (Keyframe.IntKeyframe)((Keyframe)var1.get(var4)).clone();
        }

        IntKeyframeSet var5 = new IntKeyframeSet(var3);
        return var5;
    }

    public int getIntValue(float var1) {
        if(this.mNumKeyframes == 2) {
            if(this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((Keyframe.IntKeyframe)this.mKeyframes.get(0)).getIntValue();
                this.lastValue = ((Keyframe.IntKeyframe)this.mKeyframes.get(1)).getIntValue();
                this.deltaValue = this.lastValue - this.firstValue;
            }

            if(this.mInterpolator != null) {
                var1 = this.mInterpolator.getInterpolation(var1);
            }

            return this.mEvaluator == null?this.firstValue + (int)(var1 * (float)this.deltaValue):((Number)this.mEvaluator.evaluate(var1, Integer.valueOf(this.firstValue), Integer.valueOf(this.lastValue))).intValue();
        } else {
            Keyframe.IntKeyframe var2;
            float var6;
            float var9;
            Keyframe.IntKeyframe var10;
            int var11;
            int var12;
            float var13;
            Interpolator var14;
            if(var1 <= 0.0F) {
                var2 = (Keyframe.IntKeyframe)this.mKeyframes.get(0);
                var10 = (Keyframe.IntKeyframe)this.mKeyframes.get(1);
                var11 = var2.getIntValue();
                var12 = var10.getIntValue();
                var6 = var2.getFraction();
                var13 = var10.getFraction();
                var14 = var10.getInterpolator();
                if(var14 != null) {
                    var1 = var14.getInterpolation(var1);
                }

                var9 = (var1 - var6) / (var13 - var6);
                return this.mEvaluator == null?var11 + (int)(var9 * (float)(var12 - var11)):((Number)this.mEvaluator.evaluate(var9, Integer.valueOf(var11), Integer.valueOf(var12))).intValue();
            } else if(var1 >= 1.0F) {
                var2 = (Keyframe.IntKeyframe)this.mKeyframes.get(this.mNumKeyframes - 2);
                var10 = (Keyframe.IntKeyframe)this.mKeyframes.get(this.mNumKeyframes - 1);
                var11 = var2.getIntValue();
                var12 = var10.getIntValue();
                var6 = var2.getFraction();
                var13 = var10.getFraction();
                var14 = var10.getInterpolator();
                if(var14 != null) {
                    var1 = var14.getInterpolation(var1);
                }

                var9 = (var1 - var6) / (var13 - var6);
                return this.mEvaluator == null?var11 + (int)(var9 * (float)(var12 - var11)):((Number)this.mEvaluator.evaluate(var9, Integer.valueOf(var11), Integer.valueOf(var12))).intValue();
            } else {
                var2 = (Keyframe.IntKeyframe)this.mKeyframes.get(0);

                for(int var3 = 1; var3 < this.mNumKeyframes; ++var3) {
                    Keyframe.IntKeyframe var4 = (Keyframe.IntKeyframe)this.mKeyframes.get(var3);
                    if(var1 < var4.getFraction()) {
                        Interpolator var5 = var4.getInterpolator();
                        if(var5 != null) {
                            var1 = var5.getInterpolation(var1);
                        }

                        var6 = (var1 - var2.getFraction()) / (var4.getFraction() - var2.getFraction());
                        int var7 = var2.getIntValue();
                        int var8 = var4.getIntValue();
                        return this.mEvaluator == null?var7 + (int)(var6 * (float)(var8 - var7)):((Number)this.mEvaluator.evaluate(var6, Integer.valueOf(var7), Integer.valueOf(var8))).intValue();
                    }

                    var2 = var4;
                }

                return ((Number)((Keyframe)this.mKeyframes.get(this.mNumKeyframes - 1)).getValue()).intValue();
            }
        }
    }
}

