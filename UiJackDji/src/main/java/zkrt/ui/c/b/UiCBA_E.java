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

public class UiCBA_E extends UiCAC {
    private static final UiCAG a;
    private static final UiCAF b;
    private static final UiCAG c;
    private static final UiCAF d;
    private static final UiCAG e;
    private static final UiCAG f;
    private static final UiCAG g;
    private static final UiCAA[] h;


    public UiCBA_E() {
    }

    @NonNull
    public UiCAG a() {
        return g;
    }

    @NonNull
    public UiCAA[] e() {
        return h;
    }

    /*public int c() {
        return R.layout.precheck_list_item_view;
    }*/

    static {
        a = new UiCAG(4572, 3568, 20, 20, R.id.list_item_title_icon);
        b = new UiCAF(4608, 3567, 200, 22, R.id.list_item_title, "Aircraft Battery Temperature", "Roboto-Regular");
        c = new UiCAG(4956, 3558, 98, 40, R.id.action_button);
        d = new UiCAF(4876, 3567, 291, 22, R.id.list_item_value, "Aircraft Battery Temperature Temperature", "Roboto-Regular");
        e = new UiCAG(5187, 3571, 8, 14, R.id.list_item_arrow);
        f = new UiCAG(4548, 3599, 667, 1, R.id.list_item_divider);
        g = new UiCAG(4548, 3556, 667, 44, R.layout.precheck_list_item_view);
        h = new UiCAA[]{a, b, c, d, e, f};
    }
}
