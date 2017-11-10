package zkrt.ui.c.b;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAB;
import zkrt.ui.c.a.UiCAE;
import zkrt.ui.c.a.UiCAF;
import zkrt.ui.c.a.UiCAG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCBD extends UiCAB {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAF c;
    private static final UiCAF d;
    private static final UiCAE e;
    private static final UiCAF f;
    private static final UiCAF g;
    private static final UiCAF h;
    private static final UiCAF i;
    private static final UiCAA[][] j;
    private float k = 0.0F;

    public UiCBD() {
    }

    @NonNull
    public UiCAG c() {
        return a;
    }

    @NonNull
    public UiCAG a() {
        return this.d();
    }

    public void a(float var1) {
        if(this.k == 0.0F) {
            this.k = var1;
        }

    }

    public float b() {
        return this.k;
    }

    @NonNull
    public UiCAA[] e() {
        UiCAA[] var1 = this.j();

        for(int var2 = 0; var2 < var1.length; ++var2) {
            UiCAA var10000 = var1[var2];
        }
        return var1;
    }

    @Nullable
    public UiCAA[][] g() {
        return j;
    }

    /*public int c() {
        return R.layout.widget_aperture_setting;
    }*/

    static {
        /*a = new UiCAF(8, 0, 44, 10, R.id.textview_aperture_title, "APERTURE", "Roboto-Regular");
        b = new UiCAG(1, 14, 210, 46, R.id.imageview_camera_settings_aperture_background);
        c = new UiCAG(8, 0, 200, 60, R.id.wheelview_camera_settings_aperture);
        d = new UiCAG(100, 50, 10, 7, R.id.imageview_aperture_wheel_position);
        e = new UiCAG(0, 0, 211, 60);
        f = new UiCAA[]{a, b, c, d};*/
        a = new UiCAG(201, 1043, 96, 22, R.layout.widget_battery);
        b = new UiCAE(203, 1045, 18, 18, R.id.imageview_battery_icon);
        c = new UiCAF(221, 1046, 34, 16, R.id.textview_battery_value, "100%", "Roboto-Medium");
        d = new UiCAF(255, 1046, 40, 16, R.id.textview_battery_voltage, "13.05 V", "Roboto-Medium");
        e = new UiCAE(203, 1047, 22, 18, R.id.imageview_multi_battery_icon);
        f = new UiCAF(227, 1043, 24, 11, R.id.textview_battery1_value, "100%", "Roboto-Medium");
        g = new UiCAF(227, 1054, 24, 11, R.id.textview_battery2_value, "100%", "Roboto-Medium");
        h = new UiCAF(251, 1043, 40, 10, R.id.textview_battery1_voltage, "13.05 V", "Roboto-Medium");
        i = new UiCAF(251, 1055, 40, 10, R.id.textview_battery2_voltage, "13.05 V", "Roboto-Medium");
        j = new UiCAA[][]{{b, c, d}, {e, f, h}, {e, g, i}};
    }
}
