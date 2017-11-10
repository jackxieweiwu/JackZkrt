package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import zkrt.ui.R;
import zkrt.ui.internal.exposure.DULCameraShutterSettingWidget;

/**
 * Created by jack_xie on 17-5-31.
 */

public abstract class AntiSpinnerB extends AntiSpinnerA {
    private static int y = -1;
    private final String z;
    protected int m;
    protected int n;
    protected int o;
    protected int p;
    protected int q;
    protected Drawable r;
    protected Paint s;
    protected Paint t;
    protected Animator u;
    protected Animator v;
    protected Bitmap w;
    protected Bitmap x;

    public AntiSpinnerB(Context var1, @Nullable AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.z = AntiSpinnerB.class.getName() + " #" + ++y;
    }

   protected void a(AttributeSet var1, int var2) {
        super.a(var1,var2);
        TypedArray var3 = this.getContext().obtainStyledAttributes(var1, R.styleable.AbstractWheelView, var2, 0);
        this.m = var3.getInt(R.styleable.AbstractWheelView_itemsDimmedAlpha, 50);
        this.n = var3.getInt(R.styleable.AbstractWheelView_selectionDividerActiveAlpha, 70);
        this.o = var3.getInt(R.styleable.AbstractWheelView_selectionDividerDimmedAlpha, 70);
        this.p = var3.getInt(R.styleable.AbstractWheelView_itemOffsetPercent, 10);
        this.q = var3.getDimensionPixelSize(R.styleable.AbstractWheelView_itemsPadding, 10);
        this.r = var3.getDrawable(R.styleable.AbstractWheelView_selectionDivider);
        var3.recycle();
    }

    protected void a(Context var1) {
        super.a(var1);
        this.u = ObjectAnimator.ofFloat(this, "selectorPaintCoeff", new float[]{1.0F, 0.5F});
        this.v = ObjectAnimator.ofInt(this, "separatorsPaintAlpha", new int[]{this.n, this.o});
        this.t = new Paint();
        this.t.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.t.setAlpha(this.o);
        this.s = new Paint();
        this.s.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    protected void a(int var1, int var2) {
        if(var1 == 0) {
            var1 = var2;
        }

        this.w = Bitmap.createBitmap(var1, var2, Bitmap.Config.ARGB_8888);
        this.x = Bitmap.createBitmap(var1, var2, Bitmap.Config.ARGB_8888);
        this.setSelectorPaintCoeff(0.4F);
    }

    public void setSeparatorsPaintAlpha(int var1) {
        this.t.setAlpha(var1);
        this.invalidate();
    }

    public abstract void setSelectorPaintCoeff(float var1);

    public void setSelectionDivider(Drawable var1) {
        this.r = var1;
    }

    protected void b() {
        this.u.cancel();
        this.v.cancel();
        this.setSelectorPaintCoeff(1.0F);
        this.setSeparatorsPaintAlpha(this.n);
    }

    protected void c() {
        super.c();
        this.a(750L);
        this.b(750L);
    }

    protected void d() {
        this.a(500L);
        this.b(500L);
    }

    private void a(long var1) {
        this.u.setDuration(var1);
        this.u.start();
    }

    private void b(long var1) {
        this.v.setDuration(var1);
        this.v.start();
    }

    protected abstract void k();

    protected void onDraw(Canvas var1) {
        super.onDraw(var1);
        if(this.j != null && this.j.b() > 0) {
            if(this.j()) {
                this.k();
            }

            this.f();
            this.a(var1);
        }

    }

    protected abstract void a(Canvas var1);
}
