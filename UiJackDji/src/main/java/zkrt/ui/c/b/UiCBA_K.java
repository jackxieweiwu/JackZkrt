package zkrt.ui.c.b;

import android.support.annotation.NonNull;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAE;
import zkrt.ui.c.a.UiCAG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCBA_K extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAG c;
    private static final UiCAA[] d;

    public UiCBA_K() {
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
        return R.layout.switch_button;
    }*/

    static {
        a = new UiCAG(0, 0, 40, 20, R.layout.switch_button);
        b = new UiCAE(0, 0, 20, 20, R.id.imageview_left);
        c = new UiCAG(0, 0, 40, 20, R.id.layout_track);
        d = new UiCAA[]{b, c};
    }
}
