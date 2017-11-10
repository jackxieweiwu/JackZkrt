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

public class UiCBE extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAE c;
    private static final UiCAA[] d;

    public UiCBE() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return d;
    }

/*    public int c() {
        return R.layout.widget_battery;
    }*/

    static {
        a = new UiCAG(0, 0, 54, 54, R.layout.widget_button);
        b = new UiCAE(1, 1, 52, 52, R.id.image_button_background);
        c = new UiCAE(12, 13, 30, 28, R.id.image_button_foreground);
        d = new UiCAA[]{b, c};
    }
}
