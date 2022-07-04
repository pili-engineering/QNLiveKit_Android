package com.qlive.uiwidghtbeauty;

import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_2D;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_3D;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_BG;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_HAND;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.qlive.uiwidghtbeauty.adapter.StickerOptionsAdapter;
import com.qlive.uiwidghtbeauty.model.StickerOptionsItem;
import com.qlive.uiwidghtbeauty.sticker.StickerPageItemView;
import com.qlive.uiwidghtbeauty.ui.CommonViewPagerAdapter;
import com.qlive.uiwidghtbeauty.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QStickerView extends FrameLayout {

    private static final String TAG = "QSenseBeautyView";
    private ArrayList<StickerOptionsItem> mStickerOptionsList;
    private StickerOptionsAdapter mStickerOptionsAdapter;
    private ViewPager vpStickerPage;
    private List<StickerPageItemView> pageItemViewList;

    public QStickerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public QStickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QStickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.kit_sticker_view, this, true);
        vpStickerPage = findViewById(R.id.vp_sticker_page);
        // 贴纸相关视图
        RecyclerView mStickerOptionsRecycleView = findViewById(R.id.rv_sticker_options);
        mStickerOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mStickerOptionsRecycleView.addItemDecoration(new QSenseBeautyView.SpaceItemDecoration(0));

        //mNewStickers = FileUtils.getStickerFiles(mContext, NEW_ENGINE);
        // 使用本地贴纸
        mStickerOptionsList = new ArrayList<>();
        mStickerOptionsList.add(0, new StickerOptionsItem(Constants.STICKER_NEW_ENGINE, BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_local_unselected), BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_local_selected)));
        // 2d
        mStickerOptionsList.add(1, new StickerOptionsItem(GROUP_2D, BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_2d_unselected), BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_2d_selected)));
        // 3d
        mStickerOptionsList.add(2, new StickerOptionsItem(GROUP_3D, BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_3d_unselected), BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_3d_selected)));
        // 手势贴纸
        mStickerOptionsList.add(3, new StickerOptionsItem(GROUP_HAND, BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_hand_action_unselected), BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_hand_action_selected)));
        // 背景贴纸
        mStickerOptionsList.add(4, new StickerOptionsItem(GROUP_BG, BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_bg_segment_unselected), BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_bg_segment_selected)));

        pageItemViewList = new ArrayList<>();
        pageItemViewList.add(new StickerPageItemView(context, Constants.NEW_ENGINE, Constants.STICKER_NEW_ENGINE));
        pageItemViewList.add(new StickerPageItemView(context, "2D", Constants.GROUP_2D));
        pageItemViewList.add(new StickerPageItemView(context, "3D", Constants.GROUP_3D));
        pageItemViewList.add(new StickerPageItemView(context, "hand_action", Constants.GROUP_HAND));
        pageItemViewList.add(new StickerPageItemView(context, "segment", Constants.GROUP_BG));


        vpStickerPage.setAdapter(new CommonViewPagerAdapter(pageItemViewList));
        for (StickerPageItemView stickerPageItemView : pageItemViewList) {
            stickerPageItemView.attach(true);
            stickerPageItemView.setOnStickerItemClick((s, groupIndex, stickerItem, integer) -> {
                Objects.requireNonNull(QSenseTimeManager.INSTANCE.getSSenseTimePlugin()).setSticker(stickerItem.path);
                setSelect(integer, groupIndex);
                return null;
            });
        }
        // 当点击关闭贴纸时移除贴纸，并将视图和记录状态复原
        findViewById(R.id.rv_close_sticker).setOnClickListener(v -> {
            // 重置所有状态为未选中状态
            setSelect(-1, "xxx");
            Objects.requireNonNull(QSenseTimeManager.INSTANCE.getSSenseTimePlugin()).setSticker("");
        });
        mStickerOptionsAdapter = new StickerOptionsAdapter(mStickerOptionsList, context);
        mStickerOptionsAdapter.setSelectedPosition(0);
        mStickerOptionsAdapter.notifyDataSetChanged();
        mStickerOptionsRecycleView.setAdapter(mStickerOptionsAdapter);
        initStickerTabListener();
        if (mStickerOptionsRecycleView.getAdapter() == null) {
            mStickerOptionsRecycleView.setAdapter(mStickerOptionsAdapter);
        }
    }

    private void setSelect(int index, String groupIndex) {
        for (StickerPageItemView stickerPageItemView : pageItemViewList) {
            stickerPageItemView.setSelect(index, groupIndex);
        }
    }

    /**
     * 初始化 tab 点击事件
     */
    private void initStickerTabListener() {
        // tab 切换事件订阅
        mStickerOptionsAdapter.setClickStickerListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if (mStickerOptionsList == null || mStickerOptionsList.size() <= 0) {
                    Log.e(TAG, "group 列表不能为空");
                    return;
                }
                int position = Integer.parseInt(v.getTag().toString());
                mStickerOptionsAdapter.setSelectedPosition(position);
                mStickerOptionsAdapter.notifyDataSetChanged();
                vpStickerPage.setCurrentItem(position, true);

            }
        });
    }
}
