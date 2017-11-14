package com.jack.jackzkrt.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jack.frame.core.AbsDialog;
import com.jack.frame.util.show.T;
import com.jack.jackzkrt.R;

import butterknife.Bind;
import zkrtdrone.zkrt.com.interf.CallBackExitText;

/**
 * Created by jack_xie on 17-8-19.
 */

public class SettingEditTextDialog extends AbsDialog {
    @Bind(R.id.setting_edit_name)
    TextView setting_edit_name;   //title
    @Bind(R.id.txt_xianzhi)
    TextView txt_xianzhi;   //条件
    @Bind(R.id.setting_edit_edittxt)
    EditText setting_edit_edittxt;
    @Bind(R.id.btn_edit_setting_btn)
    Button btn_edit_setting_btn;
    private CallBackExitText mCallBackExitText;
    private String mTitle,mMessage;
    private int mStart,mStop,nums;
    private Context mContext;

    public SettingEditTextDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setTextName(String title, int start, int stop, String message, CallBackExitText callBackExitText, int number){
        this.mTitle = title;
        this.mStart = start;
        this.mStop = stop;
        this.mMessage = message;
        this.nums = number;
        this.mCallBackExitText = callBackExitText;

        setting_edit_name.setText(mTitle);
        txt_xianzhi.setText(mStart+"-"+mStop+mMessage);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.setting_edittext_dialog;
    }

    @Override
    protected void init() {
        btn_edit_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =  setting_edit_edittxt.getText().toString().trim();
                if(name.length()<=0) {
                    T.show(mContext,"输入的值不符合条件",1);
                    return;
                }

                if(name.length()>=2 && (name.substring(0,1)=="0" || name.substring(0,1) == "." || name.substring(1,2).equals("."))) {
                    T.show(mContext,"输入的值不符合条件",1);
                    return;
                }

                if(name.length()>=3 && name.substring(2,3).equals(".")) {
                    T.show(mContext,"输入的值不符合条件",1);
                    return;
                }

                if(name.length()>=4 && name.substring(3,4).equals(".")) {
                    T.show(mContext,"输入的值不符合条件",1);
                    return;
                }
                if(name.length()>nums){
                    T.show(mContext,"输入的参数过量了",1);
                    return;
                }

                float number = Float.parseFloat(name);
                if(number>=mStart && number<=mStop) {
                    mCallBackExitText.exitName(number);
                    dismiss();
                }else{
                    T.show(mContext,"输入的值不符合条件",1);
                }
            }
        });
    }


    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
