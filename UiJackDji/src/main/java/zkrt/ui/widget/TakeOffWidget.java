package zkrt.ui.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import dji.common.error.DJIError;
import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.ActionCallback;
import dji.log.DJILog;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseEView;
import zkrt.ui.c.a.UiCAB;
import zkrt.ui.c.a.UiCAC;
import zkrt.ui.c.b.UiCBE;
import zkrt.ui.c.b.UiCBF;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDG;
import zkrt.ui.d.UiDH;
import zkrt.ui.internal.DInterNal;

/**
 * Created by jack_xie on 17-6-1.
 */

public class TakeOffWidget extends UiBaseEView {
    private static final String TAG = "DULTakeOffWidget";
    private static final float HEIGHT_TAKEOFF = 1.2F;
    private boolean isFlying;
    private boolean isLanding;
    private String flightModeString;
    private boolean areMotorsOn;
    private int takeoffResId;
    private int landingResId;
    private int cancelLandingResId;
    private int currentResId;
    private DInterNal slideDialog;
    private static final int OPERATE_TYPE_NONE = 0;
    private static final int OPERATE_TYPE_TAKEOFF = 1;
    private static final int OPERATE_TYPE_GOHOME = 2;
    private int operateType;
    private UiCBE widgetAppearances;
    private DJIKey isFlyingKey;
    private DJIKey isAutoLandingKey;
    private DJIKey flightModeStringKey;
    private DJIKey areMotorsOnKey;

    public TakeOffWidget(Context var1) {
        this(var1, null, 0);
    }

