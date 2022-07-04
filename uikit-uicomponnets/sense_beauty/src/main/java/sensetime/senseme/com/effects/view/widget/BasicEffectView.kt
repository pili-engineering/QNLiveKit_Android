package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import sensetime.senseme.com.effects.utils.LogUtils
import kotlinx.android.synthetic.main.view_basic_effect.view.*
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.adapter.BeautyItemAdapter
import sensetime.senseme.com.effects.adapter.BeautyOptionsAdapter
import sensetime.senseme.com.effects.utils.EffectInfoDataHelper
import sensetime.senseme.com.effects.utils.LocalDataStore
import sensetime.senseme.com.effects.utils.RippleUtils
import sensetime.senseme.com.effects.utils.STUtils
import sensetime.senseme.com.effects.view.BeautyItem
import sensetime.senseme.com.effects.view.BeautyOptionsItem
import java.util.*

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 7/1/21 7:36 PM
 */
class BasicEffectView(context: Context) : LinearLayout(context) {

    companion object {
        const val TAG = "BasicEffectView"
        const val firstMouthPosition = 0
        const val firstNosePosition = 7
        const val firstEyePosition = 11
        const val firstFacePosition = 17
        const val defContentSelectedIndex = 0
    }

    private var mContentSelectedIndexMap: EnumMap<EffectType, Int> =
        EnumMap<EffectType, Int>(EffectType::class.java)

    private val mStrengthsMap: EnumMap<EffectType, Float> =
        EnumMap<EffectType, Float>(EffectType::class.java)

    private lateinit var mTitleAdapter: BeautyOptionsAdapter

    private var mCurrentTitleData: BeautyOptionsItem? = null
    private var mCurrentContentData: BeautyItem? = null
    private var mCurrentContentAdapter: BeautyItemAdapter? = null
    var hightFaceisViewShow = false

    private val mContentAdaptersMap: EnumMap<EffectType, BeautyItemAdapter> =
        EnumMap<EffectType, BeautyItemAdapter>(EffectType::class.java)

