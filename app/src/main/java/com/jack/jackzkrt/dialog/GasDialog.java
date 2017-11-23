package com.jack.jackzkrt.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jack.frame.core.AbsDialog;
import com.jack.frame.util.SharePreUtil;
import com.jack.jackzkrt.R;

import butterknife.Bind;
import static zkrt.ui.base.BaseApplication.mActivity;
/**
 * Created by jack_xie on 2017/8/13.
 * 毒气
 * @des ${TODO}*/
public class GasDialog extends AbsDialog {
    @Bind(R.id.edit_pois_co)
    EditText edit_pois_co;
    @Bind(R.id.edit_pois_h2s)
    EditText edit_pois_h2s;
    @Bind(R.id.edit_pois_nh3)
    EditText edit_pois_nh3;
    @Bind(R.id.edit_pois_co2)
    EditText edit_pois_co2;
    @Bind(R.id.edit_pois_cl2)
    EditText edit_pois_cl2;
    @Bind(R.id.edit_pois_SO2)
    EditText edit_pois_SO2;
    @Bind(R.id.edit_pois_voc)
    EditText edit_pois_voc;
    @Bind(R.id.edit_pois_ch4)
    EditText edit_pois_ch4;
    @Bind(R.id.btn_pk_gas)
    Button btn_pk_gas;

    public GasDialog(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.moudle_poisonous;
    }

    @Override
    protected void init() {
        edit_pois_co.setText(SharePreUtil.getString("Gams", mActivity, "StrCO"));
        edit_pois_h2s.setText(SharePreUtil.getString("Gams", mActivity, "StrH2S"));
        edit_pois_nh3.setText(SharePreUtil.getString("Gams", mActivity, "StrNH3"));
        edit_pois_co2.setText(SharePreUtil.getString("Gams", mActivity, "StrCO2"));
        edit_pois_cl2.setText(SharePreUtil.getString("Gams", mActivity, "StrCL2"));
        edit_pois_SO2.setText(SharePreUtil.getString("Gams", mActivity, "StrSO2"));
        edit_pois_voc.setText(SharePreUtil.getString("Gams", mActivity, "StrVOC"));
        edit_pois_ch4.setText(SharePreUtil.getString("Gams", mActivity, "StrCH4"));


        btn_pk_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StrCO = edit_pois_co.getText().toString().trim();
                String StStrH2S = edit_pois_h2s.getText().toString().trim();
                String StrNH3 = edit_pois_nh3.getText().toString().trim();
                String StrCO2 = edit_pois_co2.getText().toString().trim();
                String StrCL2 = edit_pois_cl2.getText().toString().trim();
                String StrSO2 = edit_pois_SO2.getText().toString().trim();
                String StrVOC = edit_pois_voc.getText().toString().trim();
                String StrCH4 = edit_pois_ch4.getText().toString().trim();

                if(StrCO.isEmpty()) {StrCO = "0";edit_pois_co.setText("0");}
                if(StStrH2S.isEmpty()) {StStrH2S = "0";edit_pois_h2s.setText("0");}
                if(StrNH3.isEmpty()) {StrNH3 = "0";edit_pois_nh3.setText("0");}
                if(StrCO2.isEmpty()) {StrCO2 = "0";edit_pois_co2.setText("0");}
                if(StrCL2.isEmpty()) {StrCL2 = "0";edit_pois_cl2.setText("0");}
                if(StrSO2.isEmpty()) {StrSO2 = "0";edit_pois_SO2.setText("0");}
                if(StrVOC.isEmpty()) {StrVOC = "0";edit_pois_voc.setText("0");}
                if(StrCH4.isEmpty()) {StrCH4 = "0";edit_pois_ch4.setText("0");}

                SharePreUtil.putString("Gams", mActivity, "StrCO", StrCO);
                SharePreUtil.putString("Gams", mActivity, "StrH2S", StStrH2S);
                SharePreUtil.putString("Gams", mActivity, "StrNH3", StrNH3);
                SharePreUtil.putString("Gams", mActivity, "StrCO2", StrCO2);
                SharePreUtil.putString("Gams", mActivity, "StrCL2", StrCL2);
                SharePreUtil.putString("Gams", mActivity, "StrSO2", StrSO2);
                SharePreUtil.putString("Gams", mActivity, "StrVOC", StrVOC);
                SharePreUtil.putString("Gams", mActivity, "StrCH4", StrCH4);

                dismiss();
            }
        });
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
