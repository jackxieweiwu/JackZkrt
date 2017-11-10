package zkrt.ui.b;

import android.content.Context;

import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_A;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiBE extends UiBaseCFramelayout {
    private UiCBA_A uiCBA_a;

    public UiBE(Context var1) {
        super(var1, null, 0);
    }

    protected UiCAC getWidgetAppearances() {
        if(uiCBA_a == null) {
            uiCBA_a = new UiCBA_A();
        }

        return uiCBA_a;
    }
}
