package com.nineoldandroids.view;

import android.os.Build;
import android.view.View;
import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import java.util.WeakHashMap;

/**
 * Created by jack_xie on 17-11-2.
 */

public abstract class ViewPropertyAnimator {
    private static final WeakHashMap<View, ViewPropertyAnimator> ANIMATORS = new WeakHashMap(0);

    public ViewPropertyAnimator() {
    }

    public static ViewPropertyAnimator animate(View var0) {
        Object var1 = ANIMATORS.get(var0);
        if(var1 == null) {
            int var2 = Integer.valueOf(Build.VERSION.SDK).intValue();
            if(var2 >= 14) {
                var1 = new ViewPropertyAnimatorICS(var0);
            } else if(var2 >= 11) {
                var1 = new ViewPropertyAnimatorHC(var0);
            } else {
                var1 = new ViewPropertyAnimatorPreHC(var0);
            }
            ANIMATORS.put(var0, (ViewPropertyAnimator) var1);
        }
        return (ViewPropertyAnimator)var1;
    }

    public abstract ViewPropertyAnimator setDuration(long var1);

    public abstract long getDuration();

    public abstract long getStartDelay();

    public abstract ViewPropertyAnimator setStartDelay(long var1);

    public abstract ViewPropertyAnimator setInterpolator(Interpolator var1);

    public abstract ViewPropertyAnimator setListener(Animator.AnimatorListener var1);

    public abstract void start();

    public abstract void cancel();

    public abstract ViewPropertyAnimator x(float var1);

    public abstract ViewPropertyAnimator xBy(float var1);

    public abstract ViewPropertyAnimator y(float var1);

    public abstract ViewPropertyAnimator yBy(float var1);

    public abstract ViewPropertyAnimator rotation(float var1);

    public abstract ViewPropertyAnimator rotationBy(float var1);

    public abstract ViewPropertyAnimator rotationX(float var1);

    public abstract ViewPropertyAnimator rotationXBy(float var1);

    public abstract ViewPropertyAnimator rotationY(float var1);

    public abstract ViewPropertyAnimator rotationYBy(float var1);

    public abstract ViewPropertyAnimator translationX(float var1);

    public abstract ViewPropertyAnimator translationXBy(float var1);

    public abstract ViewPropertyAnimator translationY(float var1);

    public abstract ViewPropertyAnimator translationYBy(float var1);

    public abstract ViewPropertyAnimator scaleX(float var1);

    public abstract ViewPropertyAnimator scaleXBy(float var1);

    public abstract ViewPropertyAnimator scaleY(float var1);

    public abstract ViewPropertyAnimator scaleYBy(float var1);

    public abstract ViewPropertyAnimator alpha(float var1);

    public abstract ViewPropertyAnimator alphaBy(float var1);
}
