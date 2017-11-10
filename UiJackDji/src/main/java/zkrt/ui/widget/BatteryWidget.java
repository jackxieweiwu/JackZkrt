package zkrt.ui.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dji.common.battery.BatteryConnectionState;
import dji.common.battery.PairingState;
import dji.common.battery.WarningRecord;
import dji.common.flightcontroller.BatteryThresholdBehavior;
import dji.keysdk.BatteryKey;
import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.KeyManager;
import dji.keysdk.ProductKey;
import dji.log.DJILog;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseFDULDynamicFrameLayoutWidget;
import zkrt.ui.c.a.UiCAB;
import zkrt.ui.c.b.UiCBD;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDL;

/**
 * Created by jack_xie on 17-6-1.
 */

public class BatteryWidget extends UiBaseFDULDynamicFrameLayoutWidget {
    private static final String TAG = "DULBatteryWidget";
    private static final int EXCLUDE_ICON_VIEW = 1;
    private static final int EXCLUDE_PERCENTAGE_VIEW = 2;
    private static final int EXCLUDE_SINGLE_VOLTAGE_VIEW = 4;
    private static final int EXCLUDE_DOUBLE_VOLTAGE_VIEW = 8;
    private ImageView iconImageView;
    private TextView valueTextView;
    private int batteryPercentage;
    private int battery2Percentage;
    private BatteryConnectionState batteryConnectionState;
    private BatteryThresholdBehavior batteryWarningLevel;
    private String batteryPerInString;
    private int goHomeBattery;
    private int currentBatteryIconId;
    private int currentTextColorId;
    private DJIKey batteryEnergyRemainPercentageKey;
    private DJIKey batteryConnectionStateKey;
    private DJIKey batteryRemainFlightKey;
    private DJIKey batteryNeedToGoHomeKey;
    private UiCBD widgetAppearances;
    private boolean isConnected;
    private BatteryKey battery2EnergyRemainPercentageKey;
    private TextView value2TextView;
    private BatteryKey batteryEnergyRemainKey;
    private TextView value1TextView;
    private ImageView multiIconImageView;
    private ProductKey isProductConnectKey;
    private BatteryKey pairingStateKey;
    private PairingState pairingState;
    private String battery2PerInString;
    private TextView voltage1TextView;
    private TextView voltage2TextView;
    private int voltageBgId;
    private BatteryKey batteryCellVoltageKey;
    private BatteryKey battery2CellVoltageKey;
    private String battery1VoltageString;
    private String battery2VoltageString;
    private TextView voltageTextView;

    public BatteryWidget(Context var1) {
        this(var1, null, 0);
    }

