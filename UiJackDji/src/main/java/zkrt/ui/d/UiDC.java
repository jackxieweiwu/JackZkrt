package zkrt.ui.d;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.keysdk.CameraKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.ActionCallback;
import dji.log.DJILog;
import zkrt.ui.R;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiDC {
    public static String a(SettingsDefinitions.ShutterSpeed var0) {
        String var1 = var0.name();
        var1 = var1.replace("SHUTTER_SPEED_1_", "").replaceAll("(\\d+)_DOT_(\\d+)", "$1.$2").replaceAll("SHUTTER_SPEED_([0-9.]+)", "$1\"").replaceAll("DOT_(\\d+)", "1.$1\"");
        return var1;
    }

    public static String a(@NonNull SettingsDefinitions.Aperture var0) {
        String var1;
        if(var0 == SettingsDefinitions.Aperture.UNKNOWN) {
            var1 = var0.name();
        } else {
            int var2 = var0.value();
            int var3 = var2 / 100;
            int var4 = var2 % 100 / 10;
            if(var4 == 0) {
                var1 = String.format("%d", new Object[]{Integer.valueOf(var3)});
            } else {
                var1 = String.format("%d.%d", new Object[]{Integer.valueOf(var3), Integer.valueOf(var4)});
            }
        }

        return var1;
    }

    public static String a(SettingsDefinitions.ISO var0) {
        String var1 = var0.name();
        String var2 = var1.replace("ISO_", "");
        return var2;
    }

    public static String a(SettingsDefinitions.ExposureCompensation var0) {
        String var1 = var0.name();
        String var2 = var1.replace("N_0_0", "0").replaceAll("N_", "-").replaceAll("P_", "+").replaceAll("_", ".");
        return var2;
    }

    public static String a(SettingsDefinitions.VideoResolution var0) {
        String var1 = "Null";
        if(var0 != null) {
            switch(var0.value()) {
                case 1:
                    var1 = "480P";
                    break;
                case 2:
                    var1 = "512P";
                    break;
                case 3:
                    var1 = "720P";
                    break;
                case 4:
                    var1 = "1080P";
                    break;
                case 5:
                case 6:
                    var1 = "2.7K";
                    break;
                case 7:
                case 8:
                case 9:
                    var1 = "4K";
                    break;
                case 10:
                case 11:
                    var1 = "4.5K";
                    break;
                case 12:
                    var1 = "5K";
                    break;
                default:
                    var1 = "Unknown";
            }
        }

        return var1;
    }

    public static String a(SettingsDefinitions.VideoFrameRate var0) {
        String var1 = var0.toString();
        Matcher var3 = Pattern.compile("FRAME_RATE_(\\d{2,3})_DOT_.*").matcher(var1);
        String var2;
        if(var3.find()) {
            String var4 = var3.group(1);
            var2 = Integer.toString(Integer.valueOf(var4).intValue() + 1);
        } else {
            var3 = Pattern.compile("FRAME_RATE_(\\d{2,3})_FPS").matcher(var1);
            if(var3.find()) {
                var2 = var3.group(1);
            } else {
                var2 = "Null";
            }
        }
        return var2;
    }

    public static void a(final Context var0) {
        if(KeyManager.getInstance() != null) {
            CameraKey var1 = CameraKey.create("FormatSDCard");
            KeyManager.getInstance().performAction(var1, new ActionCallback() {
                public void onSuccess() {
                    UiDL.a(var0, 4, R.string.camera_format_sd_card_success, "");
                }

                public void onFailure(@NonNull DJIError var1) {
                    String var2 = var1.getDescription();
                    Boolean var3 = (Boolean)KeyManager.getInstance().getValue(CameraKey.create("SDCardIsConnectedToPC"));
                    if(var3 == null) {
                        var3 = Boolean.valueOf(false);
                    }

                    if(var3.booleanValue()) {
                        var2 = var0.getString(R.string.camera_format_sd_usb_connect);
                    }

                    UiDL.a(var0, R.string.camera_format_sd_card_busy_title, var2);
                }
            }, new Object[0]);
        }
    }
}
