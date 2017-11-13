package zkrt.ui.internal.a;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dji.common.camera.PhotoTimeLapseSettings;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.SetCallback;
import dji.log.DJILog;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseMView;
import zkrt.ui.c.UiCA;
import zkrt.ui.c.UiCB;
import zkrt.ui.d.UiDF;
import zkrt.ui.d.UiDG;

/**
 * Created by jack_xie on 17-6-1.
 */

public class Inter_F extends UiBaseMView {
    private SettingsDefinitions.ShootPhotoMode l;
    private int[] m;
    private int[][] n;
    private DJIKey o;
    private DJIKey p;
    private DJIKey q;
    private boolean r;
    private DJIKey s;
    private DJIKey t;
    private DJIKey u;
    private DJIKey v;
    private DJIKey w;
    private DJIKey x;
    private DJIKey y;
    private SettingsDefinitions.PhotoTimeIntervalSettings z;
    private SettingsDefinitions.PhotoAEBCount A;
    private SettingsDefinitions.PhotoBurstCount B;
    private PhotoTimeLapseSettings C;
    private SettingsDefinitions.PhotoBurstCount D;
    private SettingsDefinitions.PhotoPanoramaMode E;

    public Inter_F(Context var1) {
        super(var1);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.b((int[])null, (int[][])null);
    }

    public void updateTitle(TextView var1) {
        if(var1 != null) {
            var1.setText(R.string.camera_photo_name);
        }

    }

    protected List<UiCB> a(int[] var1, int[][] var2) {
        ArrayList var3 = new ArrayList();
        int[] var5 = Inter_G.a();
        int[] var4 = Inter_G.b();
        if(var1 == null) {
            this.m = Inter_G.c();
        } else {
            this.m = var1;
        }

        if(var2 == null) {
            this.n = Inter_G.d();
        } else {
            this.n = var2;
        }

        if(null != this.m && this.m.length > 0) {
            int[] var6 = this.m;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                int var9 = var6[var8];
                UiCB var10 = new UiCB();
                var10.d = var9;
                if(var10.d < var5.length) {
                    var10.b = this.getContext().getString(var5[var10.d]);
                    var10.a = var4[var10.d];
                    if(var10.d < this.n.length) {
                        int[] var11 = this.n[var10.d];
                        if(null != var11) {
                            if(var11.length > 1) {
                                this.a(var10, var11);
                            } else {
                                var10.c = this.a(var10, var11[0]);
                            }
                        }
                    }

                    var3.add(var10);
                }
            }
        }

