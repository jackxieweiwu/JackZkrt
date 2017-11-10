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

public class UiCBH extends UiCAC {
    private static final UiCAF a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAA[] d;

    public UiCBH() {
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
        return R.layout.widget_camera_action;
    }*/

    static {
        a = new UiCAF(167, 1598, 31, 9, R.id.TextView_camera_info_title, "SHUTTER", "Roboto-Regular");
        b = new UiCAF(167, 1608, 45, 13, R.id.TextView_camera_info_value, "UNKNOWN", "Roboto-Regular");
        c = new UiCAG(159, 1594, 60, 30, R.layout.widget_camera_info);
        d = new UiCAA[]{a, b};
    }
}
