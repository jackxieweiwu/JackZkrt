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

public class UiCBF extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAE c;
    private static final UiCAE d;
    private static final UiCAF e;
    private static final UiCAA[] f;

    public UiCBF() {
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
        return R.layout.widget_button;
    }*/

    static {
        a = new UiCAG(2018, 0, 60, 80, R.layout.widget_capture);
        b = new UiCAE(2018, 0, 60, 60, R.id.image_button_background);
        c = new UiCAE(2028, 10, 40, 40, R.id.image_button_foreground);
        d = new UiCAE(2034, 18, 28, 24, R.id.image_button_foreground_top);
        e = new UiCAF(2018, 60, 60, 18, R.id.textview_camera_controll_videotime, "00:00:00", "Roboto-Medium");
        f = new UiCAA[]{b, c, d, e};
    }
}
