package com.jack.jackzkrt.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jack.frame.core.AbsDialog;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.bean.MavlinkDjiBean;
import com.jack.jackzkrt.fragment.HandleMavlinkData;

import butterknife.Bind;
import dji.common.error.DJIError;
import static zkrt.ui.base.BaseApplication.mActivity;

/**
 * Created by jack_xie on 2017/8/13.
 * 避温
 *
 * @des ${TODO}
 */
public class AvoidTemDialog extends AbsDialog {
    @Bind(R.id.edit_temp_down)
    EditText edit_temp_down;
    @Bind(R.id.edit_temp_up)
    EditText edit_temp_up;
    @Bind(R.id.btn_pk_temp)
    Button btn_pk_temp;
    private MavlinkDjiBean mavlinkDjiBean = null;

    public AvoidTemDialog(Context context, Object obj) {
        super(context, obj);
        mavlinkDjiBean = (MavlinkDjiBean) obj;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_temp;
    }

    public void setMessage(final int tmpMax, final int tmpMin) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edit_temp_up.setText(tmpMax + "");
                edit_temp_down.setText(tmpMin + "");
            }
        });
    }

    @Override
    protected void init() {
        btn_pk_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempUp = edit_temp_up.getText().toString().trim();
                String tempDown = edit_temp_down.getText().toString().trim();
                if (!tempUp.isEmpty() && !tempDown.isEmpty()) {


                    //tempUp大于60   和大于下限温度
                    if (Integer.parseInt(tempUp) < 60) {
                        Toast.makeText(mActivity, "上限温度必须大于60", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Integer.parseInt(tempUp) < Integer.parseInt(tempDown)) {
                        Toast.makeText(mActivity, "上限温度必须大于下限温度", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Integer.parseInt(tempUp) > 1000) {
                        tempUp = "1000";
                        edit_temp_up.setText("120");
                        edit_temp_up.setSelection(tempUp.length());
                    }
                    if (Integer.parseInt(tempUp) <= 60) {
                        tempUp = "60";
                        edit_temp_up.setText("30");
                        edit_temp_up.setSelection(tempUp.length());
                    }
                    if (Integer.parseInt(tempDown) > 59) {
                        tempDown = "59";
                        edit_temp_down.setText("29");
                        edit_temp_down.setSelection(tempDown.length());
                    }
                    if (Integer.parseInt(tempDown) < -45) {
                        tempDown = "-45";
                        edit_temp_down.setText("-45");
                        edit_temp_down.setSelection(tempDown.length());
                    }

                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean, "temperature", false, "", false, tempDown, tempUp, false, "", "", 0, 0, 0, 0, new HandleMavlinkData.setDroneCallBack() {
                        @Override
                        public void backBoolean(DJIError djiError) {
                            if (djiError == null) {
                                dismiss();
                            }
                        }
                    }, 0, 0, 0, 1, 1);
                } else {
                    T.show(mActivity, "不可为空");
                }
            }
        });
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

}
