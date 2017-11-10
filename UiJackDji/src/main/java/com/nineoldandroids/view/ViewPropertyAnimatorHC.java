package com.nineoldandroids.view;

import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jack_xie on 17-11-2.
 */

class ViewPropertyAnimatorHC extends ViewPropertyAnimator {
    private final WeakReference<View> mView;
    private long mDuration;
    private boolean mDurationSet = false;
    private long mStartDelay = 0L;
    private boolean mStartDelaySet = false;
    private Interpolator mInterpolator;
    private boolean mInterpolatorSet = false;
    private Animator.AnimatorListener mListener = null;
    private ViewPropertyAnimatorHC.AnimatorEventListener mAnimatorEventListener = new ViewPropertyAnimatorHC.AnimatorEventListener();
    ArrayList<NameValuesHolder> mPendingAnimations = new ArrayList();
    private static final int NONE = 0;
    private static final int TRANSLATION_X = 1;
    private static final int TRANSLATION_Y = 2;
    private static final int SCALE_X = 4;
    private static final int SCALE_Y = 8;
    private static final int ROTATION = 16;
    private static final int ROTATION_X = 32;
    private static final int ROTATION_Y = 64;
    private static final int X = 128;
    private static final int Y = 256;
    private static final int ALPHA = 512;
    private static final int TRANSFORM_MASK = 511;
    private Runnable mAnimationStarter = new Runnable() {
        public void run() {
            ViewPropertyAnimatorHC.this.startAnimation();
        }
    };
    private HashMap<Animator, PropertyBundle> mAnimatorMap = new HashMap();

    ViewPropertyAnimatorHC(View var1) {
        this.mView = new WeakReference(var1);
    }

