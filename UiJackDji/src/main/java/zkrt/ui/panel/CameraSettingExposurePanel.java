package zkrt.ui.panel;

import android.content.Context;
import android.util.AttributeSet;
import dji.common.bus.UILibEventBus;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Action1;
import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBP;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDL;
import zkrt.ui.internal.BInterNal.b;

/**
 * Created by jack_xie on 17-6-1.
 */

public class CameraSettingExposurePanel extends UiBaseCFramelayout {
    private UiCAC widgetAppearances;

    public CameraSettingExposurePanel(Context var1) {
        this(var1, null, 0);
    }

    public CameraSettingExposurePanel(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CameraSettingExposurePanel(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.subscription.add(UILibEventBus.getInstance().register(b.class)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<b>() {
                    @Override
                    public void call(b b) {
                        if(b.a()) {
                            CameraSettingExposurePanel.this.setVisibility(VISIBLE);
                        } else {
                            CameraSettingExposurePanel.this.setVisibility(INVISIBLE);
                        }
                    }
                }));
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        UiDL.c(this);

    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBP();
        }

        return this.widgetAppearances;
    }
}
