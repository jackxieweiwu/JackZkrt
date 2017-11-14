package zkrt.ui.internal.exposure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.atomic.AtomicBoolean;

import dji.common.camera.ExposureSettings;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.SetCallback;
import dji.log.DJILog;
import antistatic.spinnerwheel.WheelHorizontalView;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.schedulers.Schedulers;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.a.UiCAD;
import zkrt.ui.c.b.UiCBA_I;
import zkrt.ui.d.UiDC;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DULCameraShutterSettingWidget extends UiBaseGViewDULFrameLayout  {
    private UiCBA_I a;
    private WheelHorizontalView b;
    private ImageView c;
    private DJIKey d;
    private SettingsDefinitions.ShutterSpeed e;
    private DULCameraShutterSettingWidget.a f;
    private int g = 0;
    private boolean h;
    private SettingsDefinitions.ExposureMode i;
    private Object[] j;
    private String[] k;
    private DJIKey l;
    private CameraKey m;
    private CameraKey n;
    private AtomicBoolean o = new AtomicBoolean(true);

    protected UiCAC getWidgetAppearances() {
        if(this.a == null) {
            this.a = new UiCBA_I();
        }
        return this.a;
    }

    public DULCameraShutterSettingWidget(Context var1) {
        super(var1, null, 0);
    }

    public DULCameraShutterSettingWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
    }

    public DULCameraShutterSettingWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.b = (WheelHorizontalView)this.findViewById(R.id.wheelview_camera_settings_shutter);
        this.c = (ImageView)this.findViewById(R.id.imageview_shutter_wheel_position);
        this.k = this.getResources().getStringArray(R.array.camera_shutter_names);
        this.a();
    }

    public void initKey() {
        this.d = CameraKey.create("ShutterSpeed");
        this.l = CameraKey.create("ExposureSettings");
        this.m = CameraKey.create("ExposureMode");
        this.n = CameraKey.create("ShutterSpeedRange");
        this.addDependentKey(this.l);
        this.addDependentKey(this.d);
        this.addDependentKey(this.m);
        this.addDependentKey(this.n);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.m)) {
            this.i = (SettingsDefinitions.ExposureMode)var1;
        } else if(var2.equals(this.d)) {
            this.e = (SettingsDefinitions.ShutterSpeed)var1;
        } else if(var2.equals(this.l)) {
            ExposureSettings var3 = (ExposureSettings)var1;
            this.e = var3.getShutterSpeed();
        } else if(var2.equals(this.n)) {
            this.a((Object[])((SettingsDefinitions.ShutterSpeed[])((SettingsDefinitions.ShutterSpeed[])var1)));
        }

    }

    private void a(Object[] var1) {
        this.j = var1;
        String[] var2 = new String[var1.length];

        for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = UiDC.a((SettingsDefinitions.ShutterSpeed)var1[var3]);
            var2[var3] = var4;
        }

        this.k = var2;
    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.m)) {
            this.b();
        } else if(!var1.equals(this.l) && !var1.equals(this.d)) {
            if(var1.equals(this.n)) {
                this.a();
            }
        } else {
            this.a(this.e);
        }

    }

    private void a() {
        this.f = new DULCameraShutterSettingWidget.a(this.getContext(), this.k);
        this.f.a(R.layout.wheel_item_camera_set_shutter);
        this.f.b(R.id.camera_settings_wheel_text);
        /*this.b.a(this.getContext());
        this.b.a(this.getContext());*/
        this.b.setViewAdapter(this.f);
        this.b.setCurrentItem(this.g);
    }

    private int b(SettingsDefinitions.ShutterSpeed var1) {
        byte var2 = 0;
        if(this.j != null) {
            for(int var3 = 0; var3 < this.j.length; ++var3) {
                if(this.j[var3] == var1) {
                    return var3;
                }
            }
        } else if(this.k != null) {
            String var5 = UiDC.a(var1);

            for(int var4 = 0; var4 < this.k.length; ++var4) {
                if(this.k[var4].equalsIgnoreCase(var5)) {
                    return var4;
                }
            }
        }

        return var2;
    }

    public void a(SettingsDefinitions.ShutterSpeed var1) {
        if(var1 != null && !this.h) {
            this.g = this.b(var1);
            this.b.setCurrentItem(this.g);
            this.f.d(this.g);
        }

    }

    private void b() {
        if(this.i != SettingsDefinitions.ExposureMode.PROGRAM && this.i != SettingsDefinitions.ExposureMode.APERTURE_PRIORITY) {
            if(this.i == SettingsDefinitions.ExposureMode.SHUTTER_PRIORITY || this.i == SettingsDefinitions.ExposureMode.MANUAL) {
                this.b.setEnabled(true);
                this.b.setAlpha(1.0F);
                this.c.setVisibility(VISIBLE);
                this.b.setVisibleItems(7);
            }
        } else {
            this.b.setAlpha(0.7F);
            this.b.setEnabled(false);
            this.c.setVisibility(INVISIBLE);
            this.b.setVisibleItems(1);
        }

        this.f.a(this.b.getVisibleItems() > 1);
    }

    public void a(antistatic.spinnerwheel.AntiSpinnerA var1) {
        this.h = true;
    }

    public void b(final antistatic.spinnerwheel.AntiSpinnerA var1) {
        this.h = false;
        Observable.just(Boolean.valueOf(true)).subscribeOn(Schedulers.computation())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        DULCameraShutterSettingWidget.this.a(var1.getCurrentItem());
                    }
                });
    }

    public void a(antistatic.spinnerwheel.AntiSpinnerA var1, int var2, int var3) {
        if(this.h) {
            this.f.d(var3);
            this.b.setCurrentItem(var3);
        }

    }

    private void a(int var1) {
        if(KeyManager.getInstance() != null) {
            if(this.j != null) {
                //this.b.a(this.getContext());
                this.g = var1;
                SettingsDefinitions.ShutterSpeed var2 = (SettingsDefinitions.ShutterSpeed)this.j[this.g];
                this.a(var2);
                if(this.o.compareAndSet(true, false)) {
                    KeyManager.getInstance().setValue(this.d, var2, new SetCallback() {
                        public void onSuccess() {
                            DULCameraShutterSettingWidget.this.o.set(true);
                        }

                        public void onFailure(@NonNull DJIError var1) {
                            DULCameraShutterSettingWidget.this.o.set(true);
                            DULCameraShutterSettingWidget.this.c();
                            DJILog.d("DULCameraShutterSettingWidget", "Failed to set Camera shutter: " + var1.getDescription());
                        }
                    });
                }
            }

        }
    }

    private void c() {
        if(this.e != null) {
            this.a(this.e);
        }

    }

    private class a extends UiCAD<String> {
        public a(Context var2, String[] var3) {
            super(var2, var3);
        }

        protected CharSequence c(int var1) {
            String var2 = (String)super.c(var1);
            if(!DULCameraShutterSettingWidget.this.b.isEnabled() && !var2.contains("\"")) {
                var2 = "1/" + var2 + "\"";
            }

            return var2;
        }
    }
}