package antistatic.spinnerwheel;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by jack_xie on 17-6-1.
 */

public class AntiSpinnerG extends AntiSpinnerI {
    public AntiSpinnerG(Context var1, a var2) {
        super(var1, var2);
    }

    protected int a() {
        return this.a.getCurrX();
    }

    protected int b() {
        return this.a.getFinalX();
    }

    protected float a(MotionEvent var1) {
        return var1.getX();
    }

    protected void a(int var1, int var2) {
        this.a.startScroll(0, 0, var1, 0, var2);
    }

    protected void a(int var1, int var2, int var3) {
        int var4 = 2147483647;
        int var5 = -2147483647;
        this.a.fling(var1, 0, -var2, 0, -2147483647, 2147483647, 0, 0);
    }
}