    public ViewPropertyAnimator setDuration(long var1) {
        if(var1 < 0L) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + var1);
        } else {
            this.mDurationSet = true;
            this.mDuration = var1;
            return this;
        }
    }

    public long getDuration() {
        return this.mDurationSet?this.mDuration:(new ValueAnimator()).getDuration();
    }

    public long getStartDelay() {
        return this.mStartDelaySet?this.mStartDelay:0L;
    }

    public ViewPropertyAnimator setStartDelay(long var1) {
        if(var1 < 0L) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + var1);
        } else {
            this.mStartDelaySet = true;
            this.mStartDelay = var1;
            return this;
        }
    }

    public ViewPropertyAnimator setInterpolator(Interpolator var1) {
        this.mInterpolatorSet = true;
        this.mInterpolator = var1;
        return this;
    }

    public ViewPropertyAnimator setListener(Animator.AnimatorListener var1) {
        this.mListener = var1;
        return this;
    }

    public void start() {
        this.startAnimation();
    }

    public void cancel() {
        if(this.mAnimatorMap.size() > 0) {
            HashMap var1 = (HashMap)this.mAnimatorMap.clone();
            Set var2 = var1.keySet();
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
                Animator var4 = (Animator)var3.next();
                var4.cancel();
            }
        }

        this.mPendingAnimations.clear();
        View var5 = this.mView.get();
        if(var5 != null) {
            var5.removeCallbacks(this.mAnimationStarter);
        }

    }

    public ViewPropertyAnimator x(float var1) {
        this.animateProperty(128, var1);
        return this;
    }

    public ViewPropertyAnimator xBy(float var1) {
        this.animatePropertyBy(128, var1);
        return this;
    }

    public ViewPropertyAnimator y(float var1) {
        this.animateProperty(256, var1);
        return this;
    }

    public ViewPropertyAnimator yBy(float var1) {
        this.animatePropertyBy(256, var1);
        return this;
    }

    public ViewPropertyAnimator rotation(float var1) {
        this.animateProperty(16, var1);
        return this;
    }

    public ViewPropertyAnimator rotationBy(float var1) {
        this.animatePropertyBy(16, var1);
        return this;
    }

    public ViewPropertyAnimator rotationX(float var1) {
        this.animateProperty(32, var1);
        return this;
    }

    public ViewPropertyAnimator rotationXBy(float var1) {
        this.animatePropertyBy(32, var1);
        return this;
    }

    public ViewPropertyAnimator rotationY(float var1) {
        this.animateProperty(64, var1);
        return this;
    }

    public ViewPropertyAnimator rotationYBy(float var1) {
        this.animatePropertyBy(64, var1);
        return this;
    }

    public ViewPropertyAnimator translationX(float var1) {
        this.animateProperty(1, var1);
        return this;
    }

    public ViewPropertyAnimator translationXBy(float var1) {
        this.animatePropertyBy(1, var1);
        return this;
    }

    public ViewPropertyAnimator translationY(float var1) {
        this.animateProperty(2, var1);
        return this;
    }

    public ViewPropertyAnimator translationYBy(float var1) {
        this.animatePropertyBy(2, var1);
        return this;
    }

    public ViewPropertyAnimator scaleX(float var1) {
        this.animateProperty(4, var1);
        return this;
    }

    public ViewPropertyAnimator scaleXBy(float var1) {
        this.animatePropertyBy(4, var1);
        return this;
    }

    public ViewPropertyAnimator scaleY(float var1) {
        this.animateProperty(8, var1);
        return this;
    }

    public ViewPropertyAnimator scaleYBy(float var1) {
        this.animatePropertyBy(8, var1);
        return this;
    }

    public ViewPropertyAnimator alpha(float var1) {
        this.animateProperty(512, var1);
        return this;
    }

    public ViewPropertyAnimator alphaBy(float var1) {
        this.animatePropertyBy(512, var1);
        return this;
    }

    private void startAnimation() {
        ValueAnimator var1 = ValueAnimator.ofFloat(new float[]{1.0F});
        ArrayList var2 = (ArrayList)this.mPendingAnimations.clone();
        this.mPendingAnimations.clear();
        int var3 = 0;
        int var4 = var2.size();

        for(int var5 = 0; var5 < var4; ++var5) {
            ViewPropertyAnimatorHC.NameValuesHolder var6 = (ViewPropertyAnimatorHC.NameValuesHolder)var2.get(var5);
            var3 |= var6.mNameConstant;
        }

        this.mAnimatorMap.put(var1, new ViewPropertyAnimatorHC.PropertyBundle(var3, var2));
        var1.addUpdateListener(this.mAnimatorEventListener);
        var1.addListener(this.mAnimatorEventListener);
        if(this.mStartDelaySet) {
            var1.setStartDelay(this.mStartDelay);
        }

        if(this.mDurationSet) {
            var1.setDuration(this.mDuration);
        }

        if(this.mInterpolatorSet) {
            var1.setInterpolator(this.mInterpolator);
        }

        var1.start();
    }

    private void animateProperty(int var1, float var2) {
        float var3 = this.getValue(var1);
        float var4 = var2 - var3;
        this.animatePropertyBy(var1, var3, var4);
    }

    private void animatePropertyBy(int var1, float var2) {
        float var3 = this.getValue(var1);
        this.animatePropertyBy(var1, var3, var2);
    }

    private void animatePropertyBy(int var1, float var2, float var3) {
        if(this.mAnimatorMap.size() > 0) {
            Animator var4 = null;
            Set var5 = this.mAnimatorMap.keySet();
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
                Animator var7 = (Animator)var6.next();
                ViewPropertyAnimatorHC.PropertyBundle var8 = this.mAnimatorMap.get(var7);
                if(var8.cancel(var1) && var8.mPropertyMask == 0) {
                    var4 = var7;
                    break;
                }
            }

            if(var4 != null) {
                var4.cancel();
            }
        }

        ViewPropertyAnimatorHC.NameValuesHolder var9 = new ViewPropertyAnimatorHC.NameValuesHolder(var1, var2, var3);
        this.mPendingAnimations.add(var9);
        View var10 = this.mView.get();
        if(var10 != null) {
            var10.removeCallbacks(this.mAnimationStarter);
            var10.post(this.mAnimationStarter);
        }

    }

    private void setValue(int var1, float var2) {
        View var3 = this.mView.get();
        if(var3 != null) {
            switch(var1) {
                case 1:
                    var3.setTranslationX(var2);
                    break;
                case 2:
                    var3.setTranslationY(var2);
                    break;
                case 4:
                    var3.setScaleX(var2);
                    break;
                case 8:
                    var3.setScaleY(var2);
                    break;
                case 16:
                    var3.setRotation(var2);
                    break;
                case 32:
                    var3.setRotationX(var2);
                    break;
                case 64:
                    var3.setRotationY(var2);
                    break;
                case 128:
                    var3.setX(var2);
                    break;
                case 256:
                    var3.setY(var2);
                    break;
                case 512:
                    var3.setAlpha(var2);
            }
        }

    }

    private float getValue(int var1) {
        View var2 = this.mView.get();
        if(var2 != null) {
            switch(var1) {
                case 1:
                    return var2.getTranslationX();
                case 2:
                    return var2.getTranslationY();
                case 4:
                    return var2.getScaleX();
                case 8:
                    return var2.getScaleY();
                case 16:
                    return var2.getRotation();
                case 32:
                    return var2.getRotationX();
                case 64:
                    return var2.getRotationY();
                case 128:
                    return var2.getX();
                case 256:
                    return var2.getY();
                case 512:
                    return var2.getAlpha();
            }
        }

        return 0.0F;
    }

    private class AnimatorEventListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        private AnimatorEventListener() {
        }

        public void onAnimationStart(Animator var1) {
            if(ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationStart(var1);
            }

        }

        public void onAnimationCancel(Animator var1) {
            if(ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationCancel(var1);
            }

        }

        public void onAnimationRepeat(Animator var1) {
            if(ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationRepeat(var1);
            }

        }

        public void onAnimationEnd(Animator var1) {
            if(ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationEnd(var1);
            }

            ViewPropertyAnimatorHC.this.mAnimatorMap.remove(var1);
            if(ViewPropertyAnimatorHC.this.mAnimatorMap.isEmpty()) {
                ViewPropertyAnimatorHC.this.mListener = null;
            }

        }

        public void onAnimationUpdate(ValueAnimator var1) {
            float var2 = var1.getAnimatedFraction();
            ViewPropertyAnimatorHC.PropertyBundle var3 = ViewPropertyAnimatorHC.this.mAnimatorMap.get(var1);
            int var4 = var3.mPropertyMask;
            if((var4 & 511) != 0) {
                View var5 = ViewPropertyAnimatorHC.this.mView.get();
                if(var5 != null) {
                    var5.invalidate();
                }
            }

            ArrayList var10 = var3.mNameValuesHolder;
            if(var10 != null) {
                int var6 = var10.size();

                for(int var7 = 0; var7 < var6; ++var7) {
                    ViewPropertyAnimatorHC.NameValuesHolder var8 = (ViewPropertyAnimatorHC.NameValuesHolder)var10.get(var7);
                    float var9 = var8.mFromValue + var2 * var8.mDeltaValue;
                    ViewPropertyAnimatorHC.this.setValue(var8.mNameConstant, var9);
                }
            }

            View var11 = ViewPropertyAnimatorHC.this.mView.get();
            if(var11 != null) {
                var11.invalidate();
            }

        }
    }

    private static class NameValuesHolder {
        int mNameConstant;
        float mFromValue;
        float mDeltaValue;

        NameValuesHolder(int var1, float var2, float var3) {
            this.mNameConstant = var1;
            this.mFromValue = var2;
            this.mDeltaValue = var3;
        }
    }

    private static class PropertyBundle {
        int mPropertyMask;
        ArrayList<ViewPropertyAnimatorHC.NameValuesHolder> mNameValuesHolder;

        PropertyBundle(int var1, ArrayList<ViewPropertyAnimatorHC.NameValuesHolder> var2) {
            this.mPropertyMask = var1;
            this.mNameValuesHolder = var2;
        }

        boolean cancel(int var1) {
            if((this.mPropertyMask & var1) != 0 && this.mNameValuesHolder != null) {
                int var2 = this.mNameValuesHolder.size();

                for(int var3 = 0; var3 < var2; ++var3) {
                    ViewPropertyAnimatorHC.NameValuesHolder var4 = this.mNameValuesHolder.get(var3);
                    if(var4.mNameConstant == var1) {
                        this.mNameValuesHolder.remove(var3);
                        this.mPropertyMask &= ~var1;
                        return true;
                    }
                }
            }

            return false;
        }
    }
}

