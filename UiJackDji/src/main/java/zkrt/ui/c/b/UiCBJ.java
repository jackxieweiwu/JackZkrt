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

public class UiCBJ extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAF c;
    private static final UiCAA[] d;

    public UiCBJ() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return d;
    }

   /* public int c() {
        return R.layout.list_item_view;
    }*/

    static {
        a = new UiCAG(159, 1594, 70, 30, R.layout.widget_camera_info);
        b = new UiCAF(167, 1598, 31, 9, R.id.TextView_camera_info_title, "SHUTTER", "Roboto-Regular");
        c = new UiCAF(167, 1608, 55, 13, R.id.TextView_camera_info_value, "Incandescent", "Roboto-Regular");
        d = new UiCAA[]{b, c};
    }
}
