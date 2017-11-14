package com.nineoldandroids.animation

/**
 * Created by jack_xie on 17-11-2.
 */
class FloatEvaluator : TypeEvaluator<Number> {

    override fun evaluate(var1: Float, var2: Number, var3: Number): Float {
        val var4 = var2.toFloat()
        return java.lang.Float.valueOf(var4 + var1 * (var3.toFloat() - var4))
    }
}