package zkrt.ui.d;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dji.common.camera.SettingsDefinitions.Aperture;
import dji.common.camera.SettingsDefinitions.ExposureCompensation;
import dji.common.camera.SettingsDefinitions.ISO;
import dji.common.camera.SettingsDefinitions.ShutterSpeed;
import dji.common.camera.SettingsDefinitions.VideoFrameRate;
import dji.common.camera.SettingsDefinitions.VideoResolution;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiDD extends UiDK {
    private int a;
    private int b;
    private int c;
    private int d;

    private UiDD(int var1, int var2, int var3, int var4) {
        this.a = var1;
        this.b = var2;
        this.c = var3;
        this.d = var4;
    }

    protected float a(float var1) {
        return var1 * (float)this.c / (float)this.a;
    }

    protected float b(float var1) {
        return var1 * (float)this.d / (float)this.b;
    }

    public static class a {
        private int a;
        private int b;
        private int c;
        private int d;

        public a() {
        }

        public a a(int var1) {
            this.a = var1;
            return this;
        }

        public a b(int var1) {
            this.b = var1;
            return this;
        }

        public a c(int var1) {
            this.c = var1;
            return this;
        }

        public a d(int var1) {
            this.d = var1;
            return this;
        }

        public UiDD a() {
            return new UiDD(this.a, this.b, this.c, this.d);
        }
    }
}
