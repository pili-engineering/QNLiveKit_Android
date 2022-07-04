package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sensetime.senseme.com.effects.utils.LogUtils
import kotlinx.android.synthetic.main.view_makeup.view.*
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.adapter.BeautyOptionsAdapter
import sensetime.senseme.com.effects.adapter.FilterAdapter
import sensetime.senseme.com.effects.utils.EffectInfoDataHelper
import sensetime.senseme.com.effects.utils.RippleUtils
import sensetime.senseme.com.effects.view.BeautyOptionsItem
import sensetime.senseme.com.effects.view.FilterItem
import java.util.*

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/23/21 11:49 AM
 */
class FilterView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ILinkageView {

    lateinit var mIbShowOriginal: ImageButton

    companion object {
        private const val TAG = "FilterView"
        const val defFilterIndex = -1
        const val defFilterStrength = 0.80f

        val filterArray = arrayOf(
                EffectType.TYPE_PEOPLE,
                EffectType.TYPE_SCENERY,
                EffectType.TYPE_STILL_LIFE,
                EffectType.TYPE_FOOD
        )
    }

    private lateinit var mTitleAdapter: BeautyOptionsAdapter

    private var mCurrentTitleData: BeautyOptionsItem? = null

    private val mContentAdaptersMap: EnumMap<EffectType, ILinkageAdapter> =
            EnumMap<EffectType, ILinkageAdapter>(EffectType::class.java)

    private val mStrengthsMap: EnumMap<EffectType, Float> =
            EnumMap<EffectType, Float>(EffectType::class.java)

    private val mContentSelectedIndexMap: EnumMap<EffectType, Int> =
            EnumMap<EffectType, Int>(EffectType::class.java)

    fun setHighLight(param1: EffectType?, param2: EnumMap<EffectType, Int>?, strengthsMap: EnumMap<EffectType, Float>?) {
        LogUtils.iTag(TAG, "setHightlight() called with: param1 = ${param1?.desc}, param2 = $param2")
        param1?.let {
            getTitleEntityByEnum(param1)?.let { it1 ->
                mTitleAdapter.setSelectedPosition(getIndexByEnum(param1))
                onClickContentAdapter(
                        it1,
                        getIndexByEnum(param1)
                )
            }
        }

        param2?.let {
            mContentSelectedIndexMap.putAll(param2)
        }

        strengthsMap?.let {
            mStrengthsMap.putAll(strengthsMap)
        }

        param2?.let {
            if (!needSetDefStyle(param2)) {
                setDefStrength()
            }
        }

        refreshUI()
    }

    private fun needSetDefStyle(contentSelectedIndexMap: EnumMap<EffectType, Int>): Boolean {
        var flag = false
        val effectTypes: Set<EffectType> = contentSelectedIndexMap.keys
        for (type in effectTypes) {
            val integer = contentSelectedIndexMap[type]
            if (integer != null && integer >= 0) flag = true
        }
        return flag
    }

    private fun getIndexByEnum(type: EffectType?): Int {
        var index: Int = -1
        type?.apply {
            for (i in mTitleAdapter.data.indices) {
                val beautyOptionsItem = mTitleAdapter.data[i]
                if (beautyOptionsItem.type == type) index = i
            }
        }
        LogUtils.iTag(TAG, "getIndexByEnum: $index")
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

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        initData()
        for (item in filterArray) mContentAdaptersMap[item] = FilterAdapter(context)

        setListener()
        LogUtils.iTag(TAG, "init total cost time:" + (System.currentTimeMillis() - startTime))
    }

    private val mContentData: EnumMap<EffectType, List<*>> =
            EnumMap<EffectType, List<*>>(EffectType::class.java)

    fun getSelectedContentItemMap(): EnumMap<EffectType, FilterItem> {
        val mData: EnumMap<EffectType, FilterItem> = EnumMap(EffectType::class.java)
        for ((type, contentSelectedIndex) in mContentSelectedIndexMap) {
            // key-Enum  value-selectedIndex
            val list = mContentData[type]
            if (contentSelectedIndex >= 0) {
                val item = list?.get(contentSelectedIndex)
                if (item != null) {
                    mData[type] = item as FilterItem?
                }
            }
        }
        return mData
    }

    fun getSelectedStrengthMap(): EnumMap<EffectType, Float> {
        return mStrengthsMap
    }

    private fun initData() {
        setDefStrength()
        setDefContentSelectedIndex()
    }

    private fun setDefStrength() {
        for (item in filterArray) mStrengthsMap[item] = defFilterStrength
    }

    private fun hasSelected():Boolean {
        val effectTypes = mContentSelectedIndexMap.keys
        LogUtils.iTag(TAG, "hasSelected() called")
        for (type in effectTypes) {
            val index = mContentSelectedIndexMap[type]
            if (null !=index && index >=0) {
                return true
            }
        }
        return false
    }

