package zkrt.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import zkrt.ui.R;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBH;

/**
 * Created by jack_xie on 17-11-3.
 */

public abstract class UiBaseDView extends UiBaseGViewDULFrameLayout {
    protected TextView valueText;
    private UiCBH widgetAppearances;

    public UiBaseDView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        TextView var4 = this.findViewById(R.id.TextView_camera_info_title);
        this.valueText = this.findViewById(R.id.TextView_camera_info_value);
        var4.setText(this.getTitle());
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBH();
        }
        return this.widgetAppearances;
    }

    protected abstract String getTitle();
}
