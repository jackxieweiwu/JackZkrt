package com.nineoldandroids.view.AnimatorProxy;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by jack_xie on 17-11-2.
 */

public final class AnimatorProxy extends Animation {
    public static final boolean NEEDS_PROXY;
    private static final WeakHashMap<View, AnimatorProxy> PROXIES;
    private final WeakReference<View> mView;
    private final Camera mCamera = new Camera();
    private boolean mHasPivot;
    private float mAlpha = 1.0F;
    private float mPivotX;
    private float mPivotY;
    private float mRotationX;
    private float mRotationY;
    private float mRotationZ;
    private float mScaleX = 1.0F;
    private float mScaleY = 1.0F;
    private float mTranslationX;
    private float mTranslationY;
    private final RectF mBefore = new RectF();
    private final RectF mAfter = new RectF();
    private final Matrix mTempMatrix = new Matrix();

    public static AnimatorProxy wrap(View var0) {
        AnimatorProxy var1 = (AnimatorProxy)PROXIES.get(var0);
        if(var1 == null || var1 != var0.getAnimation()) {
            var1 = new AnimatorProxy(var0);
            PROXIES.put(var0, var1);
        }

        return var1;
    }

    private AnimatorProxy(View var1) {
        this.setDuration(0L);
        this.setFillAfter(true);
        var1.setAnimation(this);
        this.mView = new WeakReference(var1);
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void setAlpha(float var1) {
        if(this.mAlpha != var1) {
            this.mAlpha = var1;
            View var2 = (View)this.mView.get();
            if(var2 != null) {
                var2.invalidate();
            }
        }

    }

    public float getPivotX() {
        return this.mPivotX;
    }

    public void setPivotX(float var1) {
        if(!this.mHasPivot || this.mPivotX != var1) {
            this.prepareForUpdate();
            this.mHasPivot = true;
            this.mPivotX = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getPivotY() {
        return this.mPivotY;
    }

    public void setPivotY(float var1) {
        if(!this.mHasPivot || this.mPivotY != var1) {
            this.prepareForUpdate();
            this.mHasPivot = true;
            this.mPivotY = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getRotation() {
        return this.mRotationZ;
    }

    public void setRotation(float var1) {
        if(this.mRotationZ != var1) {
            this.prepareForUpdate();
            this.mRotationZ = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getRotationX() {
        return this.mRotationX;
    }

    public void setRotationX(float var1) {
        if(this.mRotationX != var1) {
            this.prepareForUpdate();
            this.mRotationX = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getRotationY() {
        return this.mRotationY;
    }

    public void setRotationY(float var1) {
        if(this.mRotationY != var1) {
            this.prepareForUpdate();
            this.mRotationY = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public void setScaleX(float var1) {
        if(this.mScaleX != var1) {
            this.prepareForUpdate();
            this.mScaleX = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public void setScaleY(float var1) {
        if(this.mScaleY != var1) {
            this.prepareForUpdate();
            this.mScaleY = var1;
            this.invalidateAfterUpdate();
        }

    }

    public int getScrollX() {
        View var1 = (View)this.mView.get();
        return var1 == null?0:var1.getScrollX();
    }

    public void setScrollX(int var1) {
        View var2 = (View)this.mView.get();
        if(var2 != null) {
            var2.scrollTo(var1, var2.getScrollY());
        }

    }

    public int getScrollY() {
        View var1 = (View)this.mView.get();
        return var1 == null?0:var1.getScrollY();
    }

    public void setScrollY(int var1) {
        View var2 = (View)this.mView.get();
        if(var2 != null) {
            var2.scrollTo(var2.getScrollX(), var1);
        }

    }

    public float getTranslationX() {
        return this.mTranslationX;
    }

    public void setTranslationX(float var1) {
        if(this.mTranslationX != var1) {
            this.prepareForUpdate();
            this.mTranslationX = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getTranslationY() {
        return this.mTranslationY;
    }

    public void setTranslationY(float var1) {
        if(this.mTranslationY != var1) {
            this.prepareForUpdate();
            this.mTranslationY = var1;
            this.invalidateAfterUpdate();
        }

    }

    public float getX() {
        View var1 = (View)this.mView.get();
        return var1 == null?0.0F:(float)var1.getLeft() + this.mTranslationX;
    }

    public void setX(float var1) {
        View var2 = (View)this.mView.get();
        if(var2 != null) {
            this.setTranslationX(var1 - (float)var2.getLeft());
        }

    }

    public float getY() {
        View var1 = (View)this.mView.get();
        return var1 == null?0.0F:(float)var1.getTop() + this.mTranslationY;
    }

    public void setY(float var1) {
        View var2 = (View)this.mView.get();
        if(var2 != null) {
            this.setTranslationY(var1 - (float)var2.getTop());
        }

    }

    private void prepareForUpdate() {
        View var1 = (View)this.mView.get();
        if(var1 != null) {
            this.computeRect(this.mBefore, var1);
        }

    }

    private void invalidateAfterUpdate() {
        View var1 = (View)this.mView.get();
        if(var1 != null && var1.getParent() != null) {
            RectF var2 = this.mAfter;
            this.computeRect(var2, var1);
            var2.union(this.mBefore);
            ((View)var1.getParent()).invalidate((int)Math.floor((double)var2.left), (int)Math.floor((double)var2.top), (int)Math.ceil((double)var2.right), (int)Math.ceil((double)var2.bottom));
        }
    }

    private void computeRect(RectF var1, View var2) {
        float var3 = (float)var2.getWidth();
        float var4 = (float)var2.getHeight();
        var1.set(0.0F, 0.0F, var3, var4);
        Matrix var5 = this.mTempMatrix;
        var5.reset();
        this.transformMatrix(var5, var2);
        this.mTempMatrix.mapRect(var1);
        var1.offset((float)var2.getLeft(), (float)var2.getTop());
        float var6;
        if(var1.right < var1.left) {
            var6 = var1.right;
            var1.right = var1.left;
            var1.left = var6;
        }

        if(var1.bottom < var1.top) {
            var6 = var1.top;
            var1.top = var1.bottom;
            var1.bottom = var6;
        }

    }

    private void transformMatrix(Matrix var1, View var2) {
        float var3 = (float)var2.getWidth();
        float var4 = (float)var2.getHeight();
        boolean var5 = this.mHasPivot;
        float var6 = var5?this.mPivotX:var3 / 2.0F;
        float var7 = var5?this.mPivotY:var4 / 2.0F;
        float var8 = this.mRotationX;
        float var9 = this.mRotationY;
        float var10 = this.mRotationZ;
        if(var8 != 0.0F || var9 != 0.0F || var10 != 0.0F) {
            Camera var11 = this.mCamera;
            var11.save();
            var11.rotateX(var8);
            var11.rotateY(var9);
            var11.rotateZ(-var10);
            var11.getMatrix(var1);
            var11.restore();
            var1.preTranslate(-var6, -var7);
            var1.postTranslate(var6, var7);
        }

        float var15 = this.mScaleX;
        float var12 = this.mScaleY;
        if(var15 != 1.0F || var12 != 1.0F) {
            var1.postScale(var15, var12);
            float var13 = -(var6 / var3) * (var15 * var3 - var3);
            float var14 = -(var7 / var4) * (var12 * var4 - var4);
            var1.postTranslate(var13, var14);
        }

        var1.postTranslate(this.mTranslationX, this.mTranslationY);
    }

    protected void applyTransformation(float var1, Transformation var2) {
        View var3 = (View)this.mView.get();
        if(var3 != null) {
            var2.setAlpha(this.mAlpha);
            this.transformMatrix(var2.getMatrix(), var3);
        }

    }

    static {
        NEEDS_PROXY = Integer.valueOf(Build.VERSION.SDK).intValue() < 11;
        PROXIES = new WeakHashMap();
    }
}

