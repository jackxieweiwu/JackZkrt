package zkrt.ui.internal;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import zkrt.ui.R;
import zkrt.ui.base.UiBaseAViewDialog;
import zkrt.ui.d.UiDG;

/**
 * Created by jack_xie on 17-11-6.
 */

public class DInterNal extends UiBaseAViewDialog implements View.OnClickListener {
    private ImageView b;
    private TextView c;
    private TextView d;
    private TextView e;
    private LinearLayout f;
    private EditText g;
    private TextView h;
    private RelativeLayout i;
    private TextView j;
    private SeekBar k;
    private TextView l;
    private TextView m;
    private ImageView n;
    private SeekBar.OnSeekBarChangeListener o;
    protected static UiDG.dhEnumA a;
    private DInterNal.a p;
    private int q;

    public DInterNal(Context var1) {
        this(var1, R.dimen.dul_sliding_dialog_width);
    }

    public DInterNal(Context var1, int var2) {
        super(var1);
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.n = null;
        this.o = null;
        this.p = null;
        this.q = var2;
        this.c();
    }

    public DInterNal a(DInterNal.a var1) {
        this.p = var1;
        return this;
    }

    public DInterNal a(int var1) {
        if(0 == var1) {
            this.b.setBackgroundResource(R.drawable.leftmenu_popup_alert);
            this.c.setTextColor(this.getContext().getResources().getColor(R.color.green));
            this.d.setTextColor(this.getContext().getResources().getColor(R.color.green));
        } else if(1 == var1) {
            this.b.setBackgroundResource(R.drawable.leftmenu_popup_alert);
            this.c.setTextColor(this.getContext().getResources().getColor(R.color.yellow_medium));
            this.d.setTextColor(this.getContext().getResources().getColor(R.color.yellow_medium));
        } else if(2 == var1) {
            this.b.setBackgroundResource(R.drawable.leftmenu_popup_warning);
            this.c.setTextColor(this.getContext().getResources().getColor(R.color.red_light));
            this.d.setTextColor(this.getContext().getResources().getColor(R.color.red_light));
        } else if(3 == var1) {
            this.b.setBackgroundResource(R.drawable.leftmenu_popup_warning);
            this.c.setTextColor(this.getContext().getResources().getColor(R.color.red_light));
            this.d.setTextColor(this.getContext().getResources().getColor(R.color.red_light));
        } else if(4 == var1) {
            this.b.setBackgroundResource(R.drawable.leftmenu_popup_greencheck);
            this.c.setTextColor(this.getContext().getResources().getColor(R.color.green));
            this.d.setTextColor(this.getContext().getResources().getColor(R.color.green));
        } else if(6 == var1) {
            this.b.setVisibility(View.GONE);
            this.c.setTextColor(this.getContext().getResources().getColor(R.color.white));
            this.d.setVisibility(View.VISIBLE);
        }

        return this;
    }

    public DInterNal b(int var1) {
        this.b.setBackgroundResource(var1);
        return this;
    }

    public DInterNal a(String var1) {
        this.c.setText(var1);
        return this;
    }

    public DInterNal a(int var1, String var2) {
        this.d.setVisibility(var1);
        this.d.setText(var2);
        return this;
    }

    public DInterNal b(String var1) {
        this.e.setText(var1);
        return this;
    }

    public DInterNal c(int var1) {
        this.l.setVisibility(var1);
        this.n.setVisibility(var1);
        return this;
    }

    public DInterNal d(int var1) {
        this.m.setVisibility(var1);
        this.n.setVisibility(var1);
        return this;
    }

    public DInterNal a(int var1, int var2) {
        this.f.setVisibility(var1);
        if(a == UiDG.dhEnumA.b) {
            this.h.setText(R.string.setting_foot);
            var2 = (int)UiDG.b((float)var2);
        } else if(a == UiDG.dhEnumA.a) {
            this.h.setText(R.string.setting_metric);
        }

        this.g.setText(String.valueOf(var2));
        return this;
    }

    public DInterNal c(String var1) {
        this.j.setText(var1);
        return this;
    }

    public DInterNal e(int var1) {
        this.k.setProgress(0);
        this.i.setVisibility(var1);
        return this;
    }

    private void b() {
        this.o = new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar var1) {
                DInterNal.this.d();
            }

            public void onStartTrackingTouch(SeekBar var1) {
            }

            public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
            }
        };
    }

    public void show() {
        this.getWindow().setFlags(8, 8);
        super.show();
    }

    private void c() {
        this.requestWindowFeature(1);
        this.b();
        this.setContentView(R.layout.widget_sliding_dlg);
        this.b = (ImageView)this.findViewById(R.id.imageview_dlg_title_icon);
        this.c = (TextView)this.findViewById(R.id.textview_dlg_title);
        this.d = (TextView)this.findViewById(R.id.textview_dlg_little_title);
        this.e = (TextView)this.findViewById(R.id.textview_dlg_desc);
        this.f = (LinearLayout)this.findViewById(R.id.linearlayout_dlg_edit_text);
        this.g = (EditText)this.findViewById(R.id.edittext_value);
        this.h = (TextView)this.findViewById(R.id.textview_value_unit);
        this.i = (RelativeLayout)this.findViewById(R.id.relativelayout_slidebar);
        this.j = (TextView)this.findViewById(R.id.textview_slidertitle);
        this.k = (SeekBar)this.findViewById(R.id.seekbar_slider);
        this.l = (TextView)this.findViewById(R.id.textview_button_cancel);
        this.m = (TextView)this.findViewById(R.id.textview_button_ok);
        this.n = (ImageView)this.findViewById(R.id.imageview_divider);
        this.l.setOnClickListener(this);
        this.m.setOnClickListener(this);
        this.k.setOnSeekBarChangeListener(this.o);
        this.k.setPadding(0, 0, 0, 0);
    }

    private void d() {
        int var1 = this.k.getProgress();
        if(var1 >= 95) {
            this.k.setProgress(100);
            this.a(true);
        } else {
            this.k.setProgress(0);
            this.a(false);
        }

    }

    private void a(boolean var1) {
        if(null != this.p) {
            this.p.onCbChecked(this, var1, 0);
        }

    }

    private void e() {
        if(null != this.p) {
            this.p.onLeftBtnClick(this, 0);
        }

    }

    private void f() {
        if(null != this.p) {
            this.p.onRightBtnClick(this, 0);
        }

    }

    protected void onCreate(Bundle var1) {
        this.a((int)this.getContext().getResources().getDimension(this.q), -2, 0, 17, true, false);
    }

    public void onClick(View var1) {
        int var2 = var1.getId();
        if(var2 == R.id.textview_button_cancel) {
            this.e();
        } else if(var2 == R.id.textview_button_ok) {
            this.f();
        }

    }

    public void a() {
        this.l.setText(R.string.app_no);
        this.m.setText(R.string.app_yes);
    }

    static {
        a = UiDG.dhEnumA.a;
    }

    public interface a {
        void onLeftBtnClick(DialogInterface var1, int var2);

        void onRightBtnClick(DialogInterface var1, int var2);

        void onCbChecked(DialogInterface var1, boolean var2, int var3);
    }
}
