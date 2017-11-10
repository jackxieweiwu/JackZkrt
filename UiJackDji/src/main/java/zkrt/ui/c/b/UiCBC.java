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

public class UiCBC extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAG d;
    private static final UiCAG e;
    private static final UiCAA[] f;

    public UiCBC() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return f;
    }

   /* public int c() {
        return R.layout.widget_camera_advanced_setting;
    }*/

    static {
        a = new UiCAG(0, 0, 211, 60, R.layout.widget_aperture_setting);
        b = new UiCAF(8, 0, 44, 10, R.id.textview_aperture_title, "APERTURE", "Roboto-Regular");
        c = new UiCAG(1, 14, 210, 46, R.id.imageview_camera_settings_aperture_background);
        d = new UiCAG(8, 0, 200, 60, R.id.wheelview_camera_settings_aperture);
        e = new UiCAG(100, 50, 10, 7, R.id.imageview_aperture_wheel_position);
        f = new UiCAA[]{b, c, d, e};
    }
}