    init {
        val startTime = System.currentTimeMillis()
        initView(context)
        mContentAdaptersMap[EffectType.TYPE_BASE] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().beautyBaseList)
        mContentAdaptersMap[EffectType.TYPE_RESHAPE] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().professionalBeautyList)
        mContentAdaptersMap[EffectType.TYPE_PLASTIC] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().microBeautyList)
        mContentAdaptersMap[EffectType.TYPE_3D_PLASTIC] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().get3DWZhList())
        mContentAdaptersMap[EffectType.TYPE_TONE] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().adjustBeautyList)
        mContentAdaptersMap[EffectType.TYPE_MX_HIGH_THIN_FACE] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().highThinFaceBeautyList)
        mContentAdaptersMap[EffectType.TYPE_BODY] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().bodyBeautyList)
        mContentAdaptersMap[EffectType.TYPE_BOKEN] =
            BeautyItemAdapter(context, LocalDataStore.getInstance().bokenList)
        LogUtils.iTag(TAG, "init getlist cost time:" + (System.currentTimeMillis() - startTime))
        initData()

        setListener()

        mCurrentTitleData = LocalDataStore.getInstance().beautyOptionsList[0]
        mContentRecycler.adapter = mContentAdaptersMap[EffectType.TYPE_BASE]
        mCurrentContentAdapter = mContentAdaptersMap[mCurrentTitleData?.type]
        mCurrentContentData = mCurrentContentAdapter?.data?.get(0)
        LogUtils.iTag(TAG, "initData: $mCurrentContentAdapter title des: ${mCurrentTitleData?.type}")
        LogUtils.iTag(TAG, "init total cost time:" + (System.currentTimeMillis() - startTime))
    }

    fun setHighLight(
        param1: EffectType?,
        param2: EnumMap<EffectType, Int>?,
        strengthsMap: EnumMap<EffectType, Float>?
    ) {
        LogUtils.iTag(TAG, "setHighLight: $strengthsMap")
        param1?.let {
            getTitleEntityByEnum(param1)?.let { it1 ->
                mTitleAdapter.setSelectedPosition(getIndexByEnum(param1))
                onClickTitleAdapter(
                    it1,
                    getIndexByEnum(param1)
                )
            }
        }

        // 设置二级菜单子项高亮
        param2?.let {
            mContentSelectedIndexMap.putAll(param2)
            refreshUI()
        }

        strengthsMap?.let {
            for ((key, value) in strengthsMap) {
                //Log.d(TAG, "setHighLight: ${strengthsMap[item]}")
                //mStrengthsMap[item] = strengthsMap[]
                mStrengthsMap.putAll(strengthsMap)
            }

            for ((type, adapter) in mContentAdaptersMap) {
                for (entity in adapter.data) {
                    val strength = strengthsMap[entity.type]
                    if (strength != null) {
                        entity.progress = strength
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
        refreshUI()
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

    private fun setListener() {
        mContentRecycler.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mCurrentTitleData?.type!=EffectType.TYPE_3D_PLASTIC) return
                if (recyclerView.childCount > 0) {
                    try {
                        val currentPosition =
                            (recyclerView.getChildAt(0).layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
                        Log.e("=====currentPosition", "" + currentPosition)
                        val p = 2
                        when (currentPosition) {
                            in firstMouthPosition..(firstNosePosition-p) -> {//zui
                                on3dWZhBottomBtnClick(tv_mouth, true)
                            }
                            in firstNosePosition-p..firstEyePosition-p -> {// 第7个鼻子
                                on3dWZhBottomBtnClick(tv_nose, true)
                            }
                            in firstEyePosition-p..firstFacePosition-p -> {// 眼睛
                                on3dWZhBottomBtnClick(tv_eye, true)
                            }
                            in firstFacePosition-p..28-p -> {// face
                                on3dWZhBottomBtnClick(tv_face, true)
                            }
//                            in 22-p..24-p -> {//tou
//                                on3dWZhBottomBtnClick(tv_head, true)
//                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
        for(tabView in arrayOf(tv_eye, tv_nose, tv_mouth, tv_head, tv_face)) {
            tabView.setOnClickListener {
                on3dWZhBottomBtnClick(it, false)
            }
        }

        dt_basic_reset.setOnClickListener {
            mCurrentTitleData?.let { it1 -> mListener?.onClickResetBasicEffect(it1) }
        }

        // 清零
        dt_basic_clear.setOnClickListener {
            LogUtils.iTag(TAG, "setListener: ${mCurrentTitleData?.type?.desc}")
            when (mCurrentTitleData?.type) {
                EffectType.TYPE_BASE -> {
                    for (item in LocalDataStore.getInstance().beautyBaseList) {
                        mStrengthsMap[item.type] = 0f
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        LogUtils.iTag(TAG, "name:${item.type.desc}, code: ${item.type.code}")
                        mListener?.onProgressChangedBasicEffect(item, 0f, false)
                    }
                }
                EffectType.TYPE_RESHAPE -> {
                    for (item in LocalDataStore.getInstance().professionalBeautyList) {
                        mStrengthsMap[item.type] = 0f
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        LogUtils.iTag(TAG, "name:${item.type.desc}, code: ${item.type.code}")
                        mListener?.onProgressChangedBasicEffect(item, 0f, false)
                    }

                    for (item in LocalDataStore.getInstance().highThinFaceBeautyList) {
                        mStrengthsMap[item.type] = 0f
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        LogUtils.iTag(TAG, "name:${item.type.desc}, code: ${item.type.code}")
                        mListener?.onProgressChangedBasicEffect(item, 0f, false)
                    }
                }
                EffectType.TYPE_PLASTIC -> {
                    for (item in LocalDataStore.getInstance().microBeautyList) {
                        mStrengthsMap[item.type] = 0f
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        LogUtils.iTag(TAG, "name:${item.type.desc}, code: ${item.type.code}")
                        mListener?.onProgressChangedBasicEffect(item, 0f, false)
                    }
                }
                EffectType.TYPE_TONE -> {
                    for (item in LocalDataStore.getInstance().adjustBeautyList) {
                        mStrengthsMap[item.type] = 0f
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        LogUtils.iTag(TAG, "name:${item.type.desc}, code: ${item.type.code}")
                        mListener?.onProgressChangedBasicEffect(item, 0f, false)
                    }
                }
                EffectType.TYPE_3D_PLASTIC -> {
                    for (item in LocalDataStore.getInstance().get3DWZhList()) {
                        mStrengthsMap[item.type] = 0f
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        LogUtils.iTag(TAG, "name:${item.type.desc}, code: ${item.type.code}")
                        mListener?.onProgressChangedBasicEffect(item, 0f, false)
                    }
                }
                EffectType.TYPE_BOKEN -> {
                    for (item in LocalDataStore.getInstance().bokenList) {
                        mStrengthsMap[item.type] = 0f
                        EffectInfoDataHelper.getInstance().setStrength(item.type, 0f)
                        LogUtils.iTag(TAG, "name:${item.type.desc}, code: ${item.type.code}")
                        mListener?.onProgressChangedBasicEffect(item, 0f, false)
                    }
                }
            }
            val list = mContentAdaptersMap[mCurrentTitleData?.type]?.data
            if (!list.isNullOrEmpty()) {
                for (item in list) {
                    item.progress = 0f
                }
            }
            mCurrentContentAdapter?.notifyDataSetChanged()
            if (mCurrentTitleData?.type == EffectType.TYPE_RESHAPE) {
                val listHigh = mContentAdaptersMap[EffectType.TYPE_MX_HIGH_THIN_FACE]?.data
                if (!listHigh.isNullOrEmpty()) {
                    for (item in listHigh) {
                        item.progress = 0f
                    }
                }
                mContentAdaptersMap[EffectType.TYPE_MX_HIGH_THIN_FACE]?.notifyDataSetChanged()
            }

            refreshUI()
        }

        mTitleAdapter.setOnItemClickListener { position, entity ->
            onClickTitleAdapter(entity, position)
        }

        for ((_, value) in mContentAdaptersMap) {
            value.setListener { position, item ->
                when {
                    EffectType.wzh3dMouthList.contains(item.type) -> {
                        on3dWZhBottomBtnClick(tv_mouth, true)
                    }
                    EffectType.wzh3dNoseList.contains(item.type) -> {
                        on3dWZhBottomBtnClick(tv_nose, true)
                    }
                    EffectType.wzh3dEyeList.contains(item.type) -> {
                        on3dWZhBottomBtnClick(tv_eye, true)
                    }
//                    EffectType.wzh3dHeadList.contains(item.type) -> {
//                        on3dWZhBottomBtnClick(tv_head, true)
//                    }
                    EffectType.wzh3dFaceList.contains(item.type) -> {
                        on3dWZhBottomBtnClick(tv_face, true)
                    }
                }

                when (item.type) {
                    EffectType.TYPE_BASIC_1 -> {
                        if (mCurrentContentData?.type != EffectType.TYPE_BASIC_1) {
                            //setMutual(EffectType.TYPE_BASIC_1)
                            val strength = mStrengthsMap[EffectType.TYPE_BASIC_1] ?: 0f
                            //mListener?.onClickWhitening1(strength)
                        }
                    }
                    EffectType.TYPE_BASIC_2 -> {
                        if (mCurrentContentData?.type != EffectType.TYPE_BASIC_2) {
//                            setMutual(EffectType.TYPE_BASIC_2)
                            val strength = mStrengthsMap[EffectType.TYPE_BASIC_2] ?: 0f
                            //mListener?.onClickWhitening2(strength)
                        }
                    }
                    EffectType.TYPE_BASIC_3 -> {
                        if (mCurrentContentData?.type != EffectType.TYPE_BASIC_3) {
//                            setMutual(EffectType.TYPE_BASIC_3)
                            val strength = mStrengthsMap[EffectType.TYPE_BASIC_2] ?: 0f
                            //mListener?.onClickWhitening3(0f)
                        }
                    }
                }

                mCurrentContentData = item
                if (EffectType.highThinFaceList.indexOf(item.type) >= 0) {
                    mContentSelectedIndexMap[EffectType.TYPE_MX_HIGH_THIN_FACE] =
                        position
                } else {
                    mContentSelectedIndexMap[mCurrentTitleData?.type] = position
                }

                //mContentSelectedIndexMap[mCurrentTitleData?.type] = position
                refreshUI()
                LogUtils.iTag(TAG, "click 1 : ${item.type.desc}")

                when (item.type) {
                    EffectType.TYPE_MX_HIGH_THIN_FACE -> {
                        hightFaceisViewShow = true
                        mCurrentContentAdapter =
                            mContentAdaptersMap[EffectType.TYPE_MX_HIGH_THIN_FACE]
                        LogUtils.iTag(TAG, "click : ${item.type.desc}")
                        mContentRecycler.adapter = mContentAdaptersMap[item.type]
                        sb_strength.visibility = View.INVISIBLE
                    }
                    EffectType.TYPE_HIGH_BACK -> {
                        hightFaceisViewShow = false
                        mCurrentContentAdapter = mContentAdaptersMap[EffectType.TYPE_RESHAPE]
                        sb_strength.visibility = View.INVISIBLE
                        mContentRecycler.adapter = mContentAdaptersMap[mCurrentTitleData?.type]
                    }
                    else -> {
                        sb_strength.visibility = View.VISIBLE
                    }
                }
            }
        }
        sb_strength.setListener(object : TextThumbSeekBar.Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                if (fromUser) {
                    if (mCurrentContentData?.type == null) {
                        LogUtils.iTag(TAG, "onProgressChanged: is null ")
//                        return
                    } else {
                        LogUtils.iTag(TAG, "onProgressChanged: is not null ")
                    }
                    LogUtils.iTag(TAG, "onProgressChanged: progress$progress")

                    // 处理互斥
                    when (mCurrentContentData?.type) {
                        EffectType.TYPE_BASIC_1 -> {
                            mStrengthsMap[EffectType.TYPE_BASIC_2] = 0f
                            mStrengthsMap[EffectType.TYPE_BASIC_3] = 0f
                        }
                        EffectType.TYPE_BASIC_2 -> {
                            mStrengthsMap[EffectType.TYPE_BASIC_1] = 0f
                            mStrengthsMap[EffectType.TYPE_BASIC_3] = 0f
                        }
                        EffectType.TYPE_BASIC_3 -> {
                            mStrengthsMap[EffectType.TYPE_BASIC_1] = 0f
                            mStrengthsMap[EffectType.TYPE_BASIC_2] = 0f
                        }
                        EffectType.TYPE_BASIC_5 -> {
                            mStrengthsMap[EffectType.TYPE_BASIC_6] = 0f
                        }
                        EffectType.TYPE_BASIC_6 -> {// 磨皮2
                            mStrengthsMap[EffectType.TYPE_BASIC_5] = 0f
                        }
                    }

                    LogUtils.iTag(
                        TAG,
                        "onProgressChanged: ${mCurrentContentData?.type?.desc} $progress code:${mCurrentContentData?.type?.code} "
                    )
                    mStrengthsMap[mCurrentContentData?.type] = progress
                    mCurrentContentData?.progress = progress
                    mCurrentTitleData?.type?.let { setMutual(it) }
                    mContentAdaptersMap[mCurrentTitleData?.type]?.notifyDataSetChanged()
                    mCurrentContentData?.let {
                        mListener?.onProgressChangedBasicEffect(
                            it,
                            progress,
                            fromUser
                        )
                    }
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                LogUtils.iTag(
                    TAG,
                    "onStopTrackingTouch: ${mCurrentContentData?.type?.desc} strength: ${mStrengthsMap[mCurrentContentData?.type]}"
                )
                mCurrentContentData?.type?.let { setMutual(it) }
                LogUtils.iTag(TAG, "onStopTrackingTouch: ${mCurrentTitleData?.type?.desc}")
                mContentAdaptersMap[mCurrentTitleData?.type]?.notifyDataSetChanged()
                mCurrentContentAdapter?.notifyDataSetChanged()
                mContentAdaptersMap[EffectType.TYPE_MX_HIGH_THIN_FACE]?.notifyDataSetChanged()

                mStrengthsMap[mCurrentContentData?.type]?.let {
                    EffectInfoDataHelper.getInstance().setStrength(mCurrentContentData?.type, it)
                }
            }
        })
    }

    private fun setMutual(currType: EffectType) {
        var list: ArrayList<EffectType>? = null
        when {
            EffectType.mutualSmoothList.contains(currType) -> {
                list = EffectType.mutualSmoothList
            }
            EffectType.mutualWhiteList.contains(currType) -> {
                list = EffectType.mutualWhiteList
            }
            EffectType.mutualHighFaceList.contains(currType) -> {
                list = EffectType.mutualHighFaceList
            }
            EffectType.mutualBokenList.contains(currType) -> {
                list = EffectType.mutualBokenList
            }
//            EffectType.mutualThinHeadList.contains(currType) -> {
//                list = EffectType.mutualThinHeadList
//            }
        }

        list?.let {
            for (type in list) {
                if (type != currType) {
                    mStrengthsMap[type] = 0f
                    EffectInfoDataHelper.getInstance().setStrength(type, 0f)
                    //val itemEntity = mContentAdaptersMap[mCurrentTitleData?.type]?.getItemTyType(type)
                    val itemEntity = mCurrentContentAdapter?.getItemTyType(type)
                    itemEntity?.progress = 0f
                    LogUtils.iTag(TAG, "setMutual: ${itemEntity.toString()}")
                }
            }

        }
    }

    private fun onClickTitleAdapter(
        entity: BeautyOptionsItem,
        position: Int,
    ) {
        mCurrentTitleData = entity
        mCurrentContentAdapter = mContentAdaptersMap[mCurrentTitleData?.type]

        //val contentSelectedIndex = mContentSelectedIndexMap[entity.type]?:0
        val contentSelectedIndex =
            if (mCurrentTitleData?.type == EffectType.TYPE_RESHAPE && hightFaceisViewShow) {
                mContentSelectedIndexMap[EffectType.TYPE_MX_HIGH_THIN_FACE] ?: 0
            } else {
                mContentSelectedIndexMap[entity.type] ?: 0
            }
//        if (null == i) {
//            mContentSelectedIndexMap[entity.type] = 0
//        }
        LogUtils.iTag(TAG, "onClickTitleAdapter 当前二级菜单选中项:${contentSelectedIndex} , type: ${entity.type}")

        mCurrentContentData =
            if (mCurrentTitleData?.type == EffectType.TYPE_RESHAPE && hightFaceisViewShow) {
                mContentAdaptersMap[EffectType.TYPE_MX_HIGH_THIN_FACE]?.getItem(contentSelectedIndex)
            } else {
                mContentAdaptersMap[entity.type]?.getItem(contentSelectedIndex)
            }

        LogUtils.iTag(TAG, "当前二级菜单选中内容: ${mCurrentContentData.toString()}")

        mTitleAdapter.setSelectedPosition(position)
        entity?.apply {
            if (mCurrentTitleData?.type == EffectType.TYPE_RESHAPE) {
                if (hightFaceisViewShow) {
                    mContentRecycler.adapter =
                        mContentAdaptersMap[EffectType.TYPE_MX_HIGH_THIN_FACE]
                } else {
                    mContentRecycler.adapter = mContentAdaptersMap[EffectType.TYPE_RESHAPE]
                }
                ll_bottom.visibility = View.INVISIBLE
            } else {
                if (mCurrentTitleData?.type == EffectType.TYPE_3D_PLASTIC) {
                    on3dWZhBottomBtnClick(tv_mouth, true)
                    ll_bottom.visibility = View.VISIBLE
                } else {
                    ll_bottom.visibility = View.INVISIBLE
                }
                mContentRecycler.adapter = mContentAdaptersMap[entity.type]
            }
        }

        // 控制seekBar显示或隐藏
        refreshUI()
    }


    private fun refreshUI() {
        LogUtils.iTag(TAG, "refreshUI:${mCurrentContentData?.type?.desc}  ${mCurrentContentData?.progress}")
        // 刷新seekBar
        mStrengthsMap[mCurrentContentData?.type]?.apply {
            if (mCurrentContentData?.type?.startCenterSeekBar == true) {
                sb_strength.setShowType(TextThumbSeekBar.Type.START_CENTER)
            } else {
                sb_strength.setShowType(TextThumbSeekBar.Type.START_LEFT)
            }
            sb_strength.setValue(this)
        }
        if (mCurrentContentData?.type == EffectType.TYPE_HIGH_BACK || mCurrentContentData?.type == EffectType.TYPE_MX_HIGH_THIN_FACE) {
            sb_strength.visibility = View.INVISIBLE
        } else {
            sb_strength.visibility = View.VISIBLE
        }
    }

    private fun initData() {
        for (type in EffectType.getAllBasicType()) {
            mStrengthsMap[type] = 0f
        }
        setDefContentSelectedIndex()
        mTitleAdapter.refreshData(LocalDataStore.getInstance().beautyOptionsList)
    }

    private fun setDefContentSelectedIndex() {
        for (item in EffectType.basicList)
            mContentSelectedIndexMap[item] = defContentSelectedIndex
    }


    lateinit var mIbShowOriginal: ImageButton

    private fun setOpenVar() {
        mIbShowOriginal = ib_show_original
    }

    private fun scrollToPosition(recyclerView: RecyclerView, position: Int) {
        if (position != -1) {
            recyclerView.scrollToPosition(position);
            val mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            mLayoutManager.scrollToPositionWithOffset(position, 0)
        }
    }

    private fun on3dWZhBottomBtnClick(view:View, onlyUI:Boolean) {
        if (view.isSelected) {
            return
        }
        for(tabView in arrayOf(tv_eye, tv_nose, tv_mouth, tv_head, tv_face)) {
            tabView.setBackgroundResource(0)
            tabView.isSelected = false
        }
        if (!onlyUI) {
            when(view) {
                tv_eye-> {
                    scrollToPosition(mContentRecycler, firstEyePosition)
                }
                tv_nose-> {
                    scrollToPosition(mContentRecycler, firstNosePosition)
                }
                tv_mouth-> {
                    scrollToPosition(mContentRecycler, firstMouthPosition)
                }
//                tv_head-> {
//                    scrollToPosition(mContentRecycler, firstHeadPosition)
//                }
                tv_face-> {
                    scrollToPosition(mContentRecycler, firstFacePosition)
                }
            }
        }

        view.setBackgroundResource(R.drawable.ic_wzh_3d_white)
        view.isSelected = true
    }


    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_basic_effect, this, true)
        RippleUtils.setForeground(context, dt_basic_clear, dt_basic_reset)
        setOpenVar()
        initTitleRecyclerView()
        initContentRecyclerView()
        tv_eye.isSelected = true
    }

    private fun initTitleRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mTitleRecycleView.layoutManager = ms

        // 标题的listView设置
        mTitleAdapter = BeautyOptionsAdapter(context)
        mTitleRecycleView.adapter = mTitleAdapter
    }

    private fun initContentRecyclerView() {
        val ms = LinearLayoutManager(context)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        mContentRecycler.layoutManager = ms
        mContentRecycler.addItemDecoration(BeautyItemDecoration(STUtils.dip2px(context, 15f)))
    }

    internal class BeautyItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = space
            outRect.right = space
        }
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onProgressChangedBasicEffect(
            contentEntity: BeautyItem,
            progress: Float,
            fromUser: Boolean,
        )

//        fun onClickWhitening1(strength: Float)
//        fun onClickWhitening2(strength: Float)
//        fun onClickWhitening3(strength: Float)

        fun onClickResetBasicEffect(currentTitleData: BeautyOptionsItem)
    }

}