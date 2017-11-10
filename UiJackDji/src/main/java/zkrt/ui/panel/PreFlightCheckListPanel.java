package zkrt.ui.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import dji.common.bus.LogicEventBus;
import dji.common.bus.UILibEventBus;
import dji.common.remotecontroller.AircraftMappingStyle;
import dji.common.remotecontroller.ChargeRemaining;
import dji.internal.logics.BatteryLogic;
import dji.internal.logics.CompassLogic;
import dji.internal.logics.ESCLogic;
import dji.internal.logics.FPVTipLogic;
import dji.internal.logics.GimbalLogic;
import dji.internal.logics.IMULogic;
import dji.internal.logics.LogicManager;
import dji.internal.logics.Message;
import dji.internal.logics.RadioChannelQualityLogic;
import dji.internal.logics.VisionLogic;
import dji.keysdk.BatteryKey;
import dji.keysdk.CameraKey;
import dji.keysdk.DJIKey;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.RemoteControllerKey;
import dji.keysdk.callback.ActionCallback;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.android.schedulers.AndroidSchedulers;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.schedulers.Schedulers;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import zkrt.ui.R;
import zkrt.ui.a.UiAB;
import zkrt.ui.base.UiBasePView;
import zkrt.ui.c.UiCC;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDC;
import zkrt.ui.d.UiDL;
import zkrt.ui.internal.BInterNal.d;
import zkrt.ui.internal.DInterNal;
import zkrt.ui.widget.compass.CompassCalibratingWorkFlow;

/**
 * Created by jack_xie on 17-6-1.
 */

public class PreFlightCheckListPanel extends UiBasePView {
    private static final String UNKNOWN = "Unknown";
    private DJIKey flightModeStringKey;
    private DJIKey remoteControllerModeKey;
    private DJIKey remoteControllerBatteryKey;
    private DJIKey aircraftBatteryTempKey;
    private DJIKey sdRemainingSpaceKey;
    private DJIKey sdUSBConnectedKey;
    private UiCC overallItem;
    private UiCC radioItem;
    private UiCC imuItem;
    private UiCC compassItem;
    private UiCC airBatteryItem;
    private UiCC rcBatteryItem;
    private UiCC flightItem;
    private UiCC remoteModeItem;
    private UiCC airBatteryTemp;
    private UiCC sdCardItem;
    private UiCC gimbalItem;
    private UiCC escItem;
    private UiCC visionItem;
    private UiCC itemToModify;
    private CompositeSubscription subscriptions;
    private AtomicBoolean isEventRegistered;
    private Observable<VisionLogic.VisionEvent> visionObservable;
    private Observable<RadioChannelQualityLogic.RadioChannelQualityEvent> radioObservable;
    private Observable<FPVTipLogic.FPVTipEvent> fpvTipObservable;
    private Observable<GimbalLogic.GimbalEvent> gimbalObservable;
    private Observable<IMULogic.IMUEvent> imuObservable;
    private Observable<ESCLogic.ESCEvent> escObservable;
    private Observable<CompassLogic.CompassEvent> compassObservable;
    private Observable<BatteryLogic.BatteryEvent> batteryObservable;
    private boolean isUSBConnected;
    private int sdRemainingSpace;

    public PreFlightCheckListPanel(Context var1) {
        this(var1, null, 0);
    }

