package zkrt.ui.c.b;

import android.support.annotation.NonNull;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAE;
import zkrt.ui.c.a.UiCAG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCBK extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAG c;
    private static final UiCAG d;
    private static final UiCAG e;
    private static final UiCAG f;
    private static final UiCAG g;
    private static final UiCAA[] h;

    public UiCBK() {
    }

    @NonNull
    public UiCAG a() {
        return g;
    }

    @NonNull
    public UiCAA[] e() {
        return h;
    }

    /*public int c() {
        return R.layout.widget_camera_info;
    }*/

    static {
        /*a = new UiCAF(167, 1598, 31, 9, R.id.TextView_camera_info_title, "SHUTTER", "Roboto-Regular");
        b = new UiCAF(167, 1608, 55, 13, R.id.TextView_camera_info_value, "Incandescent", "Roboto-Regular");
        c = new UiCAG(159, 1594, 70, 30);
        d = new UiCAA[]{a, b};*/

        a = new UiCAG(1034, -480, 87, 87, R.id.widget_compass);
        b = new UiCAE(1083, -417, 357, 26, R.id.imageview_info_bar_background);
        c = new UiCAG(1132, -413, 71, 16, R.id.widget_altitude);
        d = new UiCAG(1203, -413, 71, 16, R.id.widget_distance);
        e = new UiCAG(1274, -413, 71, 16, R.id.widget_h_speed);
        f = new UiCAG(1354, -413, 71, 16, R.id.widget_v_speed);
        g = new UiCAG(1034, -480, 405, 91, R.layout.dashboard_compass);
        h = new UiCAA[]{a, b, c, d, e, f};
    }
}
