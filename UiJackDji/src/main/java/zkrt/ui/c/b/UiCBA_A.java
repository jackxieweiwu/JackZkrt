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

public class UiCBA_A extends UiCAC {
    private static final UiCAG a;
    private static final UiCAG b;
    private static final UiCAF c;
    private static final UiCAG d;
    private static final UiCAA[] e;

    public UiCBA_A() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    @Override
    public UiCAA[] e() {
        return e;
    }

    static {
        a = new UiCAG(594, -226, 212, 42, R.layout.list_item_view);
        b = new UiCAG(613, -220, 30, 30, R.id.list_item_title_icon);
        c = new UiCAF(651, -214, 100, 18, R.id.list_item_title, "Radiometric JPEG", "Roboto-Regular");
        d = new UiCAG(594, -185, 212, 1, R.id.list_item_divider);
        e = new UiCAA[]{b, c, d};
    }
}
