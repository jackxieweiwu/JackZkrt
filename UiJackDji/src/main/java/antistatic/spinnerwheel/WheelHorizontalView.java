package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import zkrt.ui.R;

/**
 * Created by jack_xie on 17-5-31.
 */

public class WheelHorizontalView extends AntiSpinnerB {
    private static int z = -1;
    private final String A;
    protected int y;
    private int B;

    public WheelHorizontalView(Context var1) {
        this(var1, null);
    }

    public WheelHorizontalView(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public WheelHorizontalView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.A = AntiSpinnerK.class.getName() + " #" + ++z;
        this.B = 0;
    }


    protected void a(AttributeSet var1, int var2) {
        super.a(var1, var2);
        TypedArray var3 = this.getContext().obtainStyledAttributes(var1, R.styleable.WheelHorizontalView, var2, 0);
        this.y = var3.getDimensionPixelSize(R.styleable.WheelHorizontalView_selectionDividerWidth, 2);
        var3.recycle();
        this.e();
    }

    public void setSelectionDividerWidth(int var1) {
        this.y = var1;
    }

    public void setSelectorPaintCoeff(float var1) {
        if(this.m < 100) {
            int var3 = this.getMeasuredWidth();
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
                var2 = new LinearGradient(0.0F, 0.0F, (float)var3, 0.0F, var11, var12, Shader.TileMode.CLAMP);
            } else {
                float var19 = (1.0F - (float)(var4 * 3) / (float)var3) / 2.0F;
                float var20 = (1.0F + (float)(var4 * 3) / (float)var3) / 2.0F;
                float var21 = 255.0F * var19 / var5;
                float var22 = var21 * var1;
                float var13 = var7 + var22;
                int var14 = Math.round(var8) << 24;
                int var15 = Math.round(var13) << 24;
                int var16 = Math.round(var22) << 24;
                int[] var17 = new int[]{117440512, var16, var15, var14, -16777216, -16777216, var14, var15, var16, 117440512};
                float[] var18 = new float[]{0.0F, var19, var19, var5, var5, var6, var6, var20, var20, 1.0F};
                var2 = new LinearGradient(0.0F, 0.0F, (float)var3, 0.0F, var17, var18, Shader.TileMode.CLAMP);
            }

            this.s.setShader(var2);
            this.invalidate();
        }
    }

    @Override
    protected AntiSpinnerI a(AntiSpinnerI.a var1) {
        return new AntiSpinnerG(this.getContext(), var1);
    }

    protected float a(MotionEvent var1) {
        return var1.getX();
    }

    protected int getBaseDimension() {
        return this.getWidth();
    }

    protected int getItemDimension() {
        if(this.B != 0) {
            return this.B;
        } else if(this.h != null && this.h.getChildAt(0) != null) {
            this.B = this.h.getChildAt(0).getMeasuredWidth();
            return this.B;
        } else {
            return this.getBaseDimension() / this.b;
        }
    }

    protected void c() {
        super.c();
        int var1 = this.h.getChildCount();
        Log.e(this.A, " ----- layout: " + this.h.getMeasuredWidth() + this.h.getMeasuredHeight());
        Log.e(this.A, " -------- dumping " + var1 + " items");

        for(int var3 = 0; var3 < var1; ++var3) {
            View var2 = this.h.getChildAt(var3);
            Log.e(this.A, " item #" + var3 + ": " + var2.getWidth() + "x" + var2.getHeight());
            var2.forceLayout();
        }

        Log.e(this.A, " ---------- dumping finished ");
    }

    protected void e() {
        if(this.h == null) {
            this.h = new LinearLayout(this.getContext());
            FrameLayout.LayoutParams var1 = new FrameLayout.LayoutParams(-2, -2);
            this.h.setLayoutParams(var1);
            this.h.setGravity(16);
            this.h.setOrientation(LinearLayout.HORIZONTAL);
        }

    }

    protected void f() {
        this.h.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    protected void k() {
        this.h.measure(MeasureSpec.makeMeasureSpec(this.getWidth() + this.getItemDimension(), 0), MeasureSpec.makeMeasureSpec(this.getHeight(), -2147483648));
    }

    protected void a(Canvas var1) {
        var1.save();
        int var2 = this.getMeasuredWidth();
        int var3 = this.getMeasuredHeight();
        int var4 = this.getItemDimension();
        this.w.eraseColor(0);
        Canvas var5 = new Canvas(this.w);
        Canvas var6 = new Canvas(this.w);
        int var7 = (this.a - this.i) * var4 + (var4 - this.getWidth()) / 2;
        var5.translate((float)(-var7 + this.g), (float)this.q);
        this.h.draw(var5);
        this.x.eraseColor(0);
        Canvas var8 = new Canvas(this.x);
        if(this.r != null) {
            int var9 = (this.getWidth() - var4 - this.y) / 2;
            int var10 = var9 + this.y;
            var8.save();
            var8.clipRect(var9, 0, var10, var3);
            this.r.setBounds(var9, 0, var10, var3);
            this.r.draw(var8);
            var8.restore();
            var8.save();
            int var11 = var9 + var4;
            int var12 = var10 + var4;
            var8.clipRect(var11, 0, var12, var3);
            this.r.setBounds(var11, 0, var12, var3);
            this.r.draw(var8);
            var8.restore();
        }

        var6.drawRect(0.0F, 0.0F, (float)var2, (float)var3, this.s);
        var8.drawRect(0.0F, 0.0F, (float)var2, (float)var3, this.t);
        var1.drawBitmap(this.w, 0.0F, 0.0F, (Paint)null);
        var1.drawBitmap(this.x, 0.0F, 0.0F, (Paint)null);
        var1.restore();
    }
}
