package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import sensetime.senseme.com.effects.utils.LogUtils
import com.sensetime.sensearsourcemanager.SenseArMaterial
import com.sensetime.sensearsourcemanager.SenseArMaterialService
import com.softsugar.library.api.Material
import com.yarolegovich.discretescrollview.DiscreteScrollView
import kotlinx.android.synthetic.main.view_effect_style.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.adapter.StyleContentAdapter
import sensetime.senseme.com.effects.adapter.StyleTitleAdapter
import sensetime.senseme.com.effects.utils.*
import sensetime.senseme.com.effects.view.BeautyOptionsItem
import sensetime.senseme.com.effects.view.StickerItem
import sensetime.senseme.com.effects.view.StickerState
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @Description
 * OPEN API
 * 1 setHighLight(titleSelectedType: EffectType?,contentSelectedIndexMap: EnumMap<EffectType, Int>?,strengthFilter: Float?,strengthMakeUp: Float?) : 设置View状态
 *
 * @Author Lu Guoqiang
 * @Time 6/16/21 11:30 AM
 */
class EffectStyleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr),
    DiscreteScrollView.ScrollStateChangeListener<StyleContentAdapter.NormalViewHolder>,
    DiscreteScrollView.OnItemChangedListener<StyleContentAdapter.NormalViewHolder>,
    IEffectStyleView {

    companion object {
        private const val TAG = "EffectStyleView"
        const val defIndex = -1
        var defStrength = 0.85f
            get() {
                if (ContextHolder.getGender() == 0) {// 女
                    return Constants.DEF_STYLE_GIRL_STRENGTH
                } else if (ContextHolder.getGender() == 1) {// 男
                    return Constants.DEF_STYLE_BOY_STRENGTH
                }
                return Constants.DEF_STYLE_STRENGTH
            }
    }

    private var mCurrentTitleData: BeautyOptionsItem? = null
    private var mCurrentContentData: StickerItem? = null

    private lateinit var mTitleAdapter: StyleTitleAdapter
    private lateinit var mStyleContentAdapter: StyleContentAdapter
    private var mListener: Listener? = null
    private var mCurrentContentPosition = -1
    private var mCurrentTitlePosition = -1
    private var isFromUserScrollToPosition = false

    private var mCurrentFilterStrength = 0f
    private var mCurrentMakeupStrength = 0f

    private val mContentDataMap: EnumMap<EffectType, List<StickerItem>> =
        EnumMap<EffectType, List<StickerItem>>(EffectType::class.java)
    private val mTitleDataList: ArrayList<BeautyOptionsItem> = ArrayList()

    private val mContentSelectedIndexMap: EnumMap<EffectType, Int> =
        EnumMap<EffectType, Int>(EffectType::class.java)

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        initData()
        setListener()
        LogUtils.iTag(TAG, "init total cost time:" + (System.currentTimeMillis() - startTime))
    }

    fun clearContentSelected() {
        LogUtils.iTag(TAG, "clearContentSelected() called")
        setDefContentSelectedIndex()
        setDefStrength()
        hideSeekBar()
        refreshUI()
        refreshUISeekBar()

        EffectInfoDataHelper.getInstance().styleHigh = false

        forecast_city_picker.scrollToPosition(2)
        mStyleContentAdapter.setSelectPosition2(-1)
    }

    fun getTitleType(contentSelectedIndexMap: EnumMap<EffectType, Int>): EffectType {
        for ((key, value) in contentSelectedIndexMap) {
            if (value >= 0) return key
        }
        return EffectType.TYPE_ZIRAN
    }

    fun setHighLight(
        titleSelectedType: EffectType?,
        contentSelectedIndexMap: EnumMap<EffectType, Int>?,
        strengthFilter: Float?,
        strengthMakeUp: Float?,
    ) {
        LogUtils.iTag(
            TAG,
            "setHighLight() called with: titleSelectedType = $titleSelectedType, contentSelectedIndexMap = $contentSelectedIndexMap, strengthFilter = $strengthFilter, strengthMakeUp = $strengthMakeUp"
        )
        titleSelectedType?.let {
            val index = getIndexByEnum(titleSelectedType)
            val titleEntity = getTitleEntityByEnum(titleSelectedType)

            mTitleAdapter.setSelectPosition(index)
            if (titleEntity != null)
                onClickTitleAdapter(index, titleEntity)
        }

        contentSelectedIndexMap?.let {
            mContentSelectedIndexMap.putAll(contentSelectedIndexMap)
            LogUtils.iTag(TAG, "setHighLight: $mContentSelectedIndexMap")
        }

        if (strengthMakeUp != null && strengthFilter != null) {
            mCurrentFilterStrength = strengthFilter
            mCurrentMakeupStrength = strengthMakeUp
        }

        refreshUISeekBar()
        refreshUI()

        val currentContentSelectedIndex = contentSelectedIndexMap?.get(mCurrentTitleData?.type)
        LogUtils.iTag(TAG, "setHighLight: $currentContentSelectedIndex")
        if (currentContentSelectedIndex == null || (currentContentSelectedIndex < 0)) {
            forecast_city_picker.scrollToPosition(2)
            mStyleContentAdapter.setSelectPosition2(-1)// 防止不回掉
        }

    }

    private fun hasIndex(contentSelectedIndexMap: EnumMap<EffectType, Int>): Boolean {
        var flag = false
        val effectTypes: Set<EffectType> = mContentSelectedIndexMap.keys
        for (type in effectTypes) {
            val integer = contentSelectedIndexMap[type]
            if (integer != null && integer >= 0) flag = true
        }
        return flag
    }

    private fun refreshUISeekBar() {
        LogUtils.iTag(TAG, "refreshUISeekBar() called")
        //gp_seek_bar.visibility = INVISIBLE
//        val selectedIndex = mContentSelectedIndexMap[mCurrentTitleData?.type]
//        Log.d(TAG, "refreshUISeekBar: selectedIndex: $selectedIndex")
//        if (null != selectedIndex && selectedIndex >= 0) {
//            showMakeUpSeekBar()
//        }
        if (hasIndex(mContentSelectedIndexMap)) {
            showMakeUpSeekBar()
        }
        LogUtils.iTag(TAG, "refreshUI: $mCurrentMakeupStrength")
        seek_progress_makeup.setValue(mCurrentMakeupStrength)
        //seek_progress_makeup.visibility = View.VISIBLE
        seek_progress_filter.setValue(mCurrentFilterStrength)
    }

    private fun refreshUI() {
        val selectedIndex = mContentSelectedIndexMap[mCurrentTitleData?.type]
        if (null == mCurrentTitleData) {
            LogUtils.iTag(TAG, "refreshUI() called mCurrentTitleData is null. ")
        }
        selectedIndex?.apply {
            forecast_city_picker.scrollToPosition(this)

            //if (EffectInfoDataHelper.getInstance().styleHigh)
            mStyleContentAdapter.setSelectPosition(this)
        }

    }

    private fun setDefStrength() {
        mCurrentFilterStrength = defStrength
        mCurrentMakeupStrength = defStrength
    }

    fun getIndexByEnum(type: EffectType?): Int {
        var index: Int = -1
        type?.apply {
            for (i in mTitleDataList.indices) {
                val item = mTitleDataList[i]
                if (item.type == type) index = i
            }
        }
        return index
    }

    fun getIndexByName(type: EffectType?, name: String): Int {
        val list: List<StickerItem>? = mContentDataMap[type]
        var index: Int = -1
        list?.apply {
            for (i in list.indices) {
                val item = list[i]
                if (item.name.contains(name, true)) index = i
            }
        }
        LogUtils.iTag(TAG, "getIndexByName: $name 位置 $index")
        return index
    }

    private fun getTitleEntityByEnum(type: EffectType?): BeautyOptionsItem? {
        var result: BeautyOptionsItem? = null
        type?.apply {
            for (item in mTitleDataList) {
                if (item.type == type) result = item
            }
        }
        return result
    }

    private fun hideSeekBar() {
        ib_filter.visibility = View.INVISIBLE
        ib_makeup.visibility = View.INVISIBLE
        seek_progress_filter.visibility = View.INVISIBLE
        seek_progress_makeup.visibility = View.INVISIBLE
    }

    private fun showMakeUpSeekBar() {
        ib_filter.visibility = View.VISIBLE
        ib_makeup.visibility = View.VISIBLE
        seek_progress_filter.visibility = View.INVISIBLE
        seek_progress_makeup.visibility = View.VISIBLE
        ib_filter.isSelected = false
        ib_makeup.isSelected = true
    }

    private fun setListener() {
        ib_makeup.setOnClickListener {
            resetView()
            ib_makeup.isSelected = true
            seek_progress_makeup.visibility = View.VISIBLE
        }
        ib_filter.setOnClickListener {
            resetView()
            ib_filter.isSelected = true
            seek_progress_filter.visibility = View.VISIBLE
        }

        iv_close.setOnClickListener {
            LogUtils.iTag(TAG, "iv_close click: ${mCurrentContentData.toString()}")
            mCurrentTitlePosition = -1
            mCurrentContentPosition = -1
            mTitleAdapter.setSelectPosition(-1)
            forecast_city_picker.scrollToPosition(2)
            mStyleContentAdapter.setSelectPosition(-1)
            line_close.visibility = View.VISIBLE

            resetGpSeekBarView()

            hideSeekBar()

            for (item in EffectType.styleList) {
                EffectInfoDataHelper.getInstance().setContentSelectedIndex(item, -1)
                mContentSelectedIndexMap[item] = -1
            }
            mListener?.onClickClearStyle(mCurrentContentData)
        }

        seek_progress_filter.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                mCurrentFilterStrength = progress
                mListener?.onProgressChangedStyle(SeekBarType.FILTER, seekBar, progress, fromUser)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                LogUtils.iTag(
                    TAG,
                    "onStopTrackingTouch: save filter ${mCurrentTitleData?.type?.desc} $mCurrentFilterStrength"
                )
                EffectInfoDataHelper.getInstance().strengthStyleFilter = mCurrentFilterStrength
            }
        })

        seek_progress_makeup.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                mCurrentMakeupStrength = progress
                mListener?.onProgressChangedStyle(SeekBarType.MAKE_UP, seekBar, progress, fromUser)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                LogUtils.iTag(
                    TAG,
                    "onStopTrackingTouch: save makeup ${mCurrentTitleData?.type?.desc} $mCurrentMakeupStrength"
                )
                EffectInfoDataHelper.getInstance().strengthStyleMakeup = mCurrentMakeupStrength
            }
        })

        mTitleAdapter.setOnItemClickListener(object : StyleTitleAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, entity: BeautyOptionsItem) {
                onClickTitleAdapter(position, entity)
            }
        })

        mStyleContentAdapter.setListener(object : StyleContentAdapter.Listener {
            override fun onItemClickStyleContent(position: Int, entity: StickerItem) {
                GlobalScope.launch(Dispatchers.Main) {
                    setContentSelected(position)
                }
            }
        })
    }

    private suspend fun downloadZip(senseArMaterial: SenseArMaterial) =
        suspendCoroutine<SenseArMaterial?> {
            LogUtils.iTag(TAG, "downloadZip() called $senseArMaterial")
            GlobalScope.launch(Dispatchers.IO) {
                SenseArMaterialService.shareInstance().downloadMaterial(
                    ContextHolder.getContext(),
                    senseArMaterial,
                    object : SenseArMaterialService.DownloadMaterialListener {
                        override fun onSuccess(p0: SenseArMaterial?) {
                            GlobalScope.launch(Dispatchers.IO) {
                                it.resume(p0)
                            }
                        }

                        override fun onFailure(p0: SenseArMaterial?, p1: Int, p2: String?) {
                            it.resume(p0)
                        }

                        override fun onProgress(p0: SenseArMaterial?, p1: Float, p2: Int) {
                        }
                    })
            }
        }

    fun getSelectedContentItemMap(contentSelectedIndexMap: EnumMap<EffectType, Int>): EnumMap<EffectType, StickerItem> {
        val mData: EnumMap<EffectType, StickerItem> = EnumMap(EffectType::class.java)
        for ((type, contentSelectedIndex) in contentSelectedIndexMap) {
            // key-Enum  value-selectedIndex
            val list = mContentDataMap[type]
            try {
                if (contentSelectedIndex >= 0 && null != list && list.isNotEmpty()) {
                    val item = list?.get(contentSelectedIndex)
                    if (item != null) {
                        mData[type] = item as StickerItem?
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return mData
    }

    fun getSelectedContentItemMap(): EnumMap<EffectType, StickerItem> {
        val mData: EnumMap<EffectType, StickerItem> = EnumMap(EffectType::class.java)
        for ((type, contentSelectedIndex) in mContentSelectedIndexMap) {
            // key-Enum  value-selectedIndex
            val list = mContentDataMap[type]
            if (contentSelectedIndex != -1) {
                val item = list?.get(contentSelectedIndex)
                if (item != null) {
                    mData[type] = item as StickerItem?
                }
            }
        }
        return mData
    }

    private fun onClickTitleAdapter(position: Int, entity: BeautyOptionsItem) {
        if (mStyleContentAdapter.itemCount <= 1) {
            Toast.makeText(
                ContextHolder.getContext(),
                MultiLanguageUtils.getStr(R.string.toast_sticker_pull),
                Toast.LENGTH_SHORT
            ).show()
        }
        mCurrentTitleData = entity
        isFromUserScrollToPosition = false
        LogUtils.iTag(TAG, "onItemClick() called with: ")
        var contains = false
        //val contains: Boolean = mSelectedIndexArray.indexOfKey(position) >= 0
        mCurrentTitleData?.let {
            contains = mContentSelectedIndexMap[mCurrentTitleData!!.type]!! >= 0
        }
        if (contains) {
            LogUtils.iTag(TAG, "onClickTitleAdapter ${mCurrentTitleData!!.type.desc}: 包含")
            val value = mContentSelectedIndexMap[mCurrentTitleData!!.type]
            value?.let { forecast_city_picker.scrollToPosition(it) }
            value?.let { mStyleContentAdapter.setSelectPosition(it) }
        } else {
            LogUtils.iTag(TAG, "onClickTitleAdapter ${mCurrentTitleData!!.type.desc}: 不包含")
            forecast_city_picker.scrollToPosition(2)
            mStyleContentAdapter.setSelectPosition(-1)
        }
        line_close.visibility = INVISIBLE
        mCurrentTitlePosition = position
        LogUtils.iTag(TAG, "onItemClick() called with: currentTitlePosition:$mCurrentTitlePosition")
        mStyleContentAdapter.refreshData(mContentDataMap?.get(entity.type))
    }

    private fun resetView() {
        seek_progress_makeup.visibility = View.INVISIBLE
        seek_progress_filter.visibility = View.INVISIBLE
        ib_filter.isSelected = false
        ib_makeup.isSelected = false
    }

    private fun initData() {
        mCurrentTitlePosition = 0
        mTitleAdapter.setSelectPosition(mCurrentTitlePosition)
        forecast_city_picker.scrollToPosition(2)

        setDefContentSelectedIndex()
    }

    private fun setDefContentSelectedIndex() {
        for (item in EffectType.styleList)
            mContentSelectedIndexMap[item] = defIndex
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_effect_style, this, true)
        RippleUtils.setForeground(context, ib_makeup, ib_filter, iv_close)

        initTitleRecyclerView()

        forecast_city_picker.setSlideOnFling(true)
        mStyleContentAdapter = StyleContentAdapter(context)
        forecast_city_picker.adapter = mStyleContentAdapter
        forecast_city_picker.addOnItemChangedListener(this)
        forecast_city_picker.setItemTransitionTimeMillis(60)
        forecast_city_picker.addScrollStateChangeListener(this)
//        mStyleContentAdapter.setSelectPosition(2)
        resetGpSeekBarView()
        hideSeekBar()
    }

    private fun resetGpSeekBarView() {
        // 默认显示滤镜
        seek_progress_makeup.setValue(defStrength)
        seek_progress_filter.setValue(defStrength)
        seek_progress_filter.visibility = View.INVISIBLE
        seek_progress_makeup.visibility = View.VISIBLE
        ib_makeup.isSelected = true
        ib_filter.isSelected = false
    }

    private fun initTitleRecyclerView() {
        // 用线性显示, 类似于ListView
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        mTitleRecyclerView.layoutManager = manager

        mTitleAdapter = StyleTitleAdapter(context)
        mTitleRecyclerView.adapter = mTitleAdapter
    }

    override fun onScrollStart(
        currentItemHolder: StyleContentAdapter.NormalViewHolder,
        adapterPosition: Int,
    ) {
        LogUtils.iTag(TAG, "onScrollStart() called with: adapterPosition = $adapterPosition")
        isFromUserScrollToPosition = true
    }

    private fun setContentSelected(adapterPosition: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val styleContentEntity =
                mCurrentTitleData?.let { mContentDataMap[it.type]?.get(adapterPosition) }
            mCurrentContentData = styleContentEntity
            if (mCurrentTitlePosition == -1) mCurrentTitleData?.type?.let { setTitleSelection(it) }

            setDefStrength()
            showMakeUpSeekBar()
            mContentSelectedIndexMap[mCurrentTitleData?.type] = adapterPosition
            if (styleContentEntity?.getState() == StickerState.NORMAL_STATE) {
                styleContentEntity.state = StickerState.LOADING_STATE
                mStyleContentAdapter.notifyItemChanged(adapterPosition)

                //Log.d(TAG, "onScrollEnd: " + styleContentEntity.getSenseArMaterial().toString())
                //val senseArMaterial = downloadZip(styleContentEntity.getSenseArMaterial())
                val result = Material.downLoadZip(styleContentEntity.pkgUrl)
                //senseArMaterial?.cachedPath?.let { styleContentEntity.setPath(it) }
                styleContentEntity.setPath(result.sdPath)
                styleContentEntity.setState(StickerState.DONE_STATE)
                mStyleContentAdapter.notifyItemChanged(adapterPosition)

                if (!NetworkUtils.isNetworkAvailable(context)) {
                    Toast.makeText(context, "Network unavailable.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            if (mContentSelectedIndexMap[mCurrentTitleData?.type] == adapterPosition) {
                mListener?.onItemClickStyle(adapterPosition, mCurrentTitleData, styleContentEntity)
            } else {
                LogUtils.iTag(
                    TAG,
                    "mContentSelectedIndexMap[mCurrentTitleData?.type]:${mContentSelectedIndexMap[mCurrentTitleData?.type]} adapterPosition:$adapterPosition"
                )
            }
            EffectInfoDataHelper.getInstance().strengthStyleMakeup = defStrength
            EffectInfoDataHelper.getInstance().strengthStyleMakeup = defStrength

            for (item in EffectType.styleList) {
                mContentSelectedIndexMap[item] = -1
                EffectInfoDataHelper.getInstance().setContentSelectedIndex(item, -1)
            }

            if (mCurrentTitlePosition == -1) mCurrentTitleData?.type?.let {
                LogUtils.iTag(TAG, "onScrollEnd() called -----")
                setTitleSelection(it)
            }

            LogUtils.iTag(TAG, "onScrollEnd: ${mCurrentTitleData?.type?.desc} position:${adapterPosition} adapterPosition:$adapterPosition mCurrentContentPosition:$mCurrentContentPosition")
            EffectInfoDataHelper.getInstance()
                .setContentSelectedIndex(mCurrentTitleData?.type, mCurrentContentPosition)

            refreshUISeekBar()
            mStyleContentAdapter.setSelectPosition(mCurrentContentPosition)
        }
    }

    override fun onScrollEnd(
        currentItemHolder: StyleContentAdapter.NormalViewHolder,
        adapterPosition: Int,
    ) {
        setContentSelected(adapterPosition)
    }

    override fun onScroll(
        scrollPosition: Float,
        currentPosition: Int,
        newPosition: Int,
        currentHolder: StyleContentAdapter.NormalViewHolder?,
        newCurrent: StyleContentAdapter.NormalViewHolder?,
    ) {
    }

    override fun onCurrentItemChanged(
        viewHolder: StyleContentAdapter.NormalViewHolder?,
        adapterPosition: Int,
    ) {
        LogUtils.iTag(TAG, "onCurrentItemChanged() called with: adapterPosition = $adapterPosition")
        mCurrentContentPosition = adapterPosition
        if (isFromUserScrollToPosition) {
            LogUtils.iTag(
                TAG,
                "onCurrentItemChanged: titlePosition:$mCurrentTitlePosition, selectedPosition:$adapterPosition"
            )
            setDefContentSelectedIndex()
            mContentSelectedIndexMap[mCurrentTitleData?.type] = adapterPosition
        }
        mStyleContentAdapter.setSelectPosition(adapterPosition)
        if (mCurrentTitlePosition == -1) {
            mStyleContentAdapter.setSelectPosition(-1)
        }
        //mListener?.onItemSelected(adapterPosition, StyleContentEntity("", 0, ""))

        val currentContentSelectedIndex = mContentSelectedIndexMap?.get(mCurrentTitleData?.type)
        if (currentContentSelectedIndex != null && currentContentSelectedIndex < 0) {
            mStyleContentAdapter.setSelectPosition2(-1)// 防止不回掉
        }
    }

    fun init(
        titleData: List<BeautyOptionsItem>?,
        contentData: EnumMap<EffectType, List<StickerItem>>?,
    ) {
        titleData?.let {
            mTitleDataList.clear()
            mTitleDataList.addAll(titleData)
        }
        contentData?.let {
            mContentDataMap.clear()
            mContentDataMap.putAll(it)
        }

        mTitleAdapter.refreshData(titleData)
        mCurrentTitleData = titleData?.get(0)
        mStyleContentAdapter.refreshData(contentData?.get(mCurrentTitleData?.type))
    }

    private fun setTitleSelection(type: EffectType) {
        var position = -1
        // 根据枚举值遍历出索引
        mTitleDataList?.apply {
            for (index in this.indices) {
                if (this[index].type == type) {
                    position = index
                }
            }
        }
        mCurrentTitlePosition = position
        mTitleAdapter.setSelectPosition(position)
        line_close.visibility = View.INVISIBLE
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    enum class SeekBarType {
        MAKE_UP, FILTER
    }

    interface Listener {
        fun onItemClickStyle(
            position: Int,
            titleEntity: BeautyOptionsItem?,
            contentEntity: StickerItem?,
        )

        fun onProgressChangedStyle(
            type: SeekBarType,
            seekBar: SeekBar?,
            progress: Float,
            fromUser: Boolean
        )

        fun onClickClearStyle(contentEntity: StickerItem?)
    }
}