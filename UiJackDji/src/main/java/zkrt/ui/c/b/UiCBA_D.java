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

public class UiCBA_D extends UiCAC {
    private static final UiCAE a;
    private static final UiCAE b;
    private static final UiCAE c;
    private static final UiCAG d;
    private static final UiCAA[] e;

    public UiCBA_D() {
    }

    @NonNull
    public UiCAG a() {
        return d;
    }

    @NonNull
    public UiCAA[] e() {
        return e;
    }

    static {
        a = new UiCAE(2027, -215, 40, 20, R.id.switch_button);
        b = new UiCAE(2025, -229, 10, 8, R.id.imageview_picture_icon);
        c = new UiCAE(2057, -229, 11, 7, R.id.imageview_video_icon);
        d = new UiCAG(2025, -230, 44, 36, R.layout.widget_capture_switch);
        e = new UiCAA[]{a, b, c};
    }
}
