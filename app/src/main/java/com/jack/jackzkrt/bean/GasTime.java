package com.jack.jackzkrt.bean;

/**
 * Created by xcong on 2017/9/30.
 *
 * @des ${TODO}
 */

public class GasTime {
    private String dateTime;
    private int gasValue;

    public GasTime(String dateTime, int gasValue) {
        this.dateTime = dateTime;
        this.gasValue = gasValue;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getGasValue() {
        return gasValue;
    }

    public void setGasValue(int gasValue) {
        this.gasValue = gasValue;
    }
}
