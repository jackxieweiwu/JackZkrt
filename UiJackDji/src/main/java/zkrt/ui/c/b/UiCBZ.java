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

public class UiCBZ extends UiCAC {
    private static final UiCAG a;
    private static final UiCAG b;
    private static final UiCAG c;
    private static final UiCAF d;
    private static final UiCAG e;
    private static final UiCAF f;
    private static final UiCAA[] g;

    public UiCBZ() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return g;
    }

    /*public int c() {
        return R.layout.list_item_view;
    }*/

    static {
        a = new UiCAG(594, -226, 212, 42, R.layout.expandable_view_group);
        b = new UiCAG(594, -226, 212, 42, R.id.expendable_group_layout);
        c = new UiCAG(613, -220, 30, 30, R.id.expandable_group_icon);
        d = new UiCAF(651, -214, 85, 18, R.id.expandable_group_name, "White Balance", "Roboto-Regular");
        e = new UiCAG(757, -218, 42, 26, R.id.expandable_group_value_bg);
        f = new UiCAF(757, -214, 42, 18, R.id.expandable_group_value, "7s", "Roboto-Italic");
        g = new UiCAA[]{b, c, d, f, e};
    }
}
