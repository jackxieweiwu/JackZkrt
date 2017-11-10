package zkrt.ui.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;
import org.jetbrains.annotations.NotNull;

import dji.common.error.DJIError;
import dji.common.product.Model;
import dji.common.util.CommonCallbacks;
import dji.keysdk.AirLinkKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.ProductKey;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.sdk.airlink.AirLink;
import dji.sdk.airlink.LightbridgeLink;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.sdkmanager.DJISDKManager;
import zkrt.ui.R;
import zkrt.ui.base.UiBaseIView;
import zkrt.ui.d.UiDA;
import zkrt.ui.d.UiDL;

/**
 * Created by jack_xie on 17-6-1.
 */

public class FPVWidget extends UiBaseIView implements TextureView.SurfaceTextureListener {
    private Model modeName;
    private static final String TAG = "DULFpvWidget";
    protected DJICodecManager codecManager;
    private int videoWidth;
    private int videoHeight;
    private VideoSource videoSource;
    private ProductKey modeNameKey;
    private boolean isPrimaryVideoFeed;
    private VideoFeeder.VideoDataCallback videoDataCallback;
    private VideoFeeder.VideoFeed currentVideoFeed;


    public FPVWidget(Context var1) {
        this(var1,  null, 0);
    }

    public FPVWidget(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public FPVWidget(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.codecManager = null;

    }

    public void setVideoSource(VideoSource var1) {
        this.videoSource = var1;
        this.updateVideoFeed();
    }

    public VideoSource getVideoSource() {
        return this.videoSource;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
        if(this.codecManager == null) {
            this.codecManager = new DJICodecManager(this.getContext(), var1, var2, var3, this.isPrimaryVideoFeed? UsbAccessoryService.VideoStreamSource.Camera: UsbAccessoryService.VideoStreamSource.Fpv);
        }

    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
        if(this.codecManager != null) {
            this.codecManager.onSurfaceSizeChanged(var2, var3, 0);
        }

    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
        if(this.codecManager != null) {
            this.codecManager.cleanSurface();
            this.codecManager.destroyCodec();
            this.codecManager = null;
        }

        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture var1) {
        if(this.videoHeight != this.codecManager.getVideoHeight().intValue() || this.videoWidth != this.codecManager.getVideoWidth().intValue()) {
            this.videoWidth = this.codecManager.getVideoWidth().intValue();
            this.videoHeight = this.codecManager.getVideoHeight().intValue();
            this.adjustAspectRatio(this.videoWidth, this.videoHeight);
        }

    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        if(!this.isInEditMode()) {
            this.videoSource = FPVWidget.VideoSource.find(UiDL.a(var1, var2, R.styleable.FPVWidget, R.styleable.FPVWidget_videoSource, FPVWidget.VideoSource.AUTO.value));
            this.isPrimaryVideoFeed = true;
            if(this.videoSource == FPVWidget.VideoSource.SECONDARY) {
                this.isPrimaryVideoFeed = false;
            }

            this.videoDataCallback = new VideoFeeder.VideoDataCallback() {
                public void onReceive(byte[] var1, int var2) {
                    if(FPVWidget.this.codecManager != null) {
                        FPVWidget.this.codecManager.sendDataToDecoder(var1, var2, FPVWidget.this.isPrimaryVideoFeed? UsbAccessoryService.VideoStreamSource.Camera.getIndex(): UsbAccessoryService.VideoStreamSource.Fpv.getIndex());
                    }

                }
            };
            this.setSurfaceTextureListener(this);
            UiDA.b();
        }
    }

    public void initKey() {
        this.modeNameKey = ProductKey.create("ModelName");
        this.addDependentKey(this.modeNameKey);
    }

    private boolean isExtPortSupportedProduct(Model var1) {
        return var1 == Model.MATRICE_600 || var1 == Model.MATRICE_600_PRO || var1 == Model.A3 || var1 == Model.N3;
    }

    public void transformValue(Object var1, DJIKey var2) {
        if(var2.equals(this.modeNameKey)) {
            this.modeName = (Model)var1;
            this.updateVideoFeed();
        }

    }

    private void updateVideoFeed() {
        if(VideoFeeder.getInstance() != null) {
            if(this.videoSource == FPVWidget.VideoSource.AUTO) {
                if(this.modeName != null) {
                    if(this.isExtPortSupportedProduct(this.modeName)) {
                        this.registerLiveVideo(VideoFeeder.getInstance().getSecondaryVideoFeed(), false);
                        this.autoSwitchToDJICameraChannel();
                    } else {
                        this.registerLiveVideo(VideoFeeder.getInstance().getPrimaryVideoFeed(), true);
                    }
                }
            } else if(this.videoSource == FPVWidget.VideoSource.PRIMARY) {
                this.registerLiveVideo(VideoFeeder.getInstance().getPrimaryVideoFeed(), true);
            } else if(this.videoSource == FPVWidget.VideoSource.SECONDARY) {
                this.registerLiveVideo(VideoFeeder.getInstance().getSecondaryVideoFeed(), false);
            }

        }
    }

    private void autoSwitchToDJICameraChannel() {
        BaseProduct var1 = DJISDKManager.getInstance().getProduct();
        AirLink var2;
        final LightbridgeLink var3;
        if(var1 != null && (var2 = var1.getAirLink()) != null && (var3 = var2.getLightbridgeLink()) != null) {
            this.enableEXTVideoInputPort(var3, true, new CommonCallbacks.CompletionCallback() {
                public void onResult(DJIError var1) {
                    if(var1 == null) {
                        FPVWidget.this.setDJICameraChannel(var3);
                    }

                }
            });
        }
    }

    public void registerLiveVideo(VideoFeeder.VideoFeed var1, boolean var2) {
        this.isPrimaryVideoFeed = var2;
        if(this.videoDataCallback != null && var1 != null) {
            if(this.currentVideoFeed != null && this.currentVideoFeed.getCallback() == this.videoDataCallback) {
                this.currentVideoFeed.setCallback(null);
            }

            this.currentVideoFeed = var1;
            this.currentVideoFeed.setCallback(this.videoDataCallback);
        }

        if(this.codecManager != null) {
            this.codecManager.switchSource(this.isPrimaryVideoFeed? UsbAccessoryService.VideoStreamSource.Camera: UsbAccessoryService.VideoStreamSource.Fpv);
        }

    }

    private void enableEXTVideoInputPort(@NotNull LightbridgeLink var1, boolean var2, CommonCallbacks.CompletionCallback var3) {
        Boolean var4 = (Boolean) KeyManager.getInstance().getValue(AirLinkKey.createLightbridgeLinkKey("isEXTVideoInputPortEnabled"));
        if(var4 != null && var4.booleanValue() == var2) {
            var3.onResult(null);
        } else {
            var1.setEXTVideoInputPortEnabled(var2, var3);
        }

    }

    private void setDJICameraChannel(@NotNull LightbridgeLink var1) {
        var1.setBandwidthAllocationForLBVideoInputPort(0.0F, new CommonCallbacks.CompletionCallback() {
            public void onResult(DJIError var1) {
            }
        });
    }

    private void adjustAspectRatio(int var1, int var2) {
        int var3 = this.getWidth();
        int var4 = this.getHeight();
        double var5 = (double)var2 / (double)var1;
        int var7;
        int var8;
        if(var4 > (int)((double)var3 * var5)) {
            var7 = var3;
            var8 = (int)((double)var3 * var5);
        } else {
            var7 = (int)((double)var4 / var5);
            var8 = var4;
        }

        int var9 = (var3 - var7) / 2;
        int var10 = (var4 - var8) / 2;
        Matrix var11 = new Matrix();
        this.getTransform(var11);
        var11.setScale((float)var7 / (float)var3, (float)var8 / (float)var4);
        var11.postTranslate((float)var9, (float)var10);
        this.setTransform(var11);
    }

    public static enum VideoSource {
        AUTO(0),
        PRIMARY(1),
        SECONDARY(2);

        private int value;

        private VideoSource(int var3) {
            this.value = var3;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int var1) {
            return this.value == var1;
        }

        public static FPVWidget.VideoSource find(int var0) {
            FPVWidget.VideoSource var1 = AUTO;

            for(int var2 = 0; var2 < values().length; ++var2) {
                if(values()[var2]._equals(var0)) {
                    var1 = values()[var2];
                    break;
                }
            }

            return var1;
        }
    }
}