    public PreFlightCheckListPanel(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public PreFlightCheckListPanel(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.subscriptions = new CompositeSubscription();
        this.isEventRegistered = new AtomicBoolean(false);
        UiDA.b();
    }

    public void updateTitle(TextView var1) {
        if(var1 != null) {
            var1.setText(R.string.aircraft_status);
        }

    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        super.initView(var1, var2, var3);
        int var4 = UiDL.a(var1, var2, R.styleable.PreFlightCheckListPanel, R.styleable.PreFlightCheckListPanel_excludeItem, 0);
        this.setBackgroundColor(-16777216);
        this.adapter = new UiAB(this);
        this.initItems(this.adapter, var4);
        this.adapter.a(true);
        this.contentList.setHasFixedSize(true);
        this.contentList.setAdapter(this.adapter);
        this.contentList.setLayoutManager(new LinearLayoutManager(var1));
        UILibEventBus.getInstance().register(d.class).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<d>() {
                    @Override
                    public void call(d d) {
                        if(PreFlightCheckListPanel.this.getVisibility() == VISIBLE) {
                            PreFlightCheckListPanel.this.setVisibility(GONE);
                        } else {
                            PreFlightCheckListPanel.this.setVisibility(VISIBLE);
                        }
                    }
                });
        this.fpvTipObservable = LogicEventBus.getInstance().register(FPVTipLogic.FPVTipEvent.class).observeOn(Schedulers.computation());
        this.gimbalObservable = LogicEventBus.getInstance().register(GimbalLogic.GimbalEvent.class).observeOn(Schedulers.computation());
        this.visionObservable = LogicEventBus.getInstance().register(VisionLogic.VisionEvent.class).observeOn(Schedulers.computation());
        this.radioObservable = LogicEventBus.getInstance().register(RadioChannelQualityLogic.RadioChannelQualityEvent.class).observeOn(Schedulers.computation());
        this.imuObservable = LogicEventBus.getInstance().register(IMULogic.IMUEvent.class).observeOn(Schedulers.computation());
        this.escObservable = LogicEventBus.getInstance().register(ESCLogic.ESCEvent.class).observeOn(Schedulers.computation());
        this.compassObservable = LogicEventBus.getInstance().register(CompassLogic.CompassEvent.class).observeOn(Schedulers.computation());
        this.batteryObservable = LogicEventBus.getInstance().register(BatteryLogic.BatteryEvent.class).observeOn(Schedulers.computation());
    }

