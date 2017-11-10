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

public class UiCBY extends UiCAC {
    private static final UiCAF a;
    private static final UiCAG b;
    private static final UiCAG c;
    private static final UiCAA[] d;

    public UiCBY() {
    }

    @NonNull
    public UiCAG a() {
        return c;
    }

    @NonNull
    public UiCAA[] e() {
        return d;
    }

    /*public int c() {
        return R.layout.expandable_view_group;
    }*/

    static {
        a = new UiCAF(1613, 222, 66, 16, R.id.expandable_child_sb_tv, "16000k", "Roboto-Regular");
        b = new UiCAG(1556, 238, 180, 26, R.id.expandable_child_sb);
        c = new UiCAG(1540, 222, 212, 42, R.layout.expandable_view_child_seekbar);
        d = new UiCAA[]{a, b};
    }
}
