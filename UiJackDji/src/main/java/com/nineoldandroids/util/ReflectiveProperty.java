package com.nineoldandroids.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jack_xie on 17-11-2.
 */

class ReflectiveProperty<T, V> extends Property<T, V> {
    private static final String PREFIX_GET = "get";
    private static final String PREFIX_IS = "is";
    private static final String PREFIX_SET = "set";
    private Method mSetter;
    private Method mGetter;
    private Field mField;

    public ReflectiveProperty(Class<T> var1, Class<V> var2, String var3) {
        super(var2, var3);
        char var4 = Character.toUpperCase(var3.charAt(0));
        String var5 = var3.substring(1);
        String var6 = var4 + var5;
        String var7 = "get" + var6;

        try {
            this.mGetter = var1.getMethod(var7, (Class[])null);
        } catch (NoSuchMethodException var18) {
            try {
                this.mGetter = var1.getDeclaredMethod(var7, (Class[])null);
                this.mGetter.setAccessible(true);
            } catch (NoSuchMethodException var17) {
                var7 = "is" + var6;

                try {
                    this.mGetter = var1.getMethod(var7, (Class[])null);
                } catch (NoSuchMethodException var16) {
                    try {
                        this.mGetter = var1.getDeclaredMethod(var7, (Class[])null);
                        this.mGetter.setAccessible(true);
                    } catch (NoSuchMethodException var15) {
                        try {
                            this.mField = var1.getField(var3);
                            Class var12 = this.mField.getType();
                            if(!this.typesMatch(var2, var12)) {
                                throw new NoSuchPropertyException("Underlying type (" + var12 + ") " + "does not match Property type (" + var2 + ")");
                            }

                            return;
                        } catch (NoSuchFieldException var13) {
                            throw new NoSuchPropertyException("No accessor method or field found for property with name " + var3);
                        }
                    }
                }
            }
        }

        Class var8 = this.mGetter.getReturnType();
        if(!this.typesMatch(var2, var8)) {
            throw new NoSuchPropertyException("Underlying type (" + var8 + ") " + "does not match Property type (" + var2 + ")");
        } else {
            String var9 = "set" + var6;

            try {
                this.mSetter = var1.getDeclaredMethod(var9, new Class[]{var8});
                this.mSetter.setAccessible(true);
            } catch (NoSuchMethodException var14) {
                ;
            }

        }
    }

    private boolean typesMatch(Class<V> var1, Class var2) {
        return var2 != var1?(!var2.isPrimitive()?false:var2 == Float.TYPE && var1 == Float.class || var2 == Integer.TYPE && var1 == Integer.class || var2 == Boolean.TYPE && var1 == Boolean.class || var2 == Long.TYPE && var1 == Long.class || var2 == Double.TYPE && var1 == Double.class || var2 == Short.TYPE && var1 == Short.class || var2 == Byte.TYPE && var1 == Byte.class || var2 == Character.TYPE && var1 == Character.class):true;
    }

    public void set(T var1, V var2) {
        if(this.mSetter != null) {
            try {
                this.mSetter.invoke(var1, new Object[]{var2});
            } catch (IllegalAccessException var5) {
                throw new AssertionError();
            } catch (InvocationTargetException var6) {
                throw new RuntimeException(var6.getCause());
            }
        } else {
            if(this.mField == null) {
                throw new UnsupportedOperationException("Property " + this.getName() + " is read-only");
            }

            try {
                this.mField.set(var1, var2);
            } catch (IllegalAccessException var4) {
                throw new AssertionError();
            }
        }

    }

    public V get(T var1) {
        if(this.mGetter != null) {
            try {
                return (V) this.mGetter.invoke(var1, (Object[])null);
            } catch (IllegalAccessException var3) {
                throw new AssertionError();
            } catch (InvocationTargetException var4) {
                throw new RuntimeException(var4.getCause());
            }
        } else if(this.mField != null) {
            try {
                return (V) this.mField.get(var1);
            } catch (IllegalAccessException var5) {
                throw new AssertionError();
            }
        } else {
            throw new AssertionError();
        }
    }

    public boolean isReadOnly() {
        return this.mSetter == null && this.mField == null;
    }
}

