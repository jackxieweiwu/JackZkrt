package com.nineoldandroids.animation;

/**
 * Created by jack_Xie on 17-11-2.
 */

public class TimeAnimator extends ValueAnimator {
    private TimeAnimator.TimeListener mListener;
    private long mPreviousTime = -1L;

    public TimeAnimator() {
    }

    boolean animationFrame(long var1) {
        if(this.mPlayingState == 0) {
            this.mPlayingState = 1;
            if(this.mSeekTime < 0L) {
                this.mStartTime = var1;
            } else {
                this.mStartTime = var1 - this.mSeekTime;
                this.mSeekTime = -1L;
            }
        }

        if(this.mListener != null) {
            long var3 = var1 - this.mStartTime;
            long var5 = this.mPreviousTime < 0L?0L:var1 - this.mPreviousTime;
            this.mPreviousTime = var1;
            this.mListener.onTimeUpdate(this, var3, var5);
        }

        return false;
    }

    public void setTimeListener(TimeAnimator.TimeListener var1) {
        this.mListener = var1;
    }

    void animateValue(float var1) {
    }

    void initAnimation() {
    }

    public interface TimeListener {
        void onTimeUpdate(TimeAnimator var1, long var2, long var4);
    }
}
