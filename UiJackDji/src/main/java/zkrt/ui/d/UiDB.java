package zkrt.ui.d;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.widget.TextView;

import dji.log.DJILog;
import zkrt.ui.c.a.UiCAF;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiDB {
    private MediaPlayer mediaPlayer;

    public UiDB() {
    }

    public void a(Context var1, int var2) {
        try {
            mediaPlayer = MediaPlayer.create(var1, var2);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer var1) {
                    mediaPlayer.release();
                }
            });
            AudioManager var3 = (AudioManager)var1.getSystemService("audio");
            float var4 = (float)var3.getStreamMaxVolume(3);
            float var5 = (float)var3.getStreamVolume(3);
            float var6 = var5 / var4;
            if(var6 < 0.3F) {
                var6 = 0.3F;
            }

            mediaPlayer.setVolume(var6, var6);
            mediaPlayer.start();
        } catch (Exception var7) {
            DJILog.d("PlaySound", var7.getMessage());
        }

    }

    public static void a(Context var0) {
    }
}
