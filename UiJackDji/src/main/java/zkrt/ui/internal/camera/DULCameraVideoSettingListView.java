package zkrt.ui.internal.camera;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.camera.WhiteBalance;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.log.DJILog;
import zkrt.ui.R;
import zkrt.ui.c.UiCC;
import zkrt.ui.internal.a.Inter_C;
import zkrt.ui.internal.a.Inter_G;
import zkrt.ui.internal.a.Inter_H;
import zkrt.ui.internal.a.Inter_I;
import zkrt.ui.internal.a.Inter_J;
import zkrt.ui.internal.a.Inter_K;
import zkrt.ui.internal.a.Inter_L;

/**
 * Created by jack_xie on 17-6-1.
 */

public class DULCameraVideoSettingListView extends InterCamera_A {
    private static final UiCC.a[] a;
    private DJIKey h;
    private DJIKey i;
    private DJIKey j;
    private DJIKey k;
    private DJIKey l;
    private String[] m;
    private TypedArray n;
    private DJIKey o;
    private DJIKey p;
    private DJIKey q;
    private DJIKey r;
    private DJIKey s;

    public DULCameraVideoSettingListView(Context var1) {
        super(var1, null, 0);
    }

    public DULCameraVideoSettingListView(Context var1, AttributeSet var2) {
        super(var1, var2, 0);
    }

    public DULCameraVideoSettingListView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    protected void a() {
        this.b = a;
        this.m = this.getResources().getStringArray(R.array.camera_filter_type);
        this.n = this.getResources().obtainTypedArray(R.array.camera_white_balance_img_array);
    }

    public void updateSelectedItem(View var1, int var2) {
        switch(var2) {
            case 0:
                this.g = new Inter_J(this.getContext());
                break;
            case 1:
                this.g = new Inter_I(this.getContext());
                break;
            case 2:
                this.g = new Inter_K(this.getContext());
                break;
            case 3:
                this.g = new Inter_L(this.getContext());
                break;
            case 4:
                this.g = new Inter_H(this.getContext());
                break;
            case 5:
                this.g = new Inter_C(this.getContext());
                break;
            default:
                this.g = null;
        }

        this.d();
    }

    protected void b() {
        this.a(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS);
        this.a(SettingsDefinitions.VideoFileFormat.MOV);
        this.a(SettingsDefinitions.VideoStandard.PAL);
        this.a(SettingsDefinitions.WhiteBalancePreset.AUTO, 0);
        this.a(0);
        this.a(SettingsDefinitions.DigitalFilter.NONE);
    }

