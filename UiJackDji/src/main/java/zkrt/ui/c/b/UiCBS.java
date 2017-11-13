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

public class UiCBS extends UiCAC {
    private static final UiCAE a;
    private static final UiCAE b;
    private static final UiCAG c;
    private static final UiCAA[] d;

    public UiCBS() {
    }

    @NonNull
    public UiCAG a() {
        return c;
    }

    @NonNull
    public UiCAA[] e() {
        return d;
    }

   /* public int c() {
        return R.layout.widget_focus_exposure_switch;
    }*/

    static {
        a = new UiCAE(85, 1348, 35, 35, R.id.image_button_background);
        b = new UiCAE(93, 1356, 19, 19, R.id.image_button_foreground);
        c = new UiCAG(85, 1348, 35, 35, R.layout.widget_focus_exposure_switch);
        d = new UiCAA[]{b, a};
    }
}
