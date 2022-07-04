package sensetime.senseme.com.effects.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import sensetime.senseme.com.effects.utils.LogUtils
import com.sensetime.sensearsourcemanager.SenseArMaterial
import com.sensetime.sensearsourcemanager.SenseArMaterialService
import com.sensetime.sensearsourcemanager.SenseArMaterialType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.SenseMeApplication
import sensetime.senseme.com.effects.view.MakeupItem
import sensetime.senseme.com.effects.view.StickerItem
import sensetime.senseme.com.effects.view.widget.EffectType
import java.text.Collator
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 7/5/21 7:34 PM
 */
object NetDataUtils {

    const val TAG = "NetDataUtils"

    interface Listener {
        fun onSuccessMakeup(makeUpData: EnumMap<EffectType, List<*>>)
        fun onSuccessSticker(stickerData: EnumMap<EffectType, MutableList<StickerItem>>)
    }

    fun getMakeupList(context: Context, listener: Listener) {
        GlobalScope.launch(Dispatchers.Main) {
            val ret = authorSdk(context)
            if (ret) {
                // 美妆相关
                val timeMillis = measureTimeMillis {
                    val localData: EnumMap<EffectType, MutableList<MakeupItem>> = getMakeupListsLocal2()

                    val makeUpMap: EnumMap<EffectType, List<*>> =
                        EnumMap<EffectType, List<*>>(EffectType::class.java)

                    // 美妆一组
                    val asyncHair = async { getMakeup(context, EffectType.TYPE_HAIR.groupId, localData) }
                    val asyncNet = async { getMakeup(context, EffectType.TYPE_LIP.groupId, localData) }
                    val asyncBlush = async { getMakeup(context, EffectType.TYPE_BLUSH.groupId, localData) }
                    val asyncXR = async { getMakeup(context, EffectType.TYPE_XR.groupId, localData) }
                    val asyncBrow = async { getMakeup(context, EffectType.TYPE_EYE_BROW.groupId, localData) }

                    val asyncShadow = async { getMakeup(context, EffectType.TYPE_EYE_SHADOW.groupId, localData) }
                    val asyncLiner = async { getMakeup(context, EffectType.TYPE_EYE_LINER.groupId, localData) }
                    val asyncLash = async { getMakeup(context, EffectType.TYPE_EYELASH.groupId, localData) }
                    val asyncBall = async { getMakeup(context, EffectType.TYPE_EYEBALL.groupId, localData) }

                    makeUpMap.putAll(asyncHair.await())
                    makeUpMap.putAll(asyncNet.await())
                    makeUpMap.putAll(asyncBlush.await())
                    makeUpMap.putAll(asyncXR.await())
                    makeUpMap.putAll(asyncBrow.await())

                    makeUpMap.putAll(asyncShadow.await())
                    makeUpMap.putAll(asyncLiner.await())
                    makeUpMap.putAll(asyncLash.await())
                    makeUpMap.putAll(asyncBall.await())

                    listener.onSuccessMakeup(makeUpMap)
                }
                LogUtils.iTag(TAG, "download cost time: $timeMillis")
            }
        }
    }

    fun getStickerList(context: Context, listener: Listener) {
        GlobalScope.launch(Dispatchers.Main) {
            val ret = authorSdk(context)
            if (ret) {
                // 美妆相关
                val timeMillis = measureTimeMillis {
                    val localData: EnumMap<EffectType, MutableList<MakeupItem>> = getMakeupListsLocal2()

                    val makeUpMap: EnumMap<EffectType, List<*>> =
                        EnumMap<EffectType, List<*>>(EffectType::class.java)

                    GlobalScope.launch(Dispatchers.IO) {
                        for (item in EffectType.stickerList) {
                            if (!TextUtils.isEmpty(item.groupId)) {
                                getSticker(context, item.groupId, listener)
                            }
                        }
                    }

                    // 美妆一组
                    val asyncHair = async { getMakeup(context, EffectType.TYPE_HAIR.groupId, localData) }
                    val asyncNet = async { getMakeup(context, EffectType.TYPE_LIP.groupId, localData) }
                    val asyncBlush = async { getMakeup(context, EffectType.TYPE_BLUSH.groupId, localData) }
                    val asyncXR = async { getMakeup(context, EffectType.TYPE_XR.groupId, localData) }
                    val asyncBrow = async { getMakeup(context, EffectType.TYPE_EYE_BROW.groupId, localData) }

                    val asyncShadow = async { getMakeup(context, EffectType.TYPE_EYE_SHADOW.groupId, localData) }
                    val asyncLiner = async { getMakeup(context, EffectType.TYPE_EYE_LINER.groupId, localData) }
                    val asyncLash = async { getMakeup(context, EffectType.TYPE_EYELASH.groupId, localData) }
                    val asyncBall = async { getMakeup(context, EffectType.TYPE_EYEBALL.groupId, localData) }

                    makeUpMap.putAll(asyncHair.await())
                    makeUpMap.putAll(asyncNet.await())
                    makeUpMap.putAll(asyncBlush.await())
                    makeUpMap.putAll(asyncXR.await())
                    makeUpMap.putAll(asyncBrow.await())

                    makeUpMap.putAll(asyncShadow.await())
                    makeUpMap.putAll(asyncLiner.await())
                    makeUpMap.putAll(asyncLash.await())
                    makeUpMap.putAll(asyncBall.await())

                    listener.onSuccessMakeup(makeUpMap)
                }
                LogUtils.iTag(TAG, "download cost time: $timeMillis")
            }
        }
    }

