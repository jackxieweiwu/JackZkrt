package zkrt.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import dji.keysdk.DJIKey;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Func1;
import zkrt.ui.d.UiDF;

/**
 * Created by jack_Xie on 17-11-3.
 */

public abstract class UiBaseQView extends FrameLayout implements UiBaseLView  {
    protected static final String TAG = "DULFrameLayout";
    private UiBaseJView widgetStyle;
    private List<DJIKey> dependentKeys;
    protected Context context;

    public UiBaseQView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.widgetStyle = UiBaseJView.a;
        this.context = var1;
        if(!this.isInEditMode()) {
            this.initView(var1, var2, var3);
            this.initKey();
        }
    }

    public void setWidgetStyle(UiBaseJView var1) {
        this.widgetStyle = var1;
    }

    public void destroy() {
        UiDF.a().a(this);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!this.isInEditMode()) {
            this.registerDependentKeys();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.destroy();
    }

    public Observable<Boolean> transformValueObservable(final Object var1, final DJIKey var2) {
        return  Observable.just(var2).flatMap(new Func1<DJIKey, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(DJIKey var1x) {
                UiBaseQView.this.transformValue(var1, var2);
                return Observable.just(Boolean.valueOf(true));
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> updateWidgetObservable(final DJIKey var1) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> var1x) {
                UiBaseQView.this.updateWidget(var1);
                if(!var1x.isUnsubscribed()) {
                    var1x.onNext(Boolean.valueOf(true));
                    var1x.onCompleted();
                }

            }
        });
    }

    public float aspectRatio() {
        return 1.0F;
    }

    protected void addDependentKey(DJIKey var1) {
        if(this.dependentKeys == null) {
            this.dependentKeys = new ArrayList();
        }

        if(!this.dependentKeys.contains(var1)) {
            this.dependentKeys.add(var1);
        }

    }

    public List<DJIKey> getDependentKeys() {
        return this.dependentKeys;
    }

    public void registerDependentKeys() {
        if(this.getDependentKeys() != null) {
            UiDF.a().a(this.getDependentKeys(), this);
        }

    }

    public void updateWidget(DJIKey var1) {
    }
}
