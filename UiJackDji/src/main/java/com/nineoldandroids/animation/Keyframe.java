package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

/**
 * Created by jack_xie on 17-11-2.
 */

public abstract class Keyframe implements Cloneable {
    float mFraction;
    Class mValueType;
    private Interpolator mInterpolator = null;
    boolean mHasValue = false;

    public Keyframe() {
    }

    public static Keyframe ofInt(float var0, int var1) {
        return new Keyframe.IntKeyframe(var0, var1);
    }

    public static Keyframe ofInt(float var0) {
        return new Keyframe.IntKeyframe(var0);
    }

    public static Keyframe ofFloat(float var0, float var1) {
        return new Keyframe.FloatKeyframe(var0, var1);
    }

    public static Keyframe ofFloat(float var0) {
        return new Keyframe.FloatKeyframe(var0);
    }

    public static Keyframe ofObject(float var0, Object var1) {
        return new Keyframe.ObjectKeyframe(var0, var1);
    }

    public static Keyframe ofObject(float var0) {
        return new Keyframe.ObjectKeyframe(var0, (Object)null);
    }

    public boolean hasValue() {
        return this.mHasValue;
    }

    public abstract Object getValue();

    public abstract void setValue(Object var1);

    public float getFraction() {
        return this.mFraction;
    }

    public void setFraction(float var1) {
        this.mFraction = var1;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setInterpolator(Interpolator var1) {
        this.mInterpolator = var1;
    }

    public Class getType() {
        return this.mValueType;
    }

    public abstract Keyframe clone();

    static class FloatKeyframe extends Keyframe {
        float mValue;

        FloatKeyframe(float var1, float var2) {
            this.mFraction = var1;
            this.mValue = var2;
            this.mValueType = Float.TYPE;
            this.mHasValue = true;
        }

        FloatKeyframe(float var1) {
            this.mFraction = var1;
            this.mValueType = Float.TYPE;
        }

        public float getFloatValue() {
            return this.mValue;
        }

        public Object getValue() {
            return Float.valueOf(this.mValue);
        }

        public void setValue(Object var1) {
            if(var1 != null && var1.getClass() == Float.class) {
                this.mValue = ((Float)var1).floatValue();
                this.mHasValue = true;
            }

        }

        public Keyframe.FloatKeyframe clone() {
            Keyframe.FloatKeyframe var1 = new Keyframe.FloatKeyframe(this.getFraction(), this.mValue);
            var1.setInterpolator(this.getInterpolator());
            return var1;
        }
    }

    static class IntKeyframe extends Keyframe {
        int mValue;

        IntKeyframe(float var1, int var2) {
            this.mFraction = var1;
            this.mValue = var2;
            this.mValueType = Integer.TYPE;
            this.mHasValue = true;
        }

        IntKeyframe(float var1) {
            this.mFraction = var1;
            this.mValueType = Integer.TYPE;
        }

        public int getIntValue() {
            return this.mValue;
        }

        public Object getValue() {
            return Integer.valueOf(this.mValue);
        }

        public void setValue(Object var1) {
            if(var1 != null && var1.getClass() == Integer.class) {
                this.mValue = ((Integer)var1).intValue();
                this.mHasValue = true;
            }

        }

        public Keyframe.IntKeyframe clone() {
            Keyframe.IntKeyframe var1 = new Keyframe.IntKeyframe(this.getFraction(), this.mValue);
            var1.setInterpolator(this.getInterpolator());
            return var1;
        }
    }

    static class ObjectKeyframe extends Keyframe {
        Object mValue;

        ObjectKeyframe(float var1, Object var2) {
            this.mFraction = var1;
            this.mValue = var2;
            this.mHasValue = var2 != null;
            this.mValueType = this.mHasValue?var2.getClass():Object.class;
        }

        public Object getValue() {
            return this.mValue;
        }

        public void setValue(Object var1) {
            this.mValue = var1;
            this.mHasValue = var1 != null;
        }

        public Keyframe.ObjectKeyframe clone() {
            Keyframe.ObjectKeyframe var1 = new Keyframe.ObjectKeyframe(this.getFraction(), this.mValue);
            var1.setInterpolator(this.getInterpolator());
            return var1;
        }
    }
}

