package zkrt.ui.b;

import android.content.Context;

import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBI;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiBA extends UiBF {
    private UiCBI uiCBI;

    public UiBA(Context var1) {
        super(var1, null, 0);
    }

    protected UiCAC getWidgetAppearances() {
        if(uiCBI == null) {
            uiCBI = new UiCBI();
        }
        return uiCBI;
    }
}
