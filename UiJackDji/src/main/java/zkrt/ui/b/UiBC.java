package zkrt.ui.b;

import android.content.Context;
import android.util.AttributeSet;

import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBY;

/**
 * Created by jack_Xie on 17-6-1.
 */

public class UiBC extends UiBaseCFramelayout {
    private UiCAC uiCAC;

    public UiBC(Context var1) {
        this(var1, null, 0);
    }

    public UiBC(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    protected UiCAC getWidgetAppearances() {
        if(uiCAC == null) {
            uiCAC = new UiCBY();
        }

        return uiCAC;
    }

    protected void onMeasure(int var1, int var2) {
        super.onMeasure(var1, 0);
    }
}
