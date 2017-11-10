package zkrt.ui.c.a;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCAF extends UiCAA {
    @NonNull
    private String a;

    public UiCAF(int var1, int var2, int var3, int var4, int var5, String var6, String var7) {
        super(var1, var2, var3, var4, var5);
        this.a = var6;
    }

    private UiCAF(UiCAF.a var1) {
        super(var1);
        this.a = var1.a;
    }

    public void a(TextView var1) {
        float var4 = var1.getPaint().measureText(this.a);
        float var5 = (float)var1.getMeasuredWidth();
        float var3 = var5 / var4;
        var4 = (float)var1.getMeasuredHeight();
        var5 = (float)var1.getLineHeight();
        float var2 = var4 / var5;
        var2 = Math.min(var2, var3);
        var4 = var1.getTextSize();
        var5 = var4 * var2 * 0.98F;
        if(var5 > 0.0F && Math.abs(var5 - var4) > 0.75F) {
            var1.setTextSize(0, var5);
        }

    }

    public UiCAF.a<?> g() {
        return new UiCAF.a(this) {
           /* @Override
            public UiCAA a() {
                return null;
            }*/
            @Override
            public UiCAF a() {
                return new UiCAF(this);
            }
        };
    }

    public abstract static class a<T extends UiCAF> extends UiCAA_A<T> {
        private String a;

        public a(UiCAF var1) {
            super(var1);
            this.a = var1.a;
        }

        public abstract T a();
    }
}
