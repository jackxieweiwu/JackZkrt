package zkrt.ui.c.a;

/**
 * Created by jack_xie on 17-5-31.
 */

public class UiCAA {
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;

    public UiCAA(int var1, int var2, int var3, int var4, int var5) {
        this.a = var1;
        this.b = var2;
        this.c = var3;
        this.d = var4;
        this.e = var5;
    }

    protected UiCAA(UiCAA.UiCAA_A var1) {
        this(var1.a, var1.b, var1.c, var1.d, var1.e);
    }

    public int a() {
        return this.e;
    }

    public int b() {
        return this.c;
    }

    public int c() {
        return this.d;
    }

    public int d() {
        return this.a;
    }

    public int e() {
        return this.b;
    }

    public UiCAA.UiCAA_A<?> f() {
        return new UiCAA.UiCAA_A(this) {
            @Override
            public UiCAA a() {
                return new UiCAA(this);
            }
        };
    }

    public abstract static class UiCAA_A<T extends UiCAA> {
        private int a;
        private int b;
        private int c;
        private int d;
        private int e;

        public UiCAA_A(UiCAA var1) {
            this.a = var1.d();
            this.b = var1.e();
            this.c = var1.b();
            this.d = var1.c();
            this.e = var1.a();
        }

        public UiCAA.UiCAA_A<T> a(int var1) {
            this.a = var1;
            return this;
        }

        public UiCAA.UiCAA_A<T> b(int var1) {
            this.b = var1;
            return this;
        }

        public UiCAA.UiCAA_A<T> c(int var1) {
            this.c = var1;
            return this;
        }

        public UiCAA.UiCAA_A<T> d(int var1) {
            this.d = var1;
            return this;
        }

        public abstract T a();
    }
}
