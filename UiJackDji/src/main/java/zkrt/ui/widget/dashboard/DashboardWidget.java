package zkrt.ui.widget.dashboard;

import android.content.Context;
import android.util.AttributeSet;

import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBK;
import zkrt.ui.d.UiDA;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DashboardWidget extends UiBaseCFramelayout {
    private UiCAC widgetAppearances;

    public DashboardWidget(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public DashboardWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public DashboardWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBK();
        }
        return this.widgetAppearances;
    }
}
