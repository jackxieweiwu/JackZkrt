package zkrt.ui.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import zkrt.ui.R;
import zkrt.ui.base.UiBaseQView;

/**
 * Created by jack_xie on 17-11-6.
 */

public abstract class EInterNal extends UiBaseQView implements View.OnTouchListener {
    private static final int INIT_SCALE = 75;
    private static final float Y_OFFSET_PERCENTAGE = 0.8F;
    private Bitmap background;
    private Bitmap bars;
    private int offsetX;
    private int offsetY;
    private int maxX;
    private int maxY;
    private float startY;
    private float translationY;
    private float cumulativeY;
    private final int slop;
    private boolean isDragAction;
    private int measuredHeight;
    private int measuredWidth;
    private int scale = 75;


    public EInterNal(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.background = BitmapFactory.decodeResource(this.getResources(), R.drawable.mf_control);
        this.bars = BitmapFactory.decodeResource(this.getResources(), R.drawable.slider_bars);
        this.startY = -1.0F;
        this.slop = ViewConfiguration.get(var1).getScaledTouchSlop();
    }

    public void initView(Context var1, AttributeSet var2, int var3) {
        this.setOnTouchListener(this);
        this.setWillNotDraw(false);
    }

    protected void onMeasure(int var1, int var2) {
        this.measuredHeight = MeasureSpec.getSize(var2);
        this.measuredWidth = MeasureSpec.getSize(var1);
        super.onMeasure(var1, var2);
    }

    @SuppressLint({"DrawAllocation"})
    protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        if(var1) {
            int var6 = this.background.getHeight();
            this.background = Bitmap.createScaledBitmap(this.background, this.measuredWidth, this.measuredHeight, true);
            this.bars = Bitmap.createScaledBitmap(this.bars, this.measuredWidth, (int)((float)this.measuredHeight * 0.8F), true);
            this.maxX = this.bars.getWidth();
            this.maxY = this.bars.getHeight();
            this.offsetX = (this.background.getWidth() - this.maxX) / 2;
            this.offsetY = (this.background.getHeight() - this.maxY) / 2;
            this.scale = 75 * var6 / this.background.getHeight();
            this.cumulativeY = (float)(this.maxY / 2);
        }

        super.onLayout(var1, var2, var3, var4, var5);
    }

    protected void onDraw(Canvas var1) {
        var1.save();
        var1.clipRect(this.offsetX, this.offsetY, this.maxX + this.offsetX, this.maxY + this.offsetY);
        var1.drawBitmap(this.bars, (float)this.offsetX, (float)this.offsetY - (float)this.maxY / 2.0F + this.translationY + this.cumulativeY, (Paint)null);
        var1.restore();
        var1.drawBitmap(this.background, 0.0F, 0.0F, (Paint)null);
    }

    public boolean onTouch(View var1, MotionEvent var2) {
        float var3 = var2.getY();
        switch(var2.getAction()) {
            case 0:
                this.startY = var3;
                this.isDragAction = false;
                return true;
            case 1:
                if(!this.isDragAction) {
                    if(this.startY <= (float)this.offsetY) {
                        this.onMinValue();
                        this.invalidate();
                    } else if(this.startY >= (float)(this.offsetY + this.maxY)) {
                        this.onMaxValue();
                        this.invalidate();
                    }
                }

                this.cumulativeY += this.translationY;
                this.translationY = 0.0F;
                this.onValueUpdate(this.cumulativeY / (float)this.maxY);
                return true;
            case 2:
                if(Math.abs(this.startY - var3) > (float)this.slop) {
                    this.translationY = (float)this.scale * (var3 - this.startY) / (float)this.maxY;
                    this.isDragAction = true;
                    if(this.translationY + this.cumulativeY < 0.0F) {
                        this.onMinValue();
                    } else if(this.translationY + this.cumulativeY > (float)this.maxY) {
                        this.onMaxValue();
                    }

                    this.invalidate();
                }

                return true;
            default:
                return false;
        }
    }

    protected void onMinValue() {
        this.translationY = 0.0F;
        this.cumulativeY = 0.0F;
    }

    protected void onMaxValue() {
        this.translationY = 0.0F;
        this.cumulativeY = (float)this.maxY;
    }

    protected void onValueUpdatedExternally(float var1) {
        this.cumulativeY = var1 * (float)this.maxY;
    }

    protected abstract void onValueUpdate(float var1);
}
