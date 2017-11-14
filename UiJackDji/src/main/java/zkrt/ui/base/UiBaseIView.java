package zkrt.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import java.util.ArrayList;
import java.util.List;

import dji.keysdk.DJIKey;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Func1;
import zkrt.ui.d.UiDF;


/**
 * Created by root on 17-11-3.
 */

public abstract class UiBaseIView extends TextureView implements UiBaseLView {
    private List<DJIKey> dependentKeys;

    public UiBaseIView(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public UiBaseIView(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public UiBaseIView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.initView(var1, var2, var3);
        if(!this.isInEditMode()) {
            this.initKey();
        }
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

    public void destroy() {
        UiDF.a().a(this);
    }

    public Observable<Boolean> transformValueObservable(final Object var1, final DJIKey var2) {
        return Observable.just(var2).flatMap(new Func1<DJIKey, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(DJIKey djiKey) {
                UiBaseIView.this.transformValue(var1, var2);
                return  Observable.just(Boolean.valueOf(true));
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> updateWidgetObservable(final DJIKey var1) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                UiBaseIView.this.updateWidget(var1);
                UiBaseIView.this.updateWidget();
                if(!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Boolean.valueOf(true));
                    subscriber.onCompleted();
                }
            }
        });
    }

    public void updateWidget(DJIKey var1) {
    }

    public void updateWidget() {
    }

    public float aspectRatio() {
        return -1.0F;
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
        UiDF.a().a(this.getDependentKeys(), this);
    }
}
