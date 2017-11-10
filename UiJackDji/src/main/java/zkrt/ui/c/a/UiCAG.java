package zkrt.ui.c.a;

import android.support.annotation.AnyRes;

/**
 * Created by jack_xie on 17-5-31.
 */

public class UiCAG extends UiCAA {
    public UiCAG(int var1, int var2, int var3, int var4, @AnyRes int var5) {
        super(var1, var2, var3, var4, var5);
    }

    private UiCAG(UiCAG.UiCAG_A var1) {
        super(var1);
    }

    public UiCAG.UiCAG_A<?> g() {
        return new UiCAG.UiCAG_A(this) {
            @Override
            public UiCAG a() {
                return new UiCAG(this);
            }
        };
    }

    public abstract static class UiCAG_A<T extends UiCAG> extends UiCAA_A<T> {
        public UiCAG_A(UiCAG var1) {
            super(var1);
        }

        public abstract T a();
    }
}
