package com.nineoldandroids.animation;

import android.view.View;

import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.AnimatorProxy.AnimatorProxy;

/**
 * Created by jack_xie on 17-11-2.
 */

final class PreHoneycombCompat {

    static Property<View, Float> ALPHA = new FloatProperty<View>("alpha") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setAlpha(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getAlpha());
        }
    };
    static Property<View, Float> PIVOT_X = new FloatProperty<View>("pivotX") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setPivotX(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getPivotX());
        }
    };
    static Property<View, Float> PIVOT_Y = new FloatProperty<View>("pivotY") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setPivotY(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getPivotY());
        }
    };
    static Property<View, Float> TRANSLATION_X = new FloatProperty<View>("translationX") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setTranslationX(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getTranslationX());
        }
    };
    static Property<View, Float> TRANSLATION_Y = new FloatProperty<View>("translationY") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setTranslationY(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getTranslationY());
        }
    };
    static Property<View, Float> ROTATION = new FloatProperty<View>("rotation") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setRotation(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getRotation());
        }
    };
    static Property<View, Float> ROTATION_X = new FloatProperty<View>("rotationX") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setRotationX(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getRotationX());
        }
    };
    static Property<View, Float> ROTATION_Y = new FloatProperty<View>("rotationY") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setRotationY(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getRotationY());
        }
    };
    static Property<View, Float> SCALE_X = new FloatProperty<View>("scaleX") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setScaleX(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getScaleX());
        }
    };
    static Property<View, Float> SCALE_Y = new FloatProperty<View>("scaleY") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setScaleY(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getScaleY());
        }
    };
    static Property<View, Integer> SCROLL_X = new IntProperty<View>("scrollX") {
        public void setValue(View var1, int var2) {
            AnimatorProxy.wrap(var1).setScrollX(var2);
        }

        public Integer get(View var1) {
            return Integer.valueOf(AnimatorProxy.wrap(var1).getScrollX());
        }
    };
    static Property<View, Integer> SCROLL_Y = new IntProperty<View>("scrollY") {
        public void setValue(View var1, int var2) {
            AnimatorProxy.wrap(var1).setScrollY(var2);
        }

        public Integer get(View var1) {
            return Integer.valueOf(AnimatorProxy.wrap(var1).getScrollY());
        }
    };
    static Property<View, Float> X = new FloatProperty<View>("x") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setX(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getX());
        }
    };
    static Property<View, Float> Y = new FloatProperty<View>("y") {
        public void setValue(View var1, float var2) {
            AnimatorProxy.wrap(var1).setY(var2);
        }

        public Float get(View var1) {
            return Float.valueOf(AnimatorProxy.wrap(var1).getY());
        }
    };

    private PreHoneycombCompat() {
    }
}

