package com.jack.jackzkrt.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.jack.frame.core.AbsDialog;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.bean.MavlinkDjiBean;
import com.jack.jackzkrt.fragment.HandleMavlinkData;

import butterknife.Bind;
import dji.common.error.DJIError;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static zkrt.ui.base.BaseApplication.mActivity;

/**
 * Created by jack_xie on 2017/8/14.
 *
 * @des ${TODO}*/
public class ObstacleAvoidanceDialog extends AbsDialog {
    @Bind(R.id.btn_pk_obtacle)
    Button btn_pk_obtacle;
    @Bind(R.id.edit_obstacl_istance)
    EditText edit_obstacl_istance;  //避障生效距离(cm)
    @Bind(R.id.edit_obstacl_speed)
    EditText edit_obstacl_speed;  //避障速度m/s
    @Bind(R.id.linear_cm_dialog)
    LinearLayout linear_cm_dialog;
    @Bind(R.id.linear_ms_dialog)
    LinearLayout linear_ms_dialog;
    @Bind(R.id.switch_obstacle_dialog)
    Switch switch_obstacle_dialog;
    private MavlinkDjiBean mavlinkDjiBean = null;

    public ObstacleAvoidanceDialog(Context context, Object obj) {
        super(context, obj);
        mavlinkDjiBean = (MavlinkDjiBean) obj;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_obstacle;
    }

    public void setMessage(final int mGetStatus, final float speed, final int mGetBarrierDistance){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch_obstacle_dialog.setChecked(mGetStatus==0?false:true);
                linear_cm_dialog.setVisibility(switch_obstacle_dialog.isChecked()?VISIBLE:GONE);
                linear_ms_dialog.setVisibility(switch_obstacle_dialog.isChecked()?VISIBLE:GONE);
                edit_obstacl_speed.setText(speed+"");
                edit_obstacl_istance.setText(mGetBarrierDistance+"");
            }
        });
    }

    @Override
    protected void init() {
        switch_obstacle_dialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed())
                    return;
                linear_cm_dialog.setVisibility(isChecked ? VISIBLE : GONE);
                linear_ms_dialog.setVisibility(isChecked ? VISIBLE : GONE);
            }
        });

        btn_pk_obtacle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CmObtacle = edit_obstacl_istance.getText().toString().trim();
                String Mobtacle = edit_obstacl_speed.getText().toString().trim();
                if(switch_obstacle_dialog.isChecked()){
                    if(CmObtacle.isEmpty()) {
                        T.show(mActivity,"输入参数不合法,不可为空");
                        return;
                    }

                    if(CmObtacle.substring(0,1).equals("0")){
                        T.show(mActivity,"输入参数不合法");
                        return;
                    }

                    if(Integer.parseInt(CmObtacle)<400) {
                        T.show(mActivity,"输入参数不合法,最小距离为400");
                        return;
                    }

                    if(Integer.parseInt(CmObtacle)>1500
                            ) {
                        T.show(mActivity,"输入参数不合法,最大距离为1500");
                        return;
                    }

                    if(Mobtacle.isEmpty()) {
                        T.show(mActivity,"输入参数不合法,不可为空");
                        return;
                    }

                    if(Integer.valueOf(Mobtacle)>2){
                        T.show(mActivity,"避障速度不大于2米每秒");
                        return;
                    }


                }
                if(!CmObtacle.isEmpty() && !Mobtacle.isEmpty()){
                    HandleMavlinkData.setMavlinkData(mavlinkDjiBean,"barrier",false,"",false,"","",switch_obstacle_dialog.isChecked(),
                            CmObtacle,Mobtacle,0,0,0,0, new HandleMavlinkData.setDroneCallBack() {
                                @Override
                                public void backBoolean(DJIError djiError) {
                                    if(djiError == null){
                                        dismiss();
                                    }
                                }
                            },0,0,0,1,1);
                }else{
                    T.show(mActivity,"不可为空");
                }
            }
        });
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
