package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

import java.util.ArrayList;

/**
 * Created by jack_xie on 17-11-2.
 */

public abstract class Animator implements Cloneable{
    ArrayList<AnimatorListener> mListeners = null;

    public Animator() {
    }

    public void start() {
    }

    public void cancel() {
    }

    public void end() {
    }

    public abstract long getStartDelay();

    public abstract void setStartDelay(long var1);

    public abstract Animator setDuration(long var1);

    public abstract long getDuration();

    public abstract void setInterpolator(Interpolator var1);

    public abstract boolean isRunning();

    public boolean isStarted() {
        return this.isRunning();
    }

    public void addListener(Animator.AnimatorListener var1) {
        if(this.mListeners == null) {
            this.mListeners = new ArrayList();
        }

        this.mListeners.add(var1);
    }

    public void removeListener(Animator.AnimatorListener var1) {
        if(this.mListeners != null) {
            this.mListeners.remove(var1);
            if(this.mListeners.size() == 0) {
                this.mListeners = null;
            }

        }
    }

    public ArrayList<Animator.AnimatorListener> getListeners() {
        return this.mListeners;
    }

    public void removeAllListeners() {
        if(this.mListeners != null) {
            this.mListeners.clear();
            this.mListeners = null;
        }

    }

    public Animator clone() {
        try {
            Animator var1 = (Animator)super.clone();
            if(this.mListeners != null) {
                ArrayList var2 = this.mListeners;
                var1.mListeners = new ArrayList();
                int var3 = var2.size();

                for(int var4 = 0; var4 < var3; ++var4) {
                    var1.mListeners.add((AnimatorListener)var2.get(var4));
                }
            }

            return var1;
        } catch (CloneNotSupportedException var5) {
            throw new AssertionError();
        }
    }

    public void setupStartValues() {
    }

    public void setupEndValues() {
    }

    public void setTarget(Object var1) {
    }

    public interface AnimatorListener {
        void onAnimationStart(Animator var1);

        void onAnimationEnd(Animator var1);

        void onAnimationCancel(Animator var1);

        void onAnimationRepeat(Animator var1);
    }
}
