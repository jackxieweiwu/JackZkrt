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

public class UiCBQ extends UiCAC {
    private static final UiCAE a;
    private static final UiCAG b;
    private static final UiCAA[] c;

    public UiCBQ() {
    }

    @NonNull
    public UiCAG a() {
        return b;
    }

    @NonNull
    public UiCAA[] e() {
        return c;
    }

    /*public int c() {
        return R.layout.widget_exposuresetting;
    }*/

    static {
        a = new UiCAE(20, 11, 21, 21, R.id.image_button_foreground);
        b = new UiCAG(0, 0, 60, 45, R.layout.widget_exposuresetting);
        c = new UiCAA[]{a};
    }
}
