package com.jack.jackzkrt.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jack.frame.core.AbsDialogFragment;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.databinding.DialogPairingBinding;
import com.jack.jackzkrt.moudle.BindingModule;

import butterknife.Bind;

/**
 * Created by jack_xie on 17-4-26.
 */

public class DialogPairimg extends AbsDialogFragment<DialogPairingBinding> implements View.OnClickListener {
    @Bind(R.id.enter)Button mEnter;
    @Bind(R.id.cancel)
    Button mCancel;

    @Override
    protected void init(Bundle savedInstanceState) {
        getModule(BindingModule.class).dialogBindingTest(this);
        mEnter.setOnClickListener(this);
        mEnter.setVisibility(View.GONE);
        mCancel.setOnClickListener(this);
        getBinding().setStrTitle("遥控器对频");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_pairing;
    }

    @Override
    protected void dataCallback(int result, Object data) {}

    @Override
    public void onClick(View v) {
        int numberCallback = getModule(BindingModule.class).getCallBack();
        switch (v.getId()) {
            case R.id.enter:
                //将数据回调给寄主
                //getSimplerModule().onDialog(Constance.KEY.IP_DIALOG,"对话框确认");

                break;
            case R.id.cancel:
                //getSimplerModule().onDialog(Constance.KEY.IP_DIALOG, "对话框取消");
                getModule(BindingModule.class).dialogRemoteStopPairing(this);
                break;
        }
        dismiss();
    }


    public void setAddDismiss(){
        dismiss();
    }
}
