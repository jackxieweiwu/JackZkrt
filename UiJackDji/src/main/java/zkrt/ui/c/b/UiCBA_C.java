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

public class UiCBA_C extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAG d;
    private static final UiCAA[] e;

    public UiCBA_C() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return e;
    }

    /*public int c() {
        return R.layout.widget_metric;
    }*/

    static {
        a = new UiCAG(594, -226, 212, 42, R.layout.list_item_view);
        b = new UiCAF(604, -214, 85, 18, R.id.list_item_title, "BlackAndWhite", "Roboto-Regular");
        c = new UiCAG(752, -216, 41, 21, R.id.list_item_value_switch_button);
        d = new UiCAG(594, -185, 212, 1, R.id.list_item_divider);
        e = new UiCAA[]{b, c, d};
    }
}
