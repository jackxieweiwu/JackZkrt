package com.nineoldandroids.view;

import android.view.View;

import com.nineoldandroids.view.AnimatorProxy.AnimatorProxy;

/**
 * Created by jack_xie on 17-11-2.
 */

public final class ViewHelper {
    private ViewHelper() {
    }

    public static float getAlpha(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getAlpha():var0.getAlpha();
    }

    public static void setAlpha(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setAlpha(var1);
        } else {
            var0.setAlpha(var1);
        }

    }

    public static float getPivotX(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getPivotX():var0.getPivotX();
    }

    public static void setPivotX(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setPivotX(var1);
        } else {
            var0.setPivotX(var1);
        }

    }

    public static float getPivotY(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getPivotY():var0.getPivotY();
    }

    public static void setPivotY(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setPivotY(var1);
        } else {
            var0.setPivotY(var1);
        }

    }

    public static float getRotation(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getRotation():var0.getRotation();
    }

    public static void setRotation(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setRotation(var1);
        } else {
            var0.setRotation(var1);
        }

    }

    public static float getRotationX(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getRotationX():var0.getRotationX();
    }

    public static void setRotationX(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setRotationX(var1);
        } else {
            var0.setRotationX(var1);
        }

    }

    public static float getRotationY(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getRotationY():var0.getRotationY();
    }

    public static void setRotationY(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setRotationY(var1);
        } else {
            var0.setRotationY(var1);
        }

    }

    public static float getScaleX(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getScaleX():var0.getScaleX();
    }

    public static void setScaleX(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setScaleX(var1);
        } else {
            var0.setScaleX(var1);
        }

    }

    public static float getScaleY(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getScaleY():var0.getScaleY();
    }

    public static void setScaleY(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setScaleY(var1);
        } else {
            var0.setScaleY(var1);
        }

    }

    public static float getScrollX(View var0) {
        return AnimatorProxy.NEEDS_PROXY?(float)AnimatorProxy.wrap(var0).getScrollX():ViewHelper.Honeycomb.getScrollX(var0);
    }

    public static void setScrollX(View var0, int var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setScrollX(var1);
        } else {
            var0.setScrollX(var1);
        }

    }

    public static float getScrollY(View var0) {
        return AnimatorProxy.NEEDS_PROXY?(float)AnimatorProxy.wrap(var0).getScrollY():ViewHelper.Honeycomb.getScrollY(var0);
    }

    public static void setScrollY(View var0, int var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setScrollY(var1);
        } else {
            var0.setScrollY(var1);
        }

    }

    public static float getTranslationX(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getTranslationX():var0.getTranslationX();
    }

    public static void setTranslationX(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setTranslationX(var1);
        } else {
            var0.setTranslationX(var1);
        }

    }

    public static float getTranslationY(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getTranslationY():var0.getTranslationY();
    }

    public static void setTranslationY(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setTranslationY(var1);
        } else {
            var0.setTranslationY(var1);
        }

    }

    public static float getX(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getX():var0.getX();
    }

    public static void setX(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setX(var1);
        } else {
            var0.setX(var1);
        }

    }

    public static float getY(View var0) {
        return AnimatorProxy.NEEDS_PROXY?AnimatorProxy.wrap(var0).getY():var0.getY();
    }

    public static void setY(View var0, float var1) {
        if(AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(var0).setY(var1);
        } else {
            var0.setY(var1);
        }

    }

    private static final class Honeycomb {
        private Honeycomb() {
        }

        static float getAlpha(View var0) {
            return var0.getAlpha();
        }

        static void setAlpha(View var0, float var1) {
            var0.setAlpha(var1);
        }

        static float getPivotX(View var0) {
            return var0.getPivotX();
        }

        static void setPivotX(View var0, float var1) {
            var0.setPivotX(var1);
        }

        static float getPivotY(View var0) {
            return var0.getPivotY();
        }

        static void setPivotY(View var0, float var1) {
            var0.setPivotY(var1);
        }

        static float getRotation(View var0) {
            return var0.getRotation();
        }

        static void setRotation(View var0, float var1) {
            var0.setRotation(var1);
        }

        static float getRotationX(View var0) {
            return var0.getRotationX();
        }

        static void setRotationX(View var0, float var1) {
            var0.setRotationX(var1);
        }

        static float getRotationY(View var0) {
            return var0.getRotationY();
        }

        static void setRotationY(View var0, float var1) {
            var0.setRotationY(var1);
        }

        static float getScaleX(View var0) {
            return var0.getScaleX();
        }

        static void setScaleX(View var0, float var1) {
            var0.setScaleX(var1);
        }

        static float getScaleY(View var0) {
            return var0.getScaleY();
        }

        static void setScaleY(View var0, float var1) {
            var0.setScaleY(var1);
        }

        static float getScrollX(View var0) {
            return (float)var0.getScrollX();
        }

        static void setScrollX(View var0, int var1) {
            var0.setScrollX(var1);
        }

        static float getScrollY(View var0) {
            return (float)var0.getScrollY();
        }

        static void setScrollY(View var0, int var1) {
            var0.setScrollY(var1);
        }

        static float getTranslationX(View var0) {
            return var0.getTranslationX();
        }

        static void setTranslationX(View var0, float var1) {
            var0.setTranslationX(var1);
        }

        static float getTranslationY(View var0) {
            return var0.getTranslationY();
        }

        static void setTranslationY(View var0, float var1) {
            var0.setTranslationY(var1);
        }

        static float getX(View var0) {
            return var0.getX();
        }

        static void setX(View var0, float var1) {
            var0.setX(var1);
        }

        static float getY(View var0) {
            return var0.getY();
        }

        static void setY(View var0, float var1) {
            var0.setY(var1);
        }
    }
}
