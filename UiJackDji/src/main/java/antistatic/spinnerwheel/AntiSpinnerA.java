package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dji.log.DJILog;
import dji.midware.ui.antistatic.spinnerwheel.a.AnSpinner_C;
import zkrt.ui.R;

/**
 * Created by jack_xie on 17-5-31.
 */

public abstract class AntiSpinnerA extends View{
    private static int m = -1;
    private final String n;
    protected int a;
    protected int b;
    protected boolean c;
    protected boolean d;
    protected AntiSpinnerI e;
    protected boolean f;
    protected int g;
    protected LinearLayout h;
    protected int i;
    protected AnSpinner_C j;
    protected int k;
    protected int l;
    private AntiSpinnerH o;
    private List<AntiSpinnerD> p;
    private List<AntiSpinnerF> q;
    private List<AntiSpinnerE> r;
    private DataSetObserver s;

    public AntiSpinnerA(Context context, @Nullable AttributeSet var2, int var3) {
        super(context, var2);
        this.n = a.class.getName() + " #" + ++m;
        this.a = 0;
        this.o = new AntiSpinnerH(this);
        this.p = new LinkedList();
        this.q = new LinkedList();
        this.r = new LinkedList();
        this.a(var2, var3);
        this.a(context);
    }

    protected void a(AttributeSet var1, int var2) {
        TypedArray var3 = this.getContext().obtainStyledAttributes(var1, R.styleable.AbstractWheelView, var2, 0);
        this.b = var3.getInt(R.styleable.AbstractWheelView_visibleItems, 4);
        this.c = var3.getBoolean(R.styleable.AbstractWheelView_isAllVisible, false);
        this.d = var3.getBoolean(R.styleable.AbstractWheelView_isCyclic, false);
        var3.recycle();
    }

    protected void a(Context var1) {
        this.s = new DataSetObserver() {
            public void onChanged() {
                AntiSpinnerA.this.a(false);
            }

            public void onInvalidated() {
                AntiSpinnerA.this.a(true);
            }
        };
        this.e = this.a(new AntiSpinnerI.a() {
            public void a() {
                AntiSpinnerA.this.f = true;
                AntiSpinnerA.this.h();
                AntiSpinnerA.this.a();
            }

            public void b() {
                AntiSpinnerA.this.b();
            }

            public void c() {
                if(!AntiSpinnerA.this.f) {
                    AntiSpinnerA.this.c();
                }

            }

            public void a(int var1) {
                AntiSpinnerA.this.c(var1);
                int var2 = AntiSpinnerA.this.getBaseDimension();
                if(AntiSpinnerA.this.g > var2) {
                    AntiSpinnerA.this.g = var2;
                    AntiSpinnerA.this.e.c();
                } else if(AntiSpinnerA.this.g < -var2) {
                    AntiSpinnerA.this.g = -var2;
                    AntiSpinnerA.this.e.c();
                }

                DJILog.d(AntiSpinnerA.this.n, "OnScroll + " + var1);
            }

            public void d() {
                if(AntiSpinnerA.this.f) {
                    AntiSpinnerA.this.i();
                    AntiSpinnerA.this.f = false;
                    AntiSpinnerA.this.d();
                }

                AntiSpinnerA.this.g = 0;
                AntiSpinnerA.this.invalidate();
            }

            public void e() {
                if(Math.abs(AntiSpinnerA.this.g) > 1) {
                    AntiSpinnerA.this.e.b(AntiSpinnerA.this.g, 0);
                }

            }
        });
    }

    public Parcelable onSaveInstanceState() {
        Parcelable var1 = super.onSaveInstanceState();
        AntiSpinnerA.a var2 = new AntiSpinnerA.a(var1);
        var2.a = this.getCurrentItem();
        return var2;
    }

    public void onRestoreInstanceState(Parcelable var1) {
        if(!(var1 instanceof AntiSpinnerA.a)) {
            super.onRestoreInstanceState(var1);
        } else {
            AntiSpinnerA.a var2 = (AntiSpinnerA.a)var1;
            super.onRestoreInstanceState(var2.getSuperState());
            this.a = var2.a;
            this.postDelayed(new Runnable() {
                public void run() {
                    AntiSpinnerA.this.a(false);
                }
            }, 100L);
        }
    }

    protected abstract void a(int var1, int var2);

    protected abstract AntiSpinnerI a(AntiSpinnerI.a var1);

    protected void a() {
    }

    protected void b() {
    }

    protected void c() {
    }

    protected void d() {
    }