    private void initItems(UiAB var1, int var2) {
        Resources var3 = this.getResources();
        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.OVERVIEW)) {
            this.overallItem = UiCC.a(R.drawable.ic_checklist_overall, var3.getString(R.string.overall_status), "Unknown", -7829368);
            var1.a(this.overallItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.FLIGHT_MODE)) {
            this.flightItem = UiCC.a(R.drawable.ic_topbar_flight_mode, var3.getString(R.string.flight_mode), "Unknown", -7829368);
            var1.a(this.flightItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.COMPASS)) {
            this.compassItem = UiCC.a(R.drawable.ic_checklist_compass, var3.getString(R.string.compass), "Unknown", -7829368);
            this.compassItem.c(var3.getString(R.string.compass_calibrate));
            var1.a(this.compassItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.IMU_STATUS)) {
            this.imuItem = UiCC.a(R.drawable.ic_checklist_imu, var3.getString(R.string.imu), "Unknown", -7829368);
            var1.a(this.imuItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.ESC_STATUS)) {
            this.escItem = UiCC.a(R.drawable.ic_checklist_esc, var3.getString(R.string.esc_status), "Unknown", -7829368);
            var1.a(this.escItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.VISION_SENSORS)) {
            this.visionItem = UiCC.a(R.drawable.visual_enable, var3.getString(R.string.vision_status), "Unknown", -7829368);
            var1.a(this.visionItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.RADIO_CHANNEL_QUALITY)) {
            this.radioItem = UiCC.a(R.drawable.ic_checklist_radio_channel_quality, var3.getString(R.string.radio_quality), "Unknown", -7829368);
            var1.a(this.radioItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.REMOTE_CONTROLLER_MODE)) {
            this.remoteModeItem = UiCC.a(R.drawable.ic_camera_setting_remote, var3.getString(R.string.remote_controller_mode), "Unknown", -7829368);
            var1.a(this.remoteModeItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.AIRCRAFT_BATTERY)) {
            this.airBatteryItem = UiCC.a(R.drawable.ic_camera_setting_battery, var3.getString(R.string.aircraft_battery), "Unknown", -7829368);
            var1.a(this.airBatteryItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.REMOTE_CONTROLLER_BATTERY)) {
            this.rcBatteryItem = UiCC.a(R.drawable.ic_checklist_controler_battery, var3.getString(R.string.remote_controller_battery), "Unknown", -7829368);
            var1.a(this.rcBatteryItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.AIRCRAFT_BATTERY_TEMPERATURE)) {
            this.airBatteryTemp = UiCC.a(R.drawable.ic_checklist_aircraft_battery_temperature, var3.getString(R.string.aircraft_battery_temperature), "Unknown", -7829368);
            var1.a(this.airBatteryTemp);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.REMAINING_STORAGE_CAPACITY)) {
            this.sdCardItem = UiCC.a(R.drawable.ic_checklist_sdcard, var3.getString(R.string.sd_card_capacity), "Unknown", -7829368);
            this.sdCardItem.c(var3.getString(R.string.sd_card_format));
            var1.a(this.sdCardItem);
        }

        if(!PreFlightCheckListPanel.ExcludeItem.isItemExcluded(var2, PreFlightCheckListPanel.ExcludeItem.GIMBAL_STATUS)) {
            this.gimbalItem = UiCC.a(R.drawable.ic_checklist_gimbal, var3.getString(R.string.preflight_checklist_gimbal_status), "Unknown", -7829368);
            var1.a(this.gimbalItem);
        }

    }

    private void registerEvent() {
        if(this.isEventRegistered.compareAndSet(false, true)) {
            final Resources var1 = this.getResources();
            LogicManager.getInstance().startFPVTipLogic();
            Subscription var2 = this.fpvTipObservable.subscribe(new Action1<FPVTipLogic.FPVTipEvent>() {
                @Override
                public void call(FPVTipLogic.FPVTipEvent fpvTipEvent) {
                    Message var2 = fpvTipEvent.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.overallItem, var2, var1);
                }
            });

            this.subscriptions.add(var2);
            LogicManager.getInstance().startGimbalLogic();
            Subscription var3 = this.gimbalObservable.subscribe(new Action1<GimbalLogic.GimbalEvent>() {
                @Override
                public void call(GimbalLogic.GimbalEvent var1x) {
                    Message var2 = var1x.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.gimbalItem, var2, var1);
                }
            });
            this.subscriptions.add(var3);
            LogicManager.getInstance().startRadioChannelQualityLogic();
            Subscription var4 = this.radioObservable.subscribe(new Action1<RadioChannelQualityLogic.RadioChannelQualityEvent>() {
                @Override
                public void call(RadioChannelQualityLogic.RadioChannelQualityEvent var1x) {
                    Message var2 = var1x.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.radioItem, var2, var1);
                }
            });
            this.subscriptions.add(var4);
            LogicManager.getInstance().startIMULogic();
            Subscription var5 = this.imuObservable.subscribe(new Action1<IMULogic.IMUEvent>() {
                @Override
                public void call(IMULogic.IMUEvent var1x) {
                    Message var2 = var1x.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.imuItem, var2, var1);
                }
            });
            this.subscriptions.add(var5);
            LogicManager.getInstance().startESCLogic();
            Subscription var6 = this.escObservable.subscribe(new Action1<ESCLogic.ESCEvent>() {
                @Override
                public void call(ESCLogic.ESCEvent var1x) {
                    Message var2 = var1x.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.escItem, var2, var1);
                }
            });
            this.subscriptions.add(var6);
            LogicManager.getInstance().startVisionLogic();
            Subscription var7 = this.visionObservable.subscribe(new Action1<VisionLogic.VisionEvent>() {
                @Override
                public void call(VisionLogic.VisionEvent var1x) {
                    Message var2 = var1x.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.visionItem, var2, var1);
                }
            });
            this.subscriptions.add(var7);
            LogicManager.getInstance().startCompassLogic();
            Subscription var8 = this.compassObservable.subscribe(new Action1<CompassLogic.CompassEvent>() {
                @Override
                public void call(CompassLogic.CompassEvent var1x) {
                    Message var2 = var1x.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.compassItem, var2, var1);
                }
            });
            this.subscriptions.add(var8);
            LogicManager.getInstance().startBatteryLogic();
            Subscription var9 = this.batteryObservable.subscribe(new Action1<BatteryLogic.BatteryEvent>() {
                @Override
                public void call(BatteryLogic.BatteryEvent var1x) {
                    Message var2 = var1x.getMessage();
                    PreFlightCheckListPanel.this.updateListItemUsingLogicMessage(PreFlightCheckListPanel.this.airBatteryItem, var2, var1);
                }
            });
            this.subscriptions.add(var9);
        }

    }

    private void unregisterEvent() {
        if(this.isEventRegistered.compareAndSet(true, false) && !this.subscriptions.isUnsubscribed()) {
            this.subscriptions.unsubscribe();
        }

    }

    private void updateListItemUsingLogicMessage(UiCC var1, Message var2, Resources var3) {
        if(var2 != null && !var1.d().equals(var2.getTitle())) {
            var1.b(var2.getTitle());
            switch(var1.c()) {
                case 1:
                    var1.a(var3.getColor(R.color.red));
                    break;
                case 2:
                    var1.a(var3.getColor(R.color.yellow));
                    break;
                case 3:
                    var1.a(-7829368);
                    break;
                default:
                    var1.a(-16711936);
            }

            final int var4 = this.adapter.b(var1);
            this.post(new Runnable() {
                public void run() {
                    PreFlightCheckListPanel.this.adapter.notifyItemChanged(var4);
                }
            });
        }

    }

    public void initKey() {
        this.flightModeStringKey = FlightControllerKey.create("FlightModeString");
        this.addDependentKey(this.flightModeStringKey);
        this.remoteControllerModeKey = RemoteControllerKey.create("AircraftMappingStyle");
        this.addDependentKey(this.remoteControllerModeKey);
        this.remoteControllerBatteryKey = RemoteControllerKey.create("ChargeRemaining");
        this.addDependentKey(this.remoteControllerBatteryKey);
        this.aircraftBatteryTempKey = BatteryKey.create("Temperature");
        this.addDependentKey(this.aircraftBatteryTempKey);
        this.sdRemainingSpaceKey = CameraKey.create("SDCardRemainingSpaceInMB");
        this.addDependentKey(this.sdRemainingSpaceKey);
        this.sdUSBConnectedKey = CameraKey.create("SDCardIsConnectedToPC");
        this.addDependentKey(this.sdUSBConnectedKey);
    }

    private String getRemoteControllerModeName(@NonNull AircraftMappingStyle var1) {
        String var2 = "Unknown";
        if(var1.value() == AircraftMappingStyle.STYLE_1.value()) {
            var2 = "Mode 1";
        } else if(var1.value() == AircraftMappingStyle.STYLE_2.value()) {
            var2 = "Mode 2";
        } else if(var1.value() == AircraftMappingStyle.STYLE_3.value()) {
            var2 = "Mode 3";
        } else if(var1.value() == AircraftMappingStyle.STYLE_CUSTOM.value()) {
            var2 = "Custom";
        }

        return var2;
    }

    public void transformValue(Object var1, DJIKey var2) {
        UiCC var3 = null;
        String var4 = null;
        int var5 = -16711936;
        if(var2.equals(this.flightModeStringKey)) {
            var3 = this.flightItem;
            var4 = (String)var1;
        } else if(var2.equals(this.remoteControllerModeKey)) {
            var3 = this.remoteModeItem;
            var4 = this.getRemoteControllerModeName((AircraftMappingStyle)var1);
        } else if(var2.equals(this.remoteControllerBatteryKey)) {
            var3 = this.rcBatteryItem;
            ChargeRemaining var6 = (ChargeRemaining)var1;
            var4 = var6.getRemainingChargeInPercent() + "%";
        } else if(var2.equals(this.aircraftBatteryTempKey)) {
            var3 = this.airBatteryTemp;
            var4 = String.format(Locale.getDefault(), "%.1f", new Object[]{Float.valueOf(((Float)var1).floatValue())}) + "° C";
        } else if(var2.equals(this.sdRemainingSpaceKey)) {
            var3 = this.sdCardItem;
            this.sdRemainingSpace = ((Integer)var1).intValue();
            var4 = this.updateSDCardItemStr();
            if(this.sdRemainingSpace == 0) {
                var5 = -65536;
            }
        } else if(var2.equals(this.sdUSBConnectedKey)) {
            var3 = this.sdCardItem;
            this.isUSBConnected = ((Boolean)var1).booleanValue();
            var4 = this.updateSDCardItemStr();
        }

        if(var3 != null) {
            if(var4 == null || var4.isEmpty()) {
                var4 = "Unknown";
                var5 = -7829368;
            }

            var3.b(var4);
            var3.a(var5);
        }

        this.itemToModify = var3;
    }

    private String updateSDCardItemStr() {
        String var1;
        if(this.sdRemainingSpace == 0 && this.isUSBConnected) {
            var1 = this.context.getString(R.string.sd_card_usb_connected);
        } else {
            var1 = this.sdRemainingSpace + " MB";
        }

        return var1;
    }

    public void updateWidget(DJIKey var1) {
        if(this.itemToModify != null) {
            this.adapter.notifyItemChanged(this.adapter.b(this.itemToModify));
        }

    }

    public void updateSelectedItem(View var1, int var2) {
        if(this.adapter.c(var2) == this.sdCardItem) {
            this.formatSDCard();
        } else if(this.adapter.c(var2) == this.compassItem) {
            this.calibrateCompass();
        }

    }

    private void formatSDCard() {
        DInterNal.a var1 = new DInterNal.a() {
            public void onRightBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
                UiDC.a(PreFlightCheckListPanel.this.getContext());
            }

            public void onLeftBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
            }

            public void onCbChecked(DialogInterface var1, boolean var2, int var3) {
            }
        };
        UiDL.a(this.getContext(), this.getContext().getString(R.string.camera_setting_format_sdcard_confirm), var1);
    }

    private void calibrateCompass() {
        DInterNal.a var1 = new DInterNal.a() {
            public void onRightBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
                CompassCalibratingWorkFlow.startCalibration((ActionCallback)null);
                PreFlightCheckListPanel.this.setVisibility(GONE);
            }

            public void onLeftBtnClick(DialogInterface var1, int var2) {
                var1.dismiss();
            }

            public void onCbChecked(DialogInterface var1, boolean var2, int var3) {
            }
        };
        UiDL.a(this.getContext(), this.getContext().getString(R.string.compass_calibration_confirm), var1);
    }

    protected void onWindowVisibilityChanged(int var1) {
        super.onWindowVisibilityChanged(var1);
        switch(var1) {
            case 0:
                this.registerEvent();
                break;
            case 4:
            case 8:
                this.unregisterEvent();
        }

    }

    private static enum ExcludeItem {
        NONE(0),
        OVERVIEW(1),
        FLIGHT_MODE(2),
        COMPASS(4),
        IMU_STATUS(8),
        ESC_STATUS(16),
        VISION_SENSORS(32),
        RADIO_CHANNEL_QUALITY(64),
        REMOTE_CONTROLLER_MODE(128),
        AIRCRAFT_BATTERY(256),
        REMOTE_CONTROLLER_BATTERY(512),
        AIRCRAFT_BATTERY_TEMPERATURE(1024),
        REMAINING_STORAGE_CAPACITY(2048),
        GIMBAL_STATUS(4096);

        private final int value;

        public int value() {
            return this.value;
        }

        private ExcludeItem(int var3) {
            this.value = var3;
        }

        static boolean isItemExcluded(int var0, PreFlightCheckListPanel.ExcludeItem var1) {
            return (var0 & var1.value()) == var1.value;
        }
    }
}
