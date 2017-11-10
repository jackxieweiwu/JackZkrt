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

public class UiCBW extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAA[] d;

    public UiCBW() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return d;
    }

    /*public int c() {
        return R.layout.expandable_view_child;
    }*/

    static {
        a = new UiCAG(0, 0, 211, 60, R.layout.widget_iso_setting);
        b = new UiCAF(8, 0, 16, 10, R.id.textview_iso_title, "ISO", "Roboto-Regular");
        c = new UiCAG(0, 14, 211, 46, R.id.seekbar_layout);
        d = new UiCAA[]{c, b};
    }
}
