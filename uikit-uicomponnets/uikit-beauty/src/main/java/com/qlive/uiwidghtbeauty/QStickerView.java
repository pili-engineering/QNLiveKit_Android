package com.qlive.uiwidghtbeauty;

import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_2D;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_3D;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_BG;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_HAND;
import static com.qlive.uiwidghtbeauty.utils.Constants.NEW_ENGINE;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qlive.uiwidghtbeauty.adapter.NativeStickerAdapter;
import com.qlive.uiwidghtbeauty.adapter.StickerAdapter;
import com.qlive.uiwidghtbeauty.adapter.StickerOptionsAdapter;
import com.qlive.uiwidghtbeauty.model.StickerItem;
import com.qlive.uiwidghtbeauty.model.StickerOptionsItem;
import com.qlive.uiwidghtbeauty.utils.Constants;
import com.qlive.uiwidghtbeauty.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class QStickerView extends FrameLayout {

    private Context mContext;
    private static final String TAG = "QSenseBeautyView";
    private RecyclerView mStickersRecycleView;
    private RecyclerView mStickerOptionsRecycleView;
    private ArrayList<StickerOptionsItem> mStickerOptionsList;
    private HashMap<String, StickerAdapter> mStickerAdapters = new HashMap<>();
    private HashMap<String, NativeStickerAdapter> mNativeStickerAdapters;
    private HashMap<String, ArrayList<StickerItem>> mStickerlists = new HashMap<>();
    private ArrayList<StickerItem> mNewStickers;
    private StickerOptionsAdapter mStickerOptionsAdapter;
    private RelativeLayout mStickerOptions;
    private int mCurrentStickerOptionsIndex = -1;
    private int mCurrentStickerPosition = -1;

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


    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.kit_sticker_view, this, true);

        // 贴纸面板
        mStickerOptions = findViewById(R.id.rl_sticker_options);
        // 贴纸相关视图
        mStickerOptionsRecycleView = findViewById(R.id.rv_sticker_options);
        mStickerOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mStickerOptionsRecycleView.addItemDecoration(new QSenseBeautyView.SpaceItemDecoration(0));

        mStickersRecycleView = findViewById(R.id.rv_sticker_icons);
        mStickersRecycleView.setLayoutManager(new GridLayoutManager(mContext, 6));
        mStickersRecycleView.addItemDecoration(new QSenseBeautyView.SpaceItemDecoration(0));

        mNewStickers = FileUtils.getStickerFiles(mContext, NEW_ENGINE);
        // 使用本地贴纸
        mStickerOptionsList = new ArrayList<>();
        mStickerOptionsList.add(0, new StickerOptionsItem(Constants.STICKER_NEW_ENGINE, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_local_unselected), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_local_selected)));
        // 2d
        mStickerOptionsList.add(1, new StickerOptionsItem(GROUP_2D, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_2d_unselected), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_2d_selected)));
        // 3d
        mStickerOptionsList.add(2, new StickerOptionsItem(GROUP_3D, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_3d_unselected), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_3d_selected)));
        // 手势贴纸
        mStickerOptionsList.add(3, new StickerOptionsItem(GROUP_HAND, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_hand_action_unselected), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_hand_action_selected)));
        // 背景贴纸
        mStickerOptionsList.add(4, new StickerOptionsItem(GROUP_BG, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_bg_segment_unselected), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sticker_bg_segment_selected)));

        mNativeStickerAdapters = new HashMap<>();
        mNativeStickerAdapters.put(Constants.STICKER_NEW_ENGINE, new NativeStickerAdapter(mNewStickers, mContext));
        mStickersRecycleView.setAdapter(mNativeStickerAdapters.get(Constants.STICKER_NEW_ENGINE));
        mNativeStickerAdapters.get(Constants.STICKER_NEW_ENGINE).notifyDataSetChanged();
        initNativeStickerAdapter(Constants.STICKER_NEW_ENGINE, 0);
        mStickerOptionsAdapter = new StickerOptionsAdapter(mStickerOptionsList, mContext);
        mStickerOptionsAdapter.setSelectedPosition(0);
        mStickerOptionsAdapter.notifyDataSetChanged();
        mStickerOptionsRecycleView.setAdapter(mStickerOptionsAdapter);

        // 当点击关闭贴纸时移除贴纸，并将视图和记录状态复原
        findViewById(R.id.rv_close_sticker).setOnClickListener(v -> {
            // 重置所有状态为未选中状态
            resetNewStickerAdapter();
            mCurrentStickerPosition = -1;
           QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setSticker("");
        });
        initStickerTabListener();

        if (mStickerOptionsRecycleView.getAdapter() == null) {
            mStickerOptionsRecycleView.setAdapter(mStickerOptionsAdapter);
        }
