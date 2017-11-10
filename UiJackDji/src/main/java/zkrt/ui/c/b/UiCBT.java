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

public class UiCBT extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAF c;
    private static final UiCAF d;
    private static final UiCAF e;
    private static final UiCAA[] f;

    public UiCBT() {
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
        return R.layout.widget_format_and_storage;
    }*/

    static {
        a = new UiCAG(85, 1664, 35, 35, R.layout.widget_af_mf_switch);
        b = new UiCAE(85, 1664, 35, 35, R.id.image_button_background);
        c = new UiCAF(87, 1675, 12, 11, R.id.textview_camera_control_af, "MF", "Roboto-Medium");
        d = new UiCAF(106, 1675, 12, 11, R.id.textview_camera_control_mf, "MF", "Roboto-Medium");
        e = new UiCAF(99, 1675, 7, 11, R.id.textview_camera_control_separator, "/", "Roboto-Medium");
        f = new UiCAA[]{b, c, e, d};
    }
}
