package sensetime.senseme.com.effects.utils;

import android.content.Context;

import sensetime.senseme.com.effects.utils.LogUtils;

import java.util.EnumMap;
import java.util.List;

import javax.microedition.khronos.egl.EGLContext;

import sensetime.senseme.com.effects.view.MakeupItem;
import sensetime.senseme.com.effects.view.StickerItem;
import sensetime.senseme.com.effects.view.widget.EffectType;

public class ContextHolder {

    static Context ApplicationContext;

    private static boolean camera2Mode;

    // 1:自然 2:圆脸 3:方脸 4:长脸 5:长形脸
    private static int mFaceType = 1;

    // 性别 0：女 1：男 -1:非第一次打开
    private static int gender = -1;

    private static int mMode = Constants.ATY_TYPE_CAMERA;

    private static EnumMap<EffectType, List<MakeupItem>> makeUpMap;

    private static EnumMap<EffectType, List<StickerItem>> styleMap;

    private static EGLContext mEGLContext;

    private static String stylePath;
    private static String styleMakeUpStrength;
    private static String styleFilterStrength;

    private static String currentBg;

    public static String getStylePath() {
        return stylePath;
    }

    public static void setStylePath(String stylePath) {
        ContextHolder.stylePath = stylePath;
    }

    public static String getStyleMakeUpStrength() {
        return styleMakeUpStrength;
    }

    public static void setStyleMakeUpStrength(String styleMakeUpStrength) {
        ContextHolder.styleMakeUpStrength = styleMakeUpStrength;
    }

    public static String getStyleFilterStrength() {
        return styleFilterStrength;
    }

    public static void setStyleFilterStrength(String styleFilterStrength) {
        ContextHolder.styleFilterStrength = styleFilterStrength;
    }

    public static EGLContext getEGLContext() {
        return mEGLContext;
    }

    public static void setmEGLContext(EGLContext mEGLContext) {
        ContextHolder.mEGLContext = mEGLContext;
    }

    public static EnumMap<EffectType, List<MakeupItem>> getMakeUpMap() {
        return makeUpMap;
    }

    public static void setMakeUpMap(EnumMap<EffectType, List<MakeupItem>> makeUpMap) {
        ContextHolder.makeUpMap = makeUpMap;
    }

    public static EnumMap<EffectType, List<StickerItem>> getStyleMap() {
        return styleMap;
    }

    public static void setStyleMap(EnumMap<EffectType, List<StickerItem>> styleMap) {
        ContextHolder.styleMap = styleMap;
    }

    public static int getFaceType() {
        return mFaceType;
    }

    public static void setFaceType(int faceType) {
        LogUtils.iTag("ContextHolder", "setFaceType() called with: faceType = [" + faceType + "]");
        mFaceType = faceType;
    }

    public static int getmMode() {
        return mMode;
    }

    public static void setmMode(int atyType) {
        ContextHolder.mMode = atyType;
    }

    public static void initial(Context context) {
        ApplicationContext = context;
        camera2Mode = PropertiesUtil.INSTANCE.getBooleanValue(Constants.CFG_K_USECAMERA2);
    }

    public static Context getContext() {
        return ApplicationContext;
    }

    public static String getCurrentBg() {
        return currentBg;
    }

    public static void setCurrentBg(String currentBg) {
        ContextHolder.currentBg = currentBg;
    }

    public static boolean isCamera2Mode() {
        return camera2Mode;
    }

    public static void setCamera2Mode(boolean camera2Mode) {
        ContextHolder.camera2Mode = camera2Mode;
    }

    public static int getGender() {
        return (int) SpUtils.getParam(Constants.SP_FIRST_IDENTIFY, 0);
//        return gender;
    }

    public static void setGender(int gender) {
        ContextHolder.gender = gender;
    }
}
