package sensetime.senseme.com.effects.utils;

import android.annotation.SuppressLint;

import sensetime.senseme.com.effects.utils.LogUtils;

import sensetime.senseme.com.effects.view.widget.EffectType;

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 4/25/21 5:32 PM
 */
public class EffectInfoDataHelper implements IEffectInfoDataHelper {
    private static final String TAG = "EffectInfoDataHelper";

    public enum Type {
        IMG,
        CAMERA
    }

    private EffectInfoDataHelper() {
    }

    private static Type mType = Type.CAMERA;

    public static void setType(Type type) {
        mType = type;
//        if (AtyStateContext.getInstance().getState() instanceof TryOnCameraAtyState) {
//            mType = Type.IMG;
//        }
//        LogUtils.i("setType() called with: type = [" + mType.name() + "]" + AtyStateContext.getInstance().getState());
    }

    public static EffectInfoDataHelper getInstance() {
        if (mType == Type.IMG) {
            return EffectInfoDataHolder.instanceImg;
        }
        return EffectInfoDataHolder.instanceCamera;
    }

    private static class EffectInfoDataHolder {
        @SuppressLint("StaticFieldLeak")
        private static final EffectInfoDataHelper instanceImg = new EffectInfoDataHelper();
        @SuppressLint("StaticFieldLeak")
        private static final EffectInfoDataHelper instanceCamera = new EffectInfoDataHelper();
    }

    /*///////////////////////////////////////////////////////*/
    @Override
    public void clear() {
        if (mType == Type.CAMERA) {
            return;
        }
        LogUtils.iTag(TAG, "clear() called" + mType.name());
        // 删除基础美颜参数
        for (EffectType type : EffectType.Companion.getAllBasicType()) {
            SpUtils.removeData(type, mType.name() + SP_STRENGTH);
        }

        // makeup
        for (EffectType type : EffectType.Companion.getMakeupList()) {
            SpUtils.removeData(type, mType.name());
            SpUtils.removeData(type, mType.name() + SP_STRENGTH);
        }

        // filter
        for (EffectType type : EffectType.Companion.getFilterList()) {
            SpUtils.removeData(type, mType.name());
            SpUtils.removeData(type, mType.name() + SP_STRENGTH);
        }

        // style
        SpUtils.removeData(EffectType.GROUP_STYLE, mType.name() + SP_STRENGTH_STYLE_FILTER);
        SpUtils.removeData(EffectType.GROUP_STYLE, mType.name() + SP_STRENGTH_STYLE_MAKEUP);
        for (EffectType type : EffectType.Companion.getStyleList()) {
            SpUtils.removeData(type, mType.name());
            SpUtils.removeData(type, mType.name() + SP_STRENGTH);
        }

    }

    public void setStyleHigh(boolean high) {
        LogUtils.iTag(TAG, "setStyleHigh() called with: high = [" + high + "]");
        SpUtils.setParam(SP_STYLE_IS_HIGH + mType.name(), high);
        if (high) {
            SpUtils.setParam(SP_STYLE_IS_HIGH_STAMP + mType.name(), System.currentTimeMillis());
        }
    }

    public boolean getStyleHigh() {
        boolean param = (boolean) SpUtils.getParam(SP_STYLE_IS_HIGH + mType.name(), true);
        LogUtils.iTag(TAG, "getStyleHigh() called" + mType.name() + param);
        return param;
    }

    public boolean getMakeupHigh() {
        boolean param = (boolean) SpUtils.getParam(SP_MAKEUP_IS_HIGH + mType.name(), true);
        LogUtils.iTag(TAG, "getStyleHigh() called" + mType.name() + param);
        return param;
    }

    public void setWhiteningType(int type) {
        SpUtils.setParam(SP_WHITENING_TYPE + mType.name(), type);
    }

    public int getWhiteningType() {
        return (int) SpUtils.getParam(SP_WHITENING_TYPE + mType.name(), 0);
    }

    public void setMakeupHigh(boolean high) {
        LogUtils.iTag(TAG, "setMakeupHigh() called with: high = [" + high + "]");
        SpUtils.setParam(SP_MAKEUP_IS_HIGH + mType.name(), high);
        if (high) {
            SpUtils.setParam(SP_MAKEUP_IS_HIGH_STAMP + mType.name(), System.currentTimeMillis());
        }
    }

    public boolean getFilterHigh() {
        boolean param = (boolean) SpUtils.getParam(SP_FILTER_IS_HIGH + mType.name(), true);
        LogUtils.iTag(TAG, "getFilterHigh() called" + mType.name() + param);
        return param;
    }