    private fun setListener() {
        for ((_, value) in mContentAdaptersMap) {
            value.setListener(object : ILinkageAdapter.Listener {
                override fun onItemClickSticker(position: Int, data: LinkageEntity, selected: Boolean, adapter: RecyclerView.Adapter<*>) {
                    if (!hasSelected()) {
                        LogUtils.iTag(TAG, "onItemClickSticker: first select")
                        setDefStrength()
                    } else {
                        LogUtils.iTag(TAG, "onItemClickSticker: first select222")
                    }
                    for (item in filterArray) {
                        mContentSelectedIndexMap[item] = -1

                        EffectInfoDataHelper.getInstance().setContentSelectedIndex(item, -1)
                    }
                    mContentSelectedIndexMap[mCurrentTitleData?.type] = position

                    refreshUI()
                    var strength = mStrengthsMap[mCurrentTitleData?.type]
                    if (strength == null){
                        strength = defFilterStrength
                        setDefStrength()
                    }

                    EffectInfoDataHelper.getInstance().setContentSelectedIndex(mCurrentTitleData?.type, position)
                    for (item in filterArray) {
                        EffectInfoDataHelper.getInstance().setStrength(item, strength)
                    }

                    LogUtils.iTag(TAG, "onItemClick: type:${mCurrentTitleData?.type?.desc} strength:$strength")
                    EffectInfoDataHelper.getInstance().styleHigh = false
                    mListener?.onItemClickFilter(
                            position,
                            mCurrentTitleData,
                            data,
                            selected,
                            strength,
                            adapter)
                    EffectInfoDataHelper.getInstance().filterHigh = true
                }
            })
        }

        sb_strength.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                if (fromUser) {
                    mStrengthsMap[mCurrentTitleData?.type] = progress
                    for (item in filterArray) {
                        mStrengthsMap[item] = progress
                        //EffectInfoDataHelper.getInstance().setFilterStrength(item, progress)
                    }
                    //EffectInfoDataHelper.getInstance().setFilterStrength(mCurrentTitleData?.type, progress)

                    mCurrentTitleData?.let { mListener?.onProgressChangedFilter(it, seekBar, progress, fromUser) }
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val pro = mStrengthsMap[mCurrentTitleData?.type]
                for (item in filterArray) {
                    pro?.let { EffectInfoDataHelper.getInstance().setStrength(item, it) }
                }
            }

        })

        mTitleAdapter.setOnItemClickListener { position, entity ->
            onClickContentAdapter(entity, position)
        }

        dt_clear.setOnClickListener {
            setDefContentSelectedIndex()
            setDefStrength()
            refreshUI()
            EffectInfoDataHelper.getInstance().filterHigh = false
            mListener?.onClickClearFilter()

            for ((key, value) in mContentSelectedIndexMap) {
                EffectInfoDataHelper.getInstance().setContentSelectedIndex(key, value)
                EffectInfoDataHelper.getInstance().setStrength(key, defFilterStrength)
            }
        }
    }

    private fun onClickContentAdapter(
            entity: BeautyOptionsItem,
            position: Int
    ) {
        mCurrentTitleData = entity
        mTitleAdapter.setSelectedPosition(position)
        entity?.apply {
            mContentRecycler.adapter = mContentAdaptersMap[entity.type]?.getAdapter()
        }

        refreshUI()
    }

    private fun setDefContentSelectedIndex() {
        for (item in filterArray)
            mContentSelectedIndexMap[item] = defFilterIndex
    }

    private fun refreshUI() {
        sb_strength.visibility = INVISIBLE
        val contentSelectedIndex = mContentSelectedIndexMap[mCurrentTitleData?.type]
        if (null != contentSelectedIndex && contentSelectedIndex >= 0) {
            sb_strength.visibility = VISIBLE
        } else {
            sb_strength.visibility = INVISIBLE
        }

        mStrengthsMap[mCurrentTitleData?.type]?.apply {
            sb_strength.setValue(this)
        }

        for ((index, value) in mContentSelectedIndexMap) {
            mContentAdaptersMap[index]?.setSelectedPosition(value)
        }
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_makeup, this, true)
        setOpenVar()
        RippleUtils.setForeground(context, dt_clear)
        initTitleRecyclerView()
        initContentRecyclerView()
    }

    private fun setOpenVar() {
        mIbShowOriginal = ib_show_original
    }

    private fun initTitleRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mTitleRecycleView.layoutManager = ms

        mTitleAdapter = BeautyOptionsAdapter(context)
        mTitleRecycleView.adapter = mTitleAdapter
    }

    private fun initContentRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mContentRecycler.layoutManager = ms
    }

    override fun clearContentSelected() {
        setDefContentSelectedIndex()
        setDefStrength()

        refreshUI()
        EffectInfoDataHelper.getInstance().filterHigh = false
    }

    override fun init(
            titleData: ArrayList<BeautyOptionsItem>,
            contentData: HashMap<EffectType, MutableList<*>>?
    ) {
        mContentData.clear()
        contentData?.let { mContentData.putAll(it) }

        mTitleAdapter.refreshData(titleData)

        contentData?.apply {
            for (item in titleData) {
                val type = item.type
                val list: MutableList<*>? = contentData?.get(type)

                list?.apply {
                    mContentAdaptersMap[type]?.refreshData(this)
                }
            }
        }

        val type = titleData[0].type
        mCurrentTitleData = titleData[0]
        mContentRecycler.adapter = mContentAdaptersMap[type]?.getAdapter()
        refreshUI()
    }

    override fun setData(contentData: EnumMap<EffectType, MutableList<*>>?) {
//        contentData?.apply {
//            for ((key, value) in contentData) {
//                mContentAdaptersMap[key]?.refreshData(value)
//            }
//        }
//        refreshUI()
    }

    private var mListener: Listener? = null

    interface Listener {
        /**
         * adapterStyle : 0 美妆
         * adapterStyle : 1 滤镜
         */
        fun onItemClickFilter(
                position: Int,
                titleEntity: BeautyOptionsItem?,
                contentEntity: LinkageEntity,
                selected: Boolean,
                strength: Float,
                adapter: RecyclerView.Adapter<*>)

        fun onClickClearFilter()

        fun onProgressChangedFilter(
                titleEntity: BeautyOptionsItem,
                seekBar: SeekBar?,
                progress: Float,
                fromUser: Boolean)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

}