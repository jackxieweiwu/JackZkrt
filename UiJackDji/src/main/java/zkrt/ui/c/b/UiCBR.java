package zkrt.ui.c.b;

import android.support.annotation.NonNull;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAE;
import zkrt.ui.c.a.UiCAF;
import zkrt.ui.c.a.UiCAG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCBR extends UiCAC {
    private static final UiCAE a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAA[] d;

    public UiCBR() {
    }

    @NonNull
    public UiCAG a() {
        return c;
    }

    @NonNull
    public UiCAA[] e() {
        return d;
    }

    /*public int c() {
        return R.layout.widget_flightmode;
    }*/

    static {
        a = new UiCAE(184, 2, 18, 18, R.id.imageview_flight_mode_icon);
        b = new UiCAF(207, 4, 76, 15, R.id.textview_flight_mode_string, "WIFI-Joystick", "Roboto-Medium");
        c = new UiCAG(182, 0, 103, 22, R.layout.widget_flightmode);
        d = new UiCAA[]{a, b};
    }
}
