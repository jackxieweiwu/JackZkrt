package zkrt.ui.c.a;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

import dji.thirdparty.afinal.core.Arrays;

/**
 * Created by jack_xie on 17-5-31.
 */

public abstract class UiCAB extends UiCAC {

    private float a;
    private UiCAA[] b;
    private UiCAG c;
    private View d;
    private ArrayList<View> e;
    private int f;
    private int g;

    public UiCAB() {
    }

    @NonNull
    public abstract UiCAG a();

    public abstract void a(float var1);

    public abstract float b();

    protected UiCAG c() {
        return null;
    }

    private void l() {
        UiCAG var1 = this.c();
        this.c = var1.g().c(var1.b() - this.f).d(var1.c() - this.g).a();
        this.n();
    }

    @NonNull
    protected UiCAG d() {
        this.l();
        return this.c;
    }

    @NonNull
    public abstract UiCAA[] e();

    public ArrayList<View> f() {
        if(this.e == null) {
            this.m();
        }
        return this.e;
    }

    private void m() {
        ArrayList var1 = new ArrayList();
        if(this.d != null) {
            UiCAA[] var2 = this.e();
            UiCAA[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                UiCAA var6 = var3[var5];
                View var7 = this.d.findViewById(var6.a());
                var1.add(var7);
            }
        }

        this.e = var1;
    }

    @Nullable
    protected UiCAA[][] g() {
        return null;
    }

    @Nullable
    protected UiCAA[][] h() {
        return null;
    }

    public View a(LayoutInflater var1, ViewGroup var2) {
        int a = this.a().a();
        this.d = var1.inflate(a, var2);
        return this.d;
    }

    public void a(int var1, int var2) {
        UiCAG var3 = this.a();
        int var4 = var3.b();
        int var5 = var3.c();
        if(1.0F * (float)var2 / (float)var5 < 1.0F * (float)var1 / (float)var4) {
            this.a = 1.0F * (float)var2 / (float)var5;
        } else {
            this.a = 1.0F * (float)var1 / (float)var4;
        }

        this.a(this.a);
    }

    public void a(View var1, UiCAA var2) {
        UiCAG var3 = this.a();
        int var4 = (int)((float)(var2.d() - var3.d()) * this.a);
        int var5 = (int)((float)(var2.e() - var3.e()) * this.a);
        int var6 = var4 + this.a(var2);
        int var7 = var5 + this.b(var2);
        var1.layout(var4, var5, var6, var7);
    }

    public int a(UiCAA var1) {
        return (int)((float)var1.b() * this.a);
    }

    public int b(UiCAA var1) {
        return (int)((float)var1.c() * this.a);
    }

    public void i() {
        UiCAA[][] var1 = this.g();
        if(var1 != null) {
            this.a(var1, true);
        }

        var1 = this.h();
        if(var1 != null) {
            this.a(var1, false);
        }

        this.m();
    }

    private int a(UiCAA var1, UiCAA var2) {
        return var1 != null?var2.d() - (var1.d() + var1.b()) + var2.b():var2.d() - this.a().d() + var2.b();
    }

    private int b(UiCAA var1, UiCAA var2) {
        return var1 != null?var2.e() - (var1.e() + var1.c()) + var2.c():var2.e() - this.a().e() + var2.c();
    }

    private void a(UiCAA[][] var1, boolean var2) {
        ArrayList var3 = new ArrayList();
        if(var1 != null && this.d != null) {
            Integer[] var4 = new Integer[var1.length];
            Arrays.fill(var4, Integer.valueOf(0));

            for(int var5 = 0; var5 < var1.length; ++var5) {
                UiCAA[] var6 = var1[var5];
                int var7 = 0;
                UiCAA var8 = null;
                UiCAA[] var9 = var6;
                int var10 = var6.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    UiCAA var12 = var9[var11];
                    View var13 = this.d.findViewById(var12.a());
                    if(var13 != null && var13.getVisibility() == View.GONE) {
                        if(var2) {
                            var7 += this.a(var8, var12);
                        } else {
                            var7 += this.b(var8, var12);
                        }
                    } else {
                        UiCAA var14 = this.c(var12.a());
                        if(var14 == null) {
                            var14 = var12;
                        }

                        if(var2) {
                            this.a(var3, var14.f().a(var12.d() - var7).a());
                        } else {
                            this.a(var3, var14.f().b(var12.e() - var7).a());
                        }
                    }

                    var8 = var12;
                }

                var4[var5] = Integer.valueOf(var7);
            }

            if(var2) {
                this.f = ((Integer) Collections.min(Arrays.asList(var4))).intValue();
            } else {
                this.g = ((Integer)Collections.min(Arrays.asList(var4))).intValue();
            }

            this.l();
        }

        this.b = (UiCAA[])var3.toArray(new UiCAA[var3.size()]);
    }

    @Nullable
    private UiCAA c(int var1) {
        if(this.b != null) {
            UiCAA[] var2 = this.b;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                UiCAA var5 = var2[var4];
                if(var5.a() == var1) {
                    return var5;
                }
            }
        }

        return null;
    }

    private void a(@NonNull ArrayList<UiCAA> var1, UiCAA var2) {
        boolean var3 = false;

        for(int var4 = 0; var4 < var1.size(); ++var4) {
            if(var1.get(var4).a() == var2.a()) {
                var3 = true;
                var1.set(var4, var2);
            }
        }

        if(!var3) {
            var1.add(var2);
        }

    }

    public UiCAA[] j() {
        if(this.b == null) {
            this.i();
        }

        return this.b;
    }

    public float k() {
        UiCAA var1 = this.a();
        return (float)var1.b() / (float)var1.c();
    }

    public int a(int var1) {
        UiCAG var2 = this.a();
        return var1 * var2.b() / var2.c();
    }

    public int b(int var1) {
        UiCAG var2 = this.a();
        return var1 * var2.c() / var2.b();
    }

    private void n() {
        if(this.d != null) {
            UiCAG var1 = this.c;
            int var2 = (int) TypedValue.applyDimension(1, (float)var1.c(), this.d.getResources().getDisplayMetrics());
            int var3 = (int)TypedValue.applyDimension(1, (float)var1.b(), this.d.getResources().getDisplayMetrics());
            this.a(this.d, var3, var2);
        }

    }

    private void a(View var1, int var2, int var3) {
        try {
            Constructor var4 = var1.getLayoutParams().getClass().getDeclaredConstructor(new Class[]{Integer.TYPE, Integer.TYPE});
            if(this.b() != 0.0F) {
                float var5 = var1.getContext().getResources().getDisplayMetrics().density;
                var1.setLayoutParams((ViewGroup.LayoutParams)var4.newInstance(new Object[]{Integer.valueOf((int)((float)var2 * this.b() / var5)), Integer.valueOf((int)((float)var3 * this.b() / var5))}));
            } else {
                var1.setLayoutParams((ViewGroup.LayoutParams)var4.newInstance(new Object[]{Integer.valueOf(var2), Integer.valueOf(var3)}));
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }
}
