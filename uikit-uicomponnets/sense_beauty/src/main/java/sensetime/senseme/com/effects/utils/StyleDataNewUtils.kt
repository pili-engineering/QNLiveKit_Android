package sensetime.senseme.com.effects.utils

import sensetime.senseme.com.effects.utils.LogUtils
import com.softsugar.library.api.Material
import com.softsugar.library.sdk.entity.MaterialEntity
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

object StyleDataNewUtils {
    private var makeUpMap: EnumMap<EffectType, MutableList<StickerItem>>? = null
    private var tryOnMap: EnumMap<EffectType, MutableList<StickerItem>>? = null

    fun init() {
        fetchTryOnList()
    }

    fun getTryOnData(type: EffectType): MutableList<StickerItem>? {
        return tryOnMap?.get(type)
    }

    suspend fun getData(groupID: String) =
        suspendCoroutine<MutableList<StickerItem>?> {
            GlobalScope.launch(Dispatchers.IO) {
                val list = Material.getDataListSync(groupID)
                val result = transformStickerItemCollection(list)
                it.resume(result)
            }
        }

    private fun fetchTryOnList() {
        GlobalScope.launch(Dispatchers.Main) {
            val styleMap: EnumMap<EffectType, MutableList<StickerItem>> =
                EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
                val timeMillis = measureTimeMillis {
                    val ziran = async { Material.getDataListSync(EffectType.TYPE_ZIRAN.groupId) }
                    val qingzhuang = async { Material.getDataListSync(EffectType.TYPE_QINGZHUANG.groupId) }
                    val liuxing = async { Material.getDataListSync(EffectType.TYPE_FASHION.groupId) }

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

    private val deferredMap: EnumMap<EffectType, Deferred<MutableList<MaterialEntity>>> =
        EnumMap<EffectType, Deferred<MutableList<MaterialEntity>>>(EffectType::class.java)

    suspend fun getMakeUpMap(lists: ArrayList<EffectType>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<MakeupItem>>> {
            GlobalScope.launch(Dispatchers.IO) {
                val list = getMakeUpMapP(lists)

                list[EffectType.TYPE_HAIR]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_hairdye"))
                list[EffectType.TYPE_LIP]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_lip"))
                list[EffectType.TYPE_BLUSH]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_blush"))
                list[EffectType.TYPE_XR]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_highlight"))
                list[EffectType.TYPE_EYE_BROW]?.addAll(0, FileUtils.getMakeupFiles(
                    SenseMeApplication.getContext(), "makeup_brow"))

                list[EffectType.TYPE_EYE_SHADOW]?.addAll(0, FileUtils.getMakeupFiles(
                    SenseMeApplication.getContext(), "makeup_eyeshadow"))
                list[EffectType.TYPE_EYE_LINER]?.addAll(0, FileUtils.getMakeupFiles(
                    SenseMeApplication.getContext(), "makeup_eyeliner"))
                list[EffectType.TYPE_EYELASH]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyelash"))
                list[EffectType.TYPE_EYEBALL]?.addAll(0, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeball"))

                it.resume(list)
            }
        }
    private suspend fun getMakeUpMapP(lists: ArrayList<EffectType>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<MakeupItem>>> {
            GlobalScope.launch(Dispatchers.IO) {

                val dataMap: EnumMap<EffectType, MutableList<MakeupItem>> =
                    EnumMap<EffectType, MutableList<MakeupItem>>(EffectType::class.java)
                deferredMap.clear()
                for (item in lists) {
                    deferredMap[item] = async { Material.getDataListSync(item.groupId) }
                }
                val effectTypes: Set<EffectType> = deferredMap.keys
                for (type in effectTypes) {
                    val listData = deferredMap[type]
                    val list = listData?.await()?.let { it1 ->
                        transformMakeUpItemCollection(
                            it1
                        )
                    }
                    dataMap[type] = list
                }
                it.resume(dataMap)
            }
        }

    suspend fun getStyleMap(lists: ArrayList<EffectType>) =
        suspendCoroutine<EnumMap<EffectType, MutableList<StickerItem>>> {
            GlobalScope.launch(Dispatchers.IO) {
                val dataMap: EnumMap<EffectType, MutableList<StickerItem>> =
                    EnumMap<EffectType, MutableList<StickerItem>>(EffectType::class.java)
                deferredMap.clear()
                // 并发请求原始数据
                for (item in lists) {
                    //val groupId = item.groupId.toInt()
                    deferredMap[item] = async { Material.getDataListSync(item.groupId) }
                }

                // 映射成最终数据
                val effectTypes: Set<EffectType> = deferredMap.keys
                for (type in effectTypes) {
                    val listData = deferredMap[type]
                    val await: MutableList<MaterialEntity>? = listData?.await()
                    val list = await?.let { it1 -> transformStickerItemCollection(it1) }
                    dataMap[type] = list
                }

                val localData =
                    FileUtils.getStickerFiles(ContextHolder.getContext(), "style_lightly")
                localData?.let { it ->
                    dataMap[EffectType.TYPE_QINGZHUANG]?.addAll(0, it)
                }
                it.resume(dataMap)
            }
        }

    interface Listener {
        fun onSuccessStyle(styleList: EnumMap<EffectType, MutableList<StickerItem>>)
    }

    private fun transformStickerItemCollection(list: MutableList<MaterialEntity>): MutableList<StickerItem> {
        val studentList = mutableListOf<StickerItem>()

        for (people in list) {
            studentList.add(transformStickerItem(people))
        }
        return studentList
    }

    private fun transformMakeUpItemCollection(list: MutableList<MaterialEntity>): MutableList<MakeupItem> {
        val studentList = mutableListOf<MakeupItem>()

        for (people in list) {
            studentList.add(transformMakeUpItem(people))
        }
        return studentList
    }

    private fun transformStickerItem(people: MaterialEntity): StickerItem {
        val stickerItem = StickerItem()
        stickerItem.iconUrl = people.thumbnail
        stickerItem.name = people.name
        stickerItem.id = people.id
        stickerItem.pkgUrl = people.pkgUrl
        stickerItem.setPath(people.zipSdPath)
        return stickerItem
    }

    private fun transformMakeUpItem(people: MaterialEntity): MakeupItem {
        val stickerItem = MakeupItem()
//        if (SenseArMaterialService.shareInstance()
//                .isMaterialDownloaded(ContextHolder.getContext(), people)
//        ) {
//            stickerItem.setPath(
//                SenseArMaterialService.shareInstance()
//                    .getMaterialCachedPath(ContextHolder.getContext(), people)
//            )
//        }
        stickerItem.iconUrl = people.thumbnail
        stickerItem.name = people.name
        stickerItem.pkgUrl = people.pkgUrl
        //stickerItem.material = people
        stickerItem.setPath(people.zipSdPath)
        return stickerItem
    }
}