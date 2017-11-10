package zkrt.ui.d;

import android.content.Context;
import android.media.MediaPlayer;

import dji.log.DJILog;
import zkrt.ui.R;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiDH {
    private final Context mContext;
    private MediaPlayer mediaPlayer;

    public UiDH(Context context) {
        this.mContext = context;
    }

    private int c() {
        return R.raw.shutter_1;
    }

    private int b(int var1) {
        int var2;
        switch(var1) {
            case 3:
                var2 = R.raw.shutter_3;
                break;
            case 4:
            case 6:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            default:
                var2 = R.raw.shutter_3;
                break;
            case 5:
                var2 = R.raw.shutter_5;
                break;
            case 7:
                var2 = R.raw.shutter_7;
                break;
            case 10:
                var2 = R.raw.shutter_10;
                break;
            case 14:
                var2 = R.raw.shutter_14;
        }

        return var2;
    }

    public void a(int var1) {
        int var2;
        if(var1 > 1) {
            var2 = this.b(var1);
        } else {
            var2 = this.c();
        }

        try {
            mediaPlayer = MediaPlayer.create(mContext, var2);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer var1) {
                    var1.release();
                }
            });
            mediaPlayer.start();
        } catch (Exception var4) {
            DJILog.d("startTakePic", var4.getMessage());
        }

    }

    public void a() {
        try {
            mediaPlayer = MediaPlayer.create(mContext, R.raw.video_voice);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer var1) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        } catch (Exception var2) {
            DJILog.d("startVideo", var2.getMessage());
        }

    }

    public void b() {
        try {
            mediaPlayer = MediaPlayer.create(mContext, R.raw.end_video_record);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer var1) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.start();
        } catch (Exception var2) {
            DJILog.d("stopVideo", var2.getMessage());
        }

    }
}