    public void initKey() {
        this.h = CameraKey.create("ResolutionFrameRate");
        this.i = CameraKey.create("VideoFileFormat");
        this.j = CameraKey.create("VideoStandard");
        this.k = CameraKey.create("WhiteBalance");
        this.o = CameraKey.create("PictureStylePreset");
        this.l = CameraKey.create("DigitalFilter");
        this.p = CameraKey.create("VideoResolutionFrameRateRange");
        this.q = CameraKey.create("VideoFileFormatRange");
        this.r = CameraKey.create("DigitalFilterRange");
        this.s = CameraKey.create("VideoStandardRange");
        this.addDependentKey(this.h);
        this.addDependentKey(this.i);
        this.addDependentKey(this.j);
        this.addDependentKey(this.k);
        this.addDependentKey(this.o);
        this.addDependentKey(this.l);
        this.addDependentKey(this.p);
        this.addDependentKey(this.q);
        this.addDependentKey(this.r);
        this.addDependentKey(this.s);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var1 != null && var2 != null) {
            if(var2.equals(this.h)) {
                ResolutionAndFrameRate var3 = (ResolutionAndFrameRate)var1;
                DJILog.d("LWF", "resolutionAndFrameRate " + var3);
                this.a(var3.getResolution(), var3.getFrameRate());
            } else if(var2.equals(this.i)) {
                this.a((SettingsDefinitions.VideoFileFormat)var1);
            } else if(var2.equals(this.j)) {
                this.a((SettingsDefinitions.VideoStandard)var1);
            } else if(var2.equals(this.k)) {
                WhiteBalance var4 = (WhiteBalance)var1;
                this.a(var4.getWhiteBalancePreset(), var4.getColorTemperature());
            } else if(var2.equals(this.o)) {
                SettingsDefinitions.PictureStylePreset var5 = (SettingsDefinitions.PictureStylePreset)var1;
                this.a(var5.presetType().value());
            } else if(var2.equals(this.l)) {
                this.a((SettingsDefinitions.DigitalFilter)var1);
            } else if(var2.equals(this.p)) {
                ResolutionAndFrameRate[] var6 = (ResolutionAndFrameRate[])((ResolutionAndFrameRate[])var1);
                DJILog.d("LWF", "videoSizeArray " + var6.length);
                if(var6.length == 1) {
                    this.a(var6[0].getResolution(), var6[0].getFrameRate());
                    this.a(0, false);
                } else {
                    this.a(0, true);
                }
            } else if(var2.equals(this.q)) {
                SettingsDefinitions.VideoFileFormat[] var7 = (SettingsDefinitions.VideoFileFormat[])((SettingsDefinitions.VideoFileFormat[])var1);
                DJILog.d("LWF", "videoFormatRange " + var7.length);
                if(var7.length == 1) {
                    this.a(var7[0]);
                    this.a(1, false);
                } else {
                    this.a(1, true);
                }
            } else if(var2.equals(this.r)) {
                SettingsDefinitions.DigitalFilter[] var8 = (SettingsDefinitions.DigitalFilter[])((SettingsDefinitions.DigitalFilter[])var1);
                DJILog.d("LWF", "colorRange " + var8.length);
                if(var8.length == 1) {
                    this.a(var8[0]);
                    this.a(5, false);
                } else {
                    this.a(5, true);
                }
            } else if(var2.equals(this.s)) {
                SettingsDefinitions.VideoStandard[] var9 = (SettingsDefinitions.VideoStandard[])((SettingsDefinitions.VideoStandard[])var1);
                DJILog.d("LWF", "videoStandardRange " + var9.length);
                if(var9.length == 1) {
                    this.a(var9[0]);
                    this.a(2, false);
                } else {
                    this.a(2, true);
                }
            }
        }

    }

    private void a(SettingsDefinitions.VideoResolution var1, SettingsDefinitions.VideoFrameRate var2) {
        int var3 = var1.value();
        int var4 = var2.value();
        int var5 = 0;
        if(var3 < Inter_G.e.length && var4 < Inter_G.e[var3].length) {
            var5 = Inter_G.e[var3][var4];
        }

        this.a(0, var3, var5);
    }

    private void a(SettingsDefinitions.VideoFileFormat var1) {
        int var2 = var1.value();
        int var3 = 0;
        if(var2 < Inter_G.c.length) {
            var3 = Inter_G.c[var2];
        }

        this.a(1, var2, var3);
    }

    private void a(SettingsDefinitions.VideoStandard var1) {
        int var2 = var1.value();
        int var3 = 0;
        if(var2 < Inter_G.d.length) {
            var3 = Inter_G.d[var2];
        }

        this.a(2, var2, var3);
    }

    private void a(SettingsDefinitions.WhiteBalancePreset var1, int var2) {
        if(var1 != null) {
            int var3 = var1.value();
            this.a(3, var3, this.n.getResourceId(var3, 0));
        }

    }

    private void a(int var1) {
        this.a(4, var1, Inter_G.a[var1]);
    }

    private void a(SettingsDefinitions.DigitalFilter var1) {
        int var2 = var1.value();
        this.a(5, var2, 0);
        this.c.c(5).b(this.m[var2]);
    }

    static {
        a = new UiCC.a[]{new UiCC.a(R.string.camera_video_resolution, UiCC.enum_b.a),
                new UiCC.a(R.string.camera_video_format, UiCC.enum_b.a),
                new UiCC.a(R.string.camera_video_standard_name, UiCC.enum_b.a),
                new UiCC.a(R.string.camera_white_balance, UiCC.enum_b.a),
                new UiCC.a(R.string.camera_picture_style, UiCC.enum_b.a),
                new UiCC.a(R.string.camera_filter, UiCC.enum_b.a)};
    }
}