    public TakeOffWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public TakeOffWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.slideDialog = null;
        this.operateType = 0;
        UiDA.b();
    }

    @MainThread
    @Keep
    public void onTakeOffStatusChange(boolean var1) {
        if(var1) {
            this.currentResId = this.landingResId;
        } else {
            this.currentResId = this.takeoffResId;
        }

        this.imageForeground.setImageResource(this.currentResId);
    }

    @MainThread
    @Keep
    public void onLandingStatusChange(boolean var1) {
        if(var1) {
            this.currentResId = this.cancelLandingResId;
        } else {
            this.currentResId = this.landingResId;
        }

        this.imageForeground.setImageResource(this.currentResId);
    }

    @MainThread
    @Keep
    public void performTakeOffAction() {
        FlightControllerKey var1 = FlightControllerKey.create("TakeOff");
        if(KeyManager.getInstance() != null) {
            KeyManager.getInstance().performAction(var1, new ActionCallback() {
                public void onSuccess() {
                    TakeOffWidget.this.onTakeOffActionResult((DJIError)null);
                }

                public void onFailure(@NonNull DJIError var1) {
                    if(TakeOffWidget.this.areMotorsOn) {
                        TakeOffWidget.this.onTakeOffActionResult((DJIError)null);
                    } else {
                        TakeOffWidget.this.onTakeOffActionResult(var1);
                    }

                }
            }, new Object[0]);
        }

    }

    @MainThread
    @Keep
    public void performLandingAction() {
        FlightControllerKey var1 = FlightControllerKey.create("AutoLanding");
        if(KeyManager.getInstance() != null) {
            KeyManager.getInstance().performAction(var1, new ActionCallback() {
                public void onSuccess() {
                    TakeOffWidget.this.onLandingActionResult((DJIError)null);
                }

                public void onFailure(@NonNull DJIError var1) {
                    TakeOffWidget.this.onLandingActionResult(var1);
                }
            }, new Object[0]);
        }

    }

    @MainThread
    @Keep
    public void performCancelLandingAction() {
        FlightControllerKey var1 = FlightControllerKey.create("CancelAutoLanding");
        if(KeyManager.getInstance() != null) {
            KeyManager.getInstance().performAction(var1, new ActionCallback() {
                public void onSuccess() {
                    TakeOffWidget.this.onCancelLandingActionResult((DJIError)null);
                }

                public void onFailure(@NonNull DJIError var1) {
                    TakeOffWidget.this.onCancelLandingActionResult(var1);
                }
            }, new Object[0]);
        }

    }

    @MainThread
    @Keep
    public void onTakeOffActionResult(@Nullable DJIError var1) {
        if(var1 != null) {
            this.showErrorDlg(var1.getDescription());
        }

    }

    @MainThread
    @Keep
    public void onLandingActionResult(@Nullable DJIError var1) {
        if(var1 != null) {
            this.showErrorDlg(var1.getDescription());
        }

    }

    @MainThread
    @Keep
    public void onCancelLandingActionResult(@Nullable DJIError var1) {
        if(var1 != null) {
            this.showErrorDlg(var1.getDescription());
        }

    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        this.takeoffResId = R.drawable.leftmenu_dlg_takeoff;
        this.landingResId = R.drawable.leftmenu_dlg_landing;
        this.cancelLandingResId = R.drawable.leftmenu_dlg_cancel;
        this.defaultResId = this.takeoffResId;
        this.imageForeground = (ImageView)this.findViewById(R.id.image_button_foreground);
        this.imageBackground = (ImageButton)this.findViewById(R.id.image_button_background);
        this.imageBackground.setOnClickListener(this);
    }

    public void initKey() {
        this.areMotorsOnKey = FlightControllerKey.create("AreMotorsOn");
        this.isFlyingKey = FlightControllerKey.create("IsFlying");
        this.isAutoLandingKey = FlightControllerKey.create("IsAutoLanding");
        this.flightModeStringKey = FlightControllerKey.create("FlightModeString");
        this.addDependentKey(this.areMotorsOnKey);
        this.addDependentKey(this.isFlyingKey);
        this.addDependentKey(this.isAutoLandingKey);
        this.addDependentKey(this.flightModeStringKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.isFlyingKey)) {
            this.isFlying = ((Boolean)var1).booleanValue();
        } else if(var2.equals(this.areMotorsOnKey)) {
            this.areMotorsOn = ((Boolean)var1).booleanValue();
        } else if(var2.equals(this.isAutoLandingKey)) {
            this.isLanding = ((Boolean)var1).booleanValue();
        } else if(var2.equals(this.flightModeStringKey)) {
            this.flightModeString = (String)((String)var1);
        }

    }

    public void updateWidget(DJIKey var1) {
        if(this.areMotorsOn && !this.isFlying) {
            this.onTakeOffEnable(false);
        } else {
            this.onTakeOffEnable(true);
            if(!this.areMotorsOn) {
                this.onTakeOffStatusChange(false);
            } else if(this.isLanding && this.areMotorsOn) {
                this.onLandingStatusChange(true);
            } else if(this.isLanding && !this.areMotorsOn) {
                this.isLanding = false;
                this.onTakeOffStatusChange(false);
            } else {
                this.onTakeOffStatusChange(true);
            }
        }

    }

    protected UiCAC getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBE();
        }

        return this.widgetAppearances;
    }

    public void onClick(View var1) {
        if(this.currentResId == this.cancelLandingResId) {
            this.hideSlideDialog();
            this.cancelLandAction();
        } else {
            this.showTipDialog(1);
        }

    }

    private void showTipDialog(int var1) {
        if(null == this.slideDialog) {
            this.slideDialog = new DInterNal(this.getContext());
            this.slideDialog.a(new DInterNal.a() {
                public void onLeftBtnClick(DialogInterface var1, int var2) {
                    TakeOffWidget.this.handleTipDlgLeftBtnClick();
                }

                public void onRightBtnClick(DialogInterface var1, int var2) {
                    TakeOffWidget.this.handleTipDlgRightBtnClick();
                }

                public void onCbChecked(DialogInterface var1, boolean var2, int var3) {
                    TakeOffWidget.this.handleTipDlgCbChecked(var2);
                }
            });
            this.slideDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface var1) {
                    TakeOffWidget.this.operateType = 0;
                }
            });
        }

        if(null != this.slideDialog && !this.slideDialog.isShowing()) {
            this.operateType = var1;
            if(var1 == 1) {
                this.updateTakeOffAlertDlg();
            }

            this.slideDialog.show();
        }

    }

    private void hideSlideDialog() {
        if(null != this.slideDialog && this.slideDialog.isShowing()) {
            this.slideDialog.dismiss();
        }

    }

    @MainThread
    public void onTakeOffEnable(boolean var1) {
        this.setEnabled(var1);
        this.imageBackground.setEnabled(var1);
        if(var1) {
            this.imageForeground.setAlpha(1.0F);
        } else {
            this.imageForeground.setAlpha(0.5F);
        }

    }

    private void takeOffAction() {
        this.performTakeOffAction();
    }

    private void landAction() {
        this.performLandingAction();
    }

    private void cancelLandAction() {
        this.performCancelLandingAction();
    }

    private void handleTipDlgRightBtnClick() {
        this.hideSlideDialog();
        if(this.isFlying) {
            this.landAction();
        } else {
            this.takeOffAction();
        }

    }

    private void handleTipDlgLeftBtnClick() {
        this.hideSlideDialog();
        if(2 != this.operateType && 1 == this.operateType) {
            ;
        }

    }

    private void handleTipDlgCbChecked(boolean var1) {
        if(var1 && 1 == this.operateType) {
            this.takeOffAction();
            this.hideSlideDialog();
        }

    }

    private void updateTakeOffAlertDlg() {
        String var1;
        String var2;
        if(this.areMotorsOn) {
            this.slideDialog.a(1);
            this.slideDialog.b(R.drawable.leftmenu_dlg_landing);
            var2 = this.getContext().getString(R.string.left_menu_land);
            var1 = this.getContext().getString(R.string.left_menu_land_desc);
            this.slideDialog.a(8, 0);
            this.slideDialog.e(8);
            this.slideDialog.d(0);
        } else {
            this.slideDialog.a(1);
            this.slideDialog.b(R.drawable.leftmenu_dlg_takeoff);
            String var3;
            if(value_Unit_Type == UiDG.dhEnumA.b) {
                float var4 = UiDG.a(1.2F);
                var3 = this.getContext().getString(R.string.imperial, new Object[]{Float.valueOf(var4)});
            } else {
                var3 = this.getContext().getString(R.string.metric, new Object[]{Float.valueOf(1.2F)});
            }

            if(this.flightModeString != null && this.flightModeString.toLowerCase().contains("atti")) {
                var1 = this.getContext().getString(R.string.takeoff_inatti, new Object[]{var3});
            } else {
                var1 = this.getContext().getString(R.string.left_menu_takeoff_desc, new Object[]{var3});
            }

            var2 = this.getContext().getString(R.string.left_menu_takeoff);
            this.slideDialog.b(var1);
            this.slideDialog.a(8, 0);
            this.slideDialog.e(0);
            this.slideDialog.c(this.getContext().getString(R.string.left_menu_takeoff_switch));
            this.slideDialog.d(8);
        }

        this.slideDialog.a(var2);
        this.slideDialog.b(var1);
    }
}
