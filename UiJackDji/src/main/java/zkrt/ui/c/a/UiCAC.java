package zkrt.ui.c.a;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jack_xie on 17-5-31.
 */

public abstract class UiCAC {
    private float a;

    public UiCAC() {
    }

    @NonNull
    protected abstract UiCAG a();

    @NonNull
    public abstract UiCAA[] e();

    public View a(LayoutInflater var1, ViewGroup var2) {
        int a  = this.a().a();
        return var1.inflate(a, var2);
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

    public float k() {
        UiCAG var1 = this.a();
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
}
