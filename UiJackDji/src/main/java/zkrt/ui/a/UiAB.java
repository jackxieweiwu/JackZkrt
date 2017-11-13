package zkrt.ui.a;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zkrt.ui.R;
import zkrt.ui.b.UiBG;
import zkrt.ui.base.UiBaseCFramelayout;
import zkrt.ui.c.UiCC;
import zkrt.ui.internal.DULSwitchButton;

/**
 * Created by root on 17-5-31.
 */

public class UiAB extends Adapter<UiAB.UiAbViewHolder> {
    private UiAB.a a;
    private ArrayList<UiCC> uiCCArrayList = new ArrayList();
    private int c = -1;
    private boolean d;

    public void a(boolean var1) {
        this.d = var1;
    }

    public UiAB(UiAB.a a) {
        this.a = a;
    }

    public void a(int var1) {
        if(var1 != -1) {
            int var2 = this.c;
            if(var1 != this.c) {
                this.c = var1;
                UiCC var3;
                if(var2 != -1) {
                    var3 = this.uiCCArrayList.get(var2);
                    var3.a(false);
                    this.notifyItemChanged(var2);
                }

                var3 = this.uiCCArrayList.get(this.c);
                var3.a(true);
                this.notifyItemChanged(this.c);
            }
        }

    }

    @Override
    public UiAbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UiBaseCFramelayout var3 = UiBG.a(parent.getContext(), UiCC.enum_b.a(viewType));
        var3.setBackgroundResource(R.drawable.selector_list_item);
        UiAbViewHolder var4 = new UiAbViewHolder(var3, this);
        var3.setTag(var4);
        return var4;
    }

    @Override
    public void onBindViewHolder(UiAbViewHolder holder, int position) {
        UiCC var3 = this.uiCCArrayList.get(position);
        holder.a(var3);
    }

    public int getItemViewType(int var1) {
        if(var1 < this.uiCCArrayList.size()) {
            UiCC var2 = this.c(var1);
            if(var2.d != null) {
                return var2.d.g;
            }
        }
        return 0;
    }

    public int getItemCount() {
        return this.uiCCArrayList.size();
    }

    public void a(UiCC var1) {
        this.uiCCArrayList.add(var1);
        this.notifyItemInserted(this.uiCCArrayList.size());
    }

    public int b(UiCC var1) {
        return this.uiCCArrayList.indexOf(var1);
    }

    public int b(int var1) {
        for(int var2 = 0; var2 < this.uiCCArrayList.size(); ++var2) {
            if(uiCCArrayList.get(var2).inta == var1) {
                return var2;
            }
        }

        return -1;
    }

    public UiCC c(int var1) {
        return this.uiCCArrayList.get(var1);
    }

    static final class UiAbViewHolder extends ViewHolder implements View.OnClickListener,DULSwitchButton.a {
        private ImageView a;
        private TextView b;
        private DULSwitchButton c;
        private ImageView d;
        private TextView e;
        private ImageView f;
        private UiAB g;
        private Button h;

        UiAbViewHolder(View var1, UiAB var2) {
            super(var1);
            this.a = (ImageView)var1.findViewById(R.id.list_item_title_icon);
            this.b = (TextView)var1.findViewById(R.id.list_item_title);
            this.d = (ImageView)var1.findViewById(R.id.list_item_value_icon);
            this.c = (DULSwitchButton)var1.findViewById(R.id.list_item_value_switch_button);
            this.e = (TextView)var1.findViewById(R.id.list_item_value);
            this.f = (ImageView)var1.findViewById(R.id.list_item_arrow);
            this.h = (Button)var1.findViewById(R.id.action_button);
            this.g = var2;
            if(!var2.d) {
                var1.setOnClickListener(this);
                if(this.c != null) {
                    this.c.setOnCheckedListener(this);
                }
            }
            if(this.h != null) {
                this.h.setOnClickListener(this);
            }
        }

        void a(UiCC var1) {
            if(null != var1) {
                if(var1.b == 0) {
                    this.a.setVisibility(View.GONE);
                } else {
                    this.a.setVisibility(View.VISIBLE);
                    this.a.setImageResource(var1.b);
                }

                if(var1.b().isEmpty()) {
                    this.b.setVisibility(View.GONE);
                } else {
                    this.b.setVisibility(View.VISIBLE);
                    this.b.setText(var1.b());
                }

                if(this.d != null) {
                    if(var1.c == 0) {
                        this.d.setVisibility(View.GONE);
                    } else {
                        this.d.setVisibility(View.VISIBLE);
                        this.d.setImageResource(var1.c);
                    }
                }

                if(this.e != null) {
                    if(TextUtils.isEmpty(var1.d())) {
                        this.e.setVisibility(View.GONE);
                    } else {
                        this.e.setVisibility(View.VISIBLE);
                        this.e.setText(var1.d());
                        if(var1.c() != 0) {
                            this.e.setTextColor(var1.c());
                        }
                    }
                }

                this.itemView.setEnabled(var1.f());
                if(var1.d == UiCC.enum_b.a && var1.f()) {
                    this.f.setVisibility(View.VISIBLE);
                } else {
                    this.f.setVisibility(View.GONE);
                }

                if(this.c != null) {
                    if(var1.d == UiCC.enum_b.c) {
                        this.c.setVisibility(View.VISIBLE);
                        this.c.setChecked(var1.inta != 0);
                    } else {
                        this.c.setVisibility(View.GONE);
                    }
                }

                if(this.h != null) {
                    String var2 = var1.e();
                    if(var2 != null) {
                        this.h.setVisibility(View.VISIBLE);
                        this.h.setText(var2);
                    } else {
                        this.h.setVisibility(View.GONE);
                    }
                }

                this.itemView.setSelected(var1.a());
            }

        }

        public void onClick(View var1) {
            this.a(var1);
        }

        public void onCheckedChanged(boolean var1) {
            this.a(this.c);
        }

        private void a(View var1) {
            if(this.g.a != null) {
                int var2 = this.getAdapterPosition();
                this.g.a.updateSelectedItem(var1, var2);
            }

        }
    }

    public interface a {
        void updateSelectedItem(View var1, int var2);
    }
}
