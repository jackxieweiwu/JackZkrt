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

public class UiCBO extends UiCAC {
    private static final UiCAG a;
    private static final UiCAG b;
    private static final UiCAF c;
    private static final UiCAE d;
    private static final UiCAG e;
    private static final UiCAF f;
    private static final UiCAG g;
    private static final UiCAF h;
    private static final UiCAG i;
    private static final UiCAF j;
    private static final UiCAA[] k;

    public UiCBO() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return k;
    }


   /* public int c() {
        return R.layout.widget_exposure_mode_setting;
    }*/

    static {
        a = new UiCAG(8, 1250, 160, 30, R.layout.widget_exposure_mode_setting);
        b = new UiCAG(8, 1250, 40, 30, R.id.layout_camera_mode_p);
        c = new UiCAF(17, 1254, 21, 9, R.id.textview_camera_mode_p, "AUTO", "Roboto-Medium");
        d = new UiCAE(20, 1264, 14, 11, R.id.imageview_exposure_automode_icon);
        e = new UiCAG(48, 1250, 40, 30, R.id.layout_camera_mode_s);
        f = new UiCAF(22, 1255, 11, 20, R.id.textview_camera_mode_s, "S", "Roboto-Regular");
        g = new UiCAG(88, 1250, 40, 30, R.id.layout_camera_mode_a);
        h = new UiCAF(22, 1255, 11, 20, R.id.textview_camera_mode_a, "A", "Roboto-Regular");
        i = new UiCAG(128, 1250, 40, 30, R.id.layout_camera_mode_m);
        j = new UiCAF(22, 1255, 15, 20, R.id.textview_camera_mode_m, "M", "Roboto-Regular");
        k = new UiCAA[]{b, c, d, e, f, g, h, i, j};
    }
}
