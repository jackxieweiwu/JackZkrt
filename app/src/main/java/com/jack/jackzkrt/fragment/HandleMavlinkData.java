package com.jack.jackzkrt.fragment;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.jack.frame.util.byteUtil.Utils;
import com.jack.frame.util.mavlink.MAVLinkCRC;
import com.jack.jackzkrt.JackApplication;
import com.jack.jackzkrt.bean.MavlinkDjiBean;

import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import zkrt.ui.ModuleVerificationUtil;

/**
 * Created by jack_xie on 2017/7/7.
 *
 * @des ${TODO}
 */

public class HandleMavlinkData {
    private static int num = -1;
    private static int byteIndex = -1;
    private static byte[] bytes2 = new byte[50];
    private static byte[] bytes = new byte[47];

    static int ii=0;


    private static Context mContext;
    public static TextView tishi;
    public void setContext(Context context, TextView tishi){
        this.mContext= context;
        this.tishi=tishi;
    }
    /**
     *
     * @param mavlinkDjiBean  协议bean
     * @param name   模块标识
     * @param jettisoninBool 是否抛投
     * @param jettison_name 抛投名称
     * @param irradiation_Bool 是否照射
     * @param lowTemper 最低温度
     * @param hightTemper 最高温度
     * @param isBarrier  避障使能状态控制
     * @param barrier_distance   避障生效距离
     * @param barrier_speed  避障速度
     * @param hg   当name为camera时 hg为横滚1500为初始值
     * @param fy   当name为camera时，fy为俯仰 1500为初始值
     * @param take_picture 相机拍照
     * @param recording 相机摄像
     * @param setDroneCallBack  回调函数
     * @param iso  镜像翻转切换
     *@param calibration  陀螺仪校准
     * @param fog  是否透雾
     * @param  electronic  电子稳像
     * @param  electronic  初始化标识
     */
    public static void setMavlinkData(MavlinkDjiBean mavlinkDjiBean, String name, boolean jettisoninBool,
                                      String jettison_name, boolean irradiation_Bool, String lowTemper, String hightTemper,
                                      final boolean isBarrier, final String barrier_distance, final String barrier_speed,
                                      int hg, int fy, int take_picture, int recording, final setDroneCallBack setDroneCallBack, int iso, int calibration, int fog, int electronic, int ini_status) {
        if(mavlinkDjiBean == null) return;
        bytes[0] = mavlinkDjiBean.getStartCode();
        bytes[1] = mavlinkDjiBean.getVer();
        bytes[2] = mavlinkDjiBean.getSessionAck();
        bytes[3] = mavlinkDjiBean.getPaddingEnc();
        bytes[4] = 00;
        bytes[5] = mavlinkDjiBean.getBylength();
        bytes[6] = mavlinkDjiBean.getSeq();

        //APPID 三位
        bytes[7] = mavlinkDjiBean.getAppIDOne();
        bytes[8] = mavlinkDjiBean.getAppIDTwo();
        bytes[9] = mavlinkDjiBean.getAppIDThree();

        //UAVID 6位
        bytes[10] = mavlinkDjiBean.getUavIDOne();
        bytes[11] = mavlinkDjiBean.getUavIDTwo();
        bytes[12] = mavlinkDjiBean.getUavIDThree();
        if (name.equals("jettisonin")) {
            bytes[13] = Utils.hexStringToBytes("06")[0];
        } else if (name.equals("temperature")) {
            bytes[13] = Utils.hexStringToBytes("01")[0];
        } else if (name.equals("barrier")) {
            bytes[13] = Utils.hexStringToBytes("64")[0];
        } else if (name.equals("camera")) {
            bytes[13] = Utils.hexStringToBytes("04")[0];
        } else if (name.equals("gas")) {
            bytes[13] = Utils.hexStringToBytes("05")[0];
        } else if (name.equals("parachute")) {
            bytes[13] = Utils.hexStringToBytes("08")[0];
        } else if (name.equals("irradiation")) {
            bytes[13] = Utils.hexStringToBytes("09")[0];
        } else if (name.equals("propaganda")) {
            bytes[13] = Utils.hexStringToBytes("0A")[0];
        } else if (name.equals("battery")) {
            bytes[13] = Utils.hexStringToBytes("0B")[0];
        } else if (name.equals("three_dimensional")) {
            bytes[13] = Utils.hexStringToBytes("0C")[0];
        } else if (name.equals("many_light")) {
            bytes[13] = Utils.hexStringToBytes("0D")[0];//"0D".getBytes()[0];
        }
        bytes[14] = mavlinkDjiBean.getUavIDFIve();
        bytes[15] = mavlinkDjiBean.getUavIDSix();
        bytes[16] = mavlinkDjiBean.getCommand();

        //以上为头文件数据

        //抛投
        if (name.equals("jettisonin")) {
            byteIndex = 3;
            num = 30 - 3;
            if (jettison_name.equals("one")) {
                bytes[17] = (byte) (jettisoninBool ? 01 : 00);
                bytes[18] = Utils.hexStringToBytes("EE")[0];
                bytes[19] = Utils.hexStringToBytes("EE")[0];
            }
            if (jettison_name.equals("two")) {
                bytes[17] = Utils.hexStringToBytes("EE")[0];
                bytes[18] = (byte) (jettisoninBool ? 01 : 00);
                bytes[19] = Utils.hexStringToBytes("EE")[0];
            }
            if (jettison_name.equals("three")) {
                bytes[17] = Utils.hexStringToBytes("EE")[0];
                bytes[18] = Utils.hexStringToBytes("EE")[0];
                bytes[19] = (byte) (jettisoninBool ? 01 : 00);
            }
        } else if (name.equals("temperature")) { //温度
            byteIndex = 4;
            num = 30 - byteIndex;
            if (lowTemper == null || "".equals(lowTemper))
                lowTemper = "10";
            if (hightTemper == null || "".equals(hightTemper))
                hightTemper = "100";
            if (Float.parseFloat(hightTemper) < 60)
                hightTemper = "60";
            if (Float.parseFloat(hightTemper) > 1000)
                hightTemper = "1000";
            if (Float.parseFloat(lowTemper) > 60)
                lowTemper = "59";
            if (Float.parseFloat(lowTemper) < -45)
                lowTemper = "-45";
            byte[] lowByte = Utils.intengerTo16Hex((int) (Float.parseFloat(lowTemper) * 10));
            byte[] highByte = Utils.intengerTo16Hex((int) (Float.parseFloat(hightTemper) * 10));
            bytes[17] = Utils.hexStringToBytes(Utils.bytesToHexString(lowByte).substring(0, 2))[0];
            bytes[18] = Utils.hexStringToBytes(Utils.bytesToHexString(lowByte).substring(3, 5))[0];
            bytes[19] = Utils.hexStringToBytes(Utils.bytesToHexString(highByte).substring(0, 2))[0];
            bytes[20] = Utils.hexStringToBytes(Utils.bytesToHexString(highByte).substring(3, 5))[0];
        } else if (name.equals("irradiation")) { //照射组件
            byteIndex = 1;
            num = 30 - byteIndex;
            bytes[17] = (byte) (irradiation_Bool ? 01 : 00);
        } else if (name.equals("barrier")) {//避障
            byteIndex = 6;
            num = 30 - byteIndex;
           /* if (barrier_distance == null || "".equals(barrier_distance))
                barrier_distance = "0";
            if (barrier_speed == null || "".equals(barrier_speed))
                barrier_speed = "0";*/
            bytes[17] = 01;
            if(isBarrier){
                bytes[18] = 01;
            } else {bytes[18] = 00;}

            byte[] barrierSpeedByte = Utils.intengerTo16Hex((int) (Float.parseFloat(barrier_speed) * 10));
            byte[] barrierDistanceByte = Utils.intengerTo16Hex((int) (Float.parseFloat(barrier_distance)));
            bytes[19] = Utils.hexStringToBytes(Utils.bytesToHexString(barrierDistanceByte).substring(0, 2))[0];
            bytes[20] = Utils.hexStringToBytes(Utils.bytesToHexString(barrierDistanceByte).substring(3, 5))[0];
            bytes[21] = Utils.hexStringToBytes(Utils.bytesToHexString(barrierSpeedByte).substring(0, 2))[0];
            bytes[22] = Utils.hexStringToBytes(Utils.bytesToHexString(barrierSpeedByte).substring(3, 5))[0];
        }else if(name.equals("camera")){  // 相机 单光
            byteIndex = 14;
            num = 30 - byteIndex;
            byte[] hg_byte = Utils.intengerTo16Hex(hg);
            byte[] fy_byte = Utils.intengerTo16Hex(fy);
            byte[] take = Utils.intengerTo16Hex(take_picture);
            byte[] rec = Utils.intengerTo16Hex(recording);
            bytes[17] = Utils.hexStringToBytes(Utils.bytesToHexString(hg_byte).substring(0, 2))[0];
            bytes[18] = Utils.hexStringToBytes(Utils.bytesToHexString(hg_byte).substring(3, 5))[0];

            bytes[19] = Utils.hexStringToBytes(Utils.bytesToHexString(fy_byte).substring(0, 2))[0];
            bytes[20] = Utils.hexStringToBytes(Utils.bytesToHexString(fy_byte).substring(3, 5))[0];

            bytes[21] = 00;
            bytes[22] = 00;
            bytes[23] = 00;
            bytes[24] = 00;
            bytes[25] = Utils.hexStringToBytes(Utils.bytesToHexString(take).substring(0, 2))[0];
            bytes[26] = Utils.hexStringToBytes(Utils.bytesToHexString(take).substring(3, 5))[0];

            bytes[27] = Utils.hexStringToBytes(Utils.bytesToHexString(rec).substring(0, 2))[0];
            bytes[28] = Utils.hexStringToBytes(Utils.bytesToHexString(rec).substring(3, 5))[0];

            //缩距  barrier_distance(max)  barrier_speed(min)
            byte[] max = Utils.intengerTo16Hex(Integer.parseInt(barrier_distance));
            byte[] min = Utils.intengerTo16Hex(Integer.parseInt(barrier_speed));
            bytes[29] = Utils.hexStringToBytes(Utils.bytesToHexString(max).substring(0, 2))[0];
            bytes[30] = Utils.hexStringToBytes(Utils.bytesToHexString(min).substring(0, 2))[0];
        }else if(name.equals("three_dimensional")){  //三维
            byteIndex = 2;
            num = 30 - byteIndex;
            bytes[17] = (byte) (hg);  //02开启电源  01拍照控制  03镜头控制
            bytes[18] = (byte) (irradiation_Bool ? 01 : 00);
        }else if(name.equals("many_light")){  //双光
            byteIndex = 30;
            num = 30 - byteIndex;
            byte[] hg_byte = Utils.intengerTo16Hex(hg);
            byte[] fy_byte = Utils.intengerTo16Hex(fy);

            tishi.setText(fy+"   ***   "+hg);

            bytes[17] = Utils.hexStringToBytes(Utils.bytesToHexString(hg_byte).substring(0, 2))[0];  //航向 ①②
            bytes[18] = Utils.hexStringToBytes(Utils.bytesToHexString(hg_byte).substring(3, 5))[0];

            bytes[19] = Utils.hexStringToBytes(Utils.bytesToHexString(fy_byte).substring(0, 2))[0]; //俯仰 ①②
            bytes[20] = Utils.hexStringToBytes(Utils.bytesToHexString(fy_byte).substring(3, 5))[0];
            bytes[21] = 00;
            bytes[22] = 00; //备用
            bytes[23] = 00;
            bytes[24] = 00;  //备用

            //可见光 jettisoninBool  true
            byte[] k_take = Utils.intengerTo16Hex(take_picture);
            byte[] k_rec = Utils.intengerTo16Hex(recording);
            if(jettisoninBool){//当抛投时候 开启拍照和摄像
                bytes[25] = Utils.hexStringToBytes(Utils.bytesToHexString(k_take).substring(0, 2))[0];  //可见光相机拍照 ①
                bytes[26] = Utils.hexStringToBytes(Utils.bytesToHexString(k_take).substring(3, 5))[0];
                bytes[27] = Utils.hexStringToBytes(Utils.bytesToHexString(k_rec).substring(0, 2))[0];  //可见光相机摄像 ①
                bytes[28] = Utils.hexStringToBytes(Utils.bytesToHexString(k_rec).substring(3, 5))[0];
            }else{
                bytes[25] = 00;
                bytes[26] = 00;
                bytes[27] = 00;
                bytes[28] = 00;
            }

            //可将光变焦  jettison_name  可将光变焦 ①②
            byte[] zoom = Utils.intengerTo16Hex(Integer.parseInt(jettison_name));
            if(jettisoninBool){//当抛投时候 可将光变焦

                bytes[29] = Utils.hexStringToBytes(Utils.bytesToHexString(zoom).substring(0, 2))[0];
                bytes[30] = Utils.hexStringToBytes(Utils.bytesToHexString(zoom).substring(3, 5))[0];
            }else{
                bytes[29] = 00;
                bytes[30] = 00;
            }
            bytes[31] = 00;
            bytes[32] = 00; // 备用
            bytes[33] = 00; // 视频切换（PWM1）

            //红外颜色切换  lowTemper
            byte[] Infrared_color = Utils.intengerTo16Hex(Integer.parseInt(lowTemper));
            bytes[34] = Utils.hexStringToBytes(Utils.bytesToHexString(Infrared_color).substring(0, 2))[0];

            //云台模式切换  hightTemper
            byte[] yuntaiMode = Utils.intengerTo16Hex(Integer.parseInt(hightTemper));
            bytes[35] = Utils.hexStringToBytes(Utils.bytesToHexString(yuntaiMode).substring(0, 2))[0];

            //可见光日夜切换  barrier_speed
            byte[] day_night = Utils.intengerTo16Hex(Integer.parseInt(barrier_speed));
            bytes[36] = Utils.hexStringToBytes(Utils.bytesToHexString(day_night).substring(0, 2))[0];


            //镜像翻转切换  iso
            byte[] mirror_flip = Utils.intengerTo16Hex(iso);
            bytes[37] = Utils.hexStringToBytes(Utils.bytesToHexString(mirror_flip).substring(0, 2))[0];

            //红外录像  jettisoninBool false
            if(!jettisoninBool){
                bytes[38] = Utils.hexStringToBytes(Utils.bytesToHexString(k_rec).substring(0, 2))[0];
            }else{
                bytes[38] = 00;
            }

            //红外变焦
            if(!jettisoninBool){

                bytes[39] = Utils.hexStringToBytes(Utils.bytesToHexString(zoom).substring(0, 2))[0];
                bytes[40] = Utils.hexStringToBytes(Utils.bytesToHexString(zoom).substring(3, 5))[0];
            }else{
                bytes[39] = 00;
                bytes[40] = 00;
            }


            //陀螺仪校准②
            byte[] gyroscope_calibration = Utils.intengerTo16Hex(calibration);
            bytes[41]=Utils.hexStringToBytes(Utils.bytesToHexString(gyroscope_calibration).substring(0, 2))[0];
            //透雾②
            byte[] through_fog = Utils.intengerTo16Hex(fog);
            bytes[42]=Utils.hexStringToBytes(Utils.bytesToHexString(through_fog).substring(0, 2))[0];



            //电子稳像②
            byte[] electronic_stabilization = Utils.intengerTo16Hex(electronic);
            bytes[43]=Utils.hexStringToBytes(Utils.bytesToHexString(electronic_stabilization).substring(0, 2))[0];


            //备用
            bytes[44] = 00;
            bytes[45] = 00;

            //INI_Stutas
            byte[] status = Utils.intengerTo16Hex(ini_status);
            bytes[46]=Utils.hexStringToBytes(Utils.bytesToHexString(status).substring(0, 2))[0];







        }
        for (int i = 0; i < num; i++) {
            bytes[17 + byteIndex + i] = 0;
        }

        int numberbyte = MAVLinkCRC.crc_calculate(bytes);
        int crc_b = (numberbyte & 0xff);
        int crc_c = ((numberbyte >> 8) & 0xff);

        for (int i = 0; i < bytes.length; i++) {
            bytes2[i] = bytes[i];
        }

        bytes2[47] = (byte) crc_b;
        bytes2[48] = (byte) crc_c;
        bytes2[49] = mavlinkDjiBean.getEndCode();



        if (!ModuleVerificationUtil.isFlightControllerAvailable())
            return;
        JackApplication.getAircraftInstance().getFlightController().sendDataToOnboardSDKDevice(bytes2, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(final DJIError djiError) {
                ii++;
                Log.i("rongjiajie",Utils.bytesToHexString(bytes2)+"   "+ii);
                if(djiError != null){

                }
                if(setDroneCallBack !=null){
                    setDroneCallBack.backBoolean(djiError);
                }

                /*if (djiError == null) {
                    T.show(JackApplication.mActivity, "transport success");
                }*/
            }
        });

    }

    public interface setDroneCallBack{
        void backBoolean(DJIError djiError);
    }
}
