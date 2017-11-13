package zkrt.ui.internal.exposure;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import dji.common.camera.ExposureSettings;
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
import zkrt.ui.c.b.UiCBW;
import zkrt.ui.d.UiDB;
import zkrt.ui.d.UiDC;
import zkrt.ui.internal.DULSeekBar;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DULCameraISOSettingWidget extends UiBaseGViewDULFrameLayout implements View.OnClickListener,DULSeekBar.a {
    private UiCAC a;
    private DJIKey b;
    private SettingsDefinitions.ISO c;
    private SettingsDefinitions.ISO[] d;
    private DULSeekBar e;
    private ImageView f;
    private DJIKey g;
    private DJIKey h;
    private boolean i;
    private boolean j;
    private boolean k;
    private boolean l;

    public DULCameraISOSettingWidget(Context var1) {
        super(var1, null, 0);
    }

    public DULCameraISOSettingWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
    }

    public DULCameraISOSettingWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        Resources var4 = var1.getResources();
        int[] var5 = var4.getIntArray(R.array.iso_values);
        this.d = new SettingsDefinitions.ISO[var5.length];

        for(int var6 = 0; var6 < var5.length; ++var6) {
            this.d[var6] = SettingsDefinitions.ISO.find(var5[var6]);
        }

        this.e = (DULSeekBar)this.findViewById(R.id.seekbar_iso);
        this.f = (ImageView)this.findViewById(R.id.button_iso_auto);
        this.e.setMax(this.d.length - 1);
        this.j = false;
        this.e.setProgress(0);
        this.e.a(false);
        this.e.setOnSeekBarChangeListener(this);
        this.f.setOnClickListener(this);
    }

    protected UiCAC getWidgetAppearances() {
        if(this.a == null) {
            this.a = new UiCBW();
        }
        return this.a;
    }

    public void initKey() {
        this.b = CameraKey.create("ISO");
        this.addDependentKey(this.b);
        this.h = CameraKey.create("ExposureSettings");
        this.addDependentKey(this.h);
        this.g = CameraKey.create("ISORange");
        this.addDependentKey(this.g);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.b)) {
            SettingsDefinitions.ISO var3 = (SettingsDefinitions.ISO)var1;
            this.l = var3 == SettingsDefinitions.ISO.AUTO;
        } else if(var2.equals(this.g)) {
            this.b((SettingsDefinitions.ISO[])((SettingsDefinitions.ISO[])var1));
        } else if(var2.equals(this.h)) {
            ExposureSettings var4 = (ExposureSettings)var1;
            this.c = var4.getISO();
        }

    }

    private boolean a(SettingsDefinitions.ISO[] var1) {
        SettingsDefinitions.ISO[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SettingsDefinitions.ISO var5 = var2[var4];
            if(var5 == SettingsDefinitions.ISO.AUTO) {
                return true;
            }
        }

        return false;
    }

    private void b(SettingsDefinitions.ISO[] var1) {
        this.k = this.a(var1);
        SettingsDefinitions.ISO[] var2;
        if(this.k) {
            var2 = new SettingsDefinitions.ISO[var1.length - 1];
        } else {
            var2 = new SettingsDefinitions.ISO[var1.length];
        }

        int var3 = 0;

        for(int var4 = 0; var3 < var1.length; ++var3) {
            if(var1[var3] != SettingsDefinitions.ISO.AUTO) {
                var2[var4] = var1[var3];
                ++var4;
            }
        }

        if(var2.length != 0) {
            this.d = var2;
            this.e.setMax(this.d.length - 1);
            this.j = true;
        } else {
            this.j = false;
        }

    }

    public void updateWidget(DJIKey var1) {
        if(!var1.equals(this.h) && !var1.equals(this.b)) {
            if(var1.equals(this.g)) {
                if(this.k && this.j) {
                    this.f.setVisibility(VISIBLE);
                } else {
                    this.f.setVisibility(GONE);
                }
            }
        } else {
            this.f.setSelected(this.l);
            if(!this.i) {
                int var2 = a(this.d, this.c);
                this.e.setProgress(var2);
            }
        }

        this.e.a(!this.l && this.j);
    }

    private static int a(SettingsDefinitions.ISO[] var0, SettingsDefinitions.ISO var1) {
        int var2 = -1;
        if(var0 != null) {
            for(int var3 = 0; var3 < var0.length; ++var3) {
                if(var1 == var0[var3]) {
                    var2 = var3;
                    break;
                }
            }
        }

        return var2;
    }

    public void onClick(View var1) {
        if(var1 == this.f) {
            this.l = !this.l;
            this.setAutoISO(this.l);
        }

    }

    public void a(int var1, boolean var2) {
        this.c = this.d[var1];
        this.e.setText(UiDC.a(this.c));
    }

    public void a(int var1) {
        this.i = true;
    }

    public void b(int var1) {
        this.i = false;
        UiDB.a(this.getContext());
        SettingsDefinitions.ISO var2 = this.d[var1];
        this.a(var2);
    }

    private void a(final SettingsDefinitions.ISO var1) {
        if(KeyManager.getInstance() != null) {
            KeyManager.getInstance().setValue(this.b, var1, new SetCallback() {
                public void onSuccess() {
                    DJILog.d("DULISOSettingWidget", "Camera ISO " + var1.name() + " set successfully");
                }

                public void onFailure(@NonNull DJIError var1x) {
                    DULCameraISOSettingWidget.this.e.a();
                    DJILog.d("DULISOSettingWidget", "Failed to set Camera Exposure Mode");
                }
            });
        }
    }

    private void setAutoISO(boolean var1) {
        SettingsDefinitions.ISO var2;
        if(var1) {
            var2 = SettingsDefinitions.ISO.AUTO;
        } else if(this.c != SettingsDefinitions.ISO.UNKNOWN) {
            var2 = this.c;
        } else {
            var2 = this.d[this.e.getProgress()];
        }

        this.a(var2);
    }
}
