package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

import java.util.ArrayList;

/**
 * Created by jack_xie on 17-11-2.
 */

class FloatKeyframeSet extends KeyframeSet {
    private float firstValue;
    private float lastValue;
    private float deltaValue;
    private boolean firstTime = true;

    public FloatKeyframeSet(Keyframe.FloatKeyframe... var1) {
        super(var1);
    }

    public Object getValue(float var1) {
        return Float.valueOf(this.getFloatValue(var1));
    }

    public FloatKeyframeSet clone() {
        ArrayList var1 = this.mKeyframes;
        int var2 = this.mKeyframes.size();
        Keyframe.FloatKeyframe[] var3 = new Keyframe.FloatKeyframe[var2];

        for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = (Keyframe.FloatKeyframe)((Keyframe)var1.get(var4)).clone();
        }

        FloatKeyframeSet var5 = new FloatKeyframeSet(var3);
        return var5;
    }

    public float getFloatValue(float var1) {
        if(this.mNumKeyframes == 2) {
            if(this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((Keyframe.FloatKeyframe)this.mKeyframes.get(0)).getFloatValue();
                this.lastValue = ((Keyframe.FloatKeyframe)this.mKeyframes.get(1)).getFloatValue();
                this.deltaValue = this.lastValue - this.firstValue;
            }

            if(this.mInterpolator != null) {
                var1 = this.mInterpolator.getInterpolation(var1);
            }

            return this.mEvaluator == null?this.firstValue + var1 * this.deltaValue:((Number)this.mEvaluator.evaluate(var1, Float.valueOf(this.firstValue), Float.valueOf(this.lastValue))).floatValue();
        } else {
            Keyframe.FloatKeyframe var2;
            float var6;
            float var7;
            float var9;
            Keyframe.FloatKeyframe var10;
            float var11;
            float var12;
            Interpolator var13;
            if(var1 <= 0.0F) {
                var2 = (Keyframe.FloatKeyframe)this.mKeyframes.get(0);
                var10 = (Keyframe.FloatKeyframe)this.mKeyframes.get(1);
                var11 = var2.getFloatValue();
                var12 = var10.getFloatValue();
                var6 = var2.getFraction();
                var7 = var10.getFraction();
                var13 = var10.getInterpolator();
                if(var13 != null) {
                    var1 = var13.getInterpolation(var1);
                }

                var9 = (var1 - var6) / (var7 - var6);
                return this.mEvaluator == null?var11 + var9 * (var12 - var11):((Number)this.mEvaluator.evaluate(var9, Float.valueOf(var11), Float.valueOf(var12))).floatValue();
            } else if(var1 >= 1.0F) {
                var2 = (Keyframe.FloatKeyframe)this.mKeyframes.get(this.mNumKeyframes - 2);
                var10 = (Keyframe.FloatKeyframe)this.mKeyframes.get(this.mNumKeyframes - 1);
                var11 = var2.getFloatValue();
                var12 = var10.getFloatValue();
                var6 = var2.getFraction();
                var7 = var10.getFraction();
                var13 = var10.getInterpolator();
                if(var13 != null) {
                    var1 = var13.getInterpolation(var1);
                }

                var9 = (var1 - var6) / (var7 - var6);
                return this.mEvaluator == null?var11 + var9 * (var12 - var11):((Number)this.mEvaluator.evaluate(var9, Float.valueOf(var11), Float.valueOf(var12))).floatValue();
            } else {
                var2 = (Keyframe.FloatKeyframe)this.mKeyframes.get(0);

                for(int var3 = 1; var3 < this.mNumKeyframes; ++var3) {
                    Keyframe.FloatKeyframe var4 = (Keyframe.FloatKeyframe)this.mKeyframes.get(var3);
                    if(var1 < var4.getFraction()) {
                        Interpolator var5 = var4.getInterpolator();
                        if(var5 != null) {
                            var1 = var5.getInterpolation(var1);
                        }

                        var6 = (var1 - var2.getFraction()) / (var4.getFraction() - var2.getFraction());
                        var7 = var2.getFloatValue();
                        float var8 = var4.getFloatValue();
                        return this.mEvaluator == null?var7 + var6 * (var8 - var7):((Number)this.mEvaluator.evaluate(var6, Float.valueOf(var7), Float.valueOf(var8))).floatValue();
                    }

                    var2 = var4;
                }

                return ((Number)((Keyframe)this.mKeyframes.get(this.mNumKeyframes - 1)).getValue()).floatValue();
            }
        }
    }
}

