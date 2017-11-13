package com.nineoldandroids.animation;

import android.util.Log;

import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jack_xie on 17-11-2.
 */

public class PropertyValuesHolder implements Cloneable {
    String mPropertyName;
    protected Property mProperty;
    Method mSetter;
    private Method mGetter;
    Class mValueType;
    KeyframeSet mKeyframeSet;
    private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
    private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
    private static Class[] FLOAT_VARIANTS;
    private static Class[] INTEGER_VARIANTS;
    private static Class[] DOUBLE_VARIANTS;
    private static final HashMap<Class, HashMap<String, Method>> sSetterPropertyMap;
    private static final HashMap<Class, HashMap<String, Method>> sGetterPropertyMap;
    final ReentrantReadWriteLock mPropertyMapLock;
    final Object[] mTmpValueArray;
    private TypeEvaluator mEvaluator;
    private Object mAnimatedValue;

    private PropertyValuesHolder(String var1) {
        this.mSetter = null;
        this.mGetter = null;
        this.mKeyframeSet = null;
        this.mPropertyMapLock = new ReentrantReadWriteLock();
        this.mTmpValueArray = new Object[1];
        this.mPropertyName = var1;
    }

    private PropertyValuesHolder(Property var1) {
        this.mSetter = null;
        this.mGetter = null;
        this.mKeyframeSet = null;
        this.mPropertyMapLock = new ReentrantReadWriteLock();
        this.mTmpValueArray = new Object[1];
        this.mProperty = var1;
        if(var1 != null) {
            this.mPropertyName = var1.getName();
        }

    }

    public static PropertyValuesHolder ofInt(String var0, int... var1) {
        return new PropertyValuesHolder.IntPropertyValuesHolder(var0, var1);
    }

    public static PropertyValuesHolder ofInt(Property<?, Integer> var0, int... var1) {
        return new PropertyValuesHolder.IntPropertyValuesHolder(var0, var1);
    }

