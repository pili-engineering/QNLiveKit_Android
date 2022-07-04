package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sensetime.senseme.com.effects.utils.LogUtils
import com.sensetime.stmobile.model.STEffectBeautyInfo
import com.softsugar.library.api.Material
import com.softsugar.library.sdk.listener.DownloadListener
import kotlinx.android.synthetic.main.view_makeup.view.*
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.adapter.BeautyOptionsAdapter
import sensetime.senseme.com.effects.adapter.MakeupAdapter
import sensetime.senseme.com.effects.utils.EffectInfoDataHelper
import sensetime.senseme.com.effects.utils.MultiLanguageUtils
import sensetime.senseme.com.effects.utils.NetworkUtils
import sensetime.senseme.com.effects.utils.RippleUtils
import sensetime.senseme.com.effects.view.BeautyOptionsItem
import sensetime.senseme.com.effects.view.MakeupItem
import sensetime.senseme.com.effects.view.StickerState
import java.util.*

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/23/21 11:49 AM
 */
class MakeupView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ILinkageView {

    lateinit var mIbShowOriginal: ImageButton

    companion object {
        private const val TAG = "MakeupView"
        const val defMakeupIndex = 0
        const val defMakeUpStrength = 0.80f// 美妆默认0.85  滤镜默认0.8
    }

    private lateinit var mTitleAdapter: BeautyOptionsAdapter

    private var mCurrentTitleData: BeautyOptionsItem? = null

    private val mContentAdaptersMap: EnumMap<EffectType, ILinkageAdapter> =
        EnumMap<EffectType, ILinkageAdapter>(EffectType::class.java)

    private val mStrengthsMap: EnumMap<EffectType, Float> =
        EnumMap<EffectType, Float>(EffectType::class.java)

    private val mContentSelectedIndexMap: EnumMap<EffectType, Int> =
        EnumMap<EffectType, Int>(EffectType::class.java)

    private val mContentData: EnumMap<EffectType, List<*>> =
        EnumMap<EffectType, List<*>>(EffectType::class.java)

    fun getSelectedStrengthMap(): EnumMap<EffectType, Float> {
        return mStrengthsMap
    }

