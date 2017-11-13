package zkrt.ui.b;

import android.content.Context;

import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_C;

/**
 * Created by root on 17-6-1.
 */

public class UIBH extends UiBaseCFramelayout {
    private UiCBA_C uiCBA_c;

    public UIBH(Context var1) {
        super(var1, null, 0);
    }

    protected UiCAC getWidgetAppearances() {
        if(uiCBA_c == null) {
            uiCBA_c = new UiCBA_C();
        }

        return uiCBA_c;
    }
}
