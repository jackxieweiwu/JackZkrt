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

public class UiCBL extends UiCAC {
    private static final UiCAG a;
    private static final UiCAG b;
    private static final UiCAG c;
    private static final UiCAE d;
    private static final UiCAE e;
    private static final UiCAE f;
    private static final UiCAE g;
    private static final UiCAE h;
    private static final UiCAE i;
    private static final UiCAE j;
    private static final UiCAA[] k;

    public UiCBL() {
    }

    @NonNull
    public UiCAG a() {
        return a;
    }

    @NonNull
    public UiCAA[] e() {
        return k;
    }

    /*//返回指南针的布局
    public int c() {
        return R.layout.dashboard_compass;
    }*/

    static {
        a = new UiCAG(1029, -485, 98, 98, R.layout.widget_compass);
        b = new UiCAG(1034, -480, 88, 88, R.id.progressbar_compass_sea);
        c = new UiCAG(1034, -480, 88, 88, R.id.visual_compass_view);
        d = new UiCAE(1034, -480, 88, 88, R.id.background_imageview);
        e = new UiCAE(1049, -465, 58, 58, R.id.imageview_inner_circles);
        f = new UiCAE(1074, -440, 8, 8, R.id.compass_home_img);
        g = new UiCAE(1074, -440, 8, 8, R.id.imageview_compass_rc);
        h = new UiCAE(1112, -468, 8, 8, R.id.north_imageview);
        i = new UiCAE(1070, -446, 16, 20, R.id.imageview_compass_aircraft);
        j = new UiCAE(1065, -480, 26, 44, R.id.imageview_gimbal_heading);
        k = new UiCAA[]{d, b, c, e, f, g, h, i, j};
    }
}
