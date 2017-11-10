package zkrt.ui.widget.controls;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import dji.common.bus.UILibEventBus;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBG;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDL;
import zkrt.ui.internal.BInterNal;

/**
 * Created by jack_xie on 17-6-1.
 */

public class CameraControlsWidget extends UiBaseCFramelayout {
    private UiCAC widgetAppearances;
    private ImageView cameraMenuBg;
    private ImageView exposureStatusBg;

    public CameraControlsWidget(Context var1) {
        this(var1, null, 0);
    }

    public CameraControlsWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CameraControlsWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        UiDL.c(this);
        this.setBackground(this.getResources().getDrawable(R.drawable.camera_panel_background));
        this.cameraMenuBg = (ImageView)this.findViewById(R.id.widget_camera_menu_background);
        this.exposureStatusBg = (ImageView)this.findViewById(R.id.widget_camera_exposure_status_background);
        this.cameraMenuBg.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                UILibEventBus.getInstance().post(new BInterNal.a(!CameraControlsWidget.this.cameraMenuBg.isSelected()));
                UILibEventBus.getInstance().post(new BInterNal.b(false));
                UiDL.a(CameraControlsWidget.this.cameraMenuBg);
                UiDL.b(CameraControlsWidget.this.exposureStatusBg);
            }
        });
        this.exposureStatusBg.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                UILibEventBus.getInstance().post(new BInterNal.b(!CameraControlsWidget.this.exposureStatusBg.isSelected()));
                UILibEventBus.getInstance().post(new BInterNal.a(false));
                UiDL.a(CameraControlsWidget.this.exposureStatusBg);
                UiDL.b(CameraControlsWidget.this.cameraMenuBg);
            }
        });
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBG();
        }

        return this.widgetAppearances;
    }
}
