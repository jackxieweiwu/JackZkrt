package zkrt.ui.c.b;

import android.support.annotation.NonNull;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAF;
import zkrt.ui.c.a.UiCAG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCBA_L extends UiCAC {

    private static final UiCAG a;
    private static final UiCAG b;
    private static final UiCAF c;
    private static final UiCAF d;
    private static final UiCAF e;
    private static final UiCAA[] f;

    public UiCBA_L() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return f;
    }

    /*public int c() {
        return R.layout.widget_rc_video_signal;
    }*/

    static {
        a = new UiCAG(1137, -411, 71, 16, R.layout.widget_metric);
        b = new UiCAG(1137, -409, 8, 12, R.id.imageview_metrics_icon);
        c = new UiCAF(1145, -408, 19, 11, R.id.textview_metrics_title, "H.S:", "Roboto-Regular");
        d = new UiCAF(1164, -411, 25, 14, R.id.textview_metrics_value, "99.9", "Roboto-Regular");
        e = new UiCAF(1189, -408, 19, 11, R.id.textview_metrics_unit, "M/S", "Roboto-Regular");
        f = new UiCAA[]{b, c, d, e};
    }
}
