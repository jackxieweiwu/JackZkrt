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

public class UiCBX extends UiCAC {
    private static final UiCAF a;
    private static final UiCAF b;
    private static final UiCAF c;
    private static final UiCAG d;
    private static final UiCAA[] e;

    public UiCBX() {
    }

    @NonNull
    public UiCAG a() {
        return d;
    }

    @NonNull
    public UiCAA[] e() {
        return e;
    }

    /*public int c() {
        return R.layout.expandable_view_child_seekbar;
    }*/

    static {
        a = new UiCAF(1553, 223, 44, 40, R.id.child_value1, "130fps", "Roboto-Regular");
        b = new UiCAF(1624, 223, 44, 40, R.id.child_value2, "130fps", "Roboto-Regular");
        c = new UiCAF(1696, 223, 44, 40, R.id.child_value3, "130fps", "Roboto-Regular");
        d = new UiCAG(1540, 222, 212, 42, R.layout.expandable_view_child);
        e = new UiCAA[]{a, b, c};
    }
}
