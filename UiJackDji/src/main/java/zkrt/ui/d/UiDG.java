package zkrt.ui.d;



/**
 * Created by jack_xie on 17-6-1.
 */

public class UiDG {
    public static float a(float var0) {
        return var0 * 3.2808F;
    }

    public static float b(float var0) {
        return var0 / 3.2808F;
    }

    public static float c(float var0) {
        return var0 * 2.2369F;
    }

    public static float a(float var0, UiDG.dhEnumA var1) {
        float var2;
        if(!c(var1)) {
            var2 = a(var0);
        } else {
            var2 = var0;
        }

        return var2;
    }

    public static float b(float var0, UiDG.dhEnumA var1) {
        float var2;
        if(!c(var1)) {
            var2 = c(var0);
        } else {
            var2 = var0;
        }

        return var2;
    }

    public static String a(UiDG.dhEnumA var0) {
        return c(var0)?"m":"ft";
    }

    public static String b(UiDG.dhEnumA var0) {
        return c(var0)?"m/s":"mile/h";
    }

    public static boolean c(UiDG.dhEnumA var0) {
        return var0 == UiDG.dhEnumA.a;
    }

    public static enum dhEnumA {
        a("Metric", 0),
        b("Imperial", 1);

        private String c;
        private int d;

        private dhEnumA(String var3, int var4) {
            this.c = var3;
            this.d = var4;
        }

        public String toString() {
            return this.c;
        }
    }
}
