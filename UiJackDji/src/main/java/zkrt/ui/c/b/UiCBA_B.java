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

public class UiCBA_B extends UiCAC {
    private static final UiCAG a;
    private static final UiCAG b;
    private static final UiCAF c;
    private static final UiCAG d;
    private static final UiCAF e;
    private static final UiCAG f;
    private static final UiCAG g;
    private static final UiCAA[] h;

    public UiCBA_B() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return h;
    }


    static {
        a = new UiCAG(594, -226, 212, 42, R.layout.list_item_view);
        b = new UiCAG(613, -220, 30, 30, R.id.list_item_title_icon);
        c = new UiCAF(604, -214, 85, 18, R.id.list_item_title, "BlackAndWhite", "Roboto-Regular");
        d = new UiCAG(754, -220, 30, 30, R.id.list_item_value_icon);
        e = new UiCAF(699, -214, 85, 18, R.id.list_item_value, "BlackAndWhite", "Roboto-Regular");
        f = new UiCAG(791, -210, 6, 10, R.id.list_item_arrow);
        g = new UiCAG(594, -185, 212, 1, R.id.list_item_divider);
        h = new UiCAA[]{b, c, d, e, f, g};
    }
}
