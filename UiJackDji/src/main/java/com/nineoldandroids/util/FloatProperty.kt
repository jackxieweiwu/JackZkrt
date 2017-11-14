package com.nineoldandroids.util

/**
 * Created by jack_Xie on 17-11-2.
 */
abstract class FloatProperty<T>(var1: String) : Property<T, Float>(Float::class.java, var1) {

    abstract fun setValue(var1: T, var2: Float)

    override fun set(var1: T, var2: Float?) {
        this.setValue(var1, var2!!.toFloat())
    }
}