package com.qlive.uiwidghtbeauty;

import static com.qlive.uiwidghtbeauty.utils.Constants.LICENSE_FILE;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.sensetimeplugin.QNSenseTimePlugin;
import com.qlive.uiwidghtbeauty.utils.SharedPreferencesUtils;
import com.qlive.uiwidghtbeauty.utils.ToastUtils;
import com.sensetime.sensearsourcemanager.SenseArMaterialService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QSenseTimeManager {

    public static QNSenseTimePlugin sSenseTimePlugin = null;
    private static final String DST_FOLDER = "resource";
    protected static Context sAppContext;

    public static void initEffectFromLocalLicense(Context appContext) {
        sAppContext = appContext;
        sSenseTimePlugin = new QNSenseTimePlugin.Builder(appContext)
                .setLicenseAssetPath(LICENSE_FILE)
                .setModelActionAssetPath("M_SenseME_Face_Video_5.3.3.model")
                .setCatFaceModelAssetPath("M_SenseME_CatFace_3.0.0.model")
                .setDogFaceModelAssetPath("M_SenseME_DogFace_2.0.0.model")
                // 关闭在线拉取授权文件，使用离线授权文件
                .setOnlineLicense(false)
                // 关闭在线激活授权，使用离线激活授权
                .setOnlineActivate(false)
                .build();

        boolean isAuthorized = sSenseTimePlugin.checkLicense();
        if (!isAuthorized) {
            Toast.makeText(appContext, "鉴权失败，请检查授权文件", Toast.LENGTH_SHORT).show();
        }
        Log.d("QSenseTimeManager", "authorizeWithAppId" + "鉴权onSuccess ");
    }

    public static void checkLoadResourcesTask(Context context, int resVersion, LoadResourcesTask.ILoadResourcesCallback callback) {
        SharedPreferencesUtils.resVersion = resVersion;
        if (!SharedPreferencesUtils.resourceReady(context)) {
            if (callback != null) {
                callback.onStartTask();
                callback.onEndTask(true);
            }
        } else {
            LoadResourcesTask mTask = new LoadResourcesTask(callback);
            mTask.execute(DST_FOLDER);
        }
    }

    public static List<String> sSubModelFromAssetsFile = new ArrayList<String>();

    public static void initSenseTime() {
        sSenseTimePlugin.init();
        for (String sub : sSubModelFromAssetsFile) {
            sSenseTimePlugin.addSubModelFromAssetsFile(sub);
        }
    }

    public static void addSubModelFromAssetsFile(String subModelFromAssetsFile) {
        sSubModelFromAssetsFile.add(subModelFromAssetsFile);
        sSenseTimePlugin.recoverEffects();
    }

    public static void destroySenseTime() {
        sSenseTimePlugin.destroy();
    }

}
