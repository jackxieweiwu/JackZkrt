package com.nineoldandroids.animation

/**
 * Created by root on 17-11-2.
 */
interface TypeEvaluator<T> {
    fun evaluate(var1: Float, var2: T, var3: T): T
}