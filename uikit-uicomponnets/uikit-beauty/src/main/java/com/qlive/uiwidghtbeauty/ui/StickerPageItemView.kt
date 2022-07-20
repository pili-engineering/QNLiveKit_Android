package com.qlive.uiwidghtbeauty.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qlive.uiwidghtbeauty.adapter.NativeStickerAdapter
import com.qlive.uiwidghtbeauty.model.StickerItem
import com.qlive.uiwidghtbeauty.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class StickerPageItemView(
    context: Context,
    private val assetsIndex: String,
    private val groupIndex: String
) :
    RecyclerView(context) {
    private val mStickerItem = ArrayList<StickerItem>()
    var onStickerItemClick: (assetsIndex: String, groupIndex: String, item: StickerItem, itemIndex: Int) -> Unit =
        { _: String, _: String, _: StickerItem, _: Int ->
        }

    private val mNativeStickerAdapter by lazy {
        NativeStickerAdapter(mStickerItem, context).apply {
            setClickStickerListener {
                val position: Int = it.tag.toString().toInt()
                mStickerItem[position]
                onStickerItemClick.invoke(
                    assetsIndex,
                    groupIndex,
                    mStickerItem[position],
                    position
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelect(index: Int, groupIndex: String) {
        if (groupIndex == this.groupIndex) {
            mNativeStickerAdapter.setSelectedPosition(index)
        } else {
            mNativeStickerAdapter.setSelectedPosition(-1)
        }
        mNativeStickerAdapter.notifyDataSetChanged()
    }

    init {
        layoutManager =  GridLayoutManager(context, 6)
        addItemDecoration(SpaceItemDecoration(0));
    }

    fun attach(loadFromLocal:Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            val ret = async {
                if(loadFromLocal){
                    mStickerItem.addAll(FileUtils.getStickerFiles(context, assetsIndex))
                }else{
                    //todo 下载网络素材资源
                }
            }
            ret.await()
            adapter = mNativeStickerAdapter
        }
    }
}