    public BatteryWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public BatteryWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.batteryConnectionState = BatteryConnectionState.NORMAL;
        this.batteryWarningLevel = BatteryThresholdBehavior.FLY_NORMALLY;
        this.goHomeBattery = 0;
        this.currentBatteryIconId = R.drawable.ic_topbar_battery_nor;
        this.currentTextColorId = R.color.green;
        UiDA.b();
    }

    @MainThread
    @Keep
    public void onBatteryPercentageChange(@IntRange(from = 0L,to = 100L) int var1) {
        this.updateUIResources();
    }

    @MainThread
    @Keep
    public void onBatteryConnectionStateChange(@Nullable BatteryConnectionState var1) {
        this.updateUIResources();
    }

    @MainThread
    @Keep
    public void onRemainingBatteryStateChange(@Nullable BatteryThresholdBehavior var1) {
        this.updateUIResources();
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        int var4 = UiDL.a(var1, var2, R.styleable.BatteryWidget, R.styleable.BatteryWidget_excludeView, 0);
        this.iconImageView = (ImageView)this.findViewById(R.id.imageview_battery_icon);
        this.multiIconImageView = (ImageView)this.findViewById(R.id.imageview_multi_battery_icon);
        this.valueTextView = (TextView)this.findViewById(R.id.textview_battery_value);
        this.value1TextView = (TextView)this.findViewById(R.id.textview_battery1_value);
        this.value2TextView = (TextView)this.findViewById(R.id.textview_battery2_value);
        this.voltageTextView = (TextView)this.findViewById(R.id.textview_battery_voltage);
        this.voltage1TextView = (TextView)this.findViewById(R.id.textview_battery1_voltage);
        this.voltage2TextView = (TextView)this.findViewById(R.id.textview_battery2_voltage);
        this.initViewByAttribute(var4);
    }

    private void initViewByAttribute(int var1) {
        if((var1 & 1) == 1) {
            this.setVisibility(this.iconImageView, GONE);
            this.setVisibility(this.multiIconImageView, GONE);
            this.iconImageView = null;
            this.multiIconImageView = null;
        }

        if((var1 & 2) == 2) {
            this.setVisibility(this.valueTextView, GONE);
            this.setVisibility(this.value1TextView, GONE);
            this.setVisibility(this.value2TextView, GONE);
            this.valueTextView = null;
            this.value1TextView = null;
            this.value2TextView = null;
        }

        if((var1 & 8) == 8) {
            this.setVisibility(this.voltage1TextView, GONE);
            this.setVisibility(this.voltage2TextView, GONE);
            this.voltage1TextView = null;
            this.voltage2TextView = null;
        }

        if((var1 & 4) == 4) {
            this.setVisibility(this.voltageTextView, GONE);
            this.voltageTextView = null;
        }

    }

    protected UiCAB getWidgetAppearances() {
        if(this.widgetAppearances == null) {
            this.widgetAppearances = new UiCBD();
        }

        return this.widgetAppearances;
    }

    private void setVisibility(View var1, int var2) {
        if(var1 != null) {
            var1.setVisibility(var2);
        }

    }

    private void setText(TextView var1, String var2) {
        if(var1 != null) {
            var1.setText(var2);
        }

    }

    private void setText(TextView var1, @StringRes int var2) {
        if(var1 != null) {
            var1.setText(var2);
        }

    }

    private void setTextColor(TextView var1, @ColorInt int var2) {
        if(var1 != null) {
            var1.setTextColor(var2);
        }

    }

    private void setImageResource(ImageView var1, @DrawableRes int var2) {
        if(var1 != null) {
            var1.setImageResource(var2);
        }

    }

    private void setBackgroundResource(View var1, @DrawableRes int var2) {
        if(var1 != null) {
            var1.setBackgroundResource(var2);
        }

    }

    private void updateVisibleViews() {
        boolean var1 = this.isDoubleBattery();
        if(var1) {
            if(this.multiIconImageView != null && this.multiIconImageView.getVisibility() == GONE || this.value1TextView != null && this.value1TextView.getVisibility() == GONE || this.voltage1TextView != null && this.voltage1TextView.getVisibility() == GONE) {
                this.setVisibility(this.multiIconImageView, VISIBLE);
                this.setVisibility(this.value1TextView, VISIBLE);
                this.setVisibility(this.value2TextView, VISIBLE);
                this.setVisibility(this.voltage1TextView, VISIBLE);
                this.setVisibility(this.voltage2TextView, VISIBLE);
                this.setVisibility(this.iconImageView, GONE);
                this.setVisibility(this.valueTextView, GONE);
                this.setVisibility(this.voltageTextView, GONE);
                this.getWidgetAppearances().i();
            }
        } else if(this.iconImageView != null && this.iconImageView.getVisibility() == GONE || this.valueTextView != null && this.valueTextView.getVisibility() == GONE || this.voltageTextView != null && this.voltageTextView.getVisibility() == GONE) {
            this.setVisibility(this.iconImageView, VISIBLE);
            this.setVisibility(this.valueTextView, VISIBLE);
            this.setVisibility(this.voltageTextView, VISIBLE);
            this.setVisibility(this.multiIconImageView, GONE);
            this.setVisibility(this.value1TextView, GONE);
            this.setVisibility(this.value2TextView, GONE);
            this.setVisibility(this.voltage1TextView, GONE);
            this.setVisibility(this.voltage2TextView, GONE);
            this.getWidgetAppearances().i();
        }

    }

    private void updateUIResources() {
        int var1 = this.getResources().getColor(this.currentTextColorId);
        this.updateVisibleViews();
        if(this.isDoubleBattery()) {
            this.setImageResource(this.multiIconImageView, this.currentBatteryIconId);
            this.setTextColor(this.value1TextView, var1);
            this.setText(this.value1TextView, this.batteryPerInString);
            this.setTextColor(this.value2TextView, var1);
            this.setText(this.value2TextView, this.battery2PerInString);
            this.setBackgroundResource(this.voltage1TextView, this.voltageBgId);
            this.setBackgroundResource(this.voltage2TextView, this.voltageBgId);
            this.setText(this.voltage1TextView, this.battery1VoltageString);
            this.setText(this.voltage2TextView, this.battery2VoltageString);
            this.setTextColor(this.voltage1TextView, var1);
            this.setTextColor(this.voltage2TextView, var1);
        } else {
            this.setImageResource(this.iconImageView, this.currentBatteryIconId);
            this.setTextColor(this.valueTextView, var1);
            this.setText(this.valueTextView, this.batteryPerInString);
            this.setBackgroundResource(this.voltageTextView, this.voltageBgId);
            this.setText(this.voltageTextView, this.battery1VoltageString);
            this.setTextColor(this.voltageTextView, var1);
        }

        if(!this.isConnected) {
            this.setText(this.valueTextView, R.string.string_default_value);
            this.setText(this.value1TextView, R.string.string_default_value);
            this.setText(this.value2TextView, R.string.string_default_value);
            this.setText(this.voltageTextView, R.string.string_default_value);
            this.setText(this.voltage1TextView, R.string.string_default_value);
            this.setText(this.voltage2TextView, R.string.string_default_value);
        }

    }

    public void initKey() {
        this.isProductConnectKey = ProductKey.create("Connection");
        this.batteryEnergyRemainPercentageKey = BatteryKey.create("ChargeRemainingInPercent");
        this.battery2EnergyRemainPercentageKey = BatteryKey.create("ChargeRemainingInPercent", 1);
        this.batteryCellVoltageKey = BatteryKey.create("CellVoltages");
        this.battery2CellVoltageKey = BatteryKey.create("CellVoltages", 1);
        this.batteryConnectionStateKey = BatteryKey.create("ConnectionState");
        this.pairingStateKey = BatteryKey.create("PairingState");
        this.batteryRemainFlightKey = FlightControllerKey.create("RemainingBattery");
        this.batteryNeedToGoHomeKey = FlightControllerKey.create("BatteryPercentageNeededToGoHome");
        this.addDependentKey(this.isProductConnectKey);
        this.addDependentKey(this.batteryEnergyRemainKey);
        this.addDependentKey(this.batteryEnergyRemainPercentageKey);
        this.addDependentKey(this.battery2EnergyRemainPercentageKey);
        this.addDependentKey(this.batteryCellVoltageKey);
        this.addDependentKey(this.battery2CellVoltageKey);
        this.addDependentKey(this.batteryConnectionStateKey);
        this.addDependentKey(this.pairingStateKey);
        this.addDependentKey(this.batteryRemainFlightKey);
        this.addDependentKey(this.batteryNeedToGoHomeKey);
    }

    private float getMinVoltage(Object var1) {
        if(var1 != null && var1 instanceof Integer[]) {
            Integer[] var2 = (Integer[]) var1;
            if(var2.length <= 0) {
                return 0.0F;
            } else {
                int var3 = var2[0].intValue();
                Integer[] var4 = var2;
                int var5 = var2.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Integer var7 = var4[var6];
                    var3 = Math.min(var3, var7.intValue());
                }

                return (float)var3 * 1.0F / 1000.0F;
            }
        } else {
            return 0.0F;
        }
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.batteryEnergyRemainPercentageKey)) {
            this.batteryPercentage = ((Integer)var1).intValue();
            this.batteryPerInString = this.getContext().getString(R.string.battery_percent, new Object[]{Integer.valueOf(this.batteryPercentage)});
        } else if(var2.equals(this.battery2EnergyRemainPercentageKey)) {
            this.battery2Percentage = ((Integer)var1).intValue();
            this.battery2PerInString = this.getContext().getString(R.string.battery_percent, new Object[]{Integer.valueOf(this.battery2Percentage)});
        } else {
            float var3;
            if(var2.equals(this.batteryCellVoltageKey)) {
                var3 = this.getMinVoltage(var1);
                this.battery1VoltageString = this.context.getString(R.string.battery_voltage_unit, new Object[]{Float.valueOf(var3)});
            } else if(var2.equals(this.battery2CellVoltageKey)) {
                var3 = this.getMinVoltage(var1);
                this.battery2VoltageString = this.context.getString(R.string.battery_voltage_unit, new Object[]{Float.valueOf(var3)});
            } else if(var2.equals(this.batteryConnectionStateKey)) {
                this.batteryConnectionState = (BatteryConnectionState)var1;
            } else if(var2.equals(this.batteryRemainFlightKey)) {
                this.batteryWarningLevel = (BatteryThresholdBehavior)var1;
            } else if(var2.equals(this.batteryNeedToGoHomeKey)) {
                this.goHomeBattery = ((Integer)var1).intValue();
            } else if(var2.equals(this.isProductConnectKey)) {
                this.isConnected = ((Boolean)var1).booleanValue();
            } else if(var2.equals(this.pairingStateKey)) {
                this.pairingState = (PairingState)var1;
            }
        }

        this.updateCurrentResources();
    }

    private boolean isDoubleBattery() {
        Object var1 = KeyManager.getInstance().getValue(this.pairingStateKey);
        if(var1 != null) {
            this.pairingState = (PairingState)var1;
            if(this.pairingState != PairingState.UNKNOWN) {
                return true;
            }
        }

        return false;
    }

    private void updateCurrentResources() {
        if(this.isDoubleBattery()) {
            this.updateDoubleBatteriesResources();
        } else {
            this.updateSingleBatteryResources();
        }

    }

    private boolean checkAreTwoBatteriesOverHeating() {
        for(int var1 = 0; var1 < 2; ++var1) {
            Object var2 = KeyManager.getInstance().getValue(BatteryKey.create("LatestWarningRecord", var1));
            if(var2 != null && ((WarningRecord)var2).isOverHeated()) {
                return true;
            }
        }

        return false;
    }

    private void updateDoubleBatteriesResources() {
        this.currentTextColorId = R.color.green;
        boolean var1 = this.checkAreTwoBatteriesOverHeating();
        this.voltageBgId = R.drawable.battery_voltage_bg_normal;
        if(this.batteryWarningLevel != BatteryThresholdBehavior.GO_HOME && this.batteryWarningLevel != BatteryThresholdBehavior.LAND_IMMEDIATELY) {
            if(var1) {
                this.currentTextColorId = R.color.yellow;
                this.currentBatteryIconId = R.drawable.ic_topbar_double_battery_overheat_warning;
            } else {
                this.currentBatteryIconId = R.drawable.ic_topbar_double_battery_nor;
            }
        } else {
            this.currentTextColorId = R.color.red;
            this.voltageBgId = R.drawable.battery_voltage_bg_error;
            this.currentBatteryIconId = R.drawable.ic_topbar_double_battery_error;
        }

    }

    private void updateSingleBatteryResources() {
        if(this.batteryPercentage > this.goHomeBattery && this.batteryWarningLevel != BatteryThresholdBehavior.GO_HOME) {
            if(this.batteryWarningLevel == BatteryThresholdBehavior.LAND_IMMEDIATELY) {
                this.currentBatteryIconId = R.drawable.ic_topbar_battery_thunder;
                this.currentTextColorId = R.color.red;
                this.voltageBgId = R.drawable.battery_voltage_bg_error;
                DJILog.d("DULBatteryWidget", "Battery warning level is 2");
            } else {
                this.currentBatteryIconId = R.drawable.ic_topbar_battery_nor;
                this.currentTextColorId = R.color.green;
                this.voltageBgId = R.drawable.battery_voltage_bg_normal;
            }
        } else {
            this.currentBatteryIconId = R.drawable.ic_topbar_battery_dangerous;
            this.currentTextColorId = R.color.red;
            this.voltageBgId = R.drawable.battery_voltage_bg_error;
            DJILog.d("DULBatteryWidget", "Battery percentage less than goHomeBattery or warning level is 1" + this.batteryPercentage + " " + this.goHomeBattery + " " + this.batteryWarningLevel);
        }

        if(this.batteryConnectionState != BatteryConnectionState.NORMAL) {
            this.currentBatteryIconId = R.drawable.ic_topbar_battery_error;
            if(this.batteryPercentage > this.goHomeBattery && this.batteryWarningLevel != BatteryThresholdBehavior.GO_HOME && this.batteryWarningLevel != BatteryThresholdBehavior.LAND_IMMEDIATELY) {
                this.currentTextColorId = R.color.green;
            } else {
                this.currentTextColorId = R.color.red;
                DJILog.d("DULBatteryWidget", "Battery error and battery percentage is less than goHomeBattery or level is 1 or 2");
            }
        }

    }

    public void updateWidget(DJIKey var1) {
        if(var1.equals(this.batteryEnergyRemainPercentageKey)) {
            this.onBatteryPercentageChange(this.batteryPercentage);
        } else if(var1.equals(this.batteryConnectionStateKey)) {
            this.onBatteryConnectionStateChange(this.batteryConnectionState);
        } else if(var1.equals(this.batteryRemainFlightKey)) {
            this.onRemainingBatteryStateChange(this.batteryWarningLevel);
        } else {
            this.updateUIResources();
        }

    }
}
