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

public class UiCBM extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAF c;
    private static final UiCAF d;
    private static final UiCAA[] e;

    public UiCBM() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return e;
    }

    /*public int c() {
        return R.layout.widget_compass;
    }*/

    static {
        a = new UiCAG(1137, -411, 71, 16, R.layout.widget_metric);
        b = new UiCAF(1139, -408, 11, 11, R.id.textview_metrics_title, "H:", "Roboto-Regular");
        c = new UiCAF(1152, -411, 45, 14, R.id.textview_metrics_value, "9999.9", "Roboto-Regular");
        d = new UiCAF(1199, -408, 9, 11, R.id.textview_metrics_unit, "M", "Roboto-Regular");
        e = new UiCAA[]{b, c, d};
    }
}
