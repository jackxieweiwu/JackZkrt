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

public class UiCBG extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAG d;
    private static final UiCAG e;
    private static final UiCAG f;
    private static final UiCAG g;
    private static final UiCAG h;
    private static final UiCAG i;
    private static final UiCAA[] j;

    public UiCBG() {
    }

    @NonNull
    public UiCAG a() {
        return g;
    }

    @NonNull
    public UiCAA[] e() {
        return j;
    }

   /* public int c() {
        return R.layout.widget_capture;
    }*/

    static {
        /*b = new UiCAE(2018, 0, 60, 60, R.id.image_button_background);
        c = new UiCAE(2028, 10, 40, 40, R.id.image_button_foreground);
        d = new UiCAE(2034, 18, 28, 24, R.id.image_button_foreground_top);
        e = new UiCAF(2018, 60, 60, 18, R.id.textview_camera_controll_videotime, "00:00:00", "Roboto-Medium");
        f = new UiCAA[]{b, c, d, e};*/

        a = new UiCAG(520, 430, 60, 45, R.id.widget_camera_menu_background);
        b = new UiCAF(533, 446, 33, 14, R.id.widget_camera_menu, "MENU", "Roboto-Regular");
        c = new UiCAG(530, 491, 44, 36, R.id.widget_camera_capture_switch);
        d = new UiCAG(520, 548, 60, 80, R.id.widget_camera_capture);
        e = new UiCAG(520, 641, 60, 45, R.id.widget_camera_exposure_status);
        f = new UiCAG(520, 641, 60, 45, R.id.widget_camera_exposure_status_background);
        g = new UiCAG(520, 430, 60, 256, R.layout.widget_camera_action);
        h = new UiCAG(520, 474, 60, 1, R.id.top_line_camera_panel);
        i = new UiCAG(520, 641, 60, 1, R.id.bot_line_camera_panel);
        j = new UiCAA[]{a, b, c, d, e, f, h, i};
    }
}
