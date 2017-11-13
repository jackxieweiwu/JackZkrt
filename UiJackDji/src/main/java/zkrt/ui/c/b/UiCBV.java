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

public class UiCBV extends UiCAC {
    private static final UiCAE a;
    private static final UiCAE b;
    private static final UiCAF c;
    private static final UiCAF d;
    private static final UiCAG e;
    private static final UiCAA[] f;

    public UiCBV() {
    }

    @NonNull
    public UiCAG a() {
        return e;
    }

    @NonNull
    public UiCAA[] e() {
        return f;
    }

    /*public int c() {
        return R.layout.widget_iso_setting;
    }*/

    static {
        a = new UiCAE(189, 129, 16, 15, R.id.imageview_gps_icon);
        b = new UiCAE(206, 133, 14, 11, R.id.imageview_gps_signal);
        c = new UiCAF(205, 129, 10, 7, R.id.textview_satellite_number, "12", "Roboto-Medium");
        d = new UiCAF(221, 137, 4, 7, R.id.textview_gps_type, "R", "Roboto-Medium");
        e = new UiCAG(185, 125, 42, 22, R.layout.widget_gps_signal);
        f = new UiCAA[]{a, b, c, d};
    }
}
