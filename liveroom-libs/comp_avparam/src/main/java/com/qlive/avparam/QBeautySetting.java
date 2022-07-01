package com.qlive.avparam;

public class QBeautySetting {
    public static final String TAG = "QNBeautySetting";
    private boolean mEnabled = true;
    /**
     * 磨皮
     */
    private float mSmooth;
    /**
     * 美白
     */
    private float mWhiten;
    /**
     * 红润
     */
    private float mRedden;

    public QBeautySetting(float smooth, float whiten, float redden) {
        this.mSmooth = smooth;
        this.mRedden = redden;
        this.mWhiten = whiten;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnable(boolean enable) {
        this.mEnabled = enable;
    }

    public float getSmoothLevel() {
        return this.mSmooth;
    }

    public void setSmoothLevel(float smoothLevel) {
        this.mSmooth = smoothLevel;
    }

    public float getWhiten() {
        return this.mWhiten;
    }

    public void setWhiten(float whiten) {
        this.mWhiten = whiten;
    }

    public float getRedden() {
        return this.mRedden;
    }

    public void setRedden(float redden) {
        this.mRedden = redden;
    }
}
