package com.nineoldandroids.view;

import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.AnimatorProxy.AnimatorProxy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jack_xie on 17-11-2.
 */

class ViewPropertyAnimatorPreHC extends ViewPropertyAnimator {
    private final AnimatorProxy mProxy;
    private final WeakReference<View> mView;
    private long mDuration;
    private boolean mDurationSet = false;
    private long mStartDelay = 0L;
    private boolean mStartDelaySet = false;
    private Interpolator mInterpolator;
    private boolean mInterpolatorSet = false;
    private Animator.AnimatorListener mListener = null;
    private ViewPropertyAnimatorPreHC.AnimatorEventListener mAnimatorEventListener = new ViewPropertyAnimatorPreHC.AnimatorEventListener();
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
            ViewPropertyAnimatorPreHC.this.startAnimation();
        }
    };
    private HashMap<Animator, PropertyBundle> mAnimatorMap = new HashMap();

    ViewPropertyAnimatorPreHC(View var1) {
        this.mView = new WeakReference(var1);
        this.mProxy = AnimatorProxy.wrap(var1);
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
        View var5 = (View)this.mView.get();
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
            ViewPropertyAnimatorPreHC.NameValuesHolder var6 = (ViewPropertyAnimatorPreHC.NameValuesHolder)var2.get(var5);
            var3 |= var6.mNameConstant;
        }

        this.mAnimatorMap.put(var1, new ViewPropertyAnimatorPreHC.PropertyBundle(var3, var2));
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
                ViewPropertyAnimatorPreHC.PropertyBundle var8 = (ViewPropertyAnimatorPreHC.PropertyBundle)this.mAnimatorMap.get(var7);
                if(var8.cancel(var1) && var8.mPropertyMask == 0) {
                    var4 = var7;
                    break;
                }
            }

            if(var4 != null) {
                var4.cancel();
            }
        }

        ViewPropertyAnimatorPreHC.NameValuesHolder var9 = new ViewPropertyAnimatorPreHC.NameValuesHolder(var1, var2, var3);
        this.mPendingAnimations.add(var9);
        View var10 = (View)this.mView.get();
        if(var10 != null) {
            var10.removeCallbacks(this.mAnimationStarter);
            var10.post(this.mAnimationStarter);
        }

    }

    private void setValue(int var1, float var2) {
        switch(var1) {
            case 1:
                this.mProxy.setTranslationX(var2);
                break;
            case 2:
                this.mProxy.setTranslationY(var2);
                break;
            case 4:
                this.mProxy.setScaleX(var2);
                break;
            case 8:
                this.mProxy.setScaleY(var2);
                break;
            case 16:
                this.mProxy.setRotation(var2);
                break;
            case 32:
                this.mProxy.setRotationX(var2);
                break;
            case 64:
                this.mProxy.setRotationY(var2);
                break;
            case 128:
                this.mProxy.setX(var2);
                break;
            case 256:
                this.mProxy.setY(var2);
                break;
            case 512:
                this.mProxy.setAlpha(var2);
        }

    }

    private float getValue(int var1) {
        switch(var1) {
            case 1:
                return this.mProxy.getTranslationX();
            case 2:
                return this.mProxy.getTranslationY();
            case 4:
                return this.mProxy.getScaleX();
            case 8:
                return this.mProxy.getScaleY();
            case 16:
                return this.mProxy.getRotation();
            case 32:
                return this.mProxy.getRotationX();
            case 64:
                return this.mProxy.getRotationY();
            case 128:
                return this.mProxy.getX();
            case 256:
                return this.mProxy.getY();
            case 512:
                return this.mProxy.getAlpha();
            default:
                return 0.0F;
        }
    }

    private class AnimatorEventListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        private AnimatorEventListener() {
        }

        public void onAnimationStart(Animator var1) {
            if(ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationStart(var1);
            }

        }

        public void onAnimationCancel(Animator var1) {
            if(ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationCancel(var1);
            }

        }

        public void onAnimationRepeat(Animator var1) {
            if(ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationRepeat(var1);
            }

        }

        public void onAnimationEnd(Animator var1) {
            if(ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationEnd(var1);
            }

            ViewPropertyAnimatorPreHC.this.mAnimatorMap.remove(var1);
            if(ViewPropertyAnimatorPreHC.this.mAnimatorMap.isEmpty()) {
                ViewPropertyAnimatorPreHC.this.mListener = null;
            }

        }

        public void onAnimationUpdate(ValueAnimator var1) {
            float var2 = var1.getAnimatedFraction();
            ViewPropertyAnimatorPreHC.PropertyBundle var3 = (ViewPropertyAnimatorPreHC.PropertyBundle)ViewPropertyAnimatorPreHC.this.mAnimatorMap.get(var1);
            int var4 = var3.mPropertyMask;
            if((var4 & 511) != 0) {
                View var5 = (View)ViewPropertyAnimatorPreHC.this.mView.get();
                if(var5 != null) {
                    var5.invalidate();
                }
            }

            ArrayList var10 = var3.mNameValuesHolder;
            if(var10 != null) {
                int var6 = var10.size();

                for(int var7 = 0; var7 < var6; ++var7) {
                    ViewPropertyAnimatorPreHC.NameValuesHolder var8 = (ViewPropertyAnimatorPreHC.NameValuesHolder)var10.get(var7);
                    float var9 = var8.mFromValue + var2 * var8.mDeltaValue;
                    ViewPropertyAnimatorPreHC.this.setValue(var8.mNameConstant, var9);
                }
            }

            View var11 = (View)ViewPropertyAnimatorPreHC.this.mView.get();
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
        ArrayList<ViewPropertyAnimatorPreHC.NameValuesHolder> mNameValuesHolder;

        PropertyBundle(int var1, ArrayList<ViewPropertyAnimatorPreHC.NameValuesHolder> var2) {
            this.mPropertyMask = var1;
            this.mNameValuesHolder = var2;
        }

        boolean cancel(int var1) {
            if((this.mPropertyMask & var1) != 0 && this.mNameValuesHolder != null) {
                int var2 = this.mNameValuesHolder.size();

                for(int var3 = 0; var3 < var2; ++var3) {
                    ViewPropertyAnimatorPreHC.NameValuesHolder var4 = (ViewPropertyAnimatorPreHC.NameValuesHolder)this.mNameValuesHolder.get(var3);
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

