package com.jack.jackzkrt.dialog;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackspinnerlibrary.JackSpinner;
import com.google.gson.Gson;
import com.jack.frame.core.AbsDialog;
import com.jack.frame.http.HttpUtil;
import com.jack.frame.util.AdapterHelper;
import com.jack.frame.util.SharePreUtil;
import com.jack.jackzkrt.R;
import com.jack.jackzkrt.bean.LevenBean;
import com.jack.jackzkrt.interf.RegisterCallBackIntercace;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import zkrtdrone.zkrt.com.maplib.info.Constanst;
import zkrtdrone.zkrt.com.maplib.info.mission.bean.spatil.RegionOfInterest;

import static com.jack.frame.core.AbsApplication.mActivity;


/**
 * Created by jack_xie on 2017/8/13.
 * 避温
 *
 * @des ${TODO}
 */
public class RegisterDialog extends AbsDialog implements JackSpinner.OnItemSelectedListener {
    @Bind(R.id.level_name_spinner_overall)
    JackSpinner level_name_spinner_overall;
    @Bind(R.id.level_name_spinner_detachment)
    JackSpinner level_name_spinner_detachment;
    @Bind(R.id.level_name_spinner_big)
    JackSpinner level_name_spinner_big;
    @Bind(R.id.level_name_spinner_centre)
    JackSpinner level_name_spinner_centre;
    @Bind(R.id.tv_code)
    TextView tv_code;
    @Bind(R.id.btn_pk_marker)
    Button btn_pk_marker;

    private String[] overallArr = null;
    private String[] detachmentArr = null;
    private String[] bigArr = null;
    private String[] centreArr = null;

    private String[] nullArr = null;

    private RegisterCallBackIntercace mRegisterCallBackIntercace;

    public RegisterDialog(Context context) {
        super(context);
        init();
    }

    public RegisterDialog(Context context, RegisterCallBackIntercace registerCallBackIntercace) {
        super(context);
        this.mRegisterCallBackIntercace = registerCallBackIntercace;
        init();
    }

    public void setmRegisterIntercace(RegisterCallBackIntercace registerCallBackIntercace) {
        this.mRegisterCallBackIntercace = registerCallBackIntercace;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.dialog_register;
    }