    public void setInterpolator(Interpolator var1) {
        this.e.a(var1);
    }

    public void b(int var1, int var2) {
        int var3 = var1 * this.getItemDimension() - this.g;
        this.b();
        this.e.b(var3, var2);
    }

    private void c(int var1) {
        this.g += var1;
        int var2 = this.getItemDimension();
        int var3 = this.g / var2;
        int var4 = this.a - var3;
        int var5 = this.j.b();
        int var6 = this.g % var2;
        if(Math.abs(var6) <= var2 / 2) {
            var6 = 0;
        }

        if(this.d && var5 > 0) {
            if(var6 > 0) {
                --var4;
                ++var3;
            } else if(var6 < 0) {
                ++var4;
                --var3;
            }

            while(var4 < 0) {
                var4 += var5;
            }

            var4 %= var5;
        } else if(var4 < 0) {
            var3 = this.a;
            var4 = 0;
        } else if(var4 >= var5) {
            var3 = this.a - var5 + 1;
            var4 = var5 - 1;
        } else if(var4 > 0 && var6 > 0) {
            --var4;
            ++var3;
        } else if(var4 < var5 - 1 && var6 < 0) {
            ++var4;
            --var3;
        }

        int var7 = this.g;
        if(var4 != this.a) {
            this.a(var4, false);
        } else {
            this.invalidate();
        }

        int var8 = this.getBaseDimension();
        this.g = var7 - var3 * var2;
        if(this.g > var8) {
            this.g = this.g % var8 + var8;
        }

    }

    protected abstract int getBaseDimension();

    protected abstract int getItemDimension();

    protected abstract float a(MotionEvent var1);

    protected abstract void e();

    protected abstract void f();

    protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        if(var1) {
            int var6 = var4 - var2;
            int var7 = var5 - var3;
            this.f();
            if(this.l != var6 || this.k != var7) {
                this.a(this.getMeasuredWidth(), this.getMeasuredHeight());
            }

            this.l = var6;
            this.k = var7;
        }

    }

    public void a(boolean var1) {
        if(var1) {
            this.o.c();
            if(this.h != null) {
                this.h.removeAllViews();
            }

            this.g = 0;
        } else if(this.h != null) {
            this.o.a(this.h, this.i, new AntiSpinnerC());
        }

        this.invalidate();
    }

    public int getVisibleItems() {
        return this.b;
    }

    public void setVisibleItems(int var1) {
        this.b = var1;
    }

    public void setAllItemsVisible(boolean var1) {
        this.c = var1;
        this.a(false);
    }

    public AnSpinner_C getViewAdapter() {
        return this.j;
    }

    public void setViewAdapter(AnSpinner_C var1) {
        if(this.j != null) {
            this.j.b(this.s);
        }

        this.j = var1;
        if(this.j != null) {
            this.j.a(this.s);
        }

        this.a(true);
    }

    public int getCurrentItem() {
        return this.a;
    }

    public void a(int var1, boolean var2) {
        if(this.j != null && this.j.b() != 0) {
            int var3 = this.j.b();
            if(var1 < 0 || var1 >= var3) {
                if(!this.d) {
                    return;
                }

                while(var1 < 0) {
                    var1 += var3;
                }

                var1 %= var3;
            }

            if(var1 != this.a) {
                int var4;
                if(var2) {
                    var4 = var1 - this.a;
                    if(this.d) {
                        int var5 = var3 + Math.min(var1, this.a) - Math.max(var1, this.a);
                        if(var5 < Math.abs(var4)) {
                            var4 = var4 < 0?var5:-var5;
                        }
                    }

                    this.b(var4, 0);
                } else {
                    this.g = 0;
                    var4 = this.a;
                    this.a = var1;
                    this.c(var4, this.a);
                    this.invalidate();
                }
            }

        }
    }

    public void setCurrentItem(int var1) {
        this.a(var1, false);
    }

    public boolean g() {
        return this.d;
    }

    public void setCyclic(boolean var1) {
        this.d = var1;
        this.a(false);
    }

    public void a(AntiSpinnerD var1) {
        this.p.add(var1);
    }

    protected void c(int var1, int var2) {
        Iterator var3 = this.p.iterator();

        while(var3.hasNext()) {
            AntiSpinnerD var4 = (AntiSpinnerD)var3.next();
            var4.a(this, var1, var2);
        }

    }

    public void a(AntiSpinnerF var1) {
        this.q.add(var1);
    }

    protected void h() {
        Iterator var1 = this.q.iterator();

        while(var1.hasNext()) {
            AntiSpinnerF var2 = (AntiSpinnerF)var1.next();
            var2.a(this);
        }

    }

    protected void i() {
        Iterator var1 = this.q.iterator();

        while(var1.hasNext()) {
            AntiSpinnerF var2 = (AntiSpinnerF)var1.next();
            var2.b(this);
        }

    }

    protected void a(int var1) {
        Iterator var2 = this.r.iterator();

        while(var2.hasNext()) {
            AntiSpinnerE var3 = (AntiSpinnerE)var2.next();
            var3.a(this, var1);
        }

    }

    protected boolean j() {
        AntiSpinnerC var2 = this.getItemsRange();
        boolean var1;
        int var3;
        if(this.h != null) {
            var3 = this.o.a(this.h, this.i, var2);
            var1 = this.i != var3;
            this.i = var3;
        } else {
            this.e();
            var1 = true;
        }

        if(!var1) {
            var1 = this.i != var2.a() || this.h.getChildCount() != var2.c();
        }

        if(this.i > var2.a() && this.i <= var2.b()) {
            for(var3 = this.i - 1; var3 >= var2.a() && this.b(var3, true); this.i = var3--) {
                ;
            }
        } else {
            this.i = var2.a();
        }

        var3 = this.i;

        for(int var4 = this.h.getChildCount(); var4 < var2.c(); ++var4) {
            if(!this.b(this.i + var4, false) && this.h.getChildCount() == 0) {
                ++var3;
            }
        }

        this.i = var3;
        return var1;
    }

    private AntiSpinnerC getItemsRange() {
        int var1;
        int var2;
        if(this.c) {
            var1 = this.getBaseDimension();
            var2 = this.getItemDimension();
            if(var2 != 0) {
                this.b = var1 / var2 + 1;
            }
        }

        var1 = this.a - this.b / 2;
        var2 = var1 + this.b - (this.b % 2 == 0?0:1);
        if(this.g != 0) {
            if(this.g > 0) {
                --var1;
            } else {
                ++var2;
            }
        }

        if(!this.g()) {
            if(var1 < 0) {
                var1 = 0;
            }

            if(this.j == null) {
                var2 = 0;
            } else if(var2 > this.j.b()) {
                var2 = this.j.b();
            }
        }

        return new AntiSpinnerC(var1, var2 - var1 + 1);
    }

    protected boolean b(int var1) {
        return this.j != null && this.j.b() > 0 && (this.d || var1 >= 0 && var1 < this.j.b());
    }

    private boolean b(int var1, boolean var2) {
        View var3 = this.d(var1);
        if(var3 != null) {
            if(var2) {
                this.h.addView(var3, 0);
            } else {
                this.h.addView(var3);
            }

            return true;
        } else {
            return false;
        }
    }

    private View d(int var1) {
        if(this.j != null && this.j.b() != 0) {
            int var2 = this.j.b();
            if(!this.b(var1)) {
                return this.j.a(this.o.b(), this.h);
            } else {
                while(var1 < 0) {
                    var1 += var2;
                }

                var1 %= var2;
                return this.j.a(var1, this.o.a(), this.h);
            }
        } else {
            return null;
        }
    }

    public boolean onTouchEvent(MotionEvent var1) {
        if(this.isEnabled() && this.getViewAdapter() != null) {
            switch(var1.getAction()) {
                case 0:
                case 2:
                    if(this.getParent() != null) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case 1:
                    if(!this.f) {
                        int var2 = (int)this.a(var1) - this.getBaseDimension() / 2;
                        if(var2 > 0) {
                            var2 += this.getItemDimension() / 2;
                        } else {
                            var2 -= this.getItemDimension() / 2;
                        }

                        int var3 = var2 / this.getItemDimension();
                        if(var3 != 0 && this.b(this.a + var3)) {
                            this.a(this.a + var3);
                        }
                    }
            }

            return this.e.b(var1);
        } else {
            return true;
        }
    }


    static class a extends BaseSavedState {
        int a;
        public static final Creator<AntiSpinnerA.a> CREATOR = new Creator<AntiSpinnerA.a>() {
            @Override
            public AntiSpinnerA.a createFromParcel(Parcel source) {
                return new AntiSpinnerA.a(source);
            }

            @Override
            public AntiSpinnerA.a[] newArray(int size) {
                return new AntiSpinnerA.a[size];
            }
        };

        a(Parcelable var1) {
            super(var1);
        }

        private a(Parcel var1) {
            super(var1);
            this.a = var1.readInt();
        }

        public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeInt(this.a);
        }
    }
}
