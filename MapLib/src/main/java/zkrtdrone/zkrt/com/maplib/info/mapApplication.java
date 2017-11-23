package zkrtdrone.zkrt.com.maplib.info;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import zkrt.ui.base.BaseApplication;
import zkrtdrone.zkrt.com.maplib.info.mission.MissionProxy;

/**
 * Created by jack_xie on 17-8-28.
 */

public class mapApplication extends BaseApplication {
    private static MissionProxy missionProxy;
    private static OnEditorInteraction editorListener;
    public static boolean bool = false;

    @Override
    protected void initView() {
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        missionProxy = new MissionProxy(this);
        editorListener = (OnEditorInteraction)mActivity;
    }

    public static MissionProxy getMissionProxy(){
        return missionProxy;
    }
    public static OnEditorInteraction getEditorListener(){
        return editorListener;
    }

    public static void setEditorListener(OnEditorInteraction editorListeners){
        editorListener = editorListeners;
    }


}
