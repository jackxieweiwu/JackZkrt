package zkrt.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import zkrt.ui.R;


/**
 * Created by jack_xie on 17-11-3.
 */

public abstract class UiBaseAViewDialog extends Dialog{
    public UiBaseAViewDialog(@NonNull Context var1) {
        super((Context)(new WeakReference(var1)).get());
    }

    public void a(int var1, int var2, int var3, int var4, boolean var5, boolean var6) {
        WindowManager.LayoutParams var7 = this.getWindow().getAttributes();
        var7.width = var1;
        var7.height = var2;
        var7.y = var3;
        var7.flags &= -3;
        var7.gravity = var4;
        this.getWindow().setAttributes(var7);
        this.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        this.setCancelable(var5);
        this.setCanceledOnTouchOutside(var6);
    }
}
