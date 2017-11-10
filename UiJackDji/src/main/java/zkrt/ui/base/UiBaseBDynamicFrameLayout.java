package zkrt.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAB;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAF;

/**
 * Created by jack_xie on 17-11-2.
 */

public abstract class UiBaseBDynamicFrameLayout extends UiBaseCFramelayout {
    protected static final String TAG = "BaseDynamicFrameLayout";

    protected void onMeasure(int var1, int var2) {
        UiCAB var3 = (UiCAB)this.getWidgetAppearances();
        int var4 = MeasureSpec.getSize(var1);
        int var5 = MeasureSpec.getSize(var2);
        if((var4 != 0 || var5 != 0) && var3 != null) {
            ArrayList var6 = var3.f();
            if(var4 == 0) {
                var4 = var3.a(var5);
            }

            if(var5 == 0) {
                var5 = var3.b(var4);
            }

            int var7 = this.getPaddingLeft() + this.getPaddingRight();
            int var8 = this.getPaddingTop() + this.getPaddingBottom();
            var3.a(var4, var5);
            UiCAA[] var9 = var3.e();

            for(int var10 = 0; var10 < var6.size(); ++var10) {
                UiCAA var11 = var9[var10];
                View var12 = (View)var6.get(var10);
                this.measureChild(var3, var12, var11, var1, var7, var2, var8);
                if(var12 instanceof TextView && var11 instanceof UiCAF) {
                    ((UiCAF)var11).a((TextView)var12);
                }
            }

            this.setMeasuredDimension(resolveSize(var4, var1), resolveSize(var5, var2));
        } else {
            super.onMeasure(var1, var2);
        }
    }

    protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        UiCAB var6 = (UiCAB)this.getWidgetAppearances();
        if(var6 == null) {
            super.onLayout(var1, var2, var3, var4, var5);
        } else {
            UiCAA[] var7 = var6.e();
            ArrayList var8 = var6.f();

            for(int var9 = 0; var9 < var8.size(); ++var9) {
                UiCAA var10 = var7[var9];
                View var11 = (View)var8.get(var9);
                var6.a(var11, var10);
            }
        }

    }

    private void measureChild(UiCAC var1, View var2, UiCAA var3, int var4, int var5, int var6, int var7) {
        MarginLayoutParams var8 = (MarginLayoutParams)var2.getLayoutParams();
        int var9 = var1.a(var3);
        int var10 = var1.b(var3);
        int var11 = getChildMeasureSpec(var4, var5 + var8.leftMargin + var8.rightMargin, var9);
        int var12 = getChildMeasureSpec(var6, var7 + var8.topMargin + var8.bottomMargin, var10);
        var2.measure(var11, var12);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        UiCAC var4 = this.getWidgetAppearances();
        if(var4 != null) {
            LayoutInflater var5 = LayoutInflater.from(var1);
            var4.a(var5, this);
        }

    }

    public UiBaseBDynamicFrameLayout(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }
}