    @Override
    protected void init() {
        nullArr = new String[1];
        nullArr[0] = "请选择";


        String ANDROID_ID = Settings.System.getString(getContext().getContentResolver(), Settings.System.ANDROID_ID);
        tv_code.setText(ANDROID_ID);


        level_name_spinner_overall.setOnItemSelectedListener(this);
        level_name_spinner_detachment.setOnItemSelectedListener(this);
        level_name_spinner_big.setOnItemSelectedListener(this);

        level_name_spinner_big.setAdapter(AdapterHelper.getAdapter(mActivity, nullArr));
        level_name_spinner_centre.setAdapter(AdapterHelper.getAdapter(mActivity, nullArr));

        //发送1到服务端给总队赋值
        HttpUtil util = HttpUtil.getInstance(getContext());
        String url = Constanst.RegisterIP + Constanst.getLevelHttp + "1";
        util.post(url, null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                final List<LevenBean> orders = getLevenList(data);
                overallArr = new String[orders.size()];
                for (int i = 0; i < orders.size(); i++) {
                    overallArr[i] = orders.get(i).getLevelName();
                }
                if (level_name_spinner_overall != null)
                    level_name_spinner_overall.setAdapter(AdapterHelper.getAdapter(mActivity, overallArr));
                level_name_spinner_overall.setSelectedIndex(0);

                //发送第一个总队返回一个支队
                selectOverAll();
            }

            @Override
            public void onError(Object error) {
                super.onError(error);

            }
        });


        btn_pk_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detachmentArr[level_name_spinner_detachment.getSelectedIndex()].equals("请选择")) {
                    Toast.makeText(getContext(), "请选择支队", Toast.LENGTH_LONG).show();
                    return;
                }

                if (bigArr[level_name_spinner_big.getSelectedIndex()].equals("请选择")) {
                    Toast.makeText(getContext(), "请选择大队", Toast.LENGTH_LONG).show();
                    return;
                }

                if (centreArr[level_name_spinner_centre.getSelectedIndex()].equals("请选择")) {
                    Toast.makeText(getContext(), "请选择中队", Toast.LENGTH_LONG).show();
                    return;
                }

                SharePreUtil.putString("detachment", mActivity, "detachment", detachmentArr[level_name_spinner_detachment.getSelectedIndex()]);
                SharePreUtil.putString("overall", mActivity, "overall", overallArr[level_name_spinner_overall.getSelectedIndex()]);
                SharePreUtil.putString("bigArr", mActivity, "bigArr", bigArr[level_name_spinner_big.getSelectedIndex()]);
                SharePreUtil.putString("centreArr", mActivity, "centreArr", centreArr[level_name_spinner_centre.getSelectedIndex()]);


                HttpUtil util = HttpUtil.getInstance(getContext());
                String url;
                url = Constanst.RegisterIP + Constanst.registerHttp + centreArr[level_name_spinner_centre.getSelectedIndex()] + "/" + tv_code.getText().toString();
                util.post(url, null, new HttpUtil.AbsResponse() {
                    @Override
                    public void onResponse(String data) {
                        super.onResponse(data);
                        boolean isSuccess = false;
                        try {
                            if (Integer.valueOf(data) >= 0) {
                                SharePreUtil.putString("isRegister", getContext(), "isRegister", "true");
                                isSuccess = true;
                            }
                        } catch (Exception e) {
                            SharePreUtil.putString("isRegister", getContext(), "isRegister", "flase");
                        }
                        mRegisterCallBackIntercace.getRegisterResult(isSuccess);
                        dismiss();

                    }

                    @Override
                    public void onError(Object error) {
                        super.onError(error);

                    }
                });

            }
        });

    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    public void onItemSelected(JackSpinner view, int position, long id, Object item) {
        HttpUtil util = HttpUtil.getInstance(getContext());
        String url;
        nullArr = new String[1];
        nullArr[0] = "请选择";
        switch (view.getId()) {
            case R.id.level_name_spinner_overall:
                url = Constanst.RegisterIP + Constanst.getLevelHttp + overallArr[level_name_spinner_overall.getSelectedIndex()];
                util.post(url, null, new HttpUtil.AbsResponse() {
                    @Override
                    public void onResponse(String data) {
                        super.onResponse(data);


                        final List<LevenBean> orders = getLevenList(data);
                        orders.add(0, new LevenBean("请选择"));
                        detachmentArr = new String[orders.size()];
                        for (int i = 0; i < orders.size(); i++) {
                            detachmentArr[i] = orders.get(i).getLevelName();
                        }
                        if (level_name_spinner_detachment != null)
                            level_name_spinner_detachment.setAdapter(AdapterHelper.getAdapter(mActivity, detachmentArr));
                        level_name_spinner_detachment.setSelectedIndex(0);

                        selectOverAll();
                        //大队  中队的spinner去掉
                        level_name_spinner_big.setAdapter(AdapterHelper.getAdapter(mActivity, nullArr));
                        level_name_spinner_centre.setAdapter(AdapterHelper.getAdapter(mActivity, nullArr));
                    }

                    @Override
                    public void onError(Object error) {
                        super.onError(error);

                    }
                });
                break;


            case R.id.level_name_spinner_detachment:   //

                url = Constanst.RegisterIP + Constanst.getLevelHttp + detachmentArr[level_name_spinner_detachment.getSelectedIndex()];
                util.post(url, null, new HttpUtil.AbsResponse() {
                    @Override
                    public void onResponse(String data) {
                        super.onResponse(data);


                        final List<LevenBean> orders = getLevenList(data);
                        orders.add(0, new LevenBean("请选择"));
                        bigArr = new String[orders.size()];
                        for (int i = 0; i < orders.size(); i++) {
                            bigArr[i] = orders.get(i).getLevelName();
                        }
                        if (level_name_spinner_big != null)
                            level_name_spinner_big.setAdapter(AdapterHelper.getAdapter(mActivity, bigArr));
                        level_name_spinner_big.setSelectedIndex(0);


                        level_name_spinner_centre.setAdapter(AdapterHelper.getAdapter(mActivity, nullArr));
                    }

                    @Override
                    public void onError(Object error) {
                        super.onError(error);

                    }
                });
                break;

            case R.id.level_name_spinner_big:

                url = Constanst.RegisterIP + Constanst.getLevelHttp + bigArr[level_name_spinner_big.getSelectedIndex()];
                util.post(url, null, new HttpUtil.AbsResponse() {
                    @Override
                    public void onResponse(String data) {
                        super.onResponse(data);


                        final List<LevenBean> orders = getLevenList(data);
                        orders.add(0, new LevenBean("请选择"));
                        centreArr = new String[orders.size()];
                        for (int i = 0; i < orders.size(); i++) {
                            centreArr[i] = orders.get(i).getLevelName();
                        }
                        if (level_name_spinner_centre != null)
                            level_name_spinner_centre.setAdapter(AdapterHelper.getAdapter(mActivity, centreArr));
                        level_name_spinner_centre.setSelectedIndex(0);
                    }

                    @Override
                    public void onError(Object error) {
                        super.onError(error);

                    }
                });
                break;
        }
    }


    /**
     * 解析json
     *
     * @param message
     * @return
     */
    private List<LevenBean> getLevenList(String message) {

        List<LevenBean> datas = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(message);
            for (int i = 0; i < array.length(); i++) {
                LevenBean bean = new Gson().fromJson(array.get(i).toString(), LevenBean.class);
                datas.add(bean);
            }
            return datas;
        } catch (JSONException e) {
            return null;
        }
    }


    //选择总队后的方法
    public void selectOverAll() {
        HttpUtil util = HttpUtil.getInstance(getContext());
        String url = Constanst.RegisterIP + Constanst.getLevelHttp + overallArr[0];
        util.post(url, null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);


                final List<LevenBean> orders = getLevenList(data);
                orders.add(0, new LevenBean("请选择"));
                detachmentArr = new String[orders.size()];
                for (int i = 0; i < orders.size(); i++) {
                    detachmentArr[i] = orders.get(i).getLevelName();
                }
                if (level_name_spinner_detachment != null)
                    level_name_spinner_detachment.setAdapter(AdapterHelper.getAdapter(mActivity, detachmentArr));
                level_name_spinner_detachment.setSelectedIndex(0);
            }

            @Override
            public void onError(Object error) {
                super.onError(error);

            }
        });
    }
}
