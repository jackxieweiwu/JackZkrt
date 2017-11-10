package zkrt.ui.internal.exposure;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dji.common.camera.ExposureSettings;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.SetCallback;
import dji.log.DJILog;
import zkrt.ui.R.array;
import zkrt.ui.R.id;
import zkrt.ui.R.raw;
import zkrt.ui.R.string;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBN;
import zkrt.ui.d.UiDB;
import zkrt.ui.d.UiDC;
import zkrt.ui.d.UiDD;
import zkrt.ui.internal.DULStripeView;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DULCameraEVSettingWidget extends UiBaseGViewDULFrameLayout {
    private TextView a;
    private ImageView b;
    private TextView c;
    private ImageView d;
    private DULStripeView e;
    private DJIKey f;
    private CameraKey g;
    private SettingsDefinitions.ExposureMode h;
    private SettingsDefinitions.ExposureCompensation i;
    private String[] j;
    private Object[] k;
    private int l = 0;
    private TextView m;
    private DJIKey n;
    private DJIKey o;
    private UiCBN p;
    private SettingsDefinitions.ExposureCompensation q;

    protected UiCAC getWidgetAppearances() {
        if(this.p == null) {
            this.p = new UiCBN();
        }
        return this.p;
    }

    public DULCameraEVSettingWidget(Context var1) {
        super(var1, null, 0);
    }

    public DULCameraEVSettingWidget(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
    }

    public DULCameraEVSettingWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.a = (TextView)this.findViewById(id.textview_ev_title);
        this.b = (ImageView)this.findViewById(id.imagebutton_ev_setting_minus);
        this.c = (TextView)this.findViewById(id.textview_setting_ev_value);
        this.d = (ImageView)this.findViewById(id.imagebutton_ev_setting_plus);
        this.e = (DULStripeView)this.findViewById(id.stripeview_setting_ev_status);
        this.m = (TextView)this.findViewById(id.textview_setting_ev_status_value);
        this.a();
    }

    private void a() {
        Resources var1 = this.context.getResources();
        int[] var2 = var1.getIntArray(array.camera_ev_value_array);
        SettingsDefinitions.ExposureCompensation[] var3 = new SettingsDefinitions.ExposureCompensation[var2.length];

        for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = SettingsDefinitions.ExposureCompensation.find(var2[var4]);
        }

        this.a((Object[])var3);
        this.a.setText(string.camera_exposure_ev_title);
        this.e.setZeroPosition(this.j.length / 2);
        this.c();
        this.l = this.j.length / 2;
        this.a(this.l);
        this.a(true);
    }

    private void a(int var1) {
        this.m.setText(this.j[var1]);
        this.c.setText(this.j[var1]);
        this.e.setSelectedPosition(var1);
    }

    public void initKey() {
        this.f = CameraKey.create("ExposureCompensation");
        this.o = CameraKey.create("ExposureMode");
        this.n = CameraKey.create("ExposureCompensationRange");
        this.g = CameraKey.create("ExposureSettings");
        this.addDependentKey(this.o);
        this.addDependentKey(this.f);
        this.addDependentKey(this.g);
        this.addDependentKey(this.n);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.o)) {
            this.h = (SettingsDefinitions.ExposureMode)var1;
        } else if(var2.equals(this.g)) {
            ExposureSettings var3 = (ExposureSettings)var1;
            this.q = var3.getExposureCompensation();
        } else if(var2.equals(this.f)) {
            this.i = (SettingsDefinitions.ExposureCompensation)var1;
        } else if(var2.equals(this.n)) {
            SettingsDefinitions.ExposureCompensation[] var4 = (SettingsDefinitions.ExposureCompensation[])((SettingsDefinitions.ExposureCompensation[])var1);
            this.a((Object[])var4);
        }

    }

    private void a(Object[] var1) {
        this.k = var1;
        String[] var2 = new String[var1.length];

        for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = UiDC.a((SettingsDefinitions.ExposureCompensation)((SettingsDefinitions.ExposureCompensation)var1[var3]));
            var2[var3] = var4;
        }

        this.j = var2;
    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.o)) {
            this.a(this.h);
        } else if(var1.equals(this.n)) {
            this.b();
        } else if(this.h != SettingsDefinitions.ExposureMode.MANUAL) {
            if(var1.equals(this.f)) {
                this.b(this.i);
            }
        } else if(var1.equals(this.g)) {
            this.b(this.q);
        }

    }

    private void b() {
        this.e.setZeroPosition(this.j.length / 2);
    }

    public int a(SettingsDefinitions.ExposureCompensation var1) {
        byte var2 = 0;
        if(this.k != null) {
            int var3 = 0;

            for(int var4 = this.k.length; var3 < var4; ++var3) {
                if(var1 == this.k[var3]) {
                    return var3;
                }
            }
        }

        return var2;
    }

    public void b(SettingsDefinitions.ExposureCompensation var1) {
        this.l = this.a(var1);
        this.a(this.l);
    }

    public void a(boolean var1) {
        if(var1) {
            this.d.setEnabled(true);
            this.d.setVisibility(VISIBLE);
            this.b.setEnabled(true);
            this.b.setVisibility(VISIBLE);
            this.c.setEnabled(true);
            this.c.setVisibility(VISIBLE);
            this.e.setVisibility(INVISIBLE);
            this.m.setVisibility(INVISIBLE);
        } else {
            this.b.setEnabled(false);
            this.b.setVisibility(INVISIBLE);
            this.d.setEnabled(false);
            this.d.setVisibility(INVISIBLE);
            this.c.setEnabled(false);
            this.c.setVisibility(INVISIBLE);
            this.e.setVisibility(VISIBLE);
            this.m.setVisibility(VISIBLE);
        }

    }

    private void a(SettingsDefinitions.ExposureMode var1) {
        boolean var2 = var1 != SettingsDefinitions.ExposureMode.MANUAL;
        this.a(var2);
    }

    private void c() {
        OnClickListener var1 = new OnClickListener() {
            public void onClick(View var1) {
                if(DULCameraEVSettingWidget.this.l > 0) {
                    DULCameraEVSettingWidget.this.l--;
                    DULCameraEVSettingWidget.this.a(DULCameraEVSettingWidget.this.l, false);
                }

            }
        };
        OnClickListener var2 = new OnClickListener() {
            public void onClick(View var1) {
                if(DULCameraEVSettingWidget.this.l < DULCameraEVSettingWidget.this.j.length - 1) {
                    DULCameraEVSettingWidget.this.l++;
                    DULCameraEVSettingWidget.this.a(DULCameraEVSettingWidget.this.l, false);
                }

            }
        };
        OnClickListener var3 = new OnClickListener() {
            public void onClick(View var1) {
                if(DULCameraEVSettingWidget.this.l != DULCameraEVSettingWidget.this.j.length / 2) {
                    DULCameraEVSettingWidget.this.l = DULCameraEVSettingWidget.this.j.length / 2;
                    DULCameraEVSettingWidget.this.a(DULCameraEVSettingWidget.this.l, true);
                }

            }
        };
        this.b.setOnClickListener(var1);
        this.d.setOnClickListener(var2);
        this.c.setOnClickListener(var3);
    }

    private void a(int var1, boolean var2) {
        if(KeyManager.getInstance() != null) {
            if(this.k != null && var1 < this.k.length) {
                if(var2) {
                    this.e();
                } else {
                    this.f();
                }

                final SettingsDefinitions.ExposureCompensation var3 = (SettingsDefinitions.ExposureCompensation)this.k[var1];
                this.b(var3);
                KeyManager.getInstance().setValue(this.f, var3, new SetCallback() {
                    public void onSuccess() {
                        DJILog.d("DULCameraEVSettingWidget", "Camera EV " + var3.name() + " set successfully");
                    }

                    public void onFailure(@NonNull DJIError var1) {
                        DULCameraEVSettingWidget.this.d();
                        DJILog.d("DULCameraEVSettingWidget", "Failed to set Camera EV " + var3.name());
                    }
                });
            }
        }
    }

    private void d() {
        this.b(this.i);
    }

    private void e() {
        (new UiDB()).a(this.getContext(), raw.camera_ev_center);
    }

    private void f() {
        (new UiDB()).a(this.getContext(), raw.camera_simple_click);
    }}
