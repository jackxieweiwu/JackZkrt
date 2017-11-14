package zkrt.ui.internal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zkrt.ui.R;
import zkrt.ui.base.UiBaseAViewDialog;

/**
 * Created by jack_xie on 17-11-6.
 */

public class CInterNal extends UiBaseAViewDialog implements View.OnClickListener {
    private ImageView a;
    private TextView b;
    private TextView c;
    private ImageView d;
    private ImageView e;
    private LinearLayout f;
    private TextView g;
    private TextView h;
    private ImageView i;
    private android.content.DialogInterface.OnClickListener j;
    private android.content.DialogInterface.OnClickListener k;
    private int l;

    public CInterNal(Context var1) {
        super(var1);
        this.l = R.dimen.image_dialog_width;
        this.a();
    }

    public void a() {
        this.requestWindowFeature(1);
        this.setContentView(R.layout.widget_image_dlg);
        this.a = (ImageView)this.findViewById(R.id.imageview_dlg_title_icon);
        this.b = (TextView)this.findViewById(R.id.imageview_dlg_title);
        this.c = (TextView)this.findViewById(R.id.imageview_dlg_desc);
        this.d = (ImageView)this.findViewById(R.id.imageview_dlg_img);
        this.e = (ImageView)this.findViewById(R.id.imageview_dlg_divider_img);
        this.f = (LinearLayout)this.findViewById(R.id.imageview_dlg_btn_ly);
        this.g = (TextView)this.findViewById(R.id.imageview_button_left);
        this.h = (TextView)this.findViewById(R.id.imageview_button_right);
        this.i = (ImageView)this.findViewById(R.id.imageview_divider);
        this.f.setVisibility(View.GONE);
        this.i.setVisibility(View.GONE);
        this.e.setVisibility(View.GONE);
        this.g.setOnClickListener(this);
        this.h.setOnClickListener(this);
    }

    public CInterNal a(int var1) {
        this.a.setBackgroundResource(var1);
        if(var1 == 0) {
            this.a.setVisibility(View.GONE);
        } else {
            this.a.setVisibility(View.VISIBLE);
        }

        return this;
    }

    public CInterNal a(String var1) {
        this.b.setText(var1);
        return this;
    }

    public CInterNal b(String var1) {
        this.c.setText(var1);
        return this;
    }

    public CInterNal b(int var1) {
        this.c.setVisibility(var1);
        return this;
    }

    public CInterNal c(int var1) {
        this.d.setImageResource(var1);
        this.d.setVisibility(View.VISIBLE);
        return this;
    }

    public CInterNal b() {
        this.d.setVisibility(View.GONE);
        return this;
    }

    public CInterNal c() {
        this.f.setVisibility(View.GONE);
        this.g.setVisibility(View.GONE);
        this.h.setVisibility(View.GONE);
        this.i.setVisibility(View.GONE);
        return this;
    }

    public CInterNal d(int var1) {
        return this.c(this.getContext().getString(var1));
    }

    public CInterNal c(String var1) {
        this.f.setVisibility(View.VISIBLE);
        this.i.setVisibility(View.GONE);
        this.e.setVisibility(View.VISIBLE);
        this.g.setVisibility(View.VISIBLE);
        this.g.setText(var1);
        return this;
    }

    public CInterNal e(int var1) {
        return this.d(this.getContext().getString(var1));
    }

    public CInterNal d(String var1) {
        this.f.setVisibility(View.VISIBLE);
        this.i.setVisibility(View.VISIBLE);
        this.e.setVisibility(View.VISIBLE);
        this.h.setVisibility(View.VISIBLE);
        this.h.setText(var1);
        return this;
    }

    public CInterNal a(android.content.DialogInterface.OnClickListener var1) {
        this.k = var1;
        return this;
    }

    public CInterNal b(android.content.DialogInterface.OnClickListener var1) {
        this.j = var1;
        return this;
    }

    protected void onCreate(Bundle var1) {
        this.a((int)this.getContext().getResources().getDimension(this.l), -2, 0, 17, false, false);
    }

    public void onClick(View var1) {
        int var2 = var1.getId();
        if(R.id.imageview_button_left == var2) {
            if(null != this.k) {
                this.k.onClick(this, 0);
            } else {
                this.dismiss();
            }
        } else if(R.id.imageview_button_right == var2) {
            if(null != this.j) {
                this.j.onClick(this, 1);
            } else {
                this.dismiss();
            }
        }

    }
}
