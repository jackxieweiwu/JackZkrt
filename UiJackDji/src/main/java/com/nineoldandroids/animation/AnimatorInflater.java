package com.nineoldandroids.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.animation.AnimationUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jack_xie on 17-11-2.
 */

public class AnimatorInflater {
    private static final int[] AnimatorSet = new int[]{16843490};
    private static final int AnimatorSet_ordering = 0;
    private static final int[] PropertyAnimator = new int[]{16843489};
    private static final int PropertyAnimator_propertyName = 0;
    private static final int[] Animator = new int[]{16843073, 16843160, 16843198, 16843199, 16843200, 16843486, 16843487, 16843488};
    private static final int Animator_interpolator = 0;
    private static final int Animator_duration = 1;
    private static final int Animator_startOffset = 2;
    private static final int Animator_repeatCount = 3;
    private static final int Animator_repeatMode = 4;
    private static final int Animator_valueFrom = 5;
    private static final int Animator_valueTo = 6;
    private static final int Animator_valueType = 7;
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_FLOAT = 0;

    public AnimatorInflater() {
    }

    public static Animator loadAnimator(Context var0, int var1) {
        XmlResourceParser var2 = null;

        Animator var3;
        try {
            Resources.NotFoundException var4;
            try {
                var2 = var0.getResources().getAnimation(var1);
                var3 = createAnimatorFromXml(var0, var2);
            } catch (XmlPullParserException var9) {
                var4 = new Resources.NotFoundException("Can\'t load animation resource ID #0x" + Integer.toHexString(var1));
                var4.initCause(var9);
                throw var4;
            } catch (IOException var10) {
                var4 = new Resources.NotFoundException("Can\'t load animation resource ID #0x" + Integer.toHexString(var1));
                var4.initCause(var10);
                throw var4;
            }
        } finally {
            if(var2 != null) {
                var2.close();
            }

        }

        return var3;
    }

    private static Animator createAnimatorFromXml(Context var0, XmlPullParser var1) throws IOException, XmlPullParserException {
        return createAnimatorFromXml(var0, var1, Xml.asAttributeSet(var1), null, 0);
    }

    private static Animator createAnimatorFromXml(Context var0, XmlPullParser var1, AttributeSet var2, AnimatorSet var3, int var4) throws IOException, XmlPullParserException {
        Object var5 = null;
        ArrayList var6 = null;
        int var8 = var1.getDepth();

        int var7;
        while(((var7 = var1.next()) != 3 || var1.getDepth() > var8) && var7 != 1) {
            if(var7 == 2) {
                String var9 = var1.getName();
                if(var9.equals("objectAnimator")) {
                    var5 = loadObjectAnimator(var0, var2);
                } else if(var9.equals("animator")) {
                    var5 = loadAnimator(var0, var2, (ValueAnimator)null);
                } else {
                    if(!var9.equals("set")) {
                        throw new RuntimeException("Unknown animator name: " + var1.getName());
                    }

                    var5 = new AnimatorSet();
                    TypedArray var10 = var0.obtainStyledAttributes(var2, AnimatorSet);
                    TypedValue var11 = new TypedValue();
                    var10.getValue(0, var11);
                    int var12 = var11.type == 16?var11.data:0;
                    createAnimatorFromXml(var0, var1, var2, (AnimatorSet)var5, var12);
                    var10.recycle();
                }

                if(var3 != null) {
                    if(var6 == null) {
                        var6 = new ArrayList();
                    }

                    var6.add(var5);
                }
            }
        }

        if(var3 != null && var6 != null) {
            Animator[] var13 = new Animator[var6.size()];
            int var14 = 0;

            Animator var16;
            for(Iterator var15 = var6.iterator(); var15.hasNext(); var13[var14++] = var16) {
                var16 = (Animator)var15.next();
            }

            if(var4 == 0) {
                var3.playTogether(var13);
            } else {
                var3.playSequentially(var13);
            }
        }

        return (Animator)var5;
    }

