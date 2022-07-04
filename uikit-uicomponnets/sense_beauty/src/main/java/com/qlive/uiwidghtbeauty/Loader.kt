package com.qlive.uiwidghtbeauty

import android.content.Context
import android.view.View
import android.widget.Toast
import com.sensetime.sensearsourcemanager.SenseArMaterialService
import com.sensetime.sensearsourcemanager.SenseArServerType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.utils.*
import sensetime.senseme.com.effects.view.widget.EffectType
import java.io.File

class Loader {

    fun loader(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val ret = async(Dispatchers.IO) {
                copyAssetsFiles(context)
            }
            ret.await()
            val list = StyleDataNewUtils.getStyleMap(EffectType.styleList)
            ///val makeUpList = StyleDataUtils.getMakeUpMap(EffectType.makeupList)
            val makeUpList = StyleDataNewUtils.getMakeUpMap(EffectType.makeupList)
            ContextHolder.setStyleMap(list)
            ContextHolder.setMakeUpMap(makeUpList)

        }

    }

    private suspend fun copyAssetsFiles(context: Context) = withContext(Dispatchers.IO) {
        //正式
        SenseArMaterialService.setServerType(SenseArServerType.DomesticServer)
        FileUtils.copyStickerFiles(ContextHolder.getContext(), Constants.STICKER_LOCAL)
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_eyeshadow")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_brow")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_blush")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_highlight")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_lip")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_eyeliner")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_eyelash")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_eyeball")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "makeup_hairdye")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "style_nature")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "style_lightly")
        FileUtils.copyStickerFiles(ContextHolder.getContext(), "style_fashion")

        // 拷贝TryOn本地素材
        val dataDir =
            context.getExternalFilesDir(null)?.absolutePath + File.separator + Constants.TRY_ON_ASSET_PATH
        FileUtils.copyFilesFromAssets(
            ContextHolder.getContext(),
            Constants.TRY_ON_ASSET_PATH,
            dataDir
        )

        FileUtils.copyModelsFiles(ContextHolder.getContext(), "models")

        LocalDataStore.getInstance().prepareAsync()
    }

}