    public void setFilterHigh(boolean high) {
        LogUtils.iTag(TAG, "setFilterHigh() called with: high = [" + high + "]");
        SpUtils.setParam(SP_FILTER_IS_HIGH + mType.name(), high);
        if (high) {
            SpUtils.setParam(SP_FILTER_IS_HIGH_STAMP + mType.name(), System.currentTimeMillis());
        }
    }

    public long getStyleStamp() {
        return SpUtils.getLong(SP_STYLE_IS_HIGH_STAMP + mType.name(), 0L);
    }

    public long getMakeupStamp() {
        return SpUtils.getLong(SP_MAKEUP_IS_HIGH_STAMP + mType.name(), 0L);
    }

    public long getFilterStamp() {
        return SpUtils.getLong(SP_FILTER_IS_HIGH_STAMP + mType.name(), 0L);
    }

    public float getStrength(EffectType type) {
        return (float) SpUtils.getParam(type, mType.name() + SP_STRENGTH, -2f);
    }

    public void setStrength(EffectType type, float strength) {
        LogUtils.iTag(TAG, "setStrength() called with: type = [" + type.getDesc() + "], strength = [" + strength + "]" + mType.name());
        SpUtils.setParam(type, mType.name() + SP_STRENGTH, strength);
    }

    public void setContentSelectedIndex(EffectType type, int index) {
        LogUtils.iTag(TAG, "setContentSelectedIndex() called with: type = [" + type.getDesc() + "], index = [" + index + "]");
        SpUtils.setParam(type, mType.name(), index);
    }

    public int getContentSelectedIndex(EffectType type) {
        int param = (int) SpUtils.getParam(type, mType.name(), -2);
        LogUtils.iTag(TAG, "getContentSelectedIndex() called with: type = [" + type.getDesc() + "]" + ",index:" + param);
        return param;
    }

    public float getStrengthStyleFilter() {
        float defaultStrength = Constants.DEF_STYLE_STRENGTH;
        if (ContextHolder.getGender() == 0) {
            defaultStrength = Constants.DEF_STYLE_GIRL_STRENGTH;
        }
        if (ContextHolder.getGender() == 1) {
            defaultStrength = Constants.DEF_STYLE_BOY_STRENGTH;
        }
        return (float) SpUtils.getParam(EffectType.GROUP_STYLE, mType.name() + SP_STRENGTH_STYLE_FILTER, defaultStrength);
    }

    public float getStrengthStyleMakeup() {
        float defaultStrength = Constants.DEF_STYLE_STRENGTH;
        if (ContextHolder.getGender() == 0) {
            defaultStrength = Constants.DEF_STYLE_GIRL_STRENGTH;
        }
        if (ContextHolder.getGender() == 1) {
            defaultStrength = Constants.DEF_STYLE_BOY_STRENGTH;
        }
        return (float) SpUtils.getParam(EffectType.GROUP_STYLE, mType.name() + SP_STRENGTH_STYLE_MAKEUP, defaultStrength);
    }

    public void setStrengthStyleFilter(float strength) {
        LogUtils.iTag(TAG, "setStrengthStyleFilter() called with: type = [" + EffectType.GROUP_STYLE.getDesc() + "], strength = [" + strength + "]" + mType.name());
        SpUtils.setParam(EffectType.GROUP_STYLE, mType.name() + SP_STRENGTH_STYLE_FILTER, strength);
    }

    public void setStrengthStyleMakeup(float strength) {
        LogUtils.iTag(TAG, "setStrengthStyleMakeup() called with: type = [" + EffectType.GROUP_STYLE.getDesc() + "], strength = [" + strength + "]" + mType.name());
        SpUtils.setParam(EffectType.GROUP_STYLE, mType.name() + SP_STRENGTH_STYLE_MAKEUP, strength);
    }

    /*///////////////////////////////////////////////////////*/

    private final static String SP_STRENGTH = "_strength";
    private final static String SP_STRENGTH_STYLE_FILTER = "_strength_style_filter";
    private final static String SP_STRENGTH_STYLE_MAKEUP = "_strength_style_makeup";

    private final static String SP_STYLE_IS_HIGH = "sp_style_is_high";
    private final static String SP_MAKEUP_IS_HIGH = "sp_makeup_is_high";
    private final static String SP_FILTER_IS_HIGH = "sp_filter_is_high";
    private final static String SP_WHITENING_TYPE = "sp_whitening_type";

    public static String SP_FILTER_IS_HIGH_STAMP = "sp_filter_is_high_stamp";
    public static String SP_STYLE_IS_HIGH_STAMP = "sp_style_is_high_stamp";
    public static String SP_MAKEUP_IS_HIGH_STAMP = "sp_makeup_stamp";

}
