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

public class UiCBN extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAG d;
    private static final UiCAF e;
    private static final UiCAG f;
    private static final UiCAF g;
    private static final UiCAG h;
    private static final UiCAA[] i;

    public UiCBN() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return i;
    }

   /* public int c() {
        return R.layout.widget_ev_setting;
    }*/

    static {
        a = new UiCAG(0, 0, 211, 60, R.layout.widget_ev_setting);
        b = new UiCAF(8, 0, 21, 10, R.id.textview_ev_title, "EV", "Roboto-Regular");
        c = new UiCAG(1, 14, 210, 46, R.id.imageview_camera_settings_ev_background);
        d = new UiCAG(40, 25, 28, 28, R.id.imagebutton_ev_setting_minus);
        e = new UiCAF(80, 27, 50, 19, R.id.textview_setting_ev_value, "+0.7", "Roboto-Medium");
        f = new UiCAG(143, 25, 28, 28, R.id.imagebutton_ev_setting_plus);
        g = new UiCAF(95, 25, 20, 11, R.id.textview_setting_ev_status_value, "+0.7", "Roboto-Regular");
        h = new UiCAG(63, 40, 84, 10, R.id.stripeview_setting_ev_status);
        i = new UiCAA[]{c, b, d, e, f, g, h};
    }
}
