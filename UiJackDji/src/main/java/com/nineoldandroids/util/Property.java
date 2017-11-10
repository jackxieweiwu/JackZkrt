package com.nineoldandroids.util;

/**
 * Created by jack_xie on 17-11-2.
 */

public abstract class Property<T, V> {
    private final String mName;
    private final Class<V> mType;

    public static <T, V> Property<T, V> of(Class<T> var0, Class<V> var1, String var2) {
        return new ReflectiveProperty(var0, var1, var2);
    }

    public Property(Class<V> var1, String var2) {
        this.mName = var2;
        this.mType = var1;
    }

    public boolean isReadOnly() {
        return false;
    }

    public void set(T var1, V var2) {
        throw new UnsupportedOperationException("Property " + this.getName() + " is read-only");
    }

    public abstract V get(T var1);

    public String getName() {
        return this.mName;
    }

    public Class<V> getType() {
        return this.mType;
    }
}