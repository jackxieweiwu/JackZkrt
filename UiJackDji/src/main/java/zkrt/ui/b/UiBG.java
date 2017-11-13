package zkrt.ui.b;

import android.content.Context;

import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.UiCC;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiBG {
    public static UiBaseCFramelayout a(Context context, UiCC.enum_b var1) {
        return (var1 == UiCC.enum_b.e?new UiBD(context):(var1 == UiCC.enum_b.b?new UiBA(context):(var1 == UiCC.enum_b.c?new UIBH(context):(var1 == UiCC.enum_b.d?new UiBE(context):(var1 == UiCC.enum_b.f?new UiBI(context):new UiBF(context))))));
    }
}
