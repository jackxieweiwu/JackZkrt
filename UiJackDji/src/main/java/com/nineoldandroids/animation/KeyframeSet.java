package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jack_xie on 17-11-2.
 */

class KeyframeSet {
    int mNumKeyframes;
    Keyframe mFirstKeyframe;
    Keyframe mLastKeyframe;
    Interpolator mInterpolator;
    ArrayList<Keyframe> mKeyframes;
    TypeEvaluator mEvaluator;

    public KeyframeSet(Keyframe... var1) {
        this.mNumKeyframes = var1.length;
        this.mKeyframes = new ArrayList();
        this.mKeyframes.addAll(Arrays.asList(var1));
        this.mFirstKeyframe = (Keyframe)this.mKeyframes.get(0);
        this.mLastKeyframe = (Keyframe)this.mKeyframes.get(this.mNumKeyframes - 1);
        this.mInterpolator = this.mLastKeyframe.getInterpolator();
    }

    public static KeyframeSet ofInt(int... var0) {
        int var1 = var0.length;
        Keyframe.IntKeyframe[] var2 = new Keyframe.IntKeyframe[Math.max(var1, 2)];
        if(var1 == 1) {
            var2[0] = (Keyframe.IntKeyframe)Keyframe.ofInt(0.0F);
            var2[1] = (Keyframe.IntKeyframe)Keyframe.ofInt(1.0F, var0[0]);
        } else {
            var2[0] = (Keyframe.IntKeyframe)Keyframe.ofInt(0.0F, var0[0]);

            for(int var3 = 1; var3 < var1; ++var3) {
                var2[var3] = (Keyframe.IntKeyframe)Keyframe.ofInt((float)var3 / (float)(var1 - 1), var0[var3]);
            }
        }

        return new IntKeyframeSet(var2);
    }

    public static KeyframeSet ofFloat(float... var0) {
        int var1 = var0.length;
        Keyframe.FloatKeyframe[] var2 = new Keyframe.FloatKeyframe[Math.max(var1, 2)];
        if(var1 == 1) {
            var2[0] = (Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F);
            var2[1] = (Keyframe.FloatKeyframe)Keyframe.ofFloat(1.0F, var0[0]);
        } else {
            var2[0] = (Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F, var0[0]);

            for(int var3 = 1; var3 < var1; ++var3) {
                var2[var3] = (Keyframe.FloatKeyframe)Keyframe.ofFloat((float)var3 / (float)(var1 - 1), var0[var3]);
            }
        }

        return new FloatKeyframeSet(var2);
    }

    public static KeyframeSet ofKeyframe(Keyframe... var0) {
        int var1 = var0.length;
        boolean var2 = false;
        boolean var3 = false;
        boolean var4 = false;

        for(int var5 = 0; var5 < var1; ++var5) {
            if(var0[var5] instanceof Keyframe.FloatKeyframe) {
                var2 = true;
            } else if(var0[var5] instanceof Keyframe.IntKeyframe) {
                var3 = true;
            } else {
                var4 = true;
            }
        }

        int var6;
        if(var2 && !var3 && !var4) {
            Keyframe.FloatKeyframe[] var8 = new Keyframe.FloatKeyframe[var1];

            for(var6 = 0; var6 < var1; ++var6) {
                var8[var6] = (Keyframe.FloatKeyframe)var0[var6];
            }

            return new FloatKeyframeSet(var8);
        } else if(var3 && !var2 && !var4) {
            Keyframe.IntKeyframe[] var7 = new Keyframe.IntKeyframe[var1];

            for(var6 = 0; var6 < var1; ++var6) {
                var7[var6] = (Keyframe.IntKeyframe)var0[var6];
            }

            return new IntKeyframeSet(var7);
        } else {
            return new KeyframeSet(var0);
        }
    }

    public static KeyframeSet ofObject(Object... var0) {
        int var1 = var0.length;
        Keyframe.ObjectKeyframe[] var2 = new Keyframe.ObjectKeyframe[Math.max(var1, 2)];
        if(var1 == 1) {
            var2[0] = (Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F);
            var2[1] = (Keyframe.ObjectKeyframe)Keyframe.ofObject(1.0F, var0[0]);
        } else {
            var2[0] = (Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F, var0[0]);

            for(int var3 = 1; var3 < var1; ++var3) {
                var2[var3] = (Keyframe.ObjectKeyframe)Keyframe.ofObject((float)var3 / (float)(var1 - 1), var0[var3]);
            }
        }

        return new KeyframeSet(var2);
    }

    public void setEvaluator(TypeEvaluator var1) {
        this.mEvaluator = var1;
    }

    public KeyframeSet clone() {
        ArrayList var1 = this.mKeyframes;
        int var2 = this.mKeyframes.size();
        Keyframe[] var3 = new Keyframe[var2];

        for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = ((Keyframe)var1.get(var4)).clone();
        }

        KeyframeSet var5 = new KeyframeSet(var3);
        return var5;
    }

    public Object getValue(float var1) {
        if(this.mNumKeyframes == 2) {
            if(this.mInterpolator != null) {
                var1 = this.mInterpolator.getInterpolation(var1);
            }

            return this.mEvaluator.evaluate(var1, this.mFirstKeyframe.getValue(), this.mLastKeyframe.getValue());
        } else {
            Keyframe var2;
            Interpolator var8;
            float var9;
            float var10;
            if(var1 <= 0.0F) {
                var2 = (Keyframe)this.mKeyframes.get(1);
                var8 = var2.getInterpolator();
                if(var8 != null) {
                    var1 = var8.getInterpolation(var1);
                }

                var9 = this.mFirstKeyframe.getFraction();
                var10 = (var1 - var9) / (var2.getFraction() - var9);
                return this.mEvaluator.evaluate(var10, this.mFirstKeyframe.getValue(), var2.getValue());
            } else if(var1 >= 1.0F) {
                var2 = (Keyframe)this.mKeyframes.get(this.mNumKeyframes - 2);
                var8 = this.mLastKeyframe.getInterpolator();
                if(var8 != null) {
                    var1 = var8.getInterpolation(var1);
                }

                var9 = var2.getFraction();
                var10 = (var1 - var9) / (this.mLastKeyframe.getFraction() - var9);
                return this.mEvaluator.evaluate(var10, var2.getValue(), this.mLastKeyframe.getValue());
            } else {
                var2 = this.mFirstKeyframe;

                for(int var3 = 1; var3 < this.mNumKeyframes; ++var3) {
                    Keyframe var4 = (Keyframe)this.mKeyframes.get(var3);
                    if(var1 < var4.getFraction()) {
                        Interpolator var5 = var4.getInterpolator();
                        if(var5 != null) {
                            var1 = var5.getInterpolation(var1);
                        }

                        float var6 = var2.getFraction();
                        float var7 = (var1 - var6) / (var4.getFraction() - var6);
                        return this.mEvaluator.evaluate(var7, var2.getValue(), var4.getValue());
                    }

                    var2 = var4;
                }

                return this.mLastKeyframe.getValue();
            }
        }
    }

    public String toString() {
        String var1 = " ";

        for(int var2 = 0; var2 < this.mNumKeyframes; ++var2) {
            var1 = var1 + ((Keyframe)this.mKeyframes.get(var2)).getValue() + "  ";
        }

        return var1;
    }
}

