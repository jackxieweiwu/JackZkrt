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

public class UiCBA_M extends UiCAC {
    private static final UiCAG a;
    private static final UiCAE b;
    private static final UiCAE c;
    private static final UiCAA[] d;

    public UiCBA_M() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return d;
    }

   /* public int c() {
        return R.layout.widget_signal;
    }*/

    static {
        a = new UiCAG(202, 850, 38, 22, R.layout.widget_rc_video_signal);
        b = new UiCAE(205, 858, 16, 11, R.id.imageview_rc_video_icon);
        c = new UiCAE(223, 858, 14, 11, R.id.imageview_rc_video_signal);
        d = new UiCAA[]{b, c};
    }
}
