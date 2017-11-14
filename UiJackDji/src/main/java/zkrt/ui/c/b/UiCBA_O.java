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

public class UiCBA_O extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAA[] c;

    public UiCBA_O() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return c;
    }


    static {
        a = new UiCAG(205, 308, 22, 20, R.layout.widget_signal);
        b = new UiCAE(208, 314, 16, 11, R.id.imageview_signal_icon);
        c = new UiCAA[]{b};
    }
}