    public static PropertyValuesHolder ofFloat(String var0, float... var1) {
        return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, var1);
    }

    public static PropertyValuesHolder ofFloat(Property<?, Float> var0, float... var1) {
        return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, var1);
    }

    public static PropertyValuesHolder ofObject(String var0, TypeEvaluator var1, Object... var2) {
        PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
        var3.setObjectValues(var2);
        var3.setEvaluator(var1);
        return var3;
    }

    public static <V> PropertyValuesHolder ofObject(Property var0, TypeEvaluator<V> var1, V... var2) {
        PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
        var3.setObjectValues(var2);
        var3.setEvaluator(var1);
        return var3;
    }

    public static PropertyValuesHolder ofKeyframe(String var0, Keyframe... var1) {
        KeyframeSet var2 = KeyframeSet.ofKeyframe(var1);
        if(var2 instanceof IntKeyframeSet) {
            return new PropertyValuesHolder.IntPropertyValuesHolder(var0, (IntKeyframeSet)var2);
        } else if(var2 instanceof FloatKeyframeSet) {
            return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, (FloatKeyframeSet)var2);
        } else {
            PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
            var3.mKeyframeSet = var2;
            var3.mValueType = var1[0].getType();
            return var3;
        }
    }

    public static PropertyValuesHolder ofKeyframe(Property var0, Keyframe... var1) {
        KeyframeSet var2 = KeyframeSet.ofKeyframe(var1);
        if(var2 instanceof IntKeyframeSet) {
            return new PropertyValuesHolder.IntPropertyValuesHolder(var0, (IntKeyframeSet)var2);
        } else if(var2 instanceof FloatKeyframeSet) {
            return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, (FloatKeyframeSet)var2);
        } else {
            PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
            var3.mKeyframeSet = var2;
            var3.mValueType = var1[0].getType();
            return var3;
        }
    }

    public void setIntValues(int... var1) {
        this.mValueType = Integer.TYPE;
        this.mKeyframeSet = KeyframeSet.ofInt(var1);
    }

    public void setFloatValues(float... var1) {
        this.mValueType = Float.TYPE;
        this.mKeyframeSet = KeyframeSet.ofFloat(var1);
    }

    public void setKeyframes(Keyframe... var1) {
        int var2 = var1.length;
        Keyframe[] var3 = new Keyframe[Math.max(var2, 2)];
        this.mValueType = var1[0].getType();

        for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = var1[var4];
        }

        this.mKeyframeSet = new KeyframeSet(var3);
    }

    public void setObjectValues(Object... var1) {
        this.mValueType = var1[0].getClass();
        this.mKeyframeSet = KeyframeSet.ofObject(var1);
    }

    private Method getPropertyFunction(Class var1, String var2, Class var3) {
        Method var4 = null;
        String var5 = getMethodName(var2, this.mPropertyName);
        Class[] var6 = null;
        if(var3 == null) {
            try {
                var4 = var1.getMethod(var5, var6);
            } catch (NoSuchMethodException var15) {
                try {
                    var4 = var1.getDeclaredMethod(var5, var6);
                    var4.setAccessible(true);
                } catch (NoSuchMethodException var14) {
                    Log.e("PropertyValuesHolder", "Couldn\'t find no-arg method for property " + this.mPropertyName + ": " + var15);
                }
            }
        } else {
            var6 = new Class[1];
            Class[] var7;
            if(this.mValueType.equals(Float.class)) {
                var7 = FLOAT_VARIANTS;
            } else if(this.mValueType.equals(Integer.class)) {
                var7 = INTEGER_VARIANTS;
            } else if(this.mValueType.equals(Double.class)) {
                var7 = DOUBLE_VARIANTS;
            } else {
                var7 = new Class[]{this.mValueType};
            }

            Class[] var8 = var7;
            int var9 = var7.length;
            int var10 = 0;

            while(true) {
                if(var10 >= var9) {
                    Log.e("PropertyValuesHolder", "Couldn\'t find setter/getter for property " + this.mPropertyName + " with value type " + this.mValueType);
                    break;
                }

                Class var11 = var8[var10];
                var6[0] = var11;

                try {
                    var4 = var1.getMethod(var5, var6);
                    this.mValueType = var11;
                    return var4;
                } catch (NoSuchMethodException var17) {
                    try {
                        var4 = var1.getDeclaredMethod(var5, var6);
                        var4.setAccessible(true);
                        this.mValueType = var11;
                        return var4;
                    } catch (NoSuchMethodException var16) {
                        ++var10;
                    }
                }
            }
        }

        return var4;
    }

    private Method setupSetterOrGetter(Class var1, HashMap<Class, HashMap<String, Method>> var2, String var3, Class var4) {
        Method var5 = null;

        try {
            this.mPropertyMapLock.writeLock().lock();
            HashMap var6 = (HashMap)var2.get(var1);
            if(var6 != null) {
                var5 = (Method)var6.get(this.mPropertyName);
            }

            if(var5 == null) {
                var5 = this.getPropertyFunction(var1, var3, var4);
                if(var6 == null) {
                    var6 = new HashMap();
                    var2.put(var1, var6);
                }

                var6.put(this.mPropertyName, var5);
            }
        } finally {
            this.mPropertyMapLock.writeLock().unlock();
        }

        return var5;
    }

    void setupSetter(Class var1) {
        this.mSetter = this.setupSetterOrGetter(var1, sSetterPropertyMap, "set", this.mValueType);
    }

    private void setupGetter(Class var1) {
        this.mGetter = this.setupSetterOrGetter(var1, sGetterPropertyMap, "get", (Class)null);
    }

    void setupSetterAndGetter(Object var1) {
        Iterator var3;
        Keyframe var4;
        if(this.mProperty != null) {
            try {
                this.mProperty.get(var1);
                var3 = this.mKeyframeSet.mKeyframes.iterator();

                while(var3.hasNext()) {
                    var4 = (Keyframe)var3.next();
                    if(!var4.hasValue()) {
                        var4.setValue(this.mProperty.get(var1));
                    }
                }

                return;
            } catch (ClassCastException var8) {
                Log.e("PropertyValuesHolder", "No such property (" + this.mProperty.getName() + ") on target object " + var1 + ". Trying reflection instead");
                this.mProperty = null;
            }
        }

        Class var2 = var1.getClass();
        if(this.mSetter == null) {
            this.setupSetter(var2);
        }

        var3 = this.mKeyframeSet.mKeyframes.iterator();

        while(var3.hasNext()) {
            var4 = (Keyframe)var3.next();
            if(!var4.hasValue()) {
                if(this.mGetter == null) {
                    this.setupGetter(var2);
                }

                try {
                    var4.setValue(this.mGetter.invoke(var1, new Object[0]));
                } catch (InvocationTargetException var6) {
                    Log.e("PropertyValuesHolder", var6.toString());
                } catch (IllegalAccessException var7) {
                    Log.e("PropertyValuesHolder", var7.toString());
                }
            }
        }

    }

    private void setupValue(Object var1, Keyframe var2) {
        if(this.mProperty != null) {
            var2.setValue(this.mProperty.get(var1));
        }

        try {
            if(this.mGetter == null) {
                Class var3 = var1.getClass();
                this.setupGetter(var3);
            }

            var2.setValue(this.mGetter.invoke(var1, new Object[0]));
        } catch (InvocationTargetException var4) {
            Log.e("PropertyValuesHolder", var4.toString());
        } catch (IllegalAccessException var5) {
            Log.e("PropertyValuesHolder", var5.toString());
        }

    }

    void setupStartValue(Object var1) {
        this.setupValue(var1, (Keyframe)this.mKeyframeSet.mKeyframes.get(0));
    }

    void setupEndValue(Object var1) {
        this.setupValue(var1, (Keyframe)this.mKeyframeSet.mKeyframes.get(this.mKeyframeSet.mKeyframes.size() - 1));
    }

    public PropertyValuesHolder clone() {
        try {
            PropertyValuesHolder var1 = (PropertyValuesHolder)super.clone();
            var1.mPropertyName = this.mPropertyName;
            var1.mProperty = this.mProperty;
            var1.mKeyframeSet = this.mKeyframeSet.clone();
            var1.mEvaluator = this.mEvaluator;
            return var1;
        } catch (CloneNotSupportedException var2) {
            return null;
        }
    }

    void setAnimatedValue(Object var1) {
        if(this.mProperty != null) {
            this.mProperty.set(var1, this.getAnimatedValue());
        }

        if(this.mSetter != null) {
            try {
                this.mTmpValueArray[0] = this.getAnimatedValue();
                this.mSetter.invoke(var1, this.mTmpValueArray);
            } catch (InvocationTargetException var3) {
                Log.e("PropertyValuesHolder", var3.toString());
            } catch (IllegalAccessException var4) {
                Log.e("PropertyValuesHolder", var4.toString());
            }
        }

    }

    void init() {
        if(this.mEvaluator == null) {
            this.mEvaluator = this.mValueType == Integer.class?sIntEvaluator:(this.mValueType == Float.class?sFloatEvaluator:null);
        }

        if(this.mEvaluator != null) {
            this.mKeyframeSet.setEvaluator(this.mEvaluator);
        }

    }

    public void setEvaluator(TypeEvaluator var1) {
        this.mEvaluator = var1;
        this.mKeyframeSet.setEvaluator(var1);
    }

    void calculateValue(float var1) {
        this.mAnimatedValue = this.mKeyframeSet.getValue(var1);
    }

    public void setPropertyName(String var1) {
        this.mPropertyName = var1;
    }

    public void setProperty(Property var1) {
        this.mProperty = var1;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    Object getAnimatedValue() {
        return this.mAnimatedValue;
    }

    public String toString() {
        return this.mPropertyName + ": " + this.mKeyframeSet.toString();
    }

    static String getMethodName(String var0, String var1) {
        if(var1 != null && var1.length() != 0) {
            char var2 = Character.toUpperCase(var1.charAt(0));
            String var3 = var1.substring(1);
            return var0 + var2 + var3;
        } else {
            return var0;
        }
    }

    static {
        FLOAT_VARIANTS = new Class[]{Float.TYPE, Float.class, Double.TYPE, Integer.TYPE, Double.class, Integer.class};
        INTEGER_VARIANTS = new Class[]{Integer.TYPE, Integer.class, Float.TYPE, Double.TYPE, Float.class, Double.class};
        DOUBLE_VARIANTS = new Class[]{Double.TYPE, Double.class, Float.TYPE, Integer.TYPE, Float.class, Integer.class};
        sSetterPropertyMap = new HashMap();
        sGetterPropertyMap = new HashMap();
    }

    static class FloatPropertyValuesHolder extends PropertyValuesHolder {
        private FloatProperty mFloatProperty;
        FloatKeyframeSet mFloatKeyframeSet;
        float mFloatAnimatedValue;

        public FloatPropertyValuesHolder(String var1, FloatKeyframeSet var2) {
            super(var1);
            this.mValueType = Float.TYPE;
            this.mKeyframeSet = var2;
            this.mFloatKeyframeSet = (FloatKeyframeSet)this.mKeyframeSet;
        }

        public FloatPropertyValuesHolder(Property var1, FloatKeyframeSet var2) {
            super(var1);
            this.mValueType = Float.TYPE;
            this.mKeyframeSet = var2;
            this.mFloatKeyframeSet = (FloatKeyframeSet)this.mKeyframeSet;
            if(var1 instanceof FloatProperty) {
                this.mFloatProperty = (FloatProperty)this.mProperty;
            }

        }

        public FloatPropertyValuesHolder(String var1, float... var2) {
            super(var1);
            this.setFloatValues(var2);
        }

        public FloatPropertyValuesHolder(Property var1, float... var2) {
            super(var1);
            this.setFloatValues(var2);
            if(var1 instanceof FloatProperty) {
                this.mFloatProperty = (FloatProperty)this.mProperty;
            }

        }

        public void setFloatValues(float... var1) {
            super.setFloatValues(var1);
            this.mFloatKeyframeSet = (FloatKeyframeSet)this.mKeyframeSet;
        }

        void calculateValue(float var1) {
            this.mFloatAnimatedValue = this.mFloatKeyframeSet.getFloatValue(var1);
        }

        Object getAnimatedValue() {
            return Float.valueOf(this.mFloatAnimatedValue);
        }

        public PropertyValuesHolder.FloatPropertyValuesHolder clone() {
            PropertyValuesHolder.FloatPropertyValuesHolder var1 = (PropertyValuesHolder.FloatPropertyValuesHolder)super.clone();
            var1.mFloatKeyframeSet = (FloatKeyframeSet)var1.mKeyframeSet;
            return var1;
        }

        void setAnimatedValue(Object var1) {
            if(this.mFloatProperty != null) {
                this.mFloatProperty.setValue(var1, this.mFloatAnimatedValue);
            } else if(this.mProperty != null) {
                this.mProperty.set(var1, Float.valueOf(this.mFloatAnimatedValue));
            } else {
                if(this.mSetter != null) {
                    try {
                        this.mTmpValueArray[0] = Float.valueOf(this.mFloatAnimatedValue);
                        this.mSetter.invoke(var1, this.mTmpValueArray);
                    } catch (InvocationTargetException var3) {
                        Log.e("PropertyValuesHolder", var3.toString());
                    } catch (IllegalAccessException var4) {
                        Log.e("PropertyValuesHolder", var4.toString());
                    }
                }

            }
        }

        void setupSetter(Class var1) {
            if(this.mProperty == null) {
                super.setupSetter(var1);
            }
        }
    }

    static class IntPropertyValuesHolder extends PropertyValuesHolder {
        private IntProperty mIntProperty;
        IntKeyframeSet mIntKeyframeSet;
        int mIntAnimatedValue;

        public IntPropertyValuesHolder(String var1, IntKeyframeSet var2) {
            super(var1);
            this.mValueType = Integer.TYPE;
            this.mKeyframeSet = var2;
            this.mIntKeyframeSet = (IntKeyframeSet)this.mKeyframeSet;
        }

        public IntPropertyValuesHolder(Property var1, IntKeyframeSet var2) {
            super(var1);
            this.mValueType = Integer.TYPE;
            this.mKeyframeSet = var2;
            this.mIntKeyframeSet = (IntKeyframeSet)this.mKeyframeSet;
            if(var1 instanceof IntProperty) {
                this.mIntProperty = (IntProperty)this.mProperty;
            }

        }

        public IntPropertyValuesHolder(String var1, int... var2) {
            super(var1);
            this.setIntValues(var2);
        }

        public IntPropertyValuesHolder(Property var1, int... var2) {
            super(var1);
            this.setIntValues(var2);
            if(var1 instanceof IntProperty) {
                this.mIntProperty = (IntProperty)this.mProperty;
            }

        }

        public void setIntValues(int... var1) {
            super.setIntValues(var1);
            this.mIntKeyframeSet = (IntKeyframeSet)this.mKeyframeSet;
        }

        void calculateValue(float var1) {
            this.mIntAnimatedValue = this.mIntKeyframeSet.getIntValue(var1);
        }

        Object getAnimatedValue() {
            return Integer.valueOf(this.mIntAnimatedValue);
        }

        public PropertyValuesHolder.IntPropertyValuesHolder clone() {
            PropertyValuesHolder.IntPropertyValuesHolder var1 = (PropertyValuesHolder.IntPropertyValuesHolder)super.clone();
            var1.mIntKeyframeSet = (IntKeyframeSet)var1.mKeyframeSet;
            return var1;
        }

        void setAnimatedValue(Object var1) {
            if(this.mIntProperty != null) {
                this.mIntProperty.setValue(var1, this.mIntAnimatedValue);
            } else if(this.mProperty != null) {
                this.mProperty.set(var1, Integer.valueOf(this.mIntAnimatedValue));
            } else {
                if(this.mSetter != null) {
                    try {
                        this.mTmpValueArray[0] = Integer.valueOf(this.mIntAnimatedValue);
                        this.mSetter.invoke(var1, this.mTmpValueArray);
                    } catch (InvocationTargetException var3) {
                        Log.e("PropertyValuesHolder", var3.toString());
                    } catch (IllegalAccessException var4) {
                        Log.e("PropertyValuesHolder", var4.toString());
                    }
                }

            }
        }

        void setupSetter(Class var1) {
            if(this.mProperty == null) {
                super.setupSetter(var1);
            }
        }
    }
}

