package zkrt.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAF;

/**
 * Created by root on 17-11-3.
 */

public abstract class UiBaseCFramelayout extends FrameLayout {
    protected static final String TAG = "BaseFrameLayout";
    protected CompositeSubscription subscription = new CompositeSubscription();
    protected Context context;
    private ArrayList<View> viewList;

    protected abstract UiCAC getWidgetAppearances();

    protected void onMeasure(int var1, int var2) {
        UiCAC var3 = this.getWidgetAppearances();
        int var4 = MeasureSpec.getSize(var1);
        int var5 = MeasureSpec.getSize(var2);
        if((var4 != 0 || var5 != 0) && var3 != null && this.viewList != null) {
            if(var4 == 0) {
                var4 = var3.a(var5);
            }

            if(var5 == 0) {
                var5 = var3.b(var4);
            }

            //总共的padding值
            int var6 = this.getPaddingLeft() + this.getPaddingRight();
            int var7 = this.getPaddingTop() + this.getPaddingBottom();
            var3.a(var4, var5);
            UiCAA[] var8 = var3.e();

            for(int var9 = 0; var9 < this.viewList.size(); ++var9) {
                UiCAA var10 = var8[var9];
                View var11 = this.viewList.get(var9);
                this.measureChild(var3, var11, var10, var1, var6, var2, var7);
                if(var11 instanceof TextView && var10 instanceof UiCAF) {
                    ((UiCAF)var10).a((TextView)var11);
                }
            }

            this.setMeasuredDimension(resolveSize(var4, var1), resolveSize(var5, var2));
        } else {
            super.onMeasure(var1, var2);
        }

    }

    protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        UiCAC var6 = this.getWidgetAppearances();
        if(var6 != null && this.viewList != null) {
            UiCAA[] var7 = var6.e();

            for(int var8 = 0; var8 < this.viewList.size(); ++var8) {
                UiCAA var9 = var7[var8];
                View var10 = (View)this.viewList.get(var8);
                var6.a(var10, var9);
            }
        } else {
            super.onLayout(var1, var2, var3, var4, var5);
        }

    }

    protected void onDetachedFromWindow() {
        if(this.subscription != null && !this.subscription.isUnsubscribed()) {
            this.subscription.unsubscribe();
        }

        super.onDetachedFromWindow();
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
            View var6 = var4.a(var5, this);
            if(var6 != null) {
                this.viewList = new ArrayList();
                UiCAA[] var7 = var4.e();
                UiCAA[] var8 = var7;
                int var9 = var7.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    UiCAA var11 = var8[var10];
                    View var12 = var6.findViewById(var11.a());
                    this.viewList.add(var12);
                }
            }
        }

    }

    public UiBaseCFramelayout(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.context = var1;
        this.initView(var1, var2, var3);
    }

    public float aspectRatio() {
        UiCAC var1 = this.getWidgetAppearances();
        return var1 != null?var1.k():1.0F;
    }
}
