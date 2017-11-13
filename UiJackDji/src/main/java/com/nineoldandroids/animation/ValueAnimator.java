package com.nineoldandroids.animation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AndroidRuntimeException;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by root on 17-11-2.
 */

public class ValueAnimator extends Animator {
    private static final long DEFAULT_FRAME_DELAY = 10L;
    static final int ANIMATION_START = 0;
    static final int ANIMATION_FRAME = 1;
    static final int STOPPED = 0;
    static final int RUNNING = 1;
    static final int SEEKED = 2;
    long mStartTime;
    long mSeekTime = -1L;
    private static ThreadLocal<ValueAnimator.AnimationHandler> sAnimationHandler = new ThreadLocal();
    private static final ThreadLocal<ArrayList<ValueAnimator>> sAnimations = new ThreadLocal() {
        protected ArrayList<ValueAnimator> initialValue() {
            return new ArrayList();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sPendingAnimations = new ThreadLocal() {
        protected ArrayList<ValueAnimator> initialValue() {
            return new ArrayList();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sDelayedAnims = new ThreadLocal() {
        protected ArrayList<ValueAnimator> initialValue() {
            return new ArrayList();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sEndingAnims = new ThreadLocal() {
        protected ArrayList<ValueAnimator> initialValue() {
            return new ArrayList();
        }
    };
    private static final ThreadLocal<ArrayList<ValueAnimator>> sReadyAnims = new ThreadLocal() {
        protected ArrayList<ValueAnimator> initialValue() {
            return new ArrayList();
        }
    };
    private static final Interpolator sDefaultInterpolator = new AccelerateDecelerateInterpolator();
    private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
    private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
    private boolean mPlayingBackwards = false;
    private int mCurrentIteration = 0;
    private float mCurrentFraction = 0.0F;
    private boolean mStartedDelay = false;
    private long mDelayStartTime;
    int mPlayingState = 0;
    private boolean mRunning = false;
    private boolean mStarted = false;
    boolean mInitialized = false;
    private long mDuration = 300L;
    private long mStartDelay = 0L;
    private static long sFrameDelay = 10L;
    private int mRepeatCount = 0;
    private int mRepeatMode = 1;
    private Interpolator mInterpolator;
    private ArrayList<ValueAnimator.AnimatorUpdateListener> mUpdateListeners;
    PropertyValuesHolder[] mValues;
    HashMap<String, PropertyValuesHolder> mValuesMap;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int INFINITE = -1;

    public ValueAnimator() {
        this.mInterpolator = sDefaultInterpolator;
        this.mUpdateListeners = null;
    }

    public static ValueAnimator ofInt(int... var0) {
        ValueAnimator var1 = new ValueAnimator();
        var1.setIntValues(var0);
        return var1;
    }

    public static ValueAnimator ofFloat(float... var0) {
        ValueAnimator var1 = new ValueAnimator();
        var1.setFloatValues(var0);
        return var1;
    }

    public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... var0) {
        ValueAnimator var1 = new ValueAnimator();
        var1.setValues(var0);
        return var1;
    }

    public static ValueAnimator ofObject(TypeEvaluator var0, Object... var1) {
        ValueAnimator var2 = new ValueAnimator();
        var2.setObjectValues(var1);
        var2.setEvaluator(var0);
        return var2;
    }

    public void setIntValues(int... var1) {
        if(var1 != null && var1.length != 0) {
            if(this.mValues != null && this.mValues.length != 0) {
                PropertyValuesHolder var2 = this.mValues[0];
                var2.setIntValues(var1);
            } else {
                this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt("", var1)});
            }

            this.mInitialized = false;
        }
    }

    public void setFloatValues(float... var1) {
        if(var1 != null && var1.length != 0) {
            if(this.mValues != null && this.mValues.length != 0) {
                PropertyValuesHolder var2 = this.mValues[0];
                var2.setFloatValues(var1);
            } else {
                this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("", var1)});
            }

            this.mInitialized = false;
        }
    }

    public void setObjectValues(Object... var1) {
        if(var1 != null && var1.length != 0) {
            if(this.mValues != null && this.mValues.length != 0) {
                PropertyValuesHolder var2 = this.mValues[0];
                var2.setObjectValues(var1);
            } else {
                this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofObject("", (TypeEvaluator)null, var1)});
            }

            this.mInitialized = false;
        }
    }

    public void setValues(PropertyValuesHolder... var1) {
        int var2 = var1.length;
        this.mValues = var1;
        this.mValuesMap = new HashMap(var2);

        for(int var3 = 0; var3 < var2; ++var3) {
            PropertyValuesHolder var4 = var1[var3];
            this.mValuesMap.put(var4.getPropertyName(), var4);
        }

        this.mInitialized = false;
    }

    public PropertyValuesHolder[] getValues() {
        return this.mValues;
    }

    void initAnimation() {
        if(!this.mInitialized) {
            int var1 = this.mValues.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                this.mValues[var2].init();
            }

            this.mInitialized = true;
        }

    }

    public ValueAnimator setDuration(long var1) {
        if(var1 < 0L) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + var1);
        } else {
            this.mDuration = var1;
            return this;
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setCurrentPlayTime(long var1) {
        this.initAnimation();
        long var3 = AnimationUtils.currentAnimationTimeMillis();
        if(this.mPlayingState != 1) {
            this.mSeekTime = var1;
            this.mPlayingState = 2;
        }

        this.mStartTime = var3 - var1;
        this.animationFrame(var3);
    }

    public long getCurrentPlayTime() {
        return this.mInitialized && this.mPlayingState != 0?AnimationUtils.currentAnimationTimeMillis() - this.mStartTime:0L;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public void setStartDelay(long var1) {
        this.mStartDelay = var1;
    }

    public static long getFrameDelay() {
        return sFrameDelay;
    }

    public static void setFrameDelay(long var0) {
        sFrameDelay = var0;
    }

    public Object getAnimatedValue() {
        return this.mValues != null && this.mValues.length > 0?this.mValues[0].getAnimatedValue():null;
    }

    public Object getAnimatedValue(String var1) {
        PropertyValuesHolder var2 = (PropertyValuesHolder)this.mValuesMap.get(var1);
        return var2 != null?var2.getAnimatedValue():null;
    }

    public void setRepeatCount(int var1) {
        this.mRepeatCount = var1;
    }

    public int getRepeatCount() {
        return this.mRepeatCount;
    }

    public void setRepeatMode(int var1) {
        this.mRepeatMode = var1;
    }

    public int getRepeatMode() {
        return this.mRepeatMode;
    }

    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener var1) {
        if(this.mUpdateListeners == null) {
            this.mUpdateListeners = new ArrayList();
        }

        this.mUpdateListeners.add(var1);
    }

    public void removeAllUpdateListeners() {
        if(this.mUpdateListeners != null) {
            this.mUpdateListeners.clear();
            this.mUpdateListeners = null;
        }
    }

    public void removeUpdateListener(ValueAnimator.AnimatorUpdateListener var1) {
        if(this.mUpdateListeners != null) {
            this.mUpdateListeners.remove(var1);
            if(this.mUpdateListeners.size() == 0) {
                this.mUpdateListeners = null;
            }

        }
    }

    public void setInterpolator(Interpolator var1) {
        if(var1 != null) {
            this.mInterpolator = var1;
        } else {
            this.mInterpolator = new LinearInterpolator();
        }

    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setEvaluator(TypeEvaluator var1) {
        if(var1 != null && this.mValues != null && this.mValues.length > 0) {
            this.mValues[0].setEvaluator(var1);
        }

    }

    private void start(boolean var1) {
        if(Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        } else {
            this.mPlayingBackwards = var1;
            this.mCurrentIteration = 0;
            this.mPlayingState = 0;
            this.mStarted = true;
            this.mStartedDelay = false;
            ((ArrayList)sPendingAnimations.get()).add(this);
            if(this.mStartDelay == 0L) {
                this.setCurrentPlayTime(this.getCurrentPlayTime());
                this.mPlayingState = 0;
                this.mRunning = true;
                if(this.mListeners != null) {
                    ArrayList var2 = (ArrayList)this.mListeners.clone();
                    int var3 = var2.size();

                    for(int var4 = 0; var4 < var3; ++var4) {
                        ((AnimatorListener)var2.get(var4)).onAnimationStart(this);
                    }
                }
            }

            ValueAnimator.AnimationHandler var5 = (ValueAnimator.AnimationHandler)sAnimationHandler.get();
            if(var5 == null) {
                var5 = new ValueAnimator.AnimationHandler();
                sAnimationHandler.set(var5);
            }

            var5.sendEmptyMessage(0);
        }
    }

    public void start() {
        this.start(false);
    }

    public void cancel() {
        if(this.mPlayingState != 0 || ((ArrayList)sPendingAnimations.get()).contains(this) || ((ArrayList)sDelayedAnims.get()).contains(this)) {
            if(this.mRunning && this.mListeners != null) {
                ArrayList var1 = (ArrayList)this.mListeners.clone();
                Iterator var2 = var1.iterator();

                while(var2.hasNext()) {
                    AnimatorListener var3 = (AnimatorListener)var2.next();
                    var3.onAnimationCancel(this);
                }
            }

            this.endAnimation();
        }

    }

    public void end() {
        if(!((ArrayList)sAnimations.get()).contains(this) && !((ArrayList)sPendingAnimations.get()).contains(this)) {
            this.mStartedDelay = false;
            this.startAnimation();
        } else if(!this.mInitialized) {
            this.initAnimation();
        }

        if(this.mRepeatCount > 0 && (this.mRepeatCount & 1) == 1) {
            this.animateValue(0.0F);
        } else {
            this.animateValue(1.0F);
        }

        this.endAnimation();
    }

    public boolean isRunning() {
        return this.mPlayingState == 1 || this.mRunning;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public void reverse() {
        this.mPlayingBackwards = !this.mPlayingBackwards;
        if(this.mPlayingState == 1) {
            long var1 = AnimationUtils.currentAnimationTimeMillis();
            long var3 = var1 - this.mStartTime;
            long var5 = this.mDuration - var3;
            this.mStartTime = var1 - var5;
        } else {
            this.start(true);
        }

    }

    private void endAnimation() {
        ((ArrayList)sAnimations.get()).remove(this);
        ((ArrayList)sPendingAnimations.get()).remove(this);
        ((ArrayList)sDelayedAnims.get()).remove(this);
        this.mPlayingState = 0;
        if(this.mRunning && this.mListeners != null) {
            ArrayList var1 = (ArrayList)this.mListeners.clone();
            int var2 = var1.size();

            for(int var3 = 0; var3 < var2; ++var3) {
                ((AnimatorListener)var1.get(var3)).onAnimationEnd(this);
            }
        }

        this.mRunning = false;
        this.mStarted = false;
    }

    private void startAnimation() {
        this.initAnimation();
        ((ArrayList)sAnimations.get()).add(this);
        if(this.mStartDelay > 0L && this.mListeners != null) {
            ArrayList var1 = (ArrayList)this.mListeners.clone();
            int var2 = var1.size();

            for(int var3 = 0; var3 < var2; ++var3) {
                ((AnimatorListener)var1.get(var3)).onAnimationStart(this);
            }
        }

    }

    private boolean delayedAnimationFrame(long var1) {
        if(!this.mStartedDelay) {
            this.mStartedDelay = true;
            this.mDelayStartTime = var1;
        } else {
            long var3 = var1 - this.mDelayStartTime;
            if(var3 > this.mStartDelay) {
                this.mStartTime = var1 - (var3 - this.mStartDelay);
                this.mPlayingState = 1;
                return true;
            }
        }

        return false;
    }

    boolean animationFrame(long var1) {
        boolean var3 = false;
        if(this.mPlayingState == 0) {
            this.mPlayingState = 1;
            if(this.mSeekTime < 0L) {
                this.mStartTime = var1;
            } else {
                this.mStartTime = var1 - this.mSeekTime;
                this.mSeekTime = -1L;
            }
        }

        switch(this.mPlayingState) {
            case 1:
            case 2:
                float var4 = this.mDuration > 0L?(float)(var1 - this.mStartTime) / (float)this.mDuration:1.0F;
                if(var4 >= 1.0F) {
                    if(this.mCurrentIteration >= this.mRepeatCount && this.mRepeatCount != -1) {
                        var3 = true;
                        var4 = Math.min(var4, 1.0F);
                    } else {
                        if(this.mListeners != null) {
                            int var5 = this.mListeners.size();

                            for(int var6 = 0; var6 < var5; ++var6) {
                                this.mListeners.get(var6).onAnimationRepeat(this);
                            }
                        }

                        if(this.mRepeatMode == 2) {
                            this.mPlayingBackwards = !this.mPlayingBackwards;
                        }

                        this.mCurrentIteration += (int)var4;
                        var4 %= 1.0F;
                        this.mStartTime += this.mDuration;
                    }
                }

                if(this.mPlayingBackwards) {
                    var4 = 1.0F - var4;
                }

                this.animateValue(var4);
            default:
                return var3;
        }
    }

    public float getAnimatedFraction() {
        return this.mCurrentFraction;
    }

    void animateValue(float var1) {
        var1 = this.mInterpolator.getInterpolation(var1);
        this.mCurrentFraction = var1;
        int var2 = this.mValues.length;

        int var3;
        for(var3 = 0; var3 < var2; ++var3) {
            this.mValues[var3].calculateValue(var1);
        }

        if(this.mUpdateListeners != null) {
            var3 = this.mUpdateListeners.size();

            for(int var4 = 0; var4 < var3; ++var4) {
                this.mUpdateListeners.get(var4).onAnimationUpdate(this);
            }
        }

    }

    public ValueAnimator clone() {
        ValueAnimator var1 = (ValueAnimator)super.clone();
        int var3;
        int i;
        if(this.mUpdateListeners != null) {
            ArrayList var2 = this.mUpdateListeners;
            var1.mUpdateListeners = new ArrayList();
            var3 = var2.size();

            for(i = 0; i < var3; ++i) {
                var1.mUpdateListeners.add((ValueAnimator.AnimatorUpdateListener)var2.get(i));
            }
        }

        var1.mSeekTime = -1L;
        var1.mPlayingBackwards = false;
        var1.mCurrentIteration = 0;
        var1.mInitialized = false;
        var1.mPlayingState = 0;
        var1.mStartedDelay = false;
        PropertyValuesHolder[] var6 = this.mValues;
        if(var6 != null) {
            var3 = var6.length;
            var1.mValues = new PropertyValuesHolder[var3];
            var1.mValuesMap = new HashMap(var3);

            for(i = 0; i < var3; ++i) {
                PropertyValuesHolder var5 = var6[i].clone();
                var1.mValues[i] = var5;
                var1.mValuesMap.put(var5.getPropertyName(), var5);
            }
        }

        return var1;
    }

    public static int getCurrentAnimationsCount() {
        return ((ArrayList)sAnimations.get()).size();
    }

    public static void clearAllAnimations() {
        ((ArrayList)sAnimations.get()).clear();
        ((ArrayList)sPendingAnimations.get()).clear();
        ((ArrayList)sDelayedAnims.get()).clear();
    }

    public String toString() {
        String var1 = "ValueAnimator@" + Integer.toHexString(this.hashCode());
        if(this.mValues != null) {
            for(int var2 = 0; var2 < this.mValues.length; ++var2) {
                var1 = var1 + "\n    " + this.mValues[var2].toString();
            }
        }

        return var1;
    }

    public interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimator var1);
    }

    private static class AnimationHandler extends Handler {
        private AnimationHandler() {
        }

        public void handleMessage(Message var1) {
            boolean var2 = true;
            ArrayList var3 = (ArrayList)ValueAnimator.sAnimations.get();
            ArrayList var4 = (ArrayList)ValueAnimator.sDelayedAnims.get();
            switch(var1.what) {
                case 0:
                    ArrayList var5 = (ArrayList)ValueAnimator.sPendingAnimations.get();
                    if(var3.size() > 0 || var4.size() > 0) {
                        var2 = false;
                    }

                    while(var5.size() > 0) {
                        ArrayList var6 = (ArrayList)var5.clone();
                        var5.clear();
                        int var7 = var6.size();

                        for(int var8 = 0; var8 < var7; ++var8) {
                            ValueAnimator var9 = (ValueAnimator)var6.get(var8);
                            if(var9.mStartDelay == 0L) {
                                var9.startAnimation();
                            } else {
                                var4.add(var9);
                            }
                        }
                    }
                case 1:
                    long var15 = AnimationUtils.currentAnimationTimeMillis();
                    ArrayList var16 = (ArrayList)ValueAnimator.sReadyAnims.get();
                    ArrayList var17 = (ArrayList)ValueAnimator.sEndingAnims.get();
                    int var10 = var4.size();

                    int var11;
                    for(var11 = 0; var11 < var10; ++var11) {
                        ValueAnimator var12 = (ValueAnimator)var4.get(var11);
                        if(var12.delayedAnimationFrame(var15)) {
                            var16.add(var12);
                        }
                    }

                    var11 = var16.size();
                    int var18;
                    if(var11 > 0) {
                        for(var18 = 0; var18 < var11; ++var18) {
                            ValueAnimator var13 = (ValueAnimator)var16.get(var18);
                            var13.startAnimation();
                            var13.mRunning = true;
                            var4.remove(var13);
                        }

                        var16.clear();
                    }

                    var18 = var3.size();
                    int var19 = 0;

                    while(var19 < var18) {
                        ValueAnimator var14 = (ValueAnimator)var3.get(var19);
                        if(var14.animationFrame(var15)) {
                            var17.add(var14);
                        }

                        if(var3.size() == var18) {
                            ++var19;
                        } else {
                            --var18;
                            var17.remove(var14);
                        }
                    }

                    if(var17.size() > 0) {
                        for(var19 = 0; var19 < var17.size(); ++var19) {
                            ((ValueAnimator)var17.get(var19)).endAnimation();
                        }

                        var17.clear();
                    }

                    if(var2 && (!var3.isEmpty() || !var4.isEmpty())) {
                        this.sendEmptyMessageDelayed(1, Math.max(0L, ValueAnimator.sFrameDelay - (AnimationUtils.currentAnimationTimeMillis() - var15)));
                    }
            }

        }
    }
}
