package antistatic.spinnerwheel;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by jack_xie on 17-6-1.
 */

public class AntiSpinnerJ extends AntiSpinnerI {
    public AntiSpinnerJ(Context var1, a var2) {
        super(var1, var2);
    }

    protected int a() {
        return this.a.getCurrY();
    }

    protected int b() {
        return this.a.getFinalY();
    }

    protected float a(MotionEvent var1) {
        return var1.getY();
    }

    protected void a(int var1, int var2) {
        this.a.startScroll(0, 0, 0, var1, var2);
    }

    protected void a(int var1, int var2, int var3) {
        int var4 = 2147483647;
        int var5 = -2147483647;
        this.a.fling(0, var1, 0, -var3, 0, 0, -2147483647, 2147483647);
    }
}
