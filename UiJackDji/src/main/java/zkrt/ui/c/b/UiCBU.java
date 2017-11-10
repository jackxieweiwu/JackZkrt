package zkrt.ui.c.b;

import android.support.annotation.NonNull;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAA;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAE;
import zkrt.ui.c.a.UiCAF;
import zkrt.ui.c.a.UiCAG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCBU extends UiCAC {
    private static final UiCAF a;
    private static final UiCAF b;
    private static final UiCAE c;
    private static final UiCAF d;
    private static final UiCAA[] e;
    private static final UiCAG f;

    public UiCBU() {
    }

    @NonNull
    public UiCAG a() {
        return f;
    }

    @NonNull
    public UiCAA[] e() {
        return e;
    }

    /*public int c() {
        return R.layout.widget_gps_signal;
    }*/

    static {
        a = new UiCAF(468, 1598, 35, 9, R.id.TextView_card_info_title, "CAPACITY", "Roboto-Regular");
        b = new UiCAF(468, 1608, 45, 13, R.id.TextView_card_info_value, "UNKNOWN", "Roboto-Medium");
        c = new UiCAE(396, 1599, 11, 8, R.id.ImageView_card_type);
        d = new UiCAF(395, 1608, 70, 13, R.id.TextView_image_format_value, "Radiometric JPEG", "Roboto-Medium");
        e = new UiCAA[]{a, b, c, d};
        f = new UiCAG(387, 1594, 130, 30, R.layout.widget_format_and_storage);
    }
}
