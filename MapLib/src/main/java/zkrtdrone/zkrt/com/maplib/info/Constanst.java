package zkrtdrone.zkrt.com.maplib.info;


/**
 * Created by jack_xie on 2017/6/26.
 *
 * @des ${TODO}
 */

public class Constanst {
    public static final String IP = "http://192.168.1.132:8080/";

    //注册获得信息值
    public static final String IP_Address= "101.132.107.199";//192.168.1.132
    public static final String port= "8080";
    public static final String RegisterIP = "http://"+IP_Address+":"+port+"/";
    public static final String getLevelHttp="v1/Level_Z/GetLevelDownName/";
    public static final String registerHttp="v1/BrigadeDrone/AddDroneLevel/";
    public static final String saveDataNet="v1/DroneMsg_Z/AddDroneMsg/";


    //飞机设置fragment中的值
    public static int fMaxFlightHeight = 0;
    public static int iMaxFlightRadius = 0;
    public static boolean boolSpeek = true;
    public static float goHome = 0.0f;
    public static String cameraType="";
    public static boolean redVisible; //可见光与红外   true  红外
    public static boolean visibleVideo; //可见光是否在录像模式  //true 录像
    public static boolean video; //可见光是否属于录像   //true 录像
    public static boolean redVideo; //红外是否属于录像  //true 录像
    public static boolean boolCamera;  //是否录像模式
    public static boolean boolVideo;  //是否正在录像
    public static boolean hd_lb_av;    //true lb   false av
    public static boolean booDialog;
    public static boolean mMultiEditEnabled;
}
