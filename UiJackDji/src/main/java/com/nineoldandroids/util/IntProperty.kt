package com.nineoldandroids.util

/**
 * Created by jack_xie on 17-11-2.
 */
abstract class IntProperty<T>(var1: String) : Property<T, Int>(Int::class.java, var1) {

    abstract fun setValue(var1: T, var2: Int)

    override fun set(var1: T, var2: Int?) {
        this.set(var1, Integer.valueOf(var2!!.toInt()))
    }
}