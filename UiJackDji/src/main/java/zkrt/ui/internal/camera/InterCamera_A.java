package zkrt.ui.internal.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import zkrt.ui.R;
import zkrt.ui.a.UiAB;
import zkrt.ui.base.UiBaseKView;
import zkrt.ui.base.UiBaseNView;
import zkrt.ui.base.UiBaseQView;
import zkrt.ui.c.UiCC;
import zkrt.ui.internal.DULParentChildrenViewAnimator;
import zkrt.ui.internal.RecyclerListView;

/**
 * Created by jack_xie on 17-6-1.
 */

public abstract class InterCamera_A extends UiBaseQView implements UiAB.a {
    protected UiCC.a[] b = null;
    protected UiAB c;
    protected DULParentChildrenViewAnimator.a d;
    protected RecyclerListView e;
    protected TextView f;
    protected View g;
    private Animation a;
    private Animation h;
    private Animation i;
    private Animation j;

    public InterCamera_A(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void setTitleTextView(TextView var1) {
        this.f = var1;
    }

    public void setRootViewCallback(DULParentChildrenViewAnimator.a var1) {
        this.d = var1;
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        LayoutInflater var4 = (LayoutInflater)var1.getSystemService("layout_inflater");
        var4.inflate(R.layout.widget_list_view, this, true);
        this.e = (RecyclerListView)this.findViewById(R.id.recycle_list_view_content);
        this.g();
        this.c();
        this.h();
    }

    private void g() {
        this.setRootViewCallback(new DULParentChildrenViewAnimator.a() {
            public void onRootViewIsShown(boolean var1) {
                if(InterCamera_A.this.d != null) {
                    InterCamera_A.this.d.onRootViewIsShown(var1);
                }

            }
        });
    }

    public void setRootViewIsShown(boolean var1) {
        if(this.d != null) {
            this.d.onRootViewIsShown(var1);
        }

        if(var1) {
            this.e.setVisibility(VISIBLE);
        } else {
            this.e.setVisibility(INVISIBLE);
        }

    }

    protected void c() {
        this.a();
        this.c = new UiAB(this);
        if(this.b != null && this.b.length > 0) {
            for(int var1 = 0; var1 < this.b.length; ++var1) {
                UiCC var2 = new UiCC();
                var2.a(this.getResources().getString(this.b[var1].a));
                var2.d = this.b[var1].b;
                this.c.a(var2);
            }

            this.b();
            this.e.setAdapter(this.c);
        }

    }

    private void h() {
        this.a = AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_left_in);
        this.h = AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_left_out);
        this.i = AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_right_in);
        this.j = AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_right_out);
    }

    protected void a(final int var1, boolean var2) {
        UiCC var3 = this.c.c(var1);
        var3.b(var2);
        this.post(new Runnable() {
            public void run() {
                InterCamera_A.this.c.notifyItemChanged(var1);
            }
        });
    }

    protected void a(final int var1, int var2, int var3) {
        UiCC var4 = c.c(var1);
        if(var4.inta != var2 || var4.c != var3) {
            var4.inta = var2;
            var4.c = var3;
            this.post(new Runnable() {
                public void run() {
                    c.notifyItemChanged(var1);
                }
            });
        }

    }

    protected void d() {
        if(this.g != null && this.g.getParent() instanceof ViewGroup) {
            ((ViewGroup)((ViewGroup)this.g.getParent())).removeView(this.g);
            this.g = null;
        }

    }

    protected void e() {
        if(this.g != null) {
            LayoutParams var1 = (LayoutParams) this.e.getLayoutParams();
            this.g.setLayoutParams(var1);
            this.g.startAnimation(this.i);
            this.f.startAnimation(this.i);
            this.e.startAnimation(this.h);
            this.addView(this.g);
            this.setRootViewIsShown(false);
            UiBaseNView var2 = (UiBaseNView)this.g;
            var2.updateTitle(this.f);
        }

    }

    public void f() {
        if(this.g != null) {
            this.g.startAnimation(this.j);
            this.f.startAnimation(this.j);
            this.e.startAnimation(this.a);
            this.setRootViewIsShown(true);
            this.removeView(this.g);
            this.g = null;
        }

    }

    protected abstract void a();

    protected abstract void b();
}
