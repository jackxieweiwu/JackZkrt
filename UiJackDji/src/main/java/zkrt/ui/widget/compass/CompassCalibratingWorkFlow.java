package zkrt.ui.widget.compass;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;

import dji.common.flightcontroller.CompassCalibrationState;
import dji.common.product.Model;
import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.ActionCallback;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseQView;
import zkrt.ui.d.UiDA;
import zkrt.ui.internal.CInterNal;

/**
 * Created by jack_xie on 17-11-6.
 */

public class CompassCalibratingWorkFlow extends UiBaseQView{
    private static final String MAVIC = "Mavic";
    private static final String PHANTOM3 = "Phantom 3";
    private static final String INSPIRE = "Inspire";
    private static final String RES_NAME_P3 = "p3";
    private static final String RES_NAME_P4 = "p4";
    private static final String RES_NAME_MAVIC = "mavic";
    private static final String RES_NAME_INSPIRE = "inspire";
    private static final String RES_NAME_M100 = "m100";
    private static final String RES_NAME_M600 = "m600";
    private static final String RES_NAME_UNKNOWN = "aircraft";
    private static final String RES_NAME_HORIZONTAL = "_horizontal";
    private static final String RES_NAME_VERTICAL = "_vertical";
    private Context context;
    private CInterNal calibrationDialog;
    private FlightControllerKey calibrationStateKey;
    private FlightControllerKey isCalibratingKey;
    private CompassCalibrationState calibrationState;

    public CompassCalibratingWorkFlow(Context var1) {
        this(var1, (AttributeSet)null, 0);
    }