//        mStickerOptionsAdapter.setSelectedPosition(0);
//        mStickerOptionsAdapter.notifyDataSetChanged();
    }


    /**
     * 初始化 tab 点击事件
     */
    private void initStickerTabListener() {
        // tab 切换事件订阅
        mStickerOptionsAdapter.setClickStickerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStickerOptionsList == null || mStickerOptionsList.size() <= 0) {
                    Log.e(TAG, "group 列表不能为空");
                    return;
                }
                int position = Integer.parseInt(v.getTag().toString());
                mStickerOptionsAdapter.setSelectedPosition(position);
                mStickersRecycleView.setLayoutManager(new GridLayoutManager(mContext, 6));

                // 更新这一次的选择
                StickerOptionsItem selectedItem = mStickerOptionsAdapter.getPositionItem(position);
                if (selectedItem == null) {
                    Log.e(TAG, "选择项目不能为空!");
                    return;
                }
                RecyclerView.Adapter selectedAdapter = null;
                if (selectedItem.name.equals(Constants.STICKER_NEW_ENGINE)) {
                    selectedAdapter = mNativeStickerAdapters.get(selectedItem.name);
                } else {
                    selectedAdapter = mStickerAdapters.get(selectedItem.name);
                }

                if (selectedAdapter == null) {
                    Log.e(TAG, "贴纸 adapter 不能为空");
                    Toast.makeText(mContext, "列表正在拉取，或拉取出错!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mStickersRecycleView.setAdapter(selectedAdapter);
                mStickerOptionsAdapter.notifyDataSetChanged();
                selectedAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化贴纸的 adapter
     */
    private void initNativeStickerAdapter(final String stickerClassName, final int index) {
        mNativeStickerAdapters.get(stickerClassName).setSelectedPosition(-1);
        mNativeStickerAdapters.get(stickerClassName).setClickStickerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(v.getTag().toString());

                if (mCurrentStickerOptionsIndex == index && mCurrentStickerPosition == position) {
                    mNativeStickerAdapters.get(stickerClassName).setSelectedPosition(-1);
                    mCurrentStickerOptionsIndex = -1;
                    mCurrentStickerPosition = -1;

                   QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setSticker("");
                } else {
                    mCurrentStickerOptionsIndex = index;
                    mCurrentStickerPosition = position;

                    mNativeStickerAdapters.get(stickerClassName).setSelectedPosition(position);
                   QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setSticker(mNewStickers.get(position).path);
                }

                mNativeStickerAdapters.get(stickerClassName).notifyDataSetChanged();
            }
        });
    }

    /**
     * 重置贴纸的 adapter
     */
    private void resetNewStickerAdapter() {
        if (mNativeStickerAdapters.get(Constants.STICKER_NEW_ENGINE) != null) {
            mNativeStickerAdapters.get(Constants.STICKER_NEW_ENGINE).setSelectedPosition(-1);
            mNativeStickerAdapters.get(Constants.STICKER_NEW_ENGINE).notifyDataSetChanged();
        }
    }

}
