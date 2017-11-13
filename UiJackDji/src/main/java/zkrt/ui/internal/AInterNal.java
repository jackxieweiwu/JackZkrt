package zkrt.ui.internal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import zkrt.ui.R;
import zkrt.ui.widget.FocusExposureSwitchWidget.ControlMode;

/**
 * Created by jack_xie on 17-6-1.
 */

public class AInterNal extends FrameLayout {
    private AnimatorSet a;
    private AnimatorSet b;
    private float c = -1.0F;
    private float d = -1.0F;

    public AInterNal(Context var1) {
        super(var1);
        int var2 = (int)(60.0F * var1.getResources().getDisplayMetrics().density);
        this.setLayoutParams(new LayoutParams(var2, var2));
        this.a = new AnimatorSet();
        ObjectAnimator var3 = ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0F}).setDuration(50L);
        ObjectAnimator var4 = ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.0F, 1.15F}).setDuration(100L);
        ObjectAnimator var5 = ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.0F, 1.15F}).setDuration(100L);
        ObjectAnimator var6 = ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.0F, 1.15F}).setDuration(100L);
        ObjectAnimator var7 = ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.0F, 1.15F}).setDuration(100L);
        var6.setRepeatCount(3);
        var6.setRepeatMode(Animation.REVERSE);
        var6.setStartDelay(200L);
        var7.setRepeatCount(3);
        var7.setRepeatMode(Animation.REVERSE);
        var7.setStartDelay(200L);
        this.a.play(var4).with(var3).after(400L).with(var5).before(var6).before(var7);
        this.a.setInterpolator(new OvershootInterpolator());
        this.a.addListener(new AInterNal.a());
        ObjectAnimator var8 = ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0F, 0.5F}).setDuration(400L);
        var8.setRepeatCount(3);
        var8.setRepeatMode(Animation.REVERSE);
        this.b = new AnimatorSet();
        this.b.play(ValueAnimator.ofInt(new int[]{0, 1}).setDuration(900L));
        this.b.addListener(new AInterNal.a());
    }



    public ControlMode a(ControlMode var1, float var2, float var3, float var4, float var5) {
        switch(var1.ordinal()) {
            case 1:
                this.c = -1.0F;
                this.d = -1.0F;
                var1 = ControlMode.SPOT_METER;
            case 2:
                float var6 = (var2 - this.c) / (float)this.getWidth();
                float var7 = (var3 - this.d) / (float)this.getHeight();
                if(var6 >= 0.75F && var6 <= 1.25F && var7 >= -0.25F && var7 <= 0.25F) {
                    var1 = ControlMode.CENTER_METER;
                    this.b.start();
                    this.setAlpha(1.0F);
                    var2 = var4 / 2.0F;
                    var3 = var5 / 2.0F;
                }

                this.a(var1);
                var2 -= (float)(this.getWidth() / 2);
                var3 -= (float)(this.getHeight() / 2);
                this.setTranslationX(var2);
                this.setTranslationY(var3);
                this.c = var2;
                this.d = var3;
                break;
            default:
                this.a(var1);
                this.a.start();
                this.setTranslationX(var2 - (float)(this.getWidth() / 2));
                this.setTranslationY(var3 - (float)(this.getHeight() / 2));
        }

        return var1;
    }

    public void setControlMode(ControlMode var1) {
        if(var1 == ControlMode.CENTER_METER) {
            this.a();
        }

    }

    private void a(ControlMode var1) {
        if(var1 == ControlMode.CENTER_METER) {
            this.setBackgroundResource(R.drawable.rectangle_866_copy);
            this.setScaleX(1.376F);
        } else {
            if(var1 == ControlMode.AUTO_FOCUS) {
                this.setBackgroundResource(R.drawable.rectangle_310);
            } else if(var1 == ControlMode.MANUAL_FOCUS) {
                this.setBackgroundResource(R.drawable.mf_area);
            } else {
                this.setBackgroundResource(R.drawable.oval_120);
            }

            this.setScaleX(1.0F);
        }

    }

    private void a() {
        int var1 = Build.VERSION.SDK_INT;
        if(var1 < 16) {
            this.setBackgroundDrawable(null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.setBackground(null);
            }
        }

    }

    private class a implements Animator.AnimatorListener {
        private a() {
        }

        public void onAnimationStart(Animator var1) {
        }

        public void onAnimationEnd(Animator var1) {
            AInterNal.this.c = -1.0F;
            AInterNal.this.d = -1.0F;
            AInterNal.this.a();
        }

        public void onAnimationCancel(Animator var1) {
        }

        public void onAnimationRepeat(Animator var1) {
        }
    }
}
