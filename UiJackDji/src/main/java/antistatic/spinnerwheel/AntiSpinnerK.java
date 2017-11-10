package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import zkrt.ui.R;

/**
 * Created by jack_xie on 17-6-1.
 */

public class AntiSpinnerK extends AntiSpinnerB {
    private static int z = -1;
    protected int y;
    private int A;

    public AntiSpinnerK(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    protected void a(AttributeSet var1, int var2) {
        super.a(var1, var2);
        TypedArray var3 = this.getContext().obtainStyledAttributes(var1, R.styleable.WheelVerticalView, var2, 0);
        this.y = var3.getDimensionPixelSize(R.styleable.WheelVerticalView_selectionDividerHeight, 2);
        var3.recycle();
    }

    public void setSelectorPaintCoeff(float var1) {
        int var3 = this.getMeasuredHeight();
        int var4 = this.getItemDimension();
        float var5 = (1.0F - (float)var4 / (float)var3) / 2.0F;
        float var6 = (1.0F + (float)var4 / (float)var3) / 2.0F;
        float var7 = (float)this.m * (1.0F - var1);
        float var8 = var7 + 255.0F * var1;
        LinearGradient var2;
        if(this.b == 2) {
            int var9 = Math.round(var8) << 24;
            int var10 = Math.round(var7) << 24;
            int[] var11 = new int[]{var10, var9, -16777216, -16777216, var9, var10};
            float[] var12 = new float[]{0.0F, var5, var5, var6, var6, 1.0F};
            var2 = new LinearGradient(0.0F, 0.0F, 0.0F, (float)var3, var11, var12, Shader.TileMode.CLAMP);
        } else {
            float var19 = (1.0F - (float)(var4 * 3) / (float)var3) / 2.0F;
            float var20 = (1.0F + (float)(var4 * 3) / (float)var3) / 2.0F;
            float var21 = 255.0F * var19 / var5;
            float var22 = var21 * var1;
            float var13 = var7 + var22;
            int var14 = Math.round(var8) << 24;
            int var15 = Math.round(var13) << 24;
            int var16 = Math.round(var22) << 24;
            int[] var17 = new int[]{0, var16, var15, var14, -16777216, -16777216, var14, var15, var16, 0};
            float[] var18 = new float[]{0.0F, var19, var19, var5, var5, var6, var6, var20, var20, 1.0F};
            var2 = new LinearGradient(0.0F, 0.0F, 0.0F, (float)var3, var17, var18, Shader.TileMode.CLAMP);
        }

        this.s.setShader(var2);
        this.invalidate();
    }

    @Override
    protected AntiSpinnerI a(AntiSpinnerI.a var1) {
        return new AntiSpinnerJ(this.getContext(), var1);
    }

    protected float a(MotionEvent var1) {
        return var1.getY();
    }


    protected int getBaseDimension() {
        return this.getHeight();
    }

    protected int getItemDimension() {
        if(this.A != 0) {
            return this.A;
        } else if(this.h != null && this.h.getChildAt(0) != null) {
            this.A = this.h.getChildAt(0).getMeasuredHeight();
            return this.A;
        } else {
            return this.getBaseDimension() / this.b;
        }
    }

    protected void e() {
        if(this.h == null) {
            this.h = new LinearLayout(this.getContext());
            this.h.setOrientation(LinearLayout.VERTICAL);
        }

    }

    protected void f() {
        this.h.layout(0, 0, this.getMeasuredWidth() - 2 * this.q, this.getMeasuredHeight());
    }

    protected void k() {
        this.h.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.h.measure(MeasureSpec.makeMeasureSpec(this.getWidth() - 2 * this.q, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
    }

    protected void onMeasure(int var1, int var2) {
        int var3 = MeasureSpec.getMode(var1);
        int var4 = MeasureSpec.getMode(var2);
        int var5 = MeasureSpec.getSize(var1);
        int var6 = MeasureSpec.getSize(var2);
        this.j();
        int var7 = this.d(var5, var3);
        int var8;
        if(var4 == 1073741824) {
            var8 = var6;
        } else {
            var8 = Math.max(this.getItemDimension() * (this.b - this.p / 100), this.getSuggestedMinimumHeight());
            if(var4 == -2147483648) {
                var8 = Math.min(var8, var6);
            }
        }

        this.setMeasuredDimension(var7, var8);
    }

    private int d(int var1, int var2) {
        this.h.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.h.measure(MeasureSpec.makeMeasureSpec(var1, 0), MeasureSpec.makeMeasureSpec(0, 0));
        int var3 = this.h.getMeasuredWidth();
        if(var2 == 1073741824) {
            var3 = var1;
        } else {
            var3 += 2 * this.q;
            var3 = Math.max(var3, this.getSuggestedMinimumWidth());
            if(var2 == -2147483648 && var1 < var3) {
                var3 = var1;
            }
        }

        this.h.measure(MeasureSpec.makeMeasureSpec(var3 - 2 * this.q, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
        return var3;
    }

    protected void a(Canvas var1) {
        var1.save();
        int var2 = this.getMeasuredWidth();
        int var3 = this.getMeasuredHeight();
        int var4 = this.getItemDimension();
        this.w.eraseColor(0);
        Canvas var5 = new Canvas(this.w);
        Canvas var6 = new Canvas(this.w);
        int var7 = (this.a - this.i) * var4 + (var4 - this.getHeight()) / 2;
        var5.translate((float)this.q, (float)(-var7 + this.g));
        this.h.draw(var5);
        this.x.eraseColor(0);
        Canvas var8 = new Canvas(this.x);
        if(this.r != null) {
            int var9 = (this.getHeight() - var4 - this.y) / 2;
            int var10 = var9 + this.y;
            this.r.setBounds(0, var9, var2, var10);
            this.r.draw(var8);
            int var11 = var9 + var4;
            int var12 = var10 + var4;
            this.r.setBounds(0, var11, var2, var12);
            this.r.draw(var8);
        }

        var6.drawRect(0.0F, 0.0F, (float)var2, (float)var3, this.s);
        var8.drawRect(0.0F, 0.0F, (float)var2, (float)var3, this.t);
        var1.drawBitmap(this.w, 0.0F, 0.0F, (Paint)null);
        var1.drawBitmap(this.x, 0.0F, 0.0F, (Paint)null);
        var1.restore();
    }
}