    fun getSelectedContentItemMap(): EnumMap<EffectType, MakeupItem> {
        val mData: EnumMap<EffectType, MakeupItem> = EnumMap(EffectType::class.java)
        if (mContentSelectedIndexMap.isEmpty())
            LogUtils.iTag(TAG, "getSelectedContentItemMap: mContentSelectedIndexMap is null")
        if (mContentData.isEmpty()) {
            LogUtils.iTag(TAG, "mContentData is null ")
        }
        for ((type, contentSelectedIndex) in mContentSelectedIndexMap) {
            val list = mContentData[type]
            if (contentSelectedIndex >= 0) {
                try {
                    val item = list?.get(contentSelectedIndex)
                    if (item != null) {
                        mData[type] = item as MakeupItem?
                    }
                }catch (e:IndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            }
        }
        return mData
    }

    fun setHighLight(
        titleSelectedType: EffectType?,
        contentSelectedIndexMap: EnumMap<EffectType, Int>?,
        strengthsMap: EnumMap<EffectType, Float>?
    ) {
        LogUtils.iTag(
            TAG,
            "setHighLight() called with: titleSelectedType = $titleSelectedType, contentSelectedIndexMap = $contentSelectedIndexMap, strengthsMap = $strengthsMap"
        )
        titleSelectedType?.let {
            getTitleEntityByEnum(titleSelectedType)?.let { it1 ->
                mTitleAdapter.setSelectedPosition(getIndexByEnum(titleSelectedType))
                mTitleRecycleView.smoothScrollToPosition(getIndexByEnum(titleSelectedType))
                onClickContentAdapter(
                    it1,
                    getIndexByEnum(titleSelectedType)
                )
            }
        }

        contentSelectedIndexMap?.let {
            mContentSelectedIndexMap.putAll(contentSelectedIndexMap)
            refreshUI()
        }

        strengthsMap?.let {
            mStrengthsMap.putAll(strengthsMap)
        }

        contentSelectedIndexMap?.let {
            for (type in EffectType.makeupList) {
                if (!hasContentSelected(type, contentSelectedIndexMap)) {
                    LogUtils.iTag(TAG, "setHighLight: ${type.desc} no ")
                    mStrengthsMap[type] = defMakeUpStrength
                } else {
                    LogUtils.iTag(TAG, "setHighLight: ${type.desc} yes ")
                }
            }
        }

        refreshUI()
    }

    private fun hasContentSelected(
        type: EffectType,
        contentSelectedIndexMap: EnumMap<EffectType, Int>
    ): Boolean {
        var flag = false
        val index = contentSelectedIndexMap[type]
        if (index != null && index > 0) flag = true
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
        initView(context)
        initData()
        for (item in EffectType.makeupList) mContentAdaptersMap[item] = MakeupAdapter(context)

        setListener()
    }

    private fun initData() {
        setDefStrength()
        setDefContentSelectedIndex()
    }

    private fun setDefStrength() {
        for (item in EffectType.makeupList) mStrengthsMap[item] = defMakeUpStrength
    }

    private fun setListener() {
        for ((_, value) in mContentAdaptersMap) {
            value.setListener(object : ILinkageAdapter.Listener {
                override fun onItemClickSticker(
                    position: Int,
                    data: LinkageEntity,
                    selected: Boolean,
                    adapter: RecyclerView.Adapter<*>
                ) {
                    mContentSelectedIndexMap[mCurrentTitleData?.type] = position

                    if (position == 0) {
                        LogUtils.iTag(TAG, "onItemClickSticker: $position")
                        mStrengthsMap[mCurrentTitleData?.type] = defMakeUpStrength
                        EffectInfoDataHelper.getInstance()
                            .setStrength(mCurrentTitleData?.type, defMakeUpStrength)
                    }
                    EffectInfoDataHelper.getInstance()
                        .setContentSelectedIndex(mCurrentTitleData?.type, position)

                    refreshUI()
                    var strength = mStrengthsMap[mCurrentTitleData?.type]
                    if (strength == null) strength = defMakeUpStrength

                    if (data.getState() == StickerState.NORMAL_STATE) {
                        if (!NetworkUtils.isNetworkAvailable(context)) {
                            Toast.makeText(context, MultiLanguageUtils.getStr(R.string.toast_net_error), Toast.LENGTH_SHORT)
                                .show()
                            return
                        }
                        data.setState(StickerState.LOADING_STATE)
                        mContentAdaptersMap[mCurrentTitleData?.type]?.getAdapter()
                            ?.notifyItemChanged(position)

                        Material.downLoadZip(data.getPkgUrl(), object:DownloadListener {
                            override fun onFail(errorInfo: String) {
                            }

                            override fun onFinish(path: String) {
                                sb_strength.post {
//                                    material?.apply {
                                        data.setPath(path)
                                        data.setState(StickerState.DONE_STATE)
                                        mContentAdaptersMap[mCurrentTitleData?.type]?.getAdapter()
                                            ?.notifyItemChanged(position)
                                        EffectInfoDataHelper.getInstance()
                                            .setStrength(mCurrentTitleData?.type, strength)
                                        EffectInfoDataHelper.getInstance().styleHigh = false
                                        mListener?.onItemClickMakeUp(
                                            position,
                                            mCurrentTitleData,
                                            data,
                                            selected,
                                            strength,
                                            adapter
                                        )
                                    }
//                                }
                            }

                            override fun onProgress(progress: Int) {
                            }

                            override fun onStart() {
                            }

                        })
//                        SenseArMaterialService.shareInstance().downloadMaterial(
//                            context,
//                            data.getSenseArMaterial(),
//                            object : SenseArMaterialService.DownloadMaterialListener {
//                                override fun onSuccess(material: SenseArMaterial?) {
//                                    sb_strength.post {
//                                        material?.apply {
//                                            data.setPath(this.cachedPath)
//                                            data.setState(StickerState.DONE_STATE)
//                                            mContentAdaptersMap[mCurrentTitleData?.type]?.getAdapter()
//                                                ?.notifyItemChanged(position)
//                                            EffectInfoDataHelper.getInstance()
//                                                .setStrength(mCurrentTitleData?.type, strength)
//                                            EffectInfoDataHelper.getInstance().styleHigh = false
//                                            mListener?.onItemClickMakeUp(
//                                                position,
//                                                mCurrentTitleData,
//                                                data,
//                                                selected,
//                                                strength,
//                                                adapter
//                                            )
//                                        }
//                                    }
//                                }
//
//                                override fun onFailure(p0: SenseArMaterial?, p1: Int, p2: String?) {
//                                }
//
//                                override fun onProgress(p0: SenseArMaterial?, p1: Float, p2: Int) {
//                                }
//
//                            })

                        return
                    }
                    EffectInfoDataHelper.getInstance().styleHigh = false
                    LogUtils.iTag(TAG, "onItemClickSticker: $position")
                    EffectInfoDataHelper.getInstance()
                        .setStrength(mCurrentTitleData?.type, strength)
                    mListener?.onItemClickMakeUp(
                        position,
                        mCurrentTitleData,
                        data,
                        selected,
                        strength,
                        adapter
                    )
                }
            })
        }

        sb_strength.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                if (fromUser) {
                    mStrengthsMap[mCurrentTitleData?.type] = progress

                    mCurrentTitleData?.let {
                        mListener?.onProgressChangedMakeUp(
                            it,
                            seekBar,
                            progress,
                            fromUser
                        )
                    }
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val pro = mStrengthsMap[mCurrentTitleData?.type]
                pro?.let {
                    EffectInfoDataHelper.getInstance().setStrength(mCurrentTitleData?.type, it)
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
            mListener?.onClickClearMakeupLinkage()

            for ((key, value) in mContentSelectedIndexMap) {
                EffectInfoDataHelper.getInstance().setContentSelectedIndex(key, value)
                EffectInfoDataHelper.getInstance().setStrength(key, defMakeUpStrength)
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

        // 控制seekBar显示或隐藏
        refreshUI()
    }

    private fun setDefContentSelectedIndex() {
        for (item in EffectType.makeupList)
            mContentSelectedIndexMap[item] = defMakeupIndex
    }

    private fun refreshUI() {
        sb_strength.visibility = INVISIBLE
        val contentSelectedIndex = mContentSelectedIndexMap[mCurrentTitleData?.type]
        if (contentSelectedIndex != null && contentSelectedIndex > 0) {
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

    fun clearContentSelected(type: EffectType) {
        LogUtils.iTag(TAG, "clearContentSelected: ${type.desc}")
        mContentSelectedIndexMap[type] = 0
        //setDefStrength()

        refreshUI()
    }

    override fun clearContentSelected() {
        setDefContentSelectedIndex()
        setDefStrength()

        refreshUI()
    }

    override fun init(
        titleData: ArrayList<BeautyOptionsItem>,
        contentData: HashMap<EffectType, MutableList<*>>?
    ) {
        mTitleAdapter.refreshData(titleData)

        contentData?.apply {
            mContentData.clear()
            mContentData.putAll(contentData)
            for (item in titleData) {
                val type = item.type
                val list: MutableList<*>? = contentData?.get(type)

                list?.apply {
                    //mContentAdaptersMap[type]?.refreshData(this)
                }
            }
        }

        val type = titleData[0].type
        mCurrentTitleData = titleData[0]
        mContentRecycler.adapter = mContentAdaptersMap[type]?.getAdapter()
        refreshUI()
    }

    override fun setData(contentData: EnumMap<EffectType, MutableList<*>>?) {
        TODO("Not yet implemented")
    }

    fun setDataNew(contentData: EnumMap<EffectType, MutableList<MakeupItem>>?) {
        contentData?.apply {
            LogUtils.iTag(TAG, "setData: ${contentData.size}")
            mContentData.clear()
            mContentData.putAll(contentData)
            for ((key, value) in contentData) {
                mContentAdaptersMap[key]?.refreshData(value)
            }
        }
        refreshUI()
    }

    private var mListener: Listener? = null

    interface Listener {
        /**
         * adapterStyle : 0 美妆
         * adapterStyle : 1 滤镜
         */
        fun onItemClickMakeUp(
            position: Int,
            titleEntity: BeautyOptionsItem?,
            contentEntity: LinkageEntity,
            selected: Boolean,
            strength: Float,
            adapter: RecyclerView.Adapter<*>
        )

        fun onClickClearMakeupLinkage()

        fun onProgressChangedMakeUp(
            titleEntity: BeautyOptionsItem,
            seekBar: SeekBar?,
            progress: Float,
            fromUser: Boolean
        )
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun onDestroy() {
        if (!hasSelected()) return
        LogUtils.iTag(TAG, "onDestroy() called")
        val effectTypes = mContentSelectedIndexMap.keys
        for (type in effectTypes) {
            mContentSelectedIndexMap[type]?.let {
                EffectInfoDataHelper.getInstance().setContentSelectedIndex(type,
                    it
                )
            }
        }
    }

    fun clearContentSelectedByType(item: STEffectBeautyInfo) {
        when (item.type) {
            401 -> {
                clearContentSelected(EffectType.TYPE_HAIR)
            }
            402 -> {
                clearContentSelected(EffectType.TYPE_LIP)
            }
            403 -> {
                clearContentSelected(EffectType.TYPE_BLUSH)
            }
            404 -> {
                clearContentSelected(EffectType.TYPE_XR)
            }
            405 -> {
                clearContentSelected(EffectType.TYPE_EYE_BROW)
            }
            406 -> {
                clearContentSelected(EffectType.TYPE_EYE_SHADOW)
            }
            407 -> {
                clearContentSelected(EffectType.TYPE_EYE_LINER)
            }
            408 -> {
                clearContentSelected(EffectType.TYPE_EYELASH)
            }
            409 -> {
                clearContentSelected(EffectType.TYPE_EYEBALL)
            }
        }
    }

    private fun hasSelected():Boolean {
        val effectTypes = mContentSelectedIndexMap.keys
        for (type in effectTypes) {
            val index = mContentSelectedIndexMap[type]
            if (null !=index && index >0) {
                return true
            }
        }
        return false
    }

}