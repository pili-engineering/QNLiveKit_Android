package com.qlive.uiwidghtbeauty;

import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_2D;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_3D;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_BG;
import static com.qlive.uiwidghtbeauty.utils.Constants.GROUP_HAND;
import static com.qlive.uiwidghtbeauty.utils.Constants.NEW_ENGINE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.qlive.uiwidghtbeauty.model.EffectState;
import com.qlive.uiwidghtbeauty.model.StickerItem;
import com.qlive.uiwidghtbeauty.model.StickerOptionsItem;
import com.qlive.uiwidghtbeauty.utils.Constants;
import com.qlive.uiwidghtbeauty.utils.FileUtils;
import com.qlive.uiwidghtbeauty.utils.ToastUtils;
import com.qlive.uiwidghtbeauty.utils.Utils;
import com.sensetime.sensearsourcemanager.SenseArMaterial;
import com.sensetime.sensearsourcemanager.SenseArMaterialService;
import com.sensetime.sensearsourcemanager.SenseArMaterialType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    // 记录用户最后一次点击的素材id ,包括还未下载的，方便下载完成后，直接应用素材
    private String preMaterialId = "";

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

       // fetchStickerGroupMaterialList(mStickerOptionsList);
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
//
//    /**
//     * 根据 group id 取得对应贴纸素材列表
//     *
//     * @param groups group id 列表
//     */
//    private void fetchStickerGroupMaterialList(final List<StickerOptionsItem> groups) {
//        for (int i = 0; i < groups.size(); i++) {
//            final StickerOptionsItem groupId = groups.get(i);
//            if (!groupId.name.equals(Constants.STICKER_NEW_ENGINE)) {
//                // 使用网络下载
//                final int j = i;
//                // 从服务器拉取素材
//                // 该接口仅作为 demo 层的展示，不做开放使用。若想从自己的服务器拉取素材，需自主实现。
//                SenseArMaterialService.shareInstance().fetchMaterialsFromGroupId("", groupId.name, SenseArMaterialType.Effect, new SenseArMaterialService.FetchMaterialListener() {
//                    @Override
//                    public void onSuccess(final List<SenseArMaterial> materials) {
//                        fetchStickerGroupMaterialInfo(groupId.name, materials, j);
//                    }
//
//                    @Override
//                    public void onFailure(int code, String message) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtils.showShortToast(QSenseTimeManager.INSTANCE.getSAppContext(), "下载素材信息失败" + code + "  " + message);
//                            }
//                        });
//                    }
//                });
//            }
//        }
//    }
//
//    /**
//     * 初始化素材的基本信息，如缩略图，是否已经缓存
//     *
//     * @param groupId   组id
//     * @param materials 服务器返回的素材list
//     */
//    private void fetchStickerGroupMaterialInfo(final String groupId, final List<SenseArMaterial> materials, final int index) {
//        if (materials == null || materials.size() <= 0) {
//            return;
//        }
//        final ArrayList<StickerItem> stickerList = new ArrayList<>();
//        mStickerlists.put(groupId, stickerList);
//        mStickerAdapters.put(groupId, new StickerAdapter(mStickerlists.get(groupId), QSenseTimeManager.INSTANCE.getSAppContext()));
//        mStickerAdapters.get(groupId).setSelectedPosition(-1);
//        initStickerListener(groupId, index, materials);
//        for (int i = 0; i < materials.size(); i++) {
//            SenseArMaterial sarm = materials.get(i);
//            Bitmap bitmap = null;
//            try {
//                bitmap = Utils.getImageSync(sarm.thumbnail, QSenseTimeManager.INSTANCE.getSAppContext());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (bitmap == null) {
//                bitmap = BitmapFactory.decodeResource(QSenseTimeManager.INSTANCE.getSAppContext().getResources(), R.drawable.none);
//            }
//            String path = "";
//            //如果已经下载则传入路径地址
//            if (SenseArMaterialService.shareInstance().isMaterialDownloaded(QSenseTimeManager.INSTANCE.getSAppContext(), sarm)) {
//                path = SenseArMaterialService.shareInstance().getMaterialCachedPath(QSenseTimeManager.INSTANCE.getSAppContext(), sarm);
//            }
//            stickerList.add(new StickerItem(sarm.name, bitmap, path));
//        }
//    }
//
//    /**
//     * 初始化素材列表中的点击事件回调
//     *
//     * @param groupId
//     * @param index
//     * @param materials
//     */
//    private void initStickerListener(final String groupId, final int index, final List<SenseArMaterial> materials) {
//        mStickerAdapters.get(groupId).setClickStickerListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!Utils.isNetworkAvailable(QSenseTimeManager.INSTANCE.getSAppContext())) {
//                    runOnUiThread(() -> Toast.makeText(QSenseTimeManager.INSTANCE.getSAppContext(), "Network unavailable.", Toast.LENGTH_LONG).show());
//                }
//
//                final int position = Integer.parseInt(v.getTag().toString());
//                final StickerItem stickerItem = mStickerAdapters.get(groupId).getItem(position);
//                if (stickerItem != null && stickerItem.state == EffectState.LOADING_STATE) {
//                    ToastUtils.showShortToast(QSenseTimeManager.INSTANCE.getSAppContext(), String.format(Locale.getDefault(), "正在下载，请稍后点击!"));
//                    return;
//                }
//
//                if (mCurrentStickerOptionsIndex == index && mCurrentStickerPosition == position) {
//                    preMaterialId = "";
//                    mStickerAdapters.get(groupId).setSelectedPosition(-1);
//                    mCurrentStickerOptionsIndex = -1;
//                    mCurrentStickerPosition = -1;
//
//                    findViewById(R.id.iv_close_sticker).setBackground(getResources().getDrawable(R.drawable.close_sticker_selected));
//                    QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setSticker("");
//                    mStickerAdapters.get(groupId).notifyDataSetChanged();
//                    return;
//                }
//                SenseArMaterial sarm = materials.get(position);
//                preMaterialId = sarm.id;
//                //如果素材还未下载，点击时需要下载
//                if (stickerItem.state == EffectState.NORMAL_STATE) {
//                    stickerItem.state = EffectState.LOADING_STATE;
//                    notifyStickerViewState(stickerItem, position, groupId);
//                    SenseArMaterialService.shareInstance().downloadMaterial(QSenseTimeManager.INSTANCE.getSAppContext(), sarm, new SenseArMaterialService.DownloadMaterialListener() {
//                        @Override
//                        public void onSuccess(final SenseArMaterial material) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    stickerItem.path = material.cachedPath;
//                                    stickerItem.state = EffectState.DONE_STATE;
//                                    //如果本次下载是用户用户最后一次选中项，则直接应用
//                                    if (preMaterialId.equals(material.id)) {
//                                        resetNewStickerAdapter();
//                                        resetStickerAdapter();
//                                        mCurrentStickerOptionsIndex = index;
//                                        mCurrentStickerPosition = position;
//                                        findViewById(R.id.iv_close_sticker).setBackground(getResources().getDrawable(R.drawable.close_sticker));
//
//                                        mStickerAdapters.get(groupId).setSelectedPosition(position);
//                                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setSticker(stickerItem.path);
//                                    }
//                                    notifyStickerViewState(stickerItem, position, groupId);
//                                }
//                            });
//                            Log.i(TAG, String.format(Locale.getDefault(), "素材下载成功:%s,cached path is %s", material.materials, material.cachedPath));
//                        }
//
//                        @Override
//                        public void onFailure(SenseArMaterial material, final int code, String message) {
//                            ToastUtils.showShortToast(QSenseTimeManager.INSTANCE.getSAppContext(), String.format(Locale.getDefault(), "素材下载失败:%s", material.materials));
//                            runOnUiThread(() -> {
//                                stickerItem.state = EffectState.NORMAL_STATE;
//                                notifyStickerViewState(stickerItem, position, groupId);
//                            });
//                        }
//
//                        @Override
//                        public void onProgress(SenseArMaterial material, float progress, int size) {
//
//                        }
//                    });
//                } else if (stickerItem.state == EffectState.DONE_STATE) {
//                    resetNewStickerAdapter();
//                    resetStickerAdapter();
//                    mCurrentStickerOptionsIndex = index;
//                    mCurrentStickerPosition = position;
//
//                    findViewById(R.id.iv_close_sticker).setBackground(getResources().getDrawable(R.drawable.close_sticker));
//
//                    mStickerAdapters.get(groupId).setSelectedPosition(position);
//                    QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setSticker(mStickerlists.get(groupId).get(position).path);
//                }
//            }
//        });
//    }
//
//    private void runOnUiThread(Runnable runnable
//    ) {
//        post(runnable);
//    }
//
//    /**
//     * 直接变更 ui ,不通过数据驱动，相比 notifyDataSetChanged 反应会快些
//     *
//     * @param stickerItem
//     * @param position
//     * @param name
//     */
//    public void notifyStickerViewState(StickerItem stickerItem, int position, String name) {
//        RecyclerView.ViewHolder viewHolder = mStickersRecycleView.findViewHolderForAdapterPosition(position);
//        //排除不必要变更
//        if (viewHolder == null || mStickersRecycleView.getAdapter() != mStickerAdapters.get(name)) {
//            return;
//        }
//        View itemView = viewHolder.itemView;
//        ImageView normalState = itemView.findViewById(R.id.normalState);
//        ImageView downloadingState = itemView.findViewById(R.id.downloadingState);
//        ViewGroup loadingStateParent = itemView.findViewById(R.id.loadingStateParent);
//        switch (stickerItem.state) {
//            case NORMAL_STATE:
//                //设置为等待下载状态
//                if (normalState.getVisibility() != View.VISIBLE) {
//                    normalState.setVisibility(View.VISIBLE);
//                    downloadingState.setVisibility((View.INVISIBLE));
//                    downloadingState.setActivated(false);
//                    loadingStateParent.setVisibility((View.INVISIBLE));
//                }
//                break;
//            case LOADING_STATE:
//                //设置为loading 状态
//                if (downloadingState.getVisibility() != View.VISIBLE) {
//                    normalState.setVisibility(View.INVISIBLE);
//                    downloadingState.setActivated(true);
//                    downloadingState.setVisibility((View.VISIBLE));
//                    loadingStateParent.setVisibility((View.VISIBLE));
//                }
//                break;
//            case DONE_STATE:
//                //设置为下载完成状态
//                if (normalState.getVisibility() != View.INVISIBLE || downloadingState.getVisibility() != View.INVISIBLE) {
//                    normalState.setVisibility(View.INVISIBLE);
//                    downloadingState.setVisibility((View.INVISIBLE));
//                    downloadingState.setActivated(false);
//                    loadingStateParent.setVisibility((View.INVISIBLE));
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    private void resetStickerAdapter() {
//        if (mCurrentStickerPosition != -1) {
//            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setSticker("");
//            mCurrentStickerPosition = -1;
//        }
//
//        //重置所有状态为为选中状态
//        for (StickerOptionsItem optionsItem : mStickerOptionsList) {
//            if (!optionsItem.name.equals(NEW_ENGINE)) {
//                if (mStickerAdapters.get(optionsItem.name) != null) {
//                    mStickerAdapters.get(optionsItem.name).setSelectedPosition(-1);
//                    mStickerAdapters.get(optionsItem.name).notifyDataSetChanged();
//                }
//            }
//        }
//    }
}
