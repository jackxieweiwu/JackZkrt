package com.jack.frame.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.jack.frame.util.AndroidVersionUtil;
import com.jack.frame.util.show.L;
import com.jack.frame.util.show.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2016/4/11.
 * 6.0权限帮助工具
 */
@TargetApi(Build.VERSION_CODES.M)
public class PermissionUtil {
  public static final Object LOCK = new Object();
  public volatile static PermissionUtil INSTANCE = null;
  private static final String TAG = "PermissionUtil";
  public static final int REQUEST_PERMISSION_CODE = 12345;
  public static List<String> missingPermission = new ArrayList<>();

  public static PermissionUtil getInstance() {
    if (INSTANCE == null) {
      synchronized (LOCK) {
        INSTANCE = new PermissionUtil();
      }
    }
    return INSTANCE;
  }

  private PermissionUtil() {
  }

  public  static final int getWrite(Context context,int number){
    int num = 0;
    if(number == 1) num = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if(number == 2) num = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
    if(number == 3) num = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
    return num;
  }

  private static final String[] REQUIRED_PERMISSION_LIST = new String[] {
          android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
          android.Manifest.permission.VIBRATE,
          android.Manifest.permission.INTERNET,
          android.Manifest.permission.ACCESS_WIFI_STATE,
          android.Manifest.permission.WAKE_LOCK,
          android.Manifest.permission.ACCESS_COARSE_LOCATION,
          android.Manifest.permission.ACCESS_NETWORK_STATE,
          android.Manifest.permission.ACCESS_FINE_LOCATION,
          android.Manifest.permission.CHANGE_WIFI_STATE,
          android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
          android.Manifest.permission.READ_EXTERNAL_STORAGE,
          android.Manifest.permission.SYSTEM_ALERT_WINDOW,
          android.Manifest.permission.READ_PHONE_STATE,
          android.Manifest.permission.WRITE_SETTINGS,
  };

  public static void checkAndRequestPermissions(Activity context) {
    // Check for permissions
    // When the compile and target version is higher than 22, please request the following permissions at runtime to ensure the SDK work well.
    if ((getWrite(context,1) != 0 || getWrite(context,2) != 0 || getWrite(context,3) != 0)
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      ActivityCompat.requestPermissions(context, REQUIRED_PERMISSION_LIST, 1);
    }
    /*for (String eachPermission : REQUIRED_PERMISSION_LIST) {
      if (ContextCompat.checkSelfPermission(context, eachPermission) != PackageManager.PERMISSION_GRANTED) {
        missingPermission.add(eachPermission);
      }
    }*/
  }

  /**
   * 申请权限
   */
  public void requestPermission(Object obj, int requestCode, String... permission) {
    if (!AndroidVersionUtil.hasM()) {
      return;
    }
    requestPermission(obj, "", requestCode, permission);
  }

  /**
   * 申请权限
   *
   * @param hint 如果框对话框包含”不再询问“选择框的时候的提示用语。
   */
  public void requestPermission(Object obj, String hint, int requestCode, String... permission) {
    if (!AndroidVersionUtil.hasM() || permission == null || permission.length == 0) {
      return;
    }
    Activity activity = null;
    Fragment fragment = null;
    if (obj instanceof Activity) {
      activity = (Activity) obj;
    } else if (obj instanceof Fragment) {
      fragment = (Fragment) obj;
      activity = fragment.getActivity();
    } else {
      L.e(TAG, "obj 只能是 Activity 或者 fragment 及其子类");
      return;
    }
    if (!TextUtils.isEmpty(hint)) {
      for (String str : permission) {
        if (fragment != null) {
          if (fragment.shouldShowRequestPermissionRationale(str)) {
            T.showShort(fragment.getContext(), hint);
            break;
          }
        } else {
          if (activity.shouldShowRequestPermissionRationale(str)) {
            T.showShort(activity, hint);
            break;
          }
        }
      }
    }
    if (fragment != null) {
      fragment.requestPermissions(permission, requestCode);
    } else {
      activity.requestPermissions(permission, requestCode);
    }
  }

  protected String[] list2Array(List<String> denyPermission) {
    String[] array = new String[denyPermission.size()];
    for (int i = 0, count = denyPermission.size(); i < count; i++) {
      array[i] = denyPermission.get(i);
    }
    return array;
  }

  /**
   * 检查没有被授权的权限
   */
  public List<String> checkPermission(Object obj, String... permission) {
    if (!AndroidVersionUtil.hasM() || permission == null || permission.length == 0) {
      return null;
    }
    Activity activity = null;
    if (obj instanceof Activity) {
      activity = (Activity) obj;
    } else if (obj instanceof Fragment) {
      activity = ((Fragment) obj).getActivity();
    } else {
      L.e(TAG, "obj 只能是 Activity 或者 fragment 及其子类");
      return null;
    }
    List<String> denyPermissions = new ArrayList<>();
    for (String p : permission) {
      if (activity.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
        denyPermissions.add(p);
      }
    }
    return denyPermissions;
  }

  /**
   * 检查应用是否有该权限
   *
   * @param permission 权限，Manifest.permission.CAMERA
   * @return true ==> 已经授权
   */
  public boolean checkPermission(Activity activity, String permission) {
    return AndroidVersionUtil.hasM()
        && activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
  }

  /*
   * 请求悬浮权限
   * 在onActivityResult里面添加以下代码
   * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   *      super.onActivityResult(requestCode, resultCode, data);
   *      if (requestCode == OnPermissionCallback.PERMISSION_ALERT_WINDOW) {
   *          if (Settings.canDrawOverlays(this)) {       //在这判断是否请求权限成功
   *              Log.i(LOGTAG, "onActivityResult granted");
   *          }
   *      }
   * }
   *
   * @param obj
   */
  public void requestAlertWindowPermission(Object obj) {
    if (!AndroidVersionUtil.hasM()) {
      return;
    }
    Activity activity = null;
    Fragment fragment = null;
    if (obj instanceof Activity) {
      activity = (Activity) obj;
    } else if (obj instanceof Fragment) {
      fragment = (Fragment) obj;
      activity = fragment.getActivity();
    } else {
      L.e(TAG, "obj 只能是 Activity 或者 fragment 及其衍生类");
      return;
    }
    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
    intent.setData(Uri.parse("package:" + activity.getPackageName()));
    if (fragment != null) {
      fragment.startActivityForResult(intent, OnPermissionCallback.PERMISSION_ALERT_WINDOW);
    } else {
      activity.startActivityForResult(intent, OnPermissionCallback.PERMISSION_ALERT_WINDOW);
    }
  }

  /**
   * 请求修改系统设置权限
   */
  public void requestWriteSetting(Object obj) {
    if (!AndroidVersionUtil.hasM()) {
      return;
    }
    Activity activity = null;
    Fragment fragment = null;
    if (obj instanceof Activity) {
      activity = (Activity) obj;
    } else if (obj instanceof Fragment) {
      fragment = (Fragment) obj;
      activity = fragment.getActivity();
    } else {
      L.e(TAG, "obj 只能是 Activity 或者 fragment 及其衍生类");
      return;
    }
    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
    intent.setData(Uri.parse("package:" + activity.getPackageName()));
    if (fragment != null) {
      fragment.startActivityForResult(intent, OnPermissionCallback.PERMISSION_WRITE_SETTING);
    } else {
      activity.startActivityForResult(intent, OnPermissionCallback.PERMISSION_WRITE_SETTING);
    }
  }
}
