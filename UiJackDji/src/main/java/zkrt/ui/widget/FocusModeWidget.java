package zkrt.ui.widget;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import dji.common.bus.UILibEventBus;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.SetCallback;
import dji.log.DJILog;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBT;
import zkrt.ui.d.UiDA;
import zkrt.ui.internal.BInterNal;

/**
 * Created by jack_xie on 17-6-1.
 */

public class FocusModeWidget extends UiBaseGViewDULFrameLayout implements View.OnClickListener {
    private static final String TAG = "FocusModeWidget";
    private UiCAC widgetAppearances;
    private SettingsDefinitions.FocusMode focusMode;
    private ImageButton backgroundButton;
    private TextView afText;
    private TextView mfText;
    private TextView separator;
    private DJIKey cameraFocusModeKey;

    public FocusModeWidget(Context var1) {
        this(var1, null, 0);
    }

    public FocusModeWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public FocusModeWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }

    @MainThread
    @Keep
    public void onFocusModeChange(SettingsDefinitions.FocusMode var1) {
        if(var1 != null) {
            this.enableWidget();
            if(var1 == SettingsDefinitions.FocusMode.AUTO) {
                this.afText.setTextColor(this.getResources().getColor(R.color.green));
                this.separator.setTextColor(this.getResources().getColor(R.color.green));
                this.mfText.setTextColor(this.getResources().getColor(R.color.white_half));
            } else if(var1 == SettingsDefinitions.FocusMode.MANUAL) {
                this.afText.setTextColor(this.getResources().getColor(R.color.white_half));
                this.separator.setTextColor(this.getResources().getColor(R.color.green));
                this.mfText.setTextColor(this.getResources().getColor(R.color.green));
            } else {
                this.afText.setTextColor(this.getResources().getColor(R.color.white_half));
                this.separator.setTextColor(this.getResources().getColor(R.color.white_half));
                this.mfText.setTextColor(this.getResources().getColor(R.color.white_half));
            }

            DJILog.d("FocusModeWidget", "Update widget focusMode is " + var1);
        } else {
            this.disableWidget();
        }

    }

    @MainThread
    @Keep
    public void performFocusModeAction(SettingsDefinitions.FocusMode var1) {
        if(KeyManager.getInstance() != null) {
            if(var1 != null) {
                KeyManager.getInstance().setValue(this.cameraFocusModeKey, var1, new SetCallback() {
                    public void onSuccess() {
                        FocusModeWidget.this.onFocusModeActionResult((DJIError)null);
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        FocusModeWidget.this.onFocusModeActionResult(var1);
                    }
                });
            }
        }
    }

    @MainThread
    @Keep
    public void onFocusModeActionResult(@Nullable DJIError var1) {
        if(var1 != null) {
            DJILog.d("FocusModeWidget", "Failed to set AF/MF:" + var1.getDescription());
        }

    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.backgroundButton = (ImageButton)this.findViewById(R.id.image_button_background);
        this.afText = (TextView)this.findViewById(R.id.textview_camera_control_af);
        this.separator = (TextView)this.findViewById(R.id.textview_camera_control_separator);
        this.mfText = (TextView)this.findViewById(R.id.textview_camera_control_mf);
        this.backgroundButton.setOnClickListener(this);
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBT();
        }

        return this.widgetAppearances;
    }

    public void initKey() {
        this.cameraFocusModeKey = CameraKey.create("FocusMode");
        this.addDependentKey(this.cameraFocusModeKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.cameraFocusModeKey)) {
            this.focusMode = (SettingsDefinitions.FocusMode)var1;
            UILibEventBus.getInstance().post(new BInterNal.c(this.focusMode));
        }

    }

    private void enableWidget() {
        this.backgroundButton.setEnabled(true);
        this.afText.setAlpha(1.0F);
        this.separator.setAlpha(1.0F);
        this.mfText.setAlpha(1.0F);
    }

    private void disableWidget() {
        this.backgroundButton.setEnabled(false);
        this.afText.setAlpha(0.5F);
        this.separator.setAlpha(0.5F);
        this.mfText.setAlpha(0.5F);
    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.cameraFocusModeKey)) {
            this.onFocusModeChange(this.focusMode);
        }

    }

    public void onClick(View var1) {
        if(this.focusMode == SettingsDefinitions.FocusMode.AUTO) {
            this.focusMode = SettingsDefinitions.FocusMode.MANUAL;
        } else {
            this.focusMode = SettingsDefinitions.FocusMode.AUTO;
        }

        this.performFocusModeAction(this.focusMode);
    }
}