    public CompassCalibratingWorkFlow(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public CompassCalibratingWorkFlow(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.context = null;
        this.calibrationDialog = null;
        this.context = var1;
        UiDA.b();
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
    }

    public void initKey() {
        this.calibrationStateKey = FlightControllerKey.create("CompassCalibrationStatus");
        this.addDependentKey(this.calibrationStateKey);
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.calibrationStateKey)) {
            this.calibrationState = (CompassCalibrationState)var1;
        }

    }

    public void updateWidget(DJIKey var1) {
        this.updateCalibrationDialog();
    }

    private void updateCalibrationDialog() {
        if(this.calibrationState != null) {
            if(this.calibrationState != CompassCalibrationState.NOT_CALIBRATING) {
                this.showImgDlg();
            } else {
                this.hideImgDlg();
            }

            int var1 = this.calibrationState.value();
            this.updateImgDlg(var1);
        }
    }

    public void showImgDlg() {
        if(null == this.calibrationDialog) {
            this.calibrationDialog = new CInterNal(this.getContext());
            this.calibrationDialog.a(this.context.getString(R.string.compass_calibration_tip_1));
            this.calibrationDialog.c().d(R.string.compass_calibration_cancel).a(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CompassCalibratingWorkFlow.this.handleCancelCalibrationClick();
                }
            }).b(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CompassCalibratingWorkFlow.this.handleRetryCalibrationClick();
                }
            });
        }

        if(!this.calibrationDialog.isShowing()) {
            this.updateImgDlg(0);
            this.calibrationDialog.show();
        }

    }

    private void hideImgDlg() {
        if(null != this.calibrationDialog && this.calibrationDialog.isShowing()) {
            this.calibrationDialog.dismiss();
            this.calibrationDialog = null;
        }

    }

    private void handleRetryCalibrationClick() {
        this.hideImgDlg();
        stopCalibration((ActionCallback)null);
        Handler var1 = new Handler();
        var1.postDelayed(new Runnable() {
            public void run() {
                CompassCalibratingWorkFlow.startCalibration((ActionCallback)null);
            }
        }, 50L);
    }

    private void handleCancelCalibrationClick() {
        this.hideImgDlg();
        stopCalibration((ActionCallback)null);
    }

    private void updateImgDlg(int var1) {
        if(this.calibrationDialog != null && this.calibrationDialog.isShowing()) {
            String var2 = this.context.getString(R.string.fpv_checklist_compass_tip_show_arm);
            if(var1 == 0) {
                if(this.isFoldableDrone()) {
                    this.calibrationDialog.a(this.context.getString(R.string.compass_calibration_tip_1) + "\n" + var2);
                } else {
                    this.calibrationDialog.a(this.context.getString(R.string.compass_calibration_tip_1));
                }

                this.calibrationDialog.b(0);
                this.calibrationDialog.b(this.context.getString(R.string.compass_calibration_tip_1_desc));
                this.calibrationDialog.a(0);
                this.calibrationDialog.c(this.getCompassHResId());
                this.calibrationDialog.c().d(R.string.compass_calibration_cancel);
            } else if(var1 == 1) {
                if(this.isFoldableDrone()) {
                    this.calibrationDialog.a(this.context.getString(R.string.compass_calibration_tip_2) + "\n" + var2);
                } else {
                    this.calibrationDialog.a(this.context.getString(R.string.compass_calibration_tip_2));
                }

                this.calibrationDialog.b(0);
                this.calibrationDialog.b(this.context.getString(R.string.compass_calibration_tip_2_desc));
                this.calibrationDialog.a(0);
                this.calibrationDialog.c(this.getCompassVResId());
                this.calibrationDialog.c().d(R.string.compass_calibration_cancel);
            } else if(var1 == 2) {
                this.calibrationDialog.a(R.drawable.success_icon);
                this.calibrationDialog.a(this.context.getString(R.string.compass_calibration_success));
                this.calibrationDialog.b(8);
                this.calibrationDialog.b();
                this.calibrationDialog.d(R.string.app_ok);
            } else if(var1 == 3) {
                this.calibrationDialog.a(R.drawable.alert_icon);
                this.calibrationDialog.a(this.context.getString(R.string.compass_calibration_fail) + "\n\n" + this.context.getString(R.string.compass_calibration_fail_desc));
                this.calibrationDialog.b(8);
                this.calibrationDialog.b().d(R.string.app_cancel);
                this.calibrationDialog.e(R.string.app_retry);
            }
        }

    }

    public int getCompassHResId() {
        String var1 = this.getProductModelResName() + "_horizontal";
        return this.context.getResources().getIdentifier(var1, "drawable", this.context.getPackageName());
    }

    public int getCompassVResId() {
        String var1 = this.getProductModelResName() + "_vertical";
        return this.context.getResources().getIdentifier(var1, "drawable", this.context.getPackageName());
    }

    private String getProductModelResName() {
        if(DJISDKManager.getInstance().getProduct() != null && DJISDKManager.getInstance().getProduct() instanceof Aircraft) {
            Model var1 = DJISDKManager.getInstance().getProduct().getModel();
            return getModelResName(var1);
        } else {
            return null;
        }
    }

    private boolean isFoldableDrone() {
        if(DJISDKManager.getInstance().getProduct() != null && DJISDKManager.getInstance().getProduct() instanceof Aircraft) {
            Model var1 = DJISDKManager.getInstance().getProduct().getModel();
            if(var1 != null && var1.getDisplayName().contains("Mavic")) {
                return true;
            }
        }

        return false;
    }

    private static String getModelResName(Model var0) {
        String var1 = null;
        if(var0 != null) {
            if(var0.getDisplayName().contains("Phantom 3")) {
                var1 = "p3";
            } else if(var0.getDisplayName().contains("Inspire")) {
                var1 = "inspire";
            } else if(var0.getDisplayName().contains(Model.PHANTOM_4.getDisplayName())) {
                var1 = "p4";
            } else if(var0.getDisplayName().contains(Model.MATRICE_100.getDisplayName())) {
                var1 = "m100";
            } else if(var0.getDisplayName().contains(Model.MATRICE_600.getDisplayName())) {
                var1 = "m600";
            } else if(var0.getDisplayName().contains("Mavic")) {
                var1 = "mavic";
            } else {
                var1 = "aircraft";
            }
        }

        return var1;
    }

    public static void startCalibration(ActionCallback var0) {
        KeyManager.getInstance().performAction(FlightControllerKey.create("CompassStartCalibration"), var0, new Object[0]);
    }

    public static void stopCalibration(ActionCallback var0) {
        KeyManager.getInstance().performAction(FlightControllerKey.create("CompassStopCalibration"), var0, new Object[0]);
    }
}
