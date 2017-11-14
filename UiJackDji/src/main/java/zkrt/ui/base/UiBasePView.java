package zkrt.ui.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import zkrt.ui.R;
import zkrt.ui.a.UiAB;
import zkrt.ui.c.UiCC;
import zkrt.ui.internal.RecyclerListView;

/**
 * Created by jack_xie on 17-11-3.
 */

public abstract class UiBasePView extends UiBaseOView implements UiAB.a {
    protected RecyclerListView contentList;
    protected UiAB adapter;
    protected String[] itemNameArray;
    protected TypedArray itemImageIdArray;

    public UiBasePView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        LayoutInflater var4 = (LayoutInflater)var1.getSystemService("layout_inflater");
        View var5 = var4.inflate(R.layout.widget_list_view, this);
        this.contentList = (RecyclerListView)var5.findViewById(R.id.recycle_list_view_content);
    }

    protected void initAdapter(int[] var1) {
        if(var1 != null) {
            if(this.itemNameArray != null) {
                this.adapter = new UiAB(this);
                int[] var2 = var1;
                int var3 = var1.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    int var5 = var2[var4];
                    UiCC var6 = new UiCC();
                    var6.inta = var5;
                    if(var6.inta < this.itemNameArray.length) {
                        var6.a(this.itemNameArray[var6.inta]);
                    }

                    if(this.itemImageIdArray != null && this.itemImageIdArray.hasValue(var6.inta)) {
                        var6.b = this.itemImageIdArray.getResourceId(var6.inta, 0);
                        var6.d = UiCC.enum_b.d;
                    }

                    this.adapter.a(var6);
                }

                this.contentList.setAdapter(this.adapter);
            }
        }
    }
}