    private const val TOP = "none"
    fun sort(list: List<MakeupItem>) {
        Collections.sort(list, object : Comparator<MakeupItem?> {
            override fun compare(o1: MakeupItem?, o2: MakeupItem?): Int {
                return if (o1?.name == TOP) {
                    -1
                } else if (o2?.name == TOP) {
                    1
                } else {
                    Collator.getInstance(Locale.CHINESE).compare(o1?.name, o2?.name)
                }
            }
        })
    }

    private suspend fun getMakeup(context: Context, groupId: String, localData: EnumMap<EffectType, MutableList<MakeupItem>>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<MakeupItem>>> {
            SenseArMaterialService.shareInstance().fetchMaterialsFromGroupId(
                "",
                groupId,
                SenseArMaterialType.Effect,
                object : SenseArMaterialService.FetchMaterialListener {
                    override fun onSuccess(materials: List<SenseArMaterial>) {
                        val list = ArrayList<MakeupItem>()
                        for (item in materials) {
                            var bitmap: Bitmap? = null
                            try {
                                bitmap = ImageUtils.getImageSync(item.thumbnail, context)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            if (bitmap == null) {
                                bitmap = BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.none
                                )
                            }
                            var path = ""
                            //如果已经下载则传入路径地址
                            //如果已经下载则传入路径地址
                            if (SenseArMaterialService.shareInstance().isMaterialDownloaded(context, item)) {
                                path = SenseArMaterialService.shareInstance().getMaterialCachedPath(context, item)
                            }
                            item.cachedPath = path
                            list.add(MakeupItem(item, item.name, bitmap, path))
                        }

                        // 添加本地的
                        val localD = localData[EffectType.getTypeByGroupId(groupId)]
                        localD?.let { it1 -> list.addAll(it1) }
                        sort(list)

                        val makeUpMap: EnumMap<EffectType, MutableList<MakeupItem>> =
                            EnumMap<EffectType, MutableList<MakeupItem>>(EffectType::class.java)
                        makeUpMap[EffectType.getTypeByGroupId(groupId)] = list
                        it.resume(makeUpMap)
                    }

                    override fun onFailure(code: Int, message: String) {
                        val makeUpMap: EnumMap<EffectType, MutableList<MakeupItem>> =
                            EnumMap<EffectType, MutableList<MakeupItem>>(EffectType::class.java)
                        it.resume(makeUpMap)
                    }
                })
        }

    private fun getSticker(context: Context, groupId: String, listener: Listener) {
        SenseArMaterialService.shareInstance().fetchMaterialsFromGroupId(
            "",
            groupId,
            SenseArMaterialType.Effect,
            object : SenseArMaterialService.FetchMaterialListener {
                override fun onSuccess(materials: List<SenseArMaterial>) {
                    val list = ArrayList<StickerItem>()
                    for (item in materials) {
                        var bitmap: Bitmap? = null
                        try {
                            bitmap = ImageUtils.getImageSync(item.thumbnail, context)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        if (bitmap == null) {
                            bitmap = BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.none
                            )
                        }
                        var path = ""
                        //如果已经下载则传入路径地址
                        //如果已经下载则传入路径地址
                        if (SenseArMaterialService.shareInstance().isMaterialDownloaded(context, item)) {
                            path = SenseArMaterialService.shareInstance().getMaterialCachedPath(context, item)
                        }
                        item.cachedPath = path
                        list.add(StickerItem(item, item.name, bitmap, path))
                    }

                    val stickerMap: EnumMap<EffectType, MutableList<StickerItem>> =
                        EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
                    stickerMap[EffectType.getTypeByGroupId(groupId)] = list
                    GlobalScope.launch(Dispatchers.Main) {
                        listener.onSuccessSticker(stickerMap)
                    }
                }

                override fun onFailure(code: Int, message: String) {
                    val stickerMap: EnumMap<EffectType, MutableList<StickerItem>> =
                        EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
                    listener.onSuccessSticker(stickerMap)
                }
            })
    }

    private suspend fun authorSdk(context: Context) = suspendCoroutine<Boolean> {
        SenseArMaterialService.shareInstance().authorizeWithAppId(
            context,
            Constants.APPID,
            Constants.APPKEY,
            object : SenseArMaterialService.OnAuthorizedListener {
                override fun onSuccess() {
                    LogUtils.iTag(TAG, "鉴权成功！")
                    it.resume(true)
                }

                override fun onFailure(errorCode: SenseArMaterialService.AuthorizeErrorCode, errorMsg: String) {
                    it.resume(false)
                    LogUtils.iTag(
                        TAG,
                        String.format(Locale.getDefault(), "鉴权失败！%d, %s", errorCode, errorMsg)
                    )
                }
            })
    }

    fun getMakeupListsLocal2(): EnumMap<EffectType, MutableList<MakeupItem>> {
        val mMakeupLists = EnumMap<EffectType, MutableList<MakeupItem>>(EffectType::class.java)
        mMakeupLists[EffectType.TYPE_HAIR] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_hairdye")
        mMakeupLists[EffectType.TYPE_LIP] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_lip")
        mMakeupLists[EffectType.TYPE_BLUSH] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_blush")
        mMakeupLists[EffectType.TYPE_XR] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_highlight")
        mMakeupLists[EffectType.TYPE_EYE_BROW] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_brow")

        mMakeupLists[EffectType.TYPE_EYE_SHADOW] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeshadow")
        mMakeupLists[EffectType.TYPE_EYE_LINER] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeliner")
        mMakeupLists[EffectType.TYPE_EYELASH] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyelash")
        mMakeupLists[EffectType.TYPE_EYEBALL] = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeball")
        return mMakeupLists
    }
}