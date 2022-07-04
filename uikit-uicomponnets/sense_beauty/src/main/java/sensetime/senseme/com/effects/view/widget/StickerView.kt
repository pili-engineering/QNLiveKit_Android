package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import sensetime.senseme.com.effects.utils.LogUtils
import com.sensetime.sensearsourcemanager.SenseArMaterial
import com.sensetime.sensearsourcemanager.SenseArMaterialService
import com.softsugar.library.api.Material
import com.softsugar.library.sdk.listener.DownloadListener
import kotlinx.android.synthetic.main.view_sticker.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.adapter.BeautyOptionsAdapter
import sensetime.senseme.com.effects.adapter.StickerAdapter
import sensetime.senseme.com.effects.event.EventChooseBg
import sensetime.senseme.com.effects.utils.*
import sensetime.senseme.com.effects.view.BeautyOptionsItem
import sensetime.senseme.com.effects.view.StickerItem
import sensetime.senseme.com.effects.view.StickerState
import java.util.*

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 7/1/21 1:12 PM
 */
class StickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "StickerView"

        const val defStickerIndex = -1
    }

    private val mContentAdaptersMap: EnumMap<EffectType, StickerAdapter> =
        EnumMap<EffectType, StickerAdapter>(EffectType::class.java)
    private val mContentSelectedIndexMap: EnumMap<EffectType, Int> =
        EnumMap<EffectType, Int>(EffectType::class.java)

    private lateinit var mTitleAdapter: BeautyOptionsAdapter
    private var mCurrentContentAdapter: StickerAdapter? = null

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        initData()
        setListener()
        LogUtils.iTag(TAG, "test total cost time:" + (System.currentTimeMillis() - startTime))
    }

    fun setHighLight(param1: EffectType?, param2: EnumMap<EffectType, Int>?) {
        mTitleAdapter.setSelectedPosition(getIndexByEnum(param1))
        mTitleRecyclerView.smoothScrollToPosition(getIndexByEnum(param1))
        param1?.let {
            getTitleEntityByEnum(param1)?.let { it1 ->
                onClickTitleAdapter(
                    it1,
                    getIndexByEnum(param1)
                )
            }
        }

    }

    private fun getIndexByEnum(type: EffectType?): Int {
        var index: Int = -1
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type) index = i
            }
        }
        return index
    }

    private fun getTitleEntityByEnum(type: EffectType?): BeautyOptionsItem? {
        var item: BeautyOptionsItem? = null
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type) item = beautyOptionsItem
            }
        }
        return item
    }

    private fun initData() {
        for (item in EffectType.stickerList) {
            if (item == EffectType.TYPE_STICKER_ADD) {
                mContentAdaptersMap[item] = StickerAdapter(context, true, item)
            } else if (item == EffectType.TYPE_STICKER_SYNC) {
                mContentAdaptersMap[item] = StickerAdapter(context, true, item)
            } else {
                mContentAdaptersMap[item] = StickerAdapter(context, true, item)
            }
        }
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_sticker, this, true)
        RippleUtils.setForeground(context, rv_close_sticker)
        initTitleRecyclerView()
        initContentRecyclerView()
    }

    private var mCurrentTitleData: BeautyOptionsItem? = null

    private fun onClickTitleAdapter(
        entity: BeautyOptionsItem,
        position: Int,
    ) {
        mCurrentTitleData = entity
        mTitleAdapter.setSelectedPosition(position)
        mCurrentContentAdapter = mContentAdaptersMap[mCurrentTitleData?.type]
        LogUtils.iTag(TAG, "setListener: ${entity.type.desc}")
        rv_sticker_icons.adapter = mContentAdaptersMap[entity.type]?.adapter

        if (mCurrentContentAdapter?.data.isNullOrEmpty() && mCurrentTitleData?.type!=EffectType.TYPE_STICKER_LOCAL && mCurrentTitleData?.type!=EffectType.TYPE_STICKER_ADD) {
            Toast.makeText(
                ContextHolder.getContext(),
                MultiLanguageUtils.getStr(R.string.toast_sticker_pull),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (mCurrentTitleData?.type == EffectType.TYPE_STICKER_TRACK) {
            mListener?.onSelectedObjectTrack()
        }
    }

    private var mCurrentContentPosition: Int = 0

    private fun setListener() {
        rv_close_sticker.setOnClickListener {
            LogUtils.iTag(TAG, "setListener: clear")
            setDefContentSelectedIndex()
            for (item in EffectType.stickerList) {
                mContentSelectedIndexMap[item] = -1
            }
            mListener?.onClickClearSticker()
            EventBusUtils.post(EventChooseBg(false))
        }

        mTitleAdapter.setOnItemClickListener { position, entity ->
            onClickTitleAdapter(entity, position)
        }

        for ((_, value) in mContentAdaptersMap) {
            value.setListener(object : StickerAdapter.Listener {
                override fun onItemClickSticker(
                    position: Int,
                    data: StickerItem?,
                    selected: Boolean?,
                    adapter: StickerAdapter?
                ) {
                    LogUtils.iTag(TAG, "onItemClick: $")
                    mCurrentContentPosition = position
                    if (data?.getState() == StickerState.NORMAL_STATE) {
                        if (!NetworkUtils.isNetworkAvailable(context)) {
                            Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show()
                            return
                        }
                        data.setState(StickerState.LOADING_STATE)
                        mContentAdaptersMap[mCurrentTitleData?.type]?.getAdapter()
                            ?.notifyItemChanged(position)
                        if (null == data.getSenseArMaterial()) {
                            LogUtils.iTag(TAG, "onItemClick: getSenseArMaterial is null")
                        } else {
                            LogUtils.iTag(TAG, "onItemClick: ${data.getSenseArMaterial().toString()}")
                        }
                        setOnClickDoneView(position, true)
                        refreshUI()
                        SenseArMaterialService.shareInstance()
                            .setMaterialCacheSize(ContextHolder.getContext(), 10821629 * 100)
                        Material.downLoadZip(data.pkgUrl, object:DownloadListener {
                            override fun onFail(errorInfo: String) {

                            }

                            override fun onFinish(path: String) {
                                post {
                                        data.setPath(path)
                                        data.setState(StickerState.DONE_STATE)
                                        mContentAdaptersMap[mCurrentTitleData?.type]?.adapter
                                            ?.notifyItemChanged(position)
                                        LogUtils.iTag(
                                            TAG,
                                            "onSuccess: ${mCurrentTitleData?.type?.desc}"
                                        )
                                        LogUtils.iTag(
                                            TAG,
                                            "onSuccess: mContentAdaptersMap[mCurrentTitleData?.type]?.adapter?.mSelectedPosition:${mContentAdaptersMap[mCurrentTitleData?.type]?.adapter?.mSelectedPosition}"
                                        )
                                        LogUtils.iTag(
                                            TAG,
                                            "onSuccess: mContentAdaptersMap[:${position}"
                                        )
                                        //if (mContentSelectedIndexMap[mCurrentTitleData?.type] == position) {
                                        if (mCurrentContentPosition == position) {
                                            selected?.let {
                                                mListener?.onItemClickSticker(
                                                    position,
                                                    mCurrentTitleData,
                                                    data,
                                                    it
                                                )
                                                chooseBg(data, it)
                                                LogUtils.iTag(TAG, "choosebg sticker id: ${data?.material?.id}")
                                            }
                                            LogUtils.iTag(TAG, "onItemClickSticker: ${data.toString()}")
                                        } else {
                                            LogUtils.iTag(
                                                TAG,
                                                "onSuccess: mCurrentContentPosition:$mCurrentContentPosition , position: $position"
                                            )
                                        }
                                        //}
                                        //setOnClickDoneView(position, true)
                                        //refreshUI()
                                }
                            }

                            override fun onProgress(progress: Int) {
                            }

                            override fun onStart() {
                            }
                        })
                        SenseArMaterialService.shareInstance().downloadMaterial(
                            context,
                            data.getSenseArMaterial(),
                            object : SenseArMaterialService.DownloadMaterialListener {
                                override fun onSuccess(material: SenseArMaterial?) {
                                    post {
                                        material?.apply {
                                            data.setPath(this.cachedPath)
                                            data.setState(StickerState.DONE_STATE)
                                            mContentAdaptersMap[mCurrentTitleData?.type]?.adapter
                                                ?.notifyItemChanged(position)
                                            LogUtils.iTag(
                                                TAG,
                                                "onSuccess: ${mCurrentTitleData?.type?.desc}"
                                            )
                                            LogUtils.iTag(
                                                TAG,
                                                "onSuccess: mContentAdaptersMap[mCurrentTitleData?.type]?.adapter?.mSelectedPosition:${mContentAdaptersMap[mCurrentTitleData?.type]?.adapter?.mSelectedPosition}"
                                            )
                                            LogUtils.iTag(
                                                TAG,
                                                "onSuccess: mContentAdaptersMap[:${position}"
                                            )
                                            //if (mContentSelectedIndexMap[mCurrentTitleData?.type] == position) {
                                            if (mCurrentContentPosition == position) {
                                                selected?.let {
                                                    mListener?.onItemClickSticker(
                                                        position,
                                                        mCurrentTitleData,
                                                        data,
                                                        it
                                                    )
                                                    chooseBg(data, it)
                                                    LogUtils.iTag(TAG, "choosebg sticker id: ${data?.material?.id}")
                                                }
                                                LogUtils.iTag(TAG, "onItemClickSticker: ${data.toString()}")
                                            } else {
                                                LogUtils.iTag(
                                                    TAG,
                                                    "onSuccess: mCurrentContentPosition:$mCurrentContentPosition , position: $position"
                                                )
                                            }
                                            //}
                                            //setOnClickDoneView(position, true)
                                            //refreshUI()
                                        }
                                    }
                                }

                                override fun onFailure(p0: SenseArMaterial?, p1: Int, p2: String?) {
                                }

                                override fun onProgress(p0: SenseArMaterial?, p1: Float, p2: Int) {
                                }

                            })

                        return
                    } else {
                        LogUtils.iTag(TAG, "onItemClick: ")
                        setOnClickDoneView(position, selected)
                        refreshUI()
                        data?.let {
                            selected?.let { it1 ->
                                mListener?.onItemClickSticker(
                                    position,
                                    mCurrentTitleData,
                                    it,
                                    it1
                                )
                                chooseBg(it, it1)
                                LogUtils.iTag(TAG, "choosebg sticker id: ${it?.material?.id}")
                            }
                        }
                    }
                }
            })
        }

    }

    private fun chooseBg(data: StickerItem, selected: Boolean) {
        LogUtils.iTag(TAG, "chooseBg() called with: data = $data, selected = $selected")
        if (data.id == Constants.CHOOSE_BG_STICKER_ID) {
            EventBusUtils.post(EventChooseBg(selected))
        } else {
            EventBusUtils.post(EventChooseBg(false))
        }
    }

    private fun setOnClickDoneView(position: Int, selected: Boolean?) {
        for (item in EffectType.stickerList) {
            if (mCurrentTitleData?.type != EffectType.TYPE_STICKER_TRACK && item != EffectType.TYPE_STICKER_TRACK)
                mContentSelectedIndexMap[item] = -1
        }
        mContentSelectedIndexMap[mCurrentTitleData?.type] = position
        if (selected != null && !selected)
            mContentSelectedIndexMap[mCurrentTitleData?.type] = -1
    }

    private fun refreshUI() {
        for ((index, value) in mContentSelectedIndexMap) {
            mContentAdaptersMap[index]?.setSelectedPosition(value)
        }
    }

    fun setDataSync(stickerItem: StickerItem) {
        stickerItem.apply {
            mContentAdaptersMap[EffectType.TYPE_STICKER_SYNC]?.addHeadData(this)
        }
    }

    fun setData(contentData: EnumMap<EffectType, MutableList<StickerItem>>?) {
        contentData?.apply {
            for ((key, value) in contentData) {
                if (key != EffectType.TYPE_STICKER_TRACK && key != EffectType.TYPE_STICKER_ADD && key != EffectType.TYPE_STICKER_LOCAL && key != EffectType.TYPE_STICKER_SYNC) {
                    mContentAdaptersMap[key]?.refreshData(value)
                }
            }
        }
    }

    fun init(
        titleData: ArrayList<BeautyOptionsItem>,
        contentData: HashMap<EffectType, MutableList<StickerItem>>?,
    ) {
        mTitleAdapter.refreshData(titleData)

        contentData?.apply {
            // 当前是整妆的
            // 设置子项
            // 每个adapter都设置数据
            for (item in titleData) {
                val type = item.type
                val list: MutableList<*>? = contentData[type]

                list?.apply {
                    LogUtils.iTag(TAG, "init: ${item.type.desc}")
                    // 如果是<同步>则排序
                    if (type == EffectType.TYPE_STICKER_SYNC) {
                        for(i in list) {
                            i as StickerItem
                            i.orderTimeStamp = SpUtils.getParam(i.path + "time", 0L) as Long
                        }
                        list as MutableList<StickerItem>
                        list.sortByDescending {
                            it.orderTimeStamp
                        }
                    }
                    mContentAdaptersMap[type]?.refreshData(this)
                }
            }
        }

        // 默认是第一项
        val type = titleData[0].type
        mCurrentTitleData = titleData[0]
        rv_sticker_icons.adapter = mContentAdaptersMap[type]?.getAdapter()

//        refreshUI()
        GlobalScope.launch(Dispatchers.Main) {
            val data = StyleDataNewUtils.getStyleMap(EffectType.stickerList)
            //val data = StyleDataNewUtils.getStyleMap(EffectType.stickerList)
            setData(data)
            refreshUI()
        }
    }

    private fun initTitleRecyclerView() {
        mTitleRecyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        mTitleRecyclerView.addItemDecoration(SpaceItemDecoration(0))

        mTitleAdapter =
            BeautyOptionsAdapter(LocalDataStore.getInstance().stickerOptionsListNew, context)
        mTitleRecyclerView.adapter = mTitleAdapter
    }

    private fun initContentRecyclerView() {
        rv_sticker_icons.layoutManager = GridLayoutManager(context, 6)
        rv_sticker_icons.addItemDecoration(SpaceItemDecoration(0))

    }

    var mListener: Listener? = null

    interface Listener {
        fun onItemClickSticker(
            position: Int,
            titleEntity: BeautyOptionsItem?,
            contentEntity: LinkageEntity,
            selected: Boolean,
        )

        fun onClickClearSticker()

        fun onSelectedObjectTrack()
    }

    private fun setDefContentSelectedIndex() {
        for ((_, value) in mContentAdaptersMap) {
            value.setSelectedPosition(defStickerIndex)
        }
    }
}