    private static ObjectAnimator loadObjectAnimator(Context var0, AttributeSet var1) {
        ObjectAnimator var2 = new ObjectAnimator();
        loadAnimator(var0, var1, var2);
        TypedArray var3 = var0.obtainStyledAttributes(var1, PropertyAnimator);
        String var4 = var3.getString(0);
        var2.setPropertyName(var4);
        var3.recycle();
        return var2;
    }

    private static ValueAnimator loadAnimator(Context var0, AttributeSet var1, ValueAnimator var2) {
        TypedArray var3 = var0.obtainStyledAttributes(var1, Animator);
        long var4 = (long)var3.getInt(1, 0);
        long var6 = (long)var3.getInt(2, 0);
        int var8 = var3.getInt(7, 0);
        if(var2 == null) {
            var2 = new ValueAnimator();
        }

        byte var9 = 5;
        byte var10 = 6;
        boolean var11 = var8 == 0;
        TypedValue var12 = var3.peekValue(var9);
        boolean var13 = var12 != null;
        int var14 = var13?var12.type:0;
        TypedValue var15 = var3.peekValue(var10);
        boolean var16 = var15 != null;
        int var17 = var16?var15.type:0;
        if(var13 && var14 >= 28 && var14 <= 31 || var16 && var17 >= 28 && var17 <= 31) {
            var11 = false;
            var2.setEvaluator(new ArgbEvaluator());
        }

        int var20;
        if(var11) {
            float var19;
            if(var13) {
                float var18;
                if(var14 == 5) {
                    var18 = var3.getDimension(var9, 0.0F);
                } else {
                    var18 = var3.getFloat(var9, 0.0F);
                }

                if(var16) {
                    if(var17 == 5) {
                        var19 = var3.getDimension(var10, 0.0F);
                    } else {
                        var19 = var3.getFloat(var10, 0.0F);
                    }

                    var2.setFloatValues(new float[]{var18, var19});
                } else {
                    var2.setFloatValues(new float[]{var18});
                }
            } else {
                if(var17 == 5) {
                    var19 = var3.getDimension(var10, 0.0F);
                } else {
                    var19 = var3.getFloat(var10, 0.0F);
                }

                var2.setFloatValues(new float[]{var19});
            }
        } else {
            int var21;
            if(var13) {
                if(var14 == 5) {
                    var20 = (int)var3.getDimension(var9, 0.0F);
                } else if(var14 >= 28 && var14 <= 31) {
                    var20 = var3.getColor(var9, 0);
                } else {
                    var20 = var3.getInt(var9, 0);
                }

                if(var16) {
                    if(var17 == 5) {
                        var21 = (int)var3.getDimension(var10, 0.0F);
                    } else if(var17 >= 28 && var17 <= 31) {
                        var21 = var3.getColor(var10, 0);
                    } else {
                        var21 = var3.getInt(var10, 0);
                    }

                    var2.setIntValues(new int[]{var20, var21});
                } else {
                    var2.setIntValues(new int[]{var20});
                }
            } else if(var16) {
                if(var17 == 5) {
                    var21 = (int)var3.getDimension(var10, 0.0F);
                } else if(var17 >= 28 && var17 <= 31) {
                    var21 = var3.getColor(var10, 0);
                } else {
                    var21 = var3.getInt(var10, 0);
                }

                var2.setIntValues(new int[]{var21});
            }
        }

        var2.setDuration(var4);
        var2.setStartDelay(var6);
        if(var3.hasValue(3)) {
            var2.setRepeatCount(var3.getInt(3, 0));
        }

        if(var3.hasValue(4)) {
            var2.setRepeatMode(var3.getInt(4, 1));
        }

        var20 = var3.getResourceId(0, 0);
        if(var20 > 0) {
            var2.setInterpolator(AnimationUtils.loadInterpolator(var0, var20));
        }

        var3.recycle();
        return var2;
    }
}
