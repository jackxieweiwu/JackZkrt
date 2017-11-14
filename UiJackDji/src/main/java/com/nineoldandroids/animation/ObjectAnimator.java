package com.nineoldandroids.animation;

import android.view.View;

import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.AnimatorProxy.AnimatorProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jack_xie on 17-11-2.
 */

public final class ObjectAnimator extends ValueAnimator {
    private static final boolean DBG = false;
    private static final Map<String, Property> PROXY_PROPERTIES = new HashMap();
    private Object mTarget;
    private String mPropertyName;
    private Property mProperty;

    public void setPropertyName(String var1) {
        if(this.mValues != null) {
            PropertyValuesHolder var2 = this.mValues[0];
            String var3 = var2.getPropertyName();
            var2.setPropertyName(var1);
            this.mValuesMap.remove(var3);
            this.mValuesMap.put(var1, var2);
        }

        this.mPropertyName = var1;
        this.mInitialized = false;
    }

    public void setProperty(Property var1) {
        if(this.mValues != null) {
            PropertyValuesHolder var2 = this.mValues[0];
            String var3 = var2.getPropertyName();
            var2.setProperty(var1);
            this.mValuesMap.remove(var3);
            this.mValuesMap.put(this.mPropertyName, var2);
        }

        if(this.mProperty != null) {
            this.mPropertyName = var1.getName();
        }

        this.mProperty = var1;
        this.mInitialized = false;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    public ObjectAnimator() {
    }

    private ObjectAnimator(Object var1, String var2) {
        this.mTarget = var1;
        this.setPropertyName(var2);
    }

    private <T> ObjectAnimator(T var1, Property<T, ?> var2) {
        this.mTarget = var1;
        this.setProperty(var2);
    }

    public static ObjectAnimator ofInt(Object var0, String var1, int... var2) {
        ObjectAnimator var3 = new ObjectAnimator(var0, var1);
        var3.setIntValues(var2);
        return var3;
    }

    public static <T> ObjectAnimator ofInt(T var0, Property<T, Integer> var1, int... var2) {
        ObjectAnimator var3 = new ObjectAnimator(var0, var1);
        var3.setIntValues(var2);
        return var3;
    }

    public static ObjectAnimator ofFloat(Object var0, String var1, float... var2) {
        ObjectAnimator var3 = new ObjectAnimator(var0, var1);
        var3.setFloatValues(var2);
        return var3;
    }

    public static <T> ObjectAnimator ofFloat(T var0, Property<T, Float> var1, float... var2) {
        ObjectAnimator var3 = new ObjectAnimator(var0, var1);
        var3.setFloatValues(var2);
        return var3;
    }

    public static ObjectAnimator ofObject(Object var0, String var1, TypeEvaluator var2, Object... var3) {
        ObjectAnimator var4 = new ObjectAnimator(var0, var1);
        var4.setObjectValues(var3);
        var4.setEvaluator(var2);
        return var4;
    }

    public static <T, V> ObjectAnimator ofObject(T var0, Property<T, V> var1, TypeEvaluator<V> var2, V... var3) {
        ObjectAnimator var4 = new ObjectAnimator(var0, var1);
        var4.setObjectValues(var3);
        var4.setEvaluator(var2);
        return var4;
    }

    public static ObjectAnimator ofPropertyValuesHolder(Object var0, PropertyValuesHolder... var1) {
        ObjectAnimator var2 = new ObjectAnimator();
        var2.mTarget = var0;
        var2.setValues(var1);
        return var2;
    }

    public void setIntValues(int... var1) {
        if(this.mValues != null && this.mValues.length != 0) {
            super.setIntValues(var1);
        } else if(this.mProperty != null) {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(this.mProperty, var1)});
        } else {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(this.mPropertyName, var1)});
        }

    }

    public void setFloatValues(float... var1) {
        if(this.mValues != null && this.mValues.length != 0) {
            super.setFloatValues(var1);
        } else if(this.mProperty != null) {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(this.mProperty, var1)});
        } else {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(this.mPropertyName, var1)});
        }

    }

    public void setObjectValues(Object... var1) {
        if(this.mValues != null && this.mValues.length != 0) {
            super.setObjectValues(var1);
        } else if(this.mProperty != null) {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofObject(this.mProperty, (TypeEvaluator)null, var1)});
        } else {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofObject(this.mPropertyName, (TypeEvaluator)null, var1)});
        }

    }

    public void start() {
        super.start();
    }

    void initAnimation() {
        if(!this.mInitialized) {
            if(this.mProperty == null && AnimatorProxy.NEEDS_PROXY && this.mTarget instanceof View && PROXY_PROPERTIES.containsKey(this.mPropertyName)) {
                this.setProperty(PROXY_PROPERTIES.get(this.mPropertyName));
            }

            int var1 = this.mValues.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                this.mValues[var2].setupSetterAndGetter(this.mTarget);
            }

            super.initAnimation();
        }

    }

    public ObjectAnimator setDuration(long var1) {
        super.setDuration(var1);
        return this;
    }

    public Object getTarget() {
        return this.mTarget;
    }

    public void setTarget(Object var1) {
        if(this.mTarget != var1) {
            Object var2 = this.mTarget;
            this.mTarget = var1;
            if(var2 != null && var1 != null && var2.getClass() == var1.getClass()) {
                return;
            }

            this.mInitialized = false;
        }

    }

    public void setupStartValues() {
        this.initAnimation();
        int var1 = this.mValues.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            this.mValues[var2].setupStartValue(this.mTarget);
        }

    }

    public void setupEndValues() {
        this.initAnimation();
        int var1 = this.mValues.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            this.mValues[var2].setupEndValue(this.mTarget);
        }

    }

    void animateValue(float var1) {
        super.animateValue(var1);
        int var2 = this.mValues.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            this.mValues[var3].setAnimatedValue(this.mTarget);
        }

    }

    public ObjectAnimator clone() {
        ObjectAnimator var1 = (ObjectAnimator)super.clone();
        return var1;
    }

    public String toString() {
        String var1 = "ObjectAnimator@" + Integer.toHexString(this.hashCode()) + ", target " + this.mTarget;
        if(this.mValues != null) {
            for(int var2 = 0; var2 < this.mValues.length; ++var2) {
                var1 = var1 + "\n    " + this.mValues[var2].toString();
            }
        }

        return var1;
    }

    static {
        PROXY_PROPERTIES.put("alpha", PreHoneycombCompat.ALPHA);
        PROXY_PROPERTIES.put("pivotX", PreHoneycombCompat.PIVOT_X);
        PROXY_PROPERTIES.put("pivotY", PreHoneycombCompat.PIVOT_Y);
        PROXY_PROPERTIES.put("translationX", PreHoneycombCompat.TRANSLATION_X);
        PROXY_PROPERTIES.put("translationY", PreHoneycombCompat.TRANSLATION_Y);
        PROXY_PROPERTIES.put("rotation", PreHoneycombCompat.ROTATION);
        PROXY_PROPERTIES.put("rotationX", PreHoneycombCompat.ROTATION_X);
        PROXY_PROPERTIES.put("rotationY", PreHoneycombCompat.ROTATION_Y);
        PROXY_PROPERTIES.put("scaleX", PreHoneycombCompat.SCALE_X);
        PROXY_PROPERTIES.put("scaleY", PreHoneycombCompat.SCALE_Y);
        PROXY_PROPERTIES.put("scrollX", PreHoneycombCompat.SCROLL_X);
        PROXY_PROPERTIES.put("scrollY", PreHoneycombCompat.SCROLL_Y);
        PROXY_PROPERTIES.put("x", PreHoneycombCompat.X);
        PROXY_PROPERTIES.put("y", PreHoneycombCompat.Y);
    }
}

