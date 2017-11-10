package zkrt.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;

import zkrt.ui.R;
import zkrt.ui.d.UiDG;
import zkrt.ui.internal.DInterNal;

import android.view.View.OnClickListener;

/**
 * Created by jack_xie on 17-11-3.
 */

public abstract class UiBaseEView extends UiBaseGViewDULFrameLayout implements OnClickListener{
    protected static final float DISABLE_ALPHA = 0.5F;
    protected static final float ENABLE_ALPHA = 1.0F;
    protected int defaultResId = 0;
    protected ImageButton imageBackground;
    protected ImageView imageForeground;
    protected static UiDG.dhEnumA value_Unit_Type;

    public static void setWidgetUnitType(UiDG.dhEnumA var0) {
        value_Unit_Type = var0;
    }

    public UiBaseEView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    protected void showErrorDlg(String var1) {
        Context var2 = this.getContext();
        DInterNal var3 = new DInterNal(var2);
        var3.a(3);
        var3.a(new DInterNal.a() {
            public void onRightBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
            }

            public void onLeftBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
            }

            public void onCbChecked(DialogInterface var1, boolean var2, int var3) {
            }
        });
        var3.a(8, 0).e(8).d(8);
        var3.a(8, "");
        var3.a(var2.getString(R.string.error));
        var3.b(var1);
        var3.show();
    }

    static {
        value_Unit_Type = UiDG.dhEnumA.a;
    }
}
