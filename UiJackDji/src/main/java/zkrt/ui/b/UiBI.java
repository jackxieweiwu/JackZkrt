package zkrt.ui.b;

import android.content.Context;

import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_E;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiBI extends UiBaseCFramelayout {
    private UiCAC uiCAC;

    public UiBI(Context var1) {
        super(var1, null, 0);
    }

    protected UiCAC getWidgetAppearances() {
        if(uiCAC == null) {
            uiCAC = new UiCBA_E();
        }

        return uiCAC;
    }
}
