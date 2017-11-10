package antistatic.spinnerwheel.a;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dji.midware.ui.antistatic.spinnerwheel.a.AnSpinner_C;

/**
 * Created by jack_Xie on 17-5-31.
 */

public abstract class AnSpinner_A implements AnSpinner_C {
    private List<DataSetObserver> a;

    public AnSpinner_A() {
    }

    public View a(View var1, ViewGroup var2) {
        return null;
    }

    public void a(DataSetObserver var1) {
        if(this.a == null) {
            this.a = new LinkedList();
        }

        this. a.add(var1);
    }

    public void b(DataSetObserver var1) {
        if(this.a != null) {
            this.a.remove(var1);
        }

    }

    protected void a() {
        if(this.a != null) {
            Iterator var1 = this.a.iterator();

            while(var1.hasNext()) {
                DataSetObserver var2 = (DataSetObserver)var1.next();
                var2.onChanged();
            }
        }

    }
}
