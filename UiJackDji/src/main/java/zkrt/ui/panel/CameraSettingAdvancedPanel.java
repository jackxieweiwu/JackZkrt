package zkrt.ui.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import dji.common.bus.UILibEventBus;
import dji.common.camera.SettingsDefinitions;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Action1;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseGViewDULFrameLayout;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBB;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDL;
import zkrt.ui.internal.BInterNal;
import zkrt.ui.internal.DULParentChildrenViewAnimator;
import zkrt.ui.internal.DULTabBarView;
import zkrt.ui.internal.camera.InterCamera_A;

/**
 * Created by jack_xie on 17-6-1.
 */

public class CameraSettingAdvancedPanel extends UiBaseGViewDULFrameLayout implements DULParentChildrenViewAnimator.a{
    private DJIKey cameraModeKey;
    private SettingsDefinitions.CameraMode cameraMode;
    private DULParentChildrenViewAnimator contentAnimator;
    private DULTabBarView tabBar;
    private UiCAC widgetAppearance;
    private ImageView imageBackArrow;
    private TextView textTitle;
    private FrameLayout titleBar;

    public CameraSettingAdvancedPanel(Context var1) {
        this(var1, null, 0);
    }

    public CameraSettingAdvancedPanel(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CameraSettingAdvancedPanel(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        UiDA.b();
    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearance == null) {
            this.widgetAppearance = new UiCBB();
        }
        return this.widgetAppearance;
    }

    private void initTabBar() {
        this.tabBar = this.findViewById(R.id.camera_advsetting_tab);
        ImageView var1 = this.findViewById(R.id.camera_tab_photo);
        ImageView var2 = this.findViewById(R.id.camera_tab_video);
        ImageView var3 = this.findViewById(R.id.camera_tab_other);
        ImageView var4 = this.findViewById(R.id.camera_tab_indicator);


        this.tabBar.a(new ImageView[]{var1, var2, var3}, var4, true);
        this.tabBar.setStageChangedCallback(new DULTabBarView.a() {
            public void onStageChange(int var1) {
                CameraSettingAdvancedPanel.this.showView(var1);
            }
        });
    }

    private void initTitleBar() {
        this.titleBar = (FrameLayout)this.findViewById(R.id.camera_setting_title_bar);
        this.imageBackArrow = (ImageView)this.findViewById(R.id.imageview_back);
        this.textTitle = (TextView)this.findViewById(R.id.textview_title);
        this.imageBackArrow.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View var1, MotionEvent var2) {
                if(var1 == CameraSettingAdvancedPanel.this.imageBackArrow) {
                    if(var2.getAction() == 0) {
                        CameraSettingAdvancedPanel.this.imageBackArrow.setPressed(true);
                        CameraSettingAdvancedPanel.this.onBackButtonClicked();
                    } else if(var2.getAction() == 1) {
                        CameraSettingAdvancedPanel.this.imageBackArrow.setPressed(false);
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    protected void onBackButtonClicked() {
        InterCamera_A var1 = (InterCamera_A)this.contentAnimator.getCurrentView();
        if(var1 != null) {
            var1.f();
        }

    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        UiDL.c(this);
        this.initTabBar();
        this.initTitleBar();
        this.contentAnimator = (DULParentChildrenViewAnimator)this.findViewById(R.id.camera_setting_content);
        InterCamera_A var4 = (InterCamera_A)this.findViewById(R.id.camera_setting_content_photo);
        var4.setRootViewCallback(this);
        var4.setTitleTextView(this.textTitle);
        InterCamera_A var5 = (InterCamera_A)this.findViewById(R.id.camera_setting_content_video);
        var5.setRootViewCallback(this);
        var5.setTitleTextView(this.textTitle);
        InterCamera_A var6 = (InterCamera_A)this.findViewById(R.id.camera_setting_content_other);
        var6.setRootViewCallback(this);
        var6.setTitleTextView(this.textTitle);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.subscription.add(UILibEventBus.getInstance().register(BInterNal.a.class).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BInterNal.a>() {
            @Override
            public void call(BInterNal.a a) {
                if(a.a()) {
                    CameraSettingAdvancedPanel.this.setVisibility(VISIBLE);
                } else {
                    CameraSettingAdvancedPanel.this.setVisibility(INVISIBLE);
                }
            }
        }));
    }

    public void initKey() {
        this.cameraModeKey = CameraKey.create("Mode");
        this.addDependentKey(this.cameraModeKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.cameraModeKey)) {
            this.cameraMode = (SettingsDefinitions.CameraMode)var1;
        }

    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.cameraModeKey)) {
            byte var2 = 0;
            if(this.cameraMode == SettingsDefinitions.CameraMode.SHOOT_PHOTO) {
                var2 = 0;
            } else if(this.cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO) {
                var2 = 1;
            }

            this.onBackButtonClicked();
            this.tabBar.b(var2);
        }

    }

    private void showView(int var1) {
        this.contentAnimator.setDisplayedChild(var1);
    }

    public void onRootViewIsShown(boolean var1) {
        if(var1) {
            this.tabBar.setVisibility(VISIBLE);
            this.titleBar.setVisibility(GONE);
        } else {
            this.tabBar.setVisibility(INVISIBLE);
            this.titleBar.setVisibility(VISIBLE);
        }

    }
}
