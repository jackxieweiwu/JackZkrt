package antistatic.spinnerwheel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by jack_xie on 17-6-1.
 */

public abstract class AntiSpinnerI {
    private AntiSpinnerI.a b;
    private Context c;
    private GestureDetector d;
    protected Scroller a;
    private int e;
    private float f;
    private boolean g;
    private final int h = 0;
    private final int i = 1;
    private Handler j = new Handler() {
        public void handleMessage(Message var1) {
            AntiSpinnerI.this.a.computeScrollOffset();
            int var2 = AntiSpinnerI.this.a();
            int var3 = AntiSpinnerI.this.e - var2;
            AntiSpinnerI.this.e = var2;
            if(var3 != 0) {
                AntiSpinnerI.this.b.a(var3);
            }

            if(Math.abs(var2 - AntiSpinnerI.this.b()) < 1) {
                AntiSpinnerI.this.a.forceFinished(true);
            }

            if(!AntiSpinnerI.this.a.isFinished()) {
                AntiSpinnerI.this.j.sendEmptyMessage(var1.what);
            } else if(var1.what == 0) {
                AntiSpinnerI.this.f();
            } else {
                AntiSpinnerI.this.d();
            }

        }
    };

    public AntiSpinnerI(Context var1, AntiSpinnerI.a var2) {
        this.d = new GestureDetector(var1, new GestureDetector.SimpleOnGestureListener() {
            public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
                return true;
            }

            public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
                AntiSpinnerI.this.e = 0;
                AntiSpinnerI.this.a(AntiSpinnerI.this.e, (int)var3, (int)var4);
                AntiSpinnerI.this.a(0);
                return true;
            }
        });
        this.d.setIsLongpressEnabled(false);
        this.a = new Scroller(var1);
        this.b = var2;
        this.c = var1;
    }

    public void a(Interpolator var1) {
        this.a.forceFinished(true);
        this.a = new Scroller(this.c, var1);
    }

    public void b(int var1, int var2) {
        this.a.forceFinished(true);
        this.e = 0;
        this.a(var1, var2 != 0?var2:400);
        this.a(0);
        this.g();
    }

    public void c() {
        this.a.forceFinished(true);
    }

    public boolean b(MotionEvent var1) {
        switch(var1.getAction()) {
            case 0:
                this.f = this.a(var1);
                this.a.forceFinished(true);
                this.e();
                this.b.b();
                break;
            case 1:
                if(this.a.isFinished()) {
                    this.b.c();
                }
                break;
            case 2:
                int var2 = (int)(this.a(var1) - this.f);
                if(var2 != 0) {
                    this.g();
                    this.b.a(var2);
                    this.f = this.a(var1);
                }
        }

        if(!this.d.onTouchEvent(var1) && var1.getAction() == 1) {
            this.f();
        }

        return true;
    }

    private void a(int var1) {
        this.e();
        this.j.sendEmptyMessage(var1);
    }

    private void e() {
        this.j.removeMessages(0);
        this.j.removeMessages(1);
    }

    private void f() {
        this.b.e();
        this.a(1);
    }

    private void g() {
        if(!this.g) {
            this.g = true;
            this.b.a();
        }

    }

    protected void d() {
        if(this.g) {
            this.b.d();
            this.g = false;
        }

    }

    protected abstract int a();

    protected abstract int b();

    protected abstract float a(MotionEvent var1);

    protected abstract void a(int var1, int var2);

    protected abstract void a(int var1, int var2, int var3);

    public interface a {
        void a(int var1);

        void b();

        void c();

        void a();

        void d();

        void e();
    }
}
