package zkrt.ui.d;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import zkrt.ui.R;
import zkrt.ui.internal.DInterNal;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiDL  {
    public static void a(@NonNull View var0) {
        if(var0.isSelected()) {
            var0.setSelected(false);
        } else {
            var0.setSelected(true);
        }
    }

    public static void b(@NonNull View var0) {
        var0.setSelected(false);
    }

    public static void c(@NonNull View var0) {
        var0.setClickable(true);
        var0.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View var1, MotionEvent var2) {
                return true;
            }
        });
    }

    public static void a(Context var0, String var1, DInterNal.a var2) {
        DInterNal var3 = new DInterNal(var0);
        var3.a(1);
        var3.a(var2);
        var3.a(8, 0).e(8);
        var3.a(8, "");
        var3.a(var0.getString(R.string.app_tip));
        var3.b(var1);
        var3.a();
        var3.show();
    }

    public static void a(Context var0, @StringRes int var1, String var2) {
        a(var0, 1, var1, var2);
    }

    public static void a(Context var0, int var1, @StringRes int var2, String var3) {
        DInterNal var4 = new DInterNal(var0);
        var4.a(var1);
        var4.a(new DInterNal.a() {
            public void onRightBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
            }

            public void onLeftBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
            }

            public void onCbChecked(DialogInterface var1, boolean var2, int var3) {
            }
        });
        var4.a(8, 0).e(8);
        var4.a(8, "");
        var4.a(var0.getString(R.string.app_tip));
        var4.b(var0.getString(var2) + ". " + var3);
        if(var1 == 4) {
            var4.c(8);
        } else {
            var4.d(8);
        }

        var4.show();
    }

    public static void a(@NonNull View var0, boolean var1) {
        if(var1) {
            var0.setVisibility(View.VISIBLE);
        } else {
            var0.setVisibility(View.INVISIBLE);
        }

    }

    public static int a(Context var0, AttributeSet var1, @StyleableRes int[] var2, int var3, int var4) {
        TypedArray var5 = var0.getTheme().obtainStyledAttributes(var1, var2, 0, 0);

        int var6;
        try {
            var6 = var5.getInteger(var3, 0);
        } finally {
            var5.recycle();
        }

        return var6;
    }
}
