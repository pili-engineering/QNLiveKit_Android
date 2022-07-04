package sensetime.senseme.com.effects.utils

import android.content.Context
import sensetime.senseme.com.effects.utils.LogUtils
import com.sensetime.sensearsourcemanager.SenseArMaterial
import com.sensetime.sensearsourcemanager.SenseArMaterialService
import com.sensetime.sensearsourcemanager.SenseArMaterialType
import kotlinx.coroutines.*
import sensetime.senseme.com.effects.SenseMeApplication
import sensetime.senseme.com.effects.view.MakeupItem
import sensetime.senseme.com.effects.view.StickerItem
import sensetime.senseme.com.effects.view.widget.EffectType
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/8/12 1:48 下午
 */
private const val TAG = "StyleDataUtils"

object StyleDataUtils {

    private val deferredMap: EnumMap<EffectType, Deferred<MutableList<SenseArMaterial>>> =
        EnumMap<EffectType, Deferred<MutableList<SenseArMaterial>>>(EffectType::class.java)

    suspend fun getMakeUpMap(lists: ArrayList<EffectType>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<MakeupItem>>> {
            GlobalScope.launch(Dispatchers.IO) {
                val list = getMakeUpMapP(lists)

                list[EffectType.TYPE_HAIR]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_hairdye"))
                list[EffectType.TYPE_LIP]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_lip"))
                list[EffectType.TYPE_BLUSH]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_blush"))
                list[EffectType.TYPE_XR]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_highlight"))
                list[EffectType.TYPE_EYE_BROW]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_brow"))

                list[EffectType.TYPE_EYE_SHADOW]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeshadow"))
                list[EffectType.TYPE_EYE_LINER]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeliner"))
                list[EffectType.TYPE_EYELASH]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyelash"))
                list[EffectType.TYPE_EYEBALL]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeball"))

                it.resume(list)
            }
        }

    suspend fun getStyleMap(lists: ArrayList<EffectType>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<StickerItem>>> {
            GlobalScope.launch(Dispatchers.IO) {
                val list = getStickerMap(lists)
                val localData =
                    FileUtils.getStickerFiles(ContextHolder.getContext(), "style_lightly")
                localData?.let { it ->
                    list[EffectType.TYPE_QINGZHUANG]?.addAll(0, it)
                }
                it.resume(list)
            }
        }

    private suspend fun getMakeUpMapP(lists: ArrayList<EffectType>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<MakeupItem>>> {
            GlobalScope.launch(Dispatchers.IO) {
                val ret = authorSdk(getContext())
                val dataMap: EnumMap<EffectType, MutableList<MakeupItem>> =
                    EnumMap<EffectType, MutableList<MakeupItem>>(EffectType::class.java)
                deferredMap.clear()
                for (item in lists) {
                    deferredMap[item] = async { getMakeup(item.groupId) }
                }
                val effectTypes: Set<EffectType> = deferredMap.keys
                for (type in effectTypes) {
                    val listData = deferredMap[type]
                    val list = listData?.await()?.let { it1 -> transformMakeUpItemCollection(it1) }
                    dataMap[type] = list
                }
                it.resume(dataMap)
            }
        }

    suspend fun getStickerMap(lists: ArrayList<EffectType>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<StickerItem>>> {
            GlobalScope.launch(Dispatchers.IO) {
                val ret = authorSdk(getContext())
                val dataMap: EnumMap<EffectType, MutableList<StickerItem>> =
                    EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
                deferredMap.clear()
                for (item in lists) {
                    deferredMap[item] = async { getMakeup(item.groupId) }
                }
                val effectTypes: Set<EffectType> = deferredMap.keys
                for (type in effectTypes) {
                    val listData = deferredMap[type]
                    val list = listData?.await()?.let { it1 -> transformStickerItemCollection(it1) }
                    dataMap[type] = list
                }
                it.resume(dataMap)
            }
        }

    suspend fun getData(groupID: String) =
        suspendCoroutine<MutableList<StickerItem>?> {
            GlobalScope.launch(Dispatchers.IO) {
                authorSdk(getContext())
                val list = getMakeup(groupID)
                val result = transformStickerItemCollection(list)
                it.resume(result)
            }
        }

    interface Listener {
        fun onSuccessStyle(styleList: EnumMap<EffectType, MutableList<StickerItem>>)
    }

    fun init() {
        fetchMakeUpList()
        fetchTryOnList()
    }

    private var makeUpMap: EnumMap<EffectType, MutableList<StickerItem>>? = null
    private var tryOnMap: EnumMap<EffectType, MutableList<StickerItem>>? = null

    fun getMakeUpData(type: EffectType): MutableList<StickerItem>? {
        return makeUpMap?.get(type)
    }

    fun getTryOnData(type: EffectType): MutableList<StickerItem>? {
        return tryOnMap?.get(type)
    }

    private fun fetchTryOnList() {
        GlobalScope.launch(Dispatchers.Main) {
            val ret = authorSdk(getContext())
            val styleMap: EnumMap<EffectType, MutableList<StickerItem>> =
                EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
            if (ret) {
                val timeMillis = measureTimeMillis {
                    val ziran = async { getMakeup(EffectType.TYPE_ZIRAN.groupId) }
                    val qingzhuang = async { getMakeup(EffectType.TYPE_QINGZHUANG.groupId) }
                    val liuxing = async { getMakeup(EffectType.TYPE_FASHION.groupId) }

                    styleMap[EffectType.TYPE_ZIRAN] = transformStickerItemCollection(ziran.await())
                    styleMap[EffectType.TYPE_QINGZHUANG] =
                        transformStickerItemCollection(qingzhuang.await())
                    styleMap[EffectType.TYPE_FASHION] =
                        transformStickerItemCollection(liuxing.await())

                    tryOnMap = styleMap
                    LogUtils.iTag(TAG, "getTryOnList: $styleMap")
                }
                LogUtils.iTag(TAG, "download try on data cost: $timeMillis")
            }
        }
    }

    private fun fetchMakeUpList() {
        GlobalScope.launch(Dispatchers.Main) {
            val ret = authorSdk(getContext())
            val styleMap: EnumMap<EffectType, MutableList<StickerItem>> =
                EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
            if (ret) {
                val timeMillis = measureTimeMillis {
                    for (item in EffectType.makeupList) {
                        val list =
                            async { getMakeup(item.groupId) }
                        styleMap[item] = transformStickerItemCollection(list.await())
                    }
                    makeUpMap = styleMap
                    LogUtils.iTag(TAG, "getStyleList: $styleMap")
                }
                LogUtils.iTag(TAG, "download makeup data cost: $timeMillis")
            }
        }
    }

    fun getStyleList(listener: Listener) {
        GlobalScope.launch() {
            val ret = authorSdk(getContext())
            val styleMap: EnumMap<EffectType, MutableList<StickerItem>> =
                EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
            if (ret) {
                val timeMillis = measureTimeMillis {
                    for (item in EffectType.styleList) {
                        val list =
                            async { getMakeup(item.groupId) }
                        styleMap[item] = transformStickerItemCollection(list.await())
                    }

                    val localData =
                        FileUtils.getStickerFiles(ContextHolder.getContext(), "style_lightly")
                    styleMap[EffectType.TYPE_QINGZHUANG]?.addAll(localData)

                    listener.onSuccessStyle(styleMap)
                }
                LogUtils.iTag(TAG, "download style data cost: $timeMillis")
            }
        }
    }

    private suspend fun getMakeup(
        groupId: String
    ) =
        suspendCoroutine<MutableList<SenseArMaterial>> {
            GlobalScope.launch(Dispatchers.IO) {
                SenseArMaterialService.shareInstance().fetchMaterialsFromGroupId(
                    "",
                    groupId,
                    SenseArMaterialType.Effect,
                    object : SenseArMaterialService.FetchMaterialListener {
                        override fun onSuccess(materials: MutableList<SenseArMaterial>) {
                            it.resume(materials)
                        }

                        override fun onFailure(code: Int, message: String?) {
                            val materials: MutableList<SenseArMaterial> = mutableListOf()
                            it.resume(materials)
                        }

                    })
            }
        }

    private fun getContext(): Context {
        return ContextHolder.getContext()
    }

    private suspend fun authorSdk(context: Context) = suspendCoroutine<Boolean> {
        GlobalScope.launch(Dispatchers.IO) {
            SenseArMaterialService.shareInstance().authorizeWithAppId(
                context,
                Constants.APPID,
                Constants.APPKEY,
                object : SenseArMaterialService.OnAuthorizedListener {
                    override fun onSuccess() {
                        LogUtils.iTag(TAG, "鉴权成功！")
                        it.resume(true)
                    }

                    override fun onFailure(
                        errorCode: SenseArMaterialService.AuthorizeErrorCode,
                        errorMsg: String
                    ) {
                        it.resume(false)
                        LogUtils.iTag(
                            TAG,
                            String.format(Locale.getDefault(), "鉴权失败！%d, %s", errorCode, errorMsg)
                        )
                    }
                })
        }
    }

    private fun transformStickerItemCollection(list: MutableList<SenseArMaterial>): MutableList<StickerItem> {
        val studentList = mutableListOf<StickerItem>()

        for (people in list) {
            studentList.add(transformStickerItem(people))
        }
        return studentList
    }

    private fun transformMakeUpItemCollection(list: MutableList<SenseArMaterial>): MutableList<MakeupItem> {
        val studentList = mutableListOf<MakeupItem>()

        for (people in list) {
            studentList.add(transformMakeUpItem(people))
        }
        return studentList
    }

    private fun transformStickerItem(people: SenseArMaterial): StickerItem {
        val stickerItem = StickerItem()
        if (SenseArMaterialService.shareInstance().isMaterialDownloaded(ContextHolder.getContext(), people)) {
            stickerItem.setPath(SenseArMaterialService.shareInstance().getMaterialCachedPath(ContextHolder.getContext(), people))
        }
        stickerItem.iconUrl = people.thumbnail
        stickerItem.name = people.name
        stickerItem.material = people
        return stickerItem
    }

    private fun transformMakeUpItem(people: SenseArMaterial): MakeupItem {
        val stickerItem = MakeupItem()
        if (SenseArMaterialService.shareInstance().isMaterialDownloaded(ContextHolder.getContext(), people)) {
            stickerItem.setPath(SenseArMaterialService.shareInstance().getMaterialCachedPath(ContextHolder.getContext(), people))
        }
        stickerItem.iconUrl = people.thumbnail
        stickerItem.name = people.name
        stickerItem.material = people
        return stickerItem
    }
}