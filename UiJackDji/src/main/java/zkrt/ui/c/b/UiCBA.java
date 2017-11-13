package zkrt.ui.c.b;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAE;
import zkrt.ui.c.a.UiCAF;
import zkrt.ui.c.a.UiCAG;
import android.support.annotation.NonNull;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCBA extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAE c;
    private static final UiCAF d;
    private static final UiCAA[] e;

    public UiCBA() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return e;
    }

    static {
        a = new UiCAG(85, 1506, 35, 35, R.layout.widget_aelock);
        b = new UiCAE(85, 1506, 35, 35, R.id.image_button_background);
        c = new UiCAE(89, 1518, 8, 11, R.id.imageview_lock);
        d = new UiCAF(100, 1517, 16, 14, R.id.textview_lock_title, "AE", "Roboto-Medium");
        e = new UiCAA[]{b, c, d};
    }
}