        return var3;
    }

    private void a(UiCB var1, int[] var2) {
        int var3 = 0;

        for(int var4 = var2.length; var3 < var4; ++var3) {
            UiCA var5 = new UiCA();
            var5.a = this.a(var1, var2[var3]);
            var5.c = var1.d;
            var5.b = var2[var3];
            var1.uiCAArrayList.add(var5);
        }
    }

    private String a(UiCB var1, int var2) {
        String var3;
        if(var1.d == SettingsDefinitions.ShootPhotoMode.INTERVAL.value()) {
            var3 = var2 + "s";
        } else if((var1.d == SettingsDefinitions.ShootPhotoMode.RAW_BURST.value() || var1.d == SettingsDefinitions.ShootPhotoMode.BURST.value()) && var2 == SettingsDefinitions.PhotoBurstCount.CONTINUOUS.value()) {
            var3 = this.getResources().getString(R.string.camera_photomode_raw_burst_infinity);
        } else if(var1.d == SettingsDefinitions.ShootPhotoMode.PANORAMA.value()) {
            if(SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X1.value() == var2) {
                var3 = this.getResources().getString(R.string.camera_photomode_panorama_3x1);
            } else {
                var3 = this.getResources().getString(R.string.camera_photomode_panorama_3x3);
            }
        } else {
            var3 = String.valueOf(var2);
        }

        return var3;
    }

    private void b(int[] var1, int[][] var2) {
        this.e = 2147483647;
        this.f = 2147483647;
        this.a.a(this.a(var1, var2));
        if(this.i != null && this.i.isShown()) {
            this.i.setVisibility(GONE);
        }

    }

    public void initKey() {
        this.o = CameraKey.create("ShootPhotoModeRange");
        this.s = CameraKey.create("ShootPhotoChildRange");
        this.p = CameraKey.create("IsShootingIntervalPhoto");
        this.q = CameraKey.create("ShootPhotoMode");
        this.t = CameraKey.create("PhotoTimeIntervalSettings");
        this.u = CameraKey.create("PhotoAEBCount");
        this.v = CameraKey.create("PhotoBurstCount");
        this.w = CameraKey.create("PhotoTimeLapseSettings");
        this.x = CameraKey.create("PhotoRAWBurstCount");
        this.y = CameraKey.create("PhotoPanoramaMode");
        this.addDependentKey(this.o);
        this.addDependentKey(this.p);
        this.addDependentKey(this.q);
        this.addDependentKey(this.s);
        this.addDependentKey(this.t);
        this.addDependentKey(this.u);
        this.addDependentKey(this.v);
        this.addDependentKey(this.w);
        this.addDependentKey(this.x);
        this.addDependentKey(this.y);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.q)) {
            this.l = (SettingsDefinitions.ShootPhotoMode)var1;
            if(this.l == SettingsDefinitions.ShootPhotoMode.BURST) {
                this.getBurstCount();
            } else if(this.l == SettingsDefinitions.ShootPhotoMode.TIME_LAPSE) {
                this.getTimeLapseParam();
            }
        } else if(var2.equals(this.o)) {
            SettingsDefinitions.ShootPhotoMode[] var3 = (SettingsDefinitions.ShootPhotoMode[])((SettingsDefinitions.ShootPhotoMode[])var1);
            this.m = new int[var3.length];

            for(int var4 = 0; var4 < var3.length; ++var4) {
                this.m[var4] = ((SettingsDefinitions.ShootPhotoMode)var3[var4]).value();
            }
        } else if(var2.equals(this.p)) {
            this.r = ((Boolean)var1).booleanValue();
        } else if(var2.equals(this.s)) {
            this.n = (int[][])((int[][])var1);
        } else if(var2.equals(this.t)) {
            this.z = (SettingsDefinitions.PhotoTimeIntervalSettings)var1;
        } else if(var2.equals(this.u)) {
            this.A = (SettingsDefinitions.PhotoAEBCount)var1;
        } else if(var2.equals(this.v)) {
            this.B = (SettingsDefinitions.PhotoBurstCount)var1;
        } else if(var2.equals(this.w)) {
            this.C = (PhotoTimeLapseSettings)var1;
        } else if(var2.equals(this.x)) {
            this.D = (SettingsDefinitions.PhotoBurstCount)var1;
        } else if(var2.equals(this.y)) {
            this.E = (SettingsDefinitions.PhotoPanoramaMode)var1;
            DJILog.d("LWF", "panoramaMode " + this.E);
        }

    }

    private void getBurstCount() {
        UiDF.a().a(this.w, this);
    }

    private void getTimeLapseParam() {
        UiDF.a().a(this.y, this);
    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.o)) {
            this.b(this.m, this.n);
        } else if(var1.equals(this.s)) {
            this.b(this.m, this.n);
        }

        this.a();
    }

    private void a() {
        if(this.l != null) {
            UiCB var1 = this.a.a(this.l.value());
            if(var1 != null) {
                var1.e = this.b(this.l, 2147483647);
                if(var1.e != 2147483647) {
                    var1.c = this.a(var1, var1.e);
                }

                this.a.a(var1);
            }
        }

    }

    private void a(final SettingsDefinitions.ShootPhotoMode var1, int var2) {
        if(KeyManager.getInstance() != null) {
            Object var3 = null;
            DJIKey var4 = null;
            if(var1 == SettingsDefinitions.ShootPhotoMode.BURST) {
                var4 = this.v;
                var3 = SettingsDefinitions.PhotoBurstCount.find(var2);
            } else if(var1 == SettingsDefinitions.ShootPhotoMode.AEB) {
                var4 = this.u;
                var3 = SettingsDefinitions.PhotoAEBCount.find(var2);
            } else {
                int var5;
                if(var1 == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
                    var4 = this.t;
                    if(this.z != null && this.z.getCaptureCount() >= 2) {
                        var5 = this.z.getCaptureCount();
                    } else {
                        var5 = 255;
                    }

                    var3 = new SettingsDefinitions.PhotoTimeIntervalSettings(var5, var2);
                } else if(var1 == SettingsDefinitions.ShootPhotoMode.TIME_LAPSE) {
                    var4 = this.w;
                    var5 = 0;
                    SettingsDefinitions.PhotoTimeLapseFileFormat var6 = SettingsDefinitions.PhotoTimeLapseFileFormat.JPEG_AND_VIDEO;
                    if(this.C != null) {
                        var6 = this.C.getFileFormat();
                        var5 = this.C.getDuration();
                    }

                    var3 = new PhotoTimeLapseSettings(var2, var5, var6);
                } else if(var1 == SettingsDefinitions.ShootPhotoMode.RAW_BURST) {
                    var4 = this.x;
                    var3 = SettingsDefinitions.PhotoBurstCount.find(var2);
                } else if(var1 == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                    var4 = this.y;
                    var3 = SettingsDefinitions.PhotoPanoramaMode.find(var2);
                }
            }

            if(var4 != null) {
                KeyManager.getInstance().setValue(var4, var3, new SetCallback() {
                    public void onSuccess() {
                        Inter_F.this.a(var1);
                    }

                    public void onFailure(@NonNull DJIError var1x) {
                        DJILog.d("CameraPhotoModeListWidget", "Failed to set Camera Photo Mode " + var1x.getDescription());
                    }
                });
            } else {
                this.a(var1);
            }

        }
    }

    private void a(final SettingsDefinitions.ShootPhotoMode var1) {
        if(KeyManager.getInstance() != null) {
            CameraKey var2 = CameraKey.create("ShootPhotoMode");
            KeyManager.getInstance().setValue(var2, var1, new SetCallback() {
                public void onSuccess() {
                    DJILog.d("CameraPhotoModeListWidget", "Camera mode " + var1.name() + "value " + var1.value() + " set successfully");
                }

                public void onFailure(@NonNull DJIError var1x) {
                    DJILog.d("CameraPhotoModeListWidget", "Camera mode " + var1.name() + " set failed");
                }
            });
        }
    }


    private int b(SettingsDefinitions.ShootPhotoMode var1, int var2) {
        int var3 = var2;
        if(var1 == SettingsDefinitions.ShootPhotoMode.BURST && this.B != null) {
            var3 = this.B.value();
        } else if(var1 == SettingsDefinitions.ShootPhotoMode.AEB && this.A != null) {
            var3 = this.A.value();
        } else if(var1 == SettingsDefinitions.ShootPhotoMode.INTERVAL && this.z != null) {
            var3 = this.z.getTimeIntervalInSeconds();
        } else if(var1 == SettingsDefinitions.ShootPhotoMode.TIME_LAPSE && this.C != null) {
            var3 = this.C.getInterval();
        } else if(var1 == SettingsDefinitions.ShootPhotoMode.RAW_BURST && this.D != null) {
            var3 = this.D.value();
        } else if(var1 == SettingsDefinitions.ShootPhotoMode.PANORAMA && this.E != null && this.E != SettingsDefinitions.PhotoPanoramaMode.UNKNOWN) {
            var3 = this.E.value();
        }

        return var3;
    }

    protected boolean a(ExpandableListView var1, View var2, int var3, long var4) {
        UiCB var6 = (UiCB)this.a.getGroup(var3);
        if(var6.d != this.e && !this.r) {
            SettingsDefinitions.ShootPhotoMode var7 = SettingsDefinitions.ShootPhotoMode.find(var6.d);
            int var8 = 0;
            if(var6.e != 2147483647) {
                var8 = var6.e;
            } else if(!var6.uiCAArrayList.isEmpty()) {
                var8 = ((UiCA)var6.uiCAArrayList.get(0)).b;
            }

            int var9 = this.b(var7, var8);
            this.a(var7, var9);
            this.a(this.e, var6.d, var9);
            this.e = var6.d;
            this.f = var9;
        }

        return true;
    }

    protected void a(Object var1) {
        if(var1 instanceof UiCA) {
            UiCA var2 = (UiCA)var1;
            if(this.e == var2.c && var2.b == this.f) {
                return;
            }

            if(!this.r) {
                this.a(SettingsDefinitions.ShootPhotoMode.find(var2.c), var2.b);
                this.a(this.e, var2.c, var2.b);
                this.e = var2.c;
                this.f = var2.b;
            }
        }

    }
}
