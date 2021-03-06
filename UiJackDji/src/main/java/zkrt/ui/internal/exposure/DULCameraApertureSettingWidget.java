package zkrt.ui.internal.exposure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
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
import antistatic.spinnerwheel.AntiSpinnerA;
import antistatic.spinnerwheel.AntiSpinnerD;
import antistatic.spinnerwheel.AntiSpinnerF;
import antistatic.spinnerwheel.WheelHorizontalView;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAD;
import zkrt.ui.c.b.UiCBC;
import zkrt.ui.d.UiDB;
import zkrt.ui.d.UiDC;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DULCameraApertureSettingWidget extends UiBaseGViewDULFrameLayout implements AntiSpinnerD,AntiSpinnerF {
    private WheelHorizontalView wheelHorizontalView;
    private ImageView b;
    private SettingsDefinitions.Aperture c;
    private UiCAD<String> d;
    private int e = 0;
    private String[] f;
    private boolean g;
    private DJIKey h;
    private Object[] i;
    private DJIKey j;
    private DJIKey k;
    private SettingsDefinitions.ExposureMode l;
    private DJIKey m;
    private CameraKey n;
    private boolean o;
    private UiCBC p;

    public DULCameraApertureSettingWidget(Context var1) {
        super(var1, null, 0);
    }

    public DULCameraApertureSettingWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
    }

    public DULCameraApertureSettingWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    protected UiCAC getWidgetAppearances() {
        if(this.p == null) {
            this.p = new UiCBC();
        }
        return this.p;
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.wheelHorizontalView = (WheelHorizontalView)this.findViewById(R.id.wheelview_camera_settings_aperture);
        this.b = (ImageView)this.findViewById(R.id.imageview_aperture_wheel_position);
        this.f = this.getResources().getStringArray(R.array.camera_aperture_array);
        this.a();
    }

    public void initKey() {
        this.n = CameraKey.create("CameraType");
        this.h = CameraKey.create("Aperture");
        this.m = CameraKey.create("ExposureSettings");
        this.k = CameraKey.create("ExposureMode");
        this.j = CameraKey.create("ApertureRange");
        this.addDependentKey(this.n);
        this.addDependentKey(this.h);
        this.addDependentKey(this.k);
        this.addDependentKey(this.m);
        this.addDependentKey(this.j);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.k)) {
            this.l = (SettingsDefinitions.ExposureMode)var1;
        } else if(var2.equals(this.h)) {
            this.c = (SettingsDefinitions.Aperture)var1;
        } else if(var2.equals(this.m)) {
            ExposureSettings var3 = (ExposureSettings)var1;
            this.c = var3.getAperture();
        } else if(var2.equals(this.j)) {
            SettingsDefinitions.Aperture[] var4 = (SettingsDefinitions.Aperture[])((SettingsDefinitions.Aperture[])var1);
            this.a((Object[])var4);
        } else if(var2.equals(this.n)) {
            SettingsDefinitions.CameraType var5 = (SettingsDefinitions.CameraType)var1;
            if(var5 == SettingsDefinitions.CameraType.DJICameraTypeGD600) {
                this.o = true;
            } else {
                this.o = false;
            }
        }

    }

    private void a(Object[] var1) {
        this.i = var1;
        String[] var2 = new String[var1.length];

        for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = UiDC.a((SettingsDefinitions.Aperture)var1[var3]);
        }

        this.f = var2;
    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.k)) {
            this.b();
        } else if(var1.equals(this.j)) {
            this.a();
        } else if(!var1.equals(this.h) && !var1.equals(this.m)) {
            if(var1.equals(this.n) && this.c == null && this.o) {
                Object var2 = KeyManager.getInstance().getValue(this.h);
                if(var2 != null) {
                    this.c = (SettingsDefinitions.Aperture)var2;
                    this.a(this.c);
                }
            }
        } else {
            if(this.f == null || this.f.length == 1) {
                this.a((Object[])(new SettingsDefinitions.Aperture[]{this.c}));
                this.a();
            }

            if(!this.o) {
                this.a(this.c);
            }
        }

    }

    private void a() {
        if(this.f != null) {
            this.d = new UiCAD(this.getContext(), this.f);
            this.d.a(R.layout.wheel_item_camera_set_aperture);
            this.d.b(R.id.camera_settings_wheel_text);
            /*this.wheelHorizontalView.a(this);
            this.wheelHorizontalView.a(this);*/
            this.wheelHorizontalView.setViewAdapter(this.d);
            this.wheelHorizontalView.setCurrentItem(this.e);
        }

        if(this.f != null && this.f.length <= 1) {
            this.a(false);
        }

    }

    private void b() {
        if(this.l != SettingsDefinitions.ExposureMode.PROGRAM && this.l != SettingsDefinitions.ExposureMode.SHUTTER_PRIORITY) {
            if(this.l == SettingsDefinitions.ExposureMode.APERTURE_PRIORITY || this.l == SettingsDefinitions.ExposureMode.MANUAL) {
                this.a(true);
            }
        } else {
            this.a(false);
        }

    }

    private void a(boolean var1) {
        if(var1) {
            this.wheelHorizontalView.setEnabled(true);
            this.wheelHorizontalView.setAlpha(1.0F);
            this.b.setVisibility(VISIBLE);
            this.wheelHorizontalView.setVisibleItems(7);
        } else {
            this.wheelHorizontalView.setAlpha(0.7F);
            this.wheelHorizontalView.setEnabled(false);
            this.b.setVisibility(INVISIBLE);
            this.wheelHorizontalView.setVisibleItems(1);
        }

        this.d.a(this.wheelHorizontalView.getVisibleItems() > 1);
    }

    private int b(SettingsDefinitions.Aperture var1) {
        byte var2 = 0;
        if(this.i != null) {
            for(int var3 = 0; var3 < this.i.length; ++var3) {
                if(this.i[var3] == var1) {
                    return var3;
                }
            }
        } else if(this.f != null) {
            String var5 = UiDC.a(var1);

            for(int var4 = 0; var4 < this.f.length; ++var4) {
                if(this.f[var4].equalsIgnoreCase(var5)) {
                    return var4;
                }
            }
        }

        return var2;
    }

    public void a(SettingsDefinitions.Aperture var1) {
        if(!this.g) {
            this.e = this.b(var1);
            this.wheelHorizontalView.setCurrentItem(this.e);
            this.d.d(this.e);
        }

    }

    public void a(AntiSpinnerA var1) {
        this.g = true;
    }

    public void b(AntiSpinnerA var1) {
        this.g = false;
        this.a(var1.getCurrentItem());
    }

    public void a(AntiSpinnerA var1, int var2, int var3) {
        if(this.g) {
            this.d.d(var3);
            this.wheelHorizontalView.setCurrentItem(var3);
        }

    }

    private void a(int var1) {
        if(KeyManager.getInstance() != null) {
            if(this.i != null) {
                UiDB.a(this.getContext());
                this.e = var1;
                final SettingsDefinitions.Aperture var2 = (SettingsDefinitions.Aperture)this.i[this.e];
                this.a(var2);
                KeyManager.getInstance().setValue(this.h, var2, new SetCallback() {
                    public void onSuccess() {
                        DJILog.d("DULCameraApertureSettingWidget", "Camera Aperture " + var2.name() + " set successfully");
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        DULCameraApertureSettingWidget.this.c();
                        DJILog.d("DULCameraApertureSettingWidget", "Failed to set Camera Aperture");
                    }
                });
            }

        }
    }

    private void c() {
        if(this.c != null) {
            this.a(this.c);
        }

    }
}
