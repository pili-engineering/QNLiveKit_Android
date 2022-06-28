package com.qlive.uiwidghtbeauty;

import static com.qlive.uiwidghtbeauty.utils.Constants.LICENSE_FILE;

import android.content.Context;
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

    public static void initEffect(Context appContext, String appId, String appKey) {
        sAppContext = appContext;
        sSenseTimePlugin = new QNSenseTimePlugin.Builder(appContext)
                .setLicenseAssetPath(LICENSE_FILE)
                .setModelActionAssetPath("M_SenseME_Face_Video_5.3.3.model")
                .setCatFaceModelAssetPath("M_SenseME_CatFace_3.0.0.model")
                .setDogFaceModelAssetPath("M_SenseME_DogFace_2.0.0.model")
                .build();

        boolean isAuthorized = sSenseTimePlugin.checkLicense();
        if (!isAuthorized) {
            Toast.makeText(appContext, "鉴权失败，请检查授权文件", Toast.LENGTH_SHORT).show();
        }
        SenseArMaterialService.shareInstance().authorizeWithAppId(appContext, appId, appKey, new SenseArMaterialService.OnAuthorizedListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(SenseArMaterialService.AuthorizeErrorCode errorCode, String errorMsg) {
                ToastUtils.showShortToast(appContext, String.format(Locale.getDefault(), "鉴权失败！%d, %s", errorCode, errorMsg));
            }
        });
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
//        sSenseTimePlugin.addSubModelFromAssetsFile("M_SenseME_Face_Extra_5.23.0.model");
//        sSenseTimePlugin.addSubModelFromAssetsFile("M_SenseME_Iris_2.0.0.model");
//        sSenseTimePlugin.addSubModelFromAssetsFile("M_SenseME_Hand_5.4.0.model");
//        sSenseTimePlugin.addSubModelFromAssetsFile("M_SenseME_Segment_4.10.8.model");
//        sSenseTimePlugin.addSubModelFromAssetsFile("M_SenseAR_Segment_MouthOcclusion_FastV1_1.1.1.model");
        sSenseTimePlugin.recoverEffects();
    }

    public static void destroySenseTime() {
        sSenseTimePlugin.destroy();
    }

}
