package zkrt.ui.b;

import android.content.Context;
import android.util.AttributeSet;

import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBA_B;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiBF extends UiBaseCFramelayout {
    private UiCBA_B uiCBA_b;

    public UiBF(Context var1) {
        super(var1, null, 0);
    }

    public UiBF(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    protected UiCAC getWidgetAppearances() {
        if(uiCBA_b == null) {
            uiCBA_b = new UiCBA_B();
        }

        return uiCBA_b;
    }
}
