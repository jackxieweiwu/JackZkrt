package zkrt.ui.c.a;

/**
 * Created by jack_xie on 17-6-1.
 */

public class UiCAE extends UiCAA {
    public UiCAE(int var1, int var2, int var3, int var4, int var5) {
        super(var1, var2, var3, var4, var5);
    }

    private UiCAE(UiCAE.a var1) {
        super(var1);
    }

    public UiCAE.a<?> g() {
        return new UiCAE.a(this) {
            /*@Override
            public UiCAA a() {
                return null;
            }*/
            @Override
            public UiCAE a() {
                return new UiCAE(this);
            }
        };
    }

    public abstract static class a<T extends UiCAE> extends UiCAA_A<T> {
        public a(UiCAE var1) {
            super(var1);
        }

        public abstract T a();
    }
}
