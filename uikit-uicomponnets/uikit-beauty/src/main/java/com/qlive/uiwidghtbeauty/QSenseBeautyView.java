package com.qlive.uiwidghtbeauty;

import static com.qlive.uiwidghtbeauty.utils.Constants.*;
import static com.qlive.uiwidghtbeauty.utils.ResourcesUtil.sBeautifyParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qlive.uiwidghtbeauty.adapter.BeautyItemAdapter;
import com.qlive.uiwidghtbeauty.adapter.BeautyOptionsAdapter;
import com.qlive.uiwidghtbeauty.adapter.FilterAdapter;
import com.qlive.uiwidghtbeauty.adapter.MakeupAdapter;
import com.qlive.uiwidghtbeauty.model.BeautyItem;
import com.qlive.uiwidghtbeauty.model.BeautyOptionsItem;
import com.qlive.uiwidghtbeauty.model.FilterItem;
import com.qlive.uiwidghtbeauty.model.MakeupItem;
import com.qlive.uiwidghtbeauty.utils.Constants;
import com.qlive.uiwidghtbeauty.utils.ResourcesUtil;
import com.qlive.uiwidghtbeauty.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class QSenseBeautyView extends FrameLayout {

    private static final String TAG_EFFECT = "effect";
    private static final String TAG_STICKER = "sticker";
    private static final String TAG = "QSenseBeautyView";

    private RecyclerView mFilterOptionsRecycleView;
    private RecyclerView mMakeupOptionsRecycleView;
    private RecyclerView mBeautyBaseRecycleView;

    private BeautyOptionsAdapter mBeautyOptionsAdapter;

    private HashMap<String, BeautyItemAdapter> mBeautyItemAdapters;

    private HashMap<String, ArrayList<BeautyItem>> mBeautyListMap;
    private HashMap<Integer, String> mBeautyOption;
    private HashMap<Integer, Integer> mBeautyOptionSelectedIndex;

    private HashMap<String, String> mMakeupGroupIds;
    private HashMap<String, MakeupAdapter> mMakeupAdapters;
    private HashMap<String, ArrayList<MakeupItem>> mMakeupLists;
    private HashMap<String, Integer> mMakeupOptionIndex;
    private HashMap<Integer, Integer> mMakeupOptionSelectedIndex;
    private HashMap<Integer, Integer> mMakeupStrength;

    private HashMap<String, FilterAdapter> mFilterAdapters;
    private HashMap<String, ArrayList<FilterItem>> mFilterListMap;

    private LinearLayout mFilterGroupsLinearLayout;
    private RelativeLayout mFilterIconsRelativeLayout;
    private RelativeLayout mFilterStrengthLayout;
    private RelativeLayout mMakeupIconsRelativeLayout;
    private RelativeLayout mMakeupGroupRelativeLayout;
    private ImageView mFilterGroupBack;
    private ImageView mMakeupGroupBack;
    private TextView mFilterGroupName;
    private TextView mFilterStrengthText;
    private TextView mMakeupGroupName;
    private SeekBar mFilterStrengthBar;

    private TextView mResetTextView;
    private IndicatorSeekBar mIndicatorSeekbar;
    //todo
    private RelativeLayout mFilterAndBeautyOptionView;
    private LinearLayout mBaseBeautyOptions;
    private TextView mShowOriginBtn1;
    private TextView mShowOriginBtn2;
    private TextView mShowOriginBtn3;

    private Context mContext;
    private int mCurrentFilterGroupIndex = -1;
    private int mCurrentFilterIndex = -1;
    private int mCurrentMakeupGroupIndex = -1;

    // 记录用户最后一次点击的素材id ,包括还未下载的，方便下载完成后，直接应用素材
    private String preMaterialId = "";
    private int mCurrentBeautyIndex = Constants.BEAUTY_BASE_WHITTEN;
    private int mBeautyOptionsPosition = 0;

    public QSenseBeautyView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public QSenseBeautyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QSenseBeautyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public QSenseBeautyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    //todo 优化代码 拷贝自商汤demo
    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.kit_view_beauty, this, true);
        initEffectView();
    }

    ArrayList<BeautyItem> beautyBaseItemList = null;
    ArrayList<BeautyItem> professionalBeautyItemList = null;
    ArrayList<BeautyItem> microBeautyItem =null;
    ArrayList<BeautyItem> adjustBeautyItemList =null;
    @SuppressLint("ClickableViewAccessibility")
    private void initEffectView() {
        new BeautyItemBgLoader().start(() -> {
            beautyBaseItemList = ResourcesUtil.getBeautyBaseItemList(mContext);
            professionalBeautyItemList = ResourcesUtil.getProfessionalBeautyItemList(mContext);
            microBeautyItem = ResourcesUtil.getMicroBeautyItemList(mContext);
            adjustBeautyItemList = ResourcesUtil.getAdjustBeautyItemList(mContext);

            mMakeupLists = ResourcesUtil.getMakeupListMap(mContext);
            mMakeupOptionIndex = ResourcesUtil.getMakeupOptionIndexMap();
            mFilterListMap = ResourcesUtil.getFilterListMap(mContext);
            return null;
        }, () -> {
            mMakeupGroupIds = new HashMap<>();
            mMakeupGroupIds.put(MAKEUP_LIP, GROUP_LIP);
            mMakeupGroupIds.put(MAKEUP_EYEBALL, GROUP_EYEBALL);
            mMakeupGroupIds.put(MAKEUP_BLUSH, GROUP_BLUSH);
            mMakeupGroupIds.put(MAKEUP_BROW, GROUP_BROW);
            mMakeupGroupIds.put(MAKEUP_HIGHLIGHT, GROUP_HIGHLIGHT);
            mMakeupGroupIds.put(MAKEUP_EYE, GROUP_EYE);
            mMakeupGroupIds.put(MAKEUP_EYELINER, GROUP_EYELINER);
            mMakeupGroupIds.put(MAKEUP_EYELASH, GROUP_EYELASH);
            //  改变美颜强度的 seekbar，可以更改基础美颜，美形，微整形和调整这四种美颜特效的强度
            mIndicatorSeekbar = findViewById(R.id.beauty_item_seekbar);
            mIndicatorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                    if (fromUser) {
                        if (checkMicroType()) {
                            mIndicatorSeekbar.updateTextView(Utils.convertToDisplay(progress));
                            // 设置美颜强度，强度范围是 [-1,1]
                            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[mCurrentBeautyIndex], (float) Utils.convertToDisplay(progress) / 100f);
                            mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition)).setProgress(Utils.convertToDisplay(progress));
                        } else {
                            mIndicatorSeekbar.updateTextView(progress);
                            // 设置美颜强度，强范围度是 [0,1]
                            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[mCurrentBeautyIndex], (float) progress / 100f);
                            mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition)).setProgress(progress);
                        }
                        mBeautyItemAdapters.get(mBeautyOption.get(mBeautyOptionsPosition)).notifyItemChanged(mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mIndicatorSeekbar.getSeekBar().setProgress((int) (sBeautifyParams[2] * 100));
            mIndicatorSeekbar.updateTextView((int) (sBeautifyParams[2] * 100));

            // 美颜相关视图，美颜包含基础美颜、美形、微整形、调整这四种类别
            mBeautyBaseRecycleView = findViewById(R.id.rv_beauty_base);
            LinearLayoutManager ms = new LinearLayoutManager(mContext);
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);
            mBeautyBaseRecycleView.setLayoutManager(ms);
            mBeautyBaseRecycleView.addItemDecoration(new BeautyItemDecoration(Utils.dip2px(mContext, 15)));


            mBeautyListMap = new HashMap<>();
            mBeautyListMap.put(BASE_BEAUTY, beautyBaseItemList);
            BeautyItemAdapter beautyBaseAdapter = new BeautyItemAdapter(mContext, beautyBaseItemList);
            mBeautyItemAdapters = new HashMap<>();
            mBeautyItemAdapters.put(BASE_BEAUTY, beautyBaseAdapter);
            mBeautyOption = new HashMap<>();
            mBeautyOption.put(0, BASE_BEAUTY);
            mBeautyBaseRecycleView.setAdapter(beautyBaseAdapter);


            mBeautyListMap.put(PROFESSIONAL_BEAUTY, professionalBeautyItemList);
            BeautyItemAdapter beautyProfessionalAdapter = new BeautyItemAdapter(mContext, professionalBeautyItemList);
            mBeautyItemAdapters.put(PROFESSIONAL_BEAUTY, beautyProfessionalAdapter);
            mBeautyOption.put(1, PROFESSIONAL_BEAUTY);


            mBeautyListMap.put(MICRO_BEAUTY, microBeautyItem);
            BeautyItemAdapter microAdapter = new BeautyItemAdapter(mContext, microBeautyItem);
            mBeautyItemAdapters.put(MICRO_BEAUTY, microAdapter);
            mBeautyOption.put(2, MICRO_BEAUTY);


            mBeautyListMap.put(ADJUST_BEAUTY, adjustBeautyItemList);
            BeautyItemAdapter adjustAdapter = new BeautyItemAdapter(mContext, adjustBeautyItemList);
            mBeautyItemAdapters.put(ADJUST_BEAUTY, adjustAdapter);
            mBeautyOption.put(5, ADJUST_BEAUTY);

            mBeautyOptionSelectedIndex = new HashMap<>();
            mBeautyOptionSelectedIndex.put(0, 0);
            mBeautyOptionSelectedIndex.put(1, 0);
            mBeautyOptionSelectedIndex.put(2, 0);
            mBeautyOptionSelectedIndex.put(5, 0);

            // 美妆相关视图，美妆包含口红、腮红、修容等8个部位的特效，都为二级列表，其下有各自的样式
            mMakeupOptionsRecycleView = findViewById(R.id.rv_makeup_icons);
            mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            mMakeupOptionsRecycleView.addItemDecoration(new SpaceItemDecoration(0));


            mMakeupAdapters = new HashMap<>();
            mMakeupAdapters.put(MAKEUP_LIP, new MakeupAdapter(mMakeupLists.get(MAKEUP_LIP), mContext));
            mMakeupAdapters.put(MAKEUP_HIGHLIGHT, new MakeupAdapter(mMakeupLists.get(MAKEUP_HIGHLIGHT), mContext));
            mMakeupAdapters.put(MAKEUP_BLUSH, new MakeupAdapter(mMakeupLists.get(MAKEUP_BLUSH), mContext));
            mMakeupAdapters.put(MAKEUP_BROW, new MakeupAdapter(mMakeupLists.get(MAKEUP_BROW), mContext));
            mMakeupAdapters.put(MAKEUP_EYE, new MakeupAdapter(mMakeupLists.get(MAKEUP_EYE), mContext));
            mMakeupAdapters.put(MAKEUP_EYELINER, new MakeupAdapter(mMakeupLists.get(MAKEUP_EYELINER), mContext));
            mMakeupAdapters.put(MAKEUP_EYELASH, new MakeupAdapter(mMakeupLists.get(MAKEUP_EYELASH), mContext));
            mMakeupAdapters.put(MAKEUP_EYEBALL, new MakeupAdapter(mMakeupLists.get(MAKEUP_EYEBALL), mContext));


            mMakeupOptionSelectedIndex = new HashMap<>();
            mMakeupStrength = new HashMap<>();
            for (int i = 402; i < Constants.MAKEUP_TYPE_COUNT + 402; i++) {
                mMakeupOptionSelectedIndex.put(i, 0);
                mMakeupStrength.put(i, 80);
            }
            mMakeupIconsRelativeLayout = findViewById(R.id.rl_makeup_icons);
            mMakeupGroupRelativeLayout = findViewById(R.id.rl_makeup_groups);

            // 口红
            LinearLayout makeupGroupLip = findViewById(R.id.ll_makeup_group_lip);
            makeupGroupLip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_LIP;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_LIP) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_unselected));
                    }
                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_LIP));
                    mMakeupGroupName.setText("口红");
                }
            });

            // 腮红
            LinearLayout makeupGroupCheeks = findViewById(R.id.ll_makeup_group_cheeks);
            makeupGroupCheeks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_BLUSH;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_BLUSH) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_unselected));
                    }
                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_BLUSH));
                    mMakeupGroupName.setText("腮红");
                }
            });

            // 修容
            LinearLayout makeupGroupFace = findViewById(R.id.ll_makeup_group_face);
            makeupGroupFace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_HIGHLIGHT;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_HIGHLIGHT) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_unselected));
                    }

                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_HIGHLIGHT));
                    mMakeupGroupName.setText("修容");
                }
            });

            // 眉毛
            LinearLayout makeupGroupBrow = findViewById(R.id.ll_makeup_group_brow);
            makeupGroupBrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_BROW;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_BROW) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_unselected));
                    }
                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_BROW));
                    mMakeupGroupName.setText("眉毛");
                }
            });

            // 眼影
            LinearLayout makeupGroupEye = findViewById(R.id.ll_makeup_group_eye);
            makeupGroupEye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_EYE;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYE) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_unselected));
                    }
                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_EYE));
                    mMakeupGroupName.setText("眼影");
                }
            });

            // 眼线
            LinearLayout makeupGroupEyeLiner = findViewById(R.id.ll_makeup_group_eyeliner);
            makeupGroupEyeLiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_EYELINER;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYELINER) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeliner_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeline_unselected));
                    }
                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_EYELINER));
                    mMakeupGroupName.setText("眼线");
                }
            });

            // 眼睫毛
            LinearLayout makeupGroupEyeLash = findViewById(R.id.ll_makeup_group_eyelash);
            makeupGroupEyeLash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_EYELASH;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYELASH) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_unselected));
                    }
                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_EYELASH));
                    mMakeupGroupName.setText("眼睫毛");
                }
            });

            // 美瞳
            LinearLayout makeupGroupEyeBall = findViewById(R.id.ll_makeup_group_eyeball);
            makeupGroupEyeBall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.VISIBLE);
                    mCurrentMakeupGroupIndex = Constants.ST_MAKEUP_EYEBALL;
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYEBALL) != 0) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_selected));
                    } else {
                        mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_unselected));
                    }
                    mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                    mMakeupOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mMakeupOptionsRecycleView.setAdapter(mMakeupAdapters.get(MAKEUP_EYEBALL));
                    mMakeupGroupName.setText("美瞳");
                }
            });

            // 返回按钮，检查所选特效的情况，如果选中则将其图标和文字变色，否则复原图标和文字颜色
            mMakeupGroupBack = findViewById(R.id.iv_makeup_group);
            mMakeupGroupBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMakeupGroupRelativeLayout.setVisibility(View.VISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.INVISIBLE);
                    mFilterStrengthLayout.setVisibility(View.INVISIBLE);

                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_LIP) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_lip)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_lip)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_lip)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_lip)).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_BLUSH) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_cheeks)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_cheeks)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_cheeks)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_cheeks)).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_HIGHLIGHT) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_face)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_face)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_face)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_face)).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_BROW) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_brow)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_brow)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_brow)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_brow)).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYE) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eye)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eye)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eye)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eye)).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYELINER) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eyeliner)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeliner_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eyeliner)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eyeliner)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeline_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eyeliner)).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYELASH) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eyelash)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eyelash)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eyelash)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eyelash)).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (mMakeupOptionSelectedIndex.get(Constants.ST_MAKEUP_EYEBALL) != 0) {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eyeball)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_selected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eyeball)).setTextColor(getResources().getColor(R.color.text_selected));
                    } else {
                        ((ImageView) findViewById(R.id.iv_makeup_group_eyeball)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_unselected));
                        ((TextView) findViewById(R.id.tv_makeup_group_eyeball)).setTextColor(getResources().getColor(R.color.white));
                    }
                }
            });
            mMakeupGroupName = findViewById(R.id.tv_makeup_group);


            // 滤镜相关
            mFilterAdapters = new HashMap<>();
            mFilterAdapters.put(FILTER_PORTRAIT, new FilterAdapter(mFilterListMap.get(FILTER_PORTRAIT), mContext));
            mFilterAdapters.put(FILTER_SCENERY, new FilterAdapter(mFilterListMap.get(FILTER_SCENERY), mContext));
            mFilterAdapters.put(FILTER_STILL_LIFE, new FilterAdapter(mFilterListMap.get(FILTER_STILL_LIFE), mContext));
            mFilterAdapters.put(FILTER_FOOD, new FilterAdapter(mFilterListMap.get(FILTER_FOOD), mContext));

            mFilterOptionsRecycleView = findViewById(R.id.rv_filter_icons);
            mFilterOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            mFilterOptionsRecycleView.addItemDecoration(new SpaceItemDecoration(0));

            mFilterAndBeautyOptionView = findViewById(R.id.rv_beauty_and_filter_options);
            mFilterIconsRelativeLayout = findViewById(R.id.rl_filter_icons);
            mFilterGroupsLinearLayout = findViewById(R.id.ll_filter_groups);
            LinearLayout filterGroupPortrait = findViewById(R.id.ll_filter_group_portrait);
            filterGroupPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterGroupsLinearLayout.setVisibility(View.INVISIBLE);
                    mFilterIconsRelativeLayout.setVisibility(View.VISIBLE);
                    if (mCurrentFilterGroupIndex == 0 && mCurrentFilterIndex != -1) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                    }
                    mFilterOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mFilterOptionsRecycleView.setAdapter(mFilterAdapters.get(FILTER_PORTRAIT));
                    mFilterGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.icon_portrait_selected));
                    mFilterGroupName.setText("人像");
                }
            });
            LinearLayout filterGroupScenery = findViewById(R.id.ll_filter_group_scenery);
            filterGroupScenery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterGroupsLinearLayout.setVisibility(View.INVISIBLE);
                    mFilterIconsRelativeLayout.setVisibility(View.VISIBLE);
                    if (mCurrentFilterGroupIndex == 1 && mCurrentFilterIndex != -1) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                    }
                    mFilterOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mFilterOptionsRecycleView.setAdapter(mFilterAdapters.get(FILTER_SCENERY));
                    mFilterGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.icon_scenery_selected));
                    mFilterGroupName.setText("风景");
                }
            });
            LinearLayout filterGroupStillLife = findViewById(R.id.ll_filter_group_still_life);
            filterGroupStillLife.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterGroupsLinearLayout.setVisibility(View.INVISIBLE);
                    mFilterIconsRelativeLayout.setVisibility(View.VISIBLE);
                    if (mCurrentFilterGroupIndex == 2 && mCurrentFilterIndex != -1) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                    }
                    mFilterOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mFilterOptionsRecycleView.setAdapter(mFilterAdapters.get(FILTER_STILL_LIFE));
                    mFilterGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.icon_still_life_selected));
                    mFilterGroupName.setText("静物");
                }
            });
            LinearLayout filterGroupFood = findViewById(R.id.ll_filter_group_food);
            filterGroupFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterGroupsLinearLayout.setVisibility(View.INVISIBLE);
                    mFilterIconsRelativeLayout.setVisibility(View.VISIBLE);
                    if (mCurrentFilterGroupIndex == 3 && mCurrentFilterIndex != -1) {
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                    }
                    mFilterOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                    mFilterOptionsRecycleView.setAdapter(mFilterAdapters.get(FILTER_FOOD));
                    mFilterGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.icon_food_selected));
                    mFilterGroupName.setText("食物");
                }
            });

            mFilterGroupBack = findViewById(R.id.iv_filter_group);
            mFilterGroupBack.setOnClickListener(v -> {
                mFilterGroupsLinearLayout.setVisibility(View.VISIBLE);
                mFilterIconsRelativeLayout.setVisibility(View.INVISIBLE);
                mFilterStrengthLayout.setVisibility(View.INVISIBLE);
            });
            mFilterGroupName = findViewById(R.id.tv_filter_group);
            mFilterStrengthText = findViewById(R.id.tv_filter_strength);

            mFilterStrengthLayout = findViewById(R.id.rv_filter_strength);
            mFilterStrengthBar = findViewById(R.id.sb_filter_strength);
            mFilterStrengthBar.setProgress(65);
            mFilterStrengthText.setText("65");
            mFilterStrengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        if (mBeautyOptionsPosition == 4) {
                            // 设置滤镜强度
                            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilterStrength((float) progress / 100);
                            mFilterStrengthText.setText(progress + "");
                        } else if (mBeautyOptionsPosition == 3) {
                            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setMakeupStrength(mCurrentMakeupGroupIndex, (float) progress / 100);
                            mMakeupStrength.put(mCurrentMakeupGroupIndex, progress);
                            mFilterStrengthText.setText(progress + "");
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            // 为滤镜中的人像分类添加点击监听，选择第一个选项为取消滤镜，否则设置对应的滤镜并更新视图
            mFilterAdapters.get(FILTER_PORTRAIT).setClickFilterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetFilterView();
                    final int position = Integer.parseInt(v.getTag().toString());
                    mFilterAdapters.get(FILTER_PORTRAIT).setSelectedPosition(position);
                    mCurrentFilterGroupIndex = 0;
                    mCurrentFilterIndex = -1;
                    if (position == 0) {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter("");
                    } else {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter(mFilterListMap.get(FILTER_PORTRAIT).get(position).model);
                        mCurrentFilterIndex = position;
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mShowOriginBtn1.setVisibility(View.INVISIBLE);
                        mShowOriginBtn2.setVisibility(View.INVISIBLE);
                        mShowOriginBtn3.setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.iv_filter_group_portrait)).setImageDrawable(getResources().getDrawable(R.drawable.icon_portrait_selected));
                        ((TextView) findViewById(R.id.tv_filter_group_portrait)).setTextColor(getResources().getColor(R.color.text_selected));
                    }
                    mFilterAdapters.get(FILTER_PORTRAIT).notifyDataSetChanged();
                }
            });

            // 为滤镜中的风景分类添加点击监听，选择第一个选项为取消滤镜，否则设置对应的滤镜并更新视图
            mFilterAdapters.get(FILTER_SCENERY).setClickFilterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetFilterView();
                    int position = Integer.parseInt(v.getTag().toString());
                    mFilterAdapters.get(FILTER_SCENERY).setSelectedPosition(position);
                    mCurrentFilterGroupIndex = 1;
                    mCurrentFilterIndex = -1;
                    if (position == 0) {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter("");
                    } else {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter(mFilterListMap.get(FILTER_SCENERY).get(position).model);
                        mCurrentFilterIndex = position;
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mShowOriginBtn1.setVisibility(View.INVISIBLE);
                        mShowOriginBtn2.setVisibility(View.INVISIBLE);
                        mShowOriginBtn3.setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.iv_filter_group_scenery)).setImageDrawable(getResources().getDrawable(R.drawable.icon_scenery_selected));
                        ((TextView) findViewById(R.id.tv_filter_group_scenery)).setTextColor(getResources().getColor(R.color.text_selected));
                    }
                    mFilterAdapters.get(FILTER_SCENERY).notifyDataSetChanged();
                }
            });

            // 为滤镜中的静物分类添加点击监听，选择第一个选项为取消滤镜，否则设置对应的滤镜并更新视图
            mFilterAdapters.get(FILTER_STILL_LIFE).setClickFilterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetFilterView();
                    int position = Integer.parseInt(v.getTag().toString());
                    mFilterAdapters.get(FILTER_STILL_LIFE).setSelectedPosition(position);
                    mCurrentFilterGroupIndex = 2;
                    mCurrentFilterIndex = -1;
                    if (position == 0) {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter("");
                    } else {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter(mFilterListMap.get(FILTER_STILL_LIFE).get(position).model);
                        mCurrentFilterIndex = position;
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mShowOriginBtn1.setVisibility(View.INVISIBLE);
                        mShowOriginBtn2.setVisibility(View.INVISIBLE);
                        mShowOriginBtn3.setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.iv_filter_group_still_life)).setImageDrawable(getResources().getDrawable(R.drawable.icon_still_life_selected));
                        ((TextView) findViewById(R.id.tv_filter_group_still_life)).setTextColor(getResources().getColor(R.color.text_selected));
                    }
                    mFilterAdapters.get(FILTER_STILL_LIFE).notifyDataSetChanged();
                }
            });

            // 为滤镜中的食物分类添加点击监听，选择第一个选项为取消滤镜，否则设置对应的滤镜并更新视图
            mFilterAdapters.get(FILTER_FOOD).setClickFilterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetFilterView();
                    int position = Integer.parseInt(v.getTag().toString());
                    mFilterAdapters.get(FILTER_FOOD).setSelectedPosition(position);
                    mCurrentFilterGroupIndex = 3;
                    mCurrentFilterIndex = -1;
                    if (position == 0) {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter("");
                    } else {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter(mFilterListMap.get(FILTER_FOOD).get(position).model);
                        mCurrentFilterIndex = position;
                        mFilterStrengthLayout.setVisibility(View.VISIBLE);
                        mShowOriginBtn1.setVisibility(View.INVISIBLE);
                        mShowOriginBtn2.setVisibility(View.INVISIBLE);
                        mShowOriginBtn3.setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.iv_filter_group_food)).setImageDrawable(getResources().getDrawable(R.drawable.icon_food_selected));
                        ((TextView) findViewById(R.id.tv_filter_group_food)).setTextColor(getResources().getColor(R.color.text_selected));
                    }
                    mFilterAdapters.get(FILTER_FOOD).notifyDataSetChanged();
                }
            });

            // 美颜面板上的滑动选项
            RecyclerView beautyOptionsRecycleView = findViewById(R.id.rv_beauty_options);
            beautyOptionsRecycleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            beautyOptionsRecycleView.addItemDecoration(new SpaceItemDecoration(0));

            ArrayList<BeautyOptionsItem> beautyOptionsList = new ArrayList<>();
            beautyOptionsList.add(0, new BeautyOptionsItem("基础美颜"));
            beautyOptionsList.add(1, new BeautyOptionsItem("美形"));
            beautyOptionsList.add(2, new BeautyOptionsItem("微整形"));
            beautyOptionsList.add(3, new BeautyOptionsItem("美妆"));
            beautyOptionsList.add(4, new BeautyOptionsItem("滤镜"));
            beautyOptionsList.add(5, new BeautyOptionsItem("调整"));

            mBeautyOptionsAdapter = new BeautyOptionsAdapter(beautyOptionsList, mContext);
            beautyOptionsRecycleView.setAdapter(mBeautyOptionsAdapter);

            // 当点击某一选项的时候的切换下面展示的视图并更新对应的进度条
            mBeautyOptionsAdapter.setClickBeautyListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    mBeautyOptionsAdapter.setSelectedPosition(position);
                    mBeautyOptionsPosition = position;
                    mResetTextView.setVisibility(View.VISIBLE);
                    mFilterGroupsLinearLayout.setVisibility(View.INVISIBLE);
                    mBaseBeautyOptions.setVisibility(View.VISIBLE);
                    mMakeupGroupRelativeLayout.setVisibility(View.INVISIBLE);
                    if (mBeautyOptionsPosition != 3 && mBeautyOptionsPosition != 4) {
                        mCurrentBeautyIndex = ResourcesUtil.calculateBeautyIndex(mBeautyOptionsPosition, mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition));
                        mIndicatorSeekbar.setVisibility(View.VISIBLE);
                        if (mBeautyOptionsPosition == 2 && mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition) != 0 && mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition) != 3) {
                            mIndicatorSeekbar.getSeekBar().setProgress(Utils.convertToData(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(position)).getProgress()));
                        } else {
                            mIndicatorSeekbar.getSeekBar().setProgress(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(position)).getProgress());
                        }
                        mIndicatorSeekbar.updateTextView(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(position)).getProgress());
                    } else {
                        mIndicatorSeekbar.setVisibility(View.INVISIBLE);
                    }
                    mFilterIconsRelativeLayout.setVisibility(View.INVISIBLE);
                    mMakeupIconsRelativeLayout.setVisibility(View.INVISIBLE);
                    mShowOriginBtn3.setVisibility(View.VISIBLE);
                    mFilterStrengthLayout.setVisibility(View.INVISIBLE);
                    if (position == 0) {
                        mBeautyBaseRecycleView.setAdapter(mBeautyItemAdapters.get(BASE_BEAUTY));
                    } else if (position == 1) {
                        mBeautyBaseRecycleView.setAdapter(mBeautyItemAdapters.get(PROFESSIONAL_BEAUTY));
                    } else if (position == 2) {
                        mBeautyBaseRecycleView.setAdapter(mBeautyItemAdapters.get(MICRO_BEAUTY));
                    } else if (position == 3) {
                        mMakeupGroupRelativeLayout.setVisibility(View.VISIBLE);
                        mBaseBeautyOptions.setVisibility(View.INVISIBLE);
                    } else if (position == 4) {
                        mFilterGroupsLinearLayout.setVisibility(View.VISIBLE);
                        mBaseBeautyOptions.setVisibility(View.INVISIBLE);
                    } else if (position == 5) {
                        mBeautyBaseRecycleView.setAdapter(mBeautyItemAdapters.get(ADJUST_BEAUTY));
                    }
                    mBeautyOptionsAdapter.notifyDataSetChanged();
                }
            });

            // 为美妆列表的每一个部位的 adapter 都设置一个监听，当点击该视图时更新 seekbar 和下面的指示数字
            for (Map.Entry<String, BeautyItemAdapter> entry : mBeautyItemAdapters.entrySet()) {
                final BeautyItemAdapter adapter = entry.getValue();
                adapter.setClickBeautyListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = Integer.parseInt(v.getTag().toString());
                        adapter.setSelectedPosition(position);
                        mBeautyOptionSelectedIndex.put(mBeautyOptionsPosition, position);
                        if (checkMicroType()) {
                            mIndicatorSeekbar.getSeekBar().setProgress(Utils.convertToData(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(position).getProgress()));
                        } else {
                            mIndicatorSeekbar.getSeekBar().setProgress(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(position).getProgress());
                        }
                        mIndicatorSeekbar.updateTextView(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(position).getProgress());
                        mCurrentBeautyIndex = ResourcesUtil.calculateBeautyIndex(mBeautyOptionsPosition, position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            // 为美妆的每一个类别的 adapter 都添加监听，点击第一个为取消该部位美妆，其它则为设置，都会更新视图
            for (final Map.Entry<String, MakeupAdapter> entry : mMakeupAdapters.entrySet()) {
                entry.getValue().setClickMakeupListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = Integer.parseInt(v.getTag().toString());
                        if (position == 0) {
                            entry.getValue().setSelectedPosition(position);
                            mMakeupOptionSelectedIndex.put(mMakeupOptionIndex.get(entry.getKey()), position);
                            mFilterStrengthLayout.setVisibility(View.INVISIBLE);
                            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setMakeup(mCurrentMakeupGroupIndex, "");
                            updateMakeupOptions(mCurrentMakeupGroupIndex, false);
                        } else {
                            entry.getValue().setSelectedPosition(position);
                            mMakeupOptionSelectedIndex.put(mMakeupOptionIndex.get(entry.getKey()), position);
                            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setMakeup(mCurrentMakeupGroupIndex, mMakeupLists.get(ResourcesUtil.getMakeupNameOfType(mCurrentMakeupGroupIndex)).get(position).path);
                            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setMakeupStrength(mCurrentMakeupGroupIndex, (float) mMakeupStrength.get(mCurrentMakeupGroupIndex) / 100.f);
                            mFilterStrengthLayout.setVisibility(View.VISIBLE);
                            mFilterStrengthBar.setProgress(mMakeupStrength.get(mCurrentMakeupGroupIndex));
                            updateMakeupOptions(mCurrentMakeupGroupIndex, true);
                        }
                        entry.getValue().notifyDataSetChanged();
                    }
                });
            }

            // 美颜面板
            mBaseBeautyOptions = findViewById(R.id.ll_base_beauty_options);

            // 重置视图，点击会执行各种特效的重置逻辑
            mResetTextView = findViewById(R.id.reset);
            mResetTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBeautyOptionsPosition == 3) {
                        resetMakeup();
                    } else if (mBeautyOptionsPosition == 4) {
                        setDefaultFilter();
                        mFilterStrengthBar.setProgress(65);
                    } else {
                        resetSetBeautyParam(mBeautyOptionsPosition);
                        resetBeautyLists(mBeautyOptionsPosition);
                        mBeautyItemAdapters.get(mBeautyOption.get(mBeautyOptionsPosition)).notifyDataSetChanged();
                        if (checkMicroType()) {
                            mIndicatorSeekbar.getSeekBar().setProgress(Utils.convertToData(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition)).getProgress()));
                        } else {
                            mIndicatorSeekbar.getSeekBar().setProgress(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition)).getProgress());
                        }
                        mIndicatorSeekbar.updateTextView(mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition)).getProgress());
                    }
                }
            });

            // 三个对比按钮，分别对应面板未出现时、美颜面板出现时、贴纸面板出现时的情况
            // 展示其中一个的时候会将另外两个隐藏
            mShowOriginBtn1 = findViewById(R.id.tv_show_origin1);
            mShowOriginBtn1.setVisibility(View.VISIBLE);
            mShowOriginBtn2 = findViewById(R.id.tv_show_origin2);
            mShowOriginBtn2.setVisibility(View.INVISIBLE);
            mShowOriginBtn3 = findViewById(R.id.tv_show_origin3);
            mShowOriginBtn3.setVisibility(View.INVISIBLE);

            // 当按住对比按钮时关闭特效处理，抬起时开启特效处理
            View.OnTouchListener onTouchShowOriginBtnListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setEffectEnabled(false);
                    } else if (action == MotionEvent.ACTION_UP) {
                        QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setEffectEnabled(true);
                    }
                    return true;
                }
            };
            // 为三个重置按钮都加上上面的监听
            mShowOriginBtn1.setOnTouchListener(onTouchShowOriginBtnListener);
            mShowOriginBtn2.setOnTouchListener(onTouchShowOriginBtnListener);
            mShowOriginBtn3.setOnTouchListener(onTouchShowOriginBtnListener);

            mBaseBeautyOptions.setVisibility(View.VISIBLE);
            mIndicatorSeekbar.setVisibility(View.VISIBLE);

            if (mBeautyOptionsPosition == 3) {
                mBaseBeautyOptions.setVisibility(View.INVISIBLE);
                mMakeupGroupRelativeLayout.setVisibility(View.VISIBLE);
                mMakeupIconsRelativeLayout.setVisibility(View.INVISIBLE);
                mFilterStrengthLayout.setVisibility(View.INVISIBLE);
                mIndicatorSeekbar.setVisibility(View.INVISIBLE);
            } else if (mBeautyOptionsPosition == 4) {
                mBaseBeautyOptions.setVisibility(View.INVISIBLE);
                mFilterGroupsLinearLayout.setVisibility(View.VISIBLE);
                mFilterIconsRelativeLayout.setVisibility(View.INVISIBLE);
                mFilterStrengthLayout.setVisibility(View.INVISIBLE);
                mIndicatorSeekbar.setVisibility(View.INVISIBLE);
            }
            mFilterAndBeautyOptionView.setVisibility(View.VISIBLE);
            mShowOriginBtn1.setVisibility(View.INVISIBLE);
            mShowOriginBtn2.setVisibility(View.INVISIBLE);
            mShowOriginBtn3.setVisibility(View.VISIBLE);
            mResetTextView.setVisibility(View.VISIBLE);
            mBeautyOptionsAdapter.notifyDataSetChanged();

            return null;
        });
    }

    /**
     * 重置滤镜视图
     */
    private void resetFilterView() {
        ((ImageView) findViewById(R.id.iv_filter_group_portrait)).setImageDrawable(getResources().getDrawable(R.drawable.icon_portrait_unselected));
        ((TextView) findViewById(R.id.tv_filter_group_portrait)).setTextColor(getResources().getColor(R.color.white));

        ((ImageView) findViewById(R.id.iv_filter_group_scenery)).setImageDrawable(getResources().getDrawable(R.drawable.icon_scenery_unselected));
        ((TextView) findViewById(R.id.tv_filter_group_scenery)).setTextColor(getResources().getColor(R.color.white));

        ((ImageView) findViewById(R.id.iv_filter_group_still_life)).setImageDrawable(getResources().getDrawable(R.drawable.icon_still_life_unselected));
        ((TextView) findViewById(R.id.tv_filter_group_still_life)).setTextColor(getResources().getColor(R.color.white));

        ((ImageView) findViewById(R.id.iv_filter_group_food)).setImageDrawable(getResources().getDrawable(R.drawable.icon_food_unselected));
        ((TextView) findViewById(R.id.tv_filter_group_food)).setTextColor(getResources().getColor(R.color.white));

        mFilterAdapters.get(FILTER_PORTRAIT).setSelectedPosition(-1);
        mFilterAdapters.get(FILTER_PORTRAIT).notifyDataSetChanged();
        mFilterAdapters.get(FILTER_SCENERY).setSelectedPosition(-1);
        mFilterAdapters.get(FILTER_SCENERY).notifyDataSetChanged();
        mFilterAdapters.get(FILTER_STILL_LIFE).setSelectedPosition(-1);
        mFilterAdapters.get(FILTER_STILL_LIFE).notifyDataSetChanged();
        mFilterAdapters.get(FILTER_FOOD).setSelectedPosition(-1);
        mFilterAdapters.get(FILTER_FOOD).notifyDataSetChanged();

        mFilterStrengthLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * 判别是否为强度范围为 [-1~1] 的特效
     */
    private boolean checkMicroType() {
        int type = mBeautyOptionSelectedIndex.get(mBeautyOptionsPosition);
        boolean ans = ((type != BEAUTY_BASE_REDDEN) && (type != BEAUTY_RESHAPE_SHRINK_FACE)
                && (type != BEAUTY_PLASTIC_THIN_FACE) && (type != BEAUTY_PLASTIC_HAIRLINE_HEIGHT)
                && (type != BEAUTY_PLASTIC_APPLE_MUSLE) && (type != BEAUTY_PLASTIC_NARROW_NOSE)
                && (type != BEAUTY_PLASTIC_NOSE_LENGTH) && (type != BEAUTY_PLASTIC_PROFILE_RHINOPLASTY)
                && (type != BEAUTY_BASE_FACE_SMOOTH) && (type != BEAUTY_PLASTIC_MOUTH_SIZE));
        return ans && (2 == mBeautyOptionsPosition);
    }

    /**
     * 给定一个美妆的 type 和是否选中的标志来更新外部的视图
     * 如果选中则将图标和文字更换为紫色，否则为白色
     */
    private void updateMakeupOptions(int type, boolean value) {
        if (value) {
            if (type == Constants.ST_MAKEUP_LIP) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_lip)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_lip)).setTextColor(getResources().getColor(R.color.text_selected));
            }
            if (type == Constants.ST_MAKEUP_BLUSH) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_cheeks)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_cheeks)).setTextColor(getResources().getColor(R.color.text_selected));
            }
            if (type == Constants.ST_MAKEUP_HIGHLIGHT) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_face)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_face)).setTextColor(getResources().getColor(R.color.text_selected));
            }
            if (type == Constants.ST_MAKEUP_BROW) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_brow)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_brow)).setTextColor(getResources().getColor(R.color.text_selected));
            }
            if (type == Constants.ST_MAKEUP_EYE) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eye)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_eye)).setTextColor(getResources().getColor(R.color.text_selected));
            }
            if (type == Constants.ST_MAKEUP_EYELINER) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeliner_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eyeliner)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeliner_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_eyeliner)).setTextColor(getResources().getColor(R.color.text_selected));
            }
            if (type == Constants.ST_MAKEUP_EYELASH) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eyelash)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_eyelash)).setTextColor(getResources().getColor(R.color.text_selected));
            }
            if (type == Constants.ST_MAKEUP_EYEBALL) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_selected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eyeball)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_selected));
                ((TextView) findViewById(R.id.tv_makeup_group_eyeball)).setTextColor(getResources().getColor(R.color.text_selected));
            }
        } else {
            if (type == Constants.ST_MAKEUP_LIP) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_lip)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_lip)).setTextColor(getResources().getColor(R.color.white));
            }
            if (type == Constants.ST_MAKEUP_BLUSH) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_cheeks)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_cheeks)).setTextColor(getResources().getColor(R.color.white));
            }
            if (type == Constants.ST_MAKEUP_HIGHLIGHT) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_face)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_face)).setTextColor(getResources().getColor(R.color.white));
            }
            if (type == Constants.ST_MAKEUP_BROW) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_brow)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_brow)).setTextColor(getResources().getColor(R.color.white));
            }
            if (type == Constants.ST_MAKEUP_EYE) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eye)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_eye)).setTextColor(getResources().getColor(R.color.white));
            }
            if (type == Constants.ST_MAKEUP_EYELINER) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeline_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eyeliner)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeline_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_eyeliner)).setTextColor(getResources().getColor(R.color.white));
            }
            if (type == Constants.ST_MAKEUP_EYELASH) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eyelash)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_eyelash)).setTextColor(getResources().getColor(R.color.white));
            }
            if (type == Constants.ST_MAKEUP_EYEBALL) {
                mMakeupGroupBack.setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_unselected));
                ((ImageView) findViewById(R.id.iv_makeup_group_eyeball)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_unselected));
                ((TextView) findViewById(R.id.tv_makeup_group_eyeball)).setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    /**
     * 重置美妆特效并重置视图和状态
     */
    private void resetMakeup() {
        for (int i = 402; i < Constants.MAKEUP_TYPE_COUNT + 402; i++) {
            final int finalI = i;
            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setMakeup(finalI, "");
            QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setMakeupStrength(finalI, 0);
            mMakeupOptionSelectedIndex.put(i, 0);
            mMakeupStrength.put(i, 80);
        }

        mFilterStrengthLayout.setVisibility(View.INVISIBLE);

        ((ImageView) findViewById(R.id.iv_makeup_group_lip)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_lip_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_lip)).setTextColor(getResources().getColor(R.color.white));
        ((ImageView) findViewById(R.id.iv_makeup_group_cheeks)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_cheeks_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_cheeks)).setTextColor(getResources().getColor(R.color.white));
        ((ImageView) findViewById(R.id.iv_makeup_group_face)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_face_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_face)).setTextColor(getResources().getColor(R.color.white));
        ((ImageView) findViewById(R.id.iv_makeup_group_brow)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_brow_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_brow)).setTextColor(getResources().getColor(R.color.white));
        ((ImageView) findViewById(R.id.iv_makeup_group_eye)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eye_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_eye)).setTextColor(getResources().getColor(R.color.white));
        ((ImageView) findViewById(R.id.iv_makeup_group_eyeliner)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeline_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_eyeliner)).setTextColor(getResources().getColor(R.color.white));
        ((ImageView) findViewById(R.id.iv_makeup_group_eyelash)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyelash_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_eyelash)).setTextColor(getResources().getColor(R.color.white));

        ((ImageView) findViewById(R.id.iv_makeup_group_eyeball)).setImageDrawable(getResources().getDrawable(R.drawable.makeup_eyeball_unselected));
        ((TextView) findViewById(R.id.tv_makeup_group_eyeball)).setTextColor(getResources().getColor(R.color.white));

        mMakeupAdapters.get(MAKEUP_LIP).setSelectedPosition(0);
        mMakeupAdapters.get(MAKEUP_LIP).notifyDataSetChanged();
        mMakeupAdapters.get(Constants.MAKEUP_HIGHLIGHT).setSelectedPosition(0);
        mMakeupAdapters.get(Constants.MAKEUP_HIGHLIGHT).notifyDataSetChanged();
        mMakeupAdapters.get(MAKEUP_BLUSH).setSelectedPosition(0);
        mMakeupAdapters.get(MAKEUP_BLUSH).notifyDataSetChanged();
        mMakeupAdapters.get(Constants.MAKEUP_BROW).setSelectedPosition(0);
        mMakeupAdapters.get(Constants.MAKEUP_BROW).notifyDataSetChanged();
        mMakeupAdapters.get(Constants.MAKEUP_EYE).setSelectedPosition(0);
        mMakeupAdapters.get(Constants.MAKEUP_EYE).notifyDataSetChanged();
        mMakeupAdapters.get(Constants.MAKEUP_EYELINER).setSelectedPosition(0);
        mMakeupAdapters.get(Constants.MAKEUP_EYELINER).notifyDataSetChanged();
        mMakeupAdapters.get(Constants.MAKEUP_EYELASH).setSelectedPosition(0);
        mMakeupAdapters.get(Constants.MAKEUP_EYELASH).notifyDataSetChanged();
        mMakeupAdapters.get(MAKEUP_EYEBALL).setSelectedPosition(0);
        mMakeupAdapters.get(MAKEUP_EYEBALL).notifyDataSetChanged();
    }

    /**
     * 重置滤镜特效并重置视图和状态
     */
    public void setDefaultFilter() {
        resetFilterView();
        if (mFilterListMap.get(Constants.FILTER_PORTRAIT).size() > 0) {
            for (int i = 0; i < mFilterListMap.get(Constants.FILTER_PORTRAIT).size(); i++) {
                if (mFilterListMap.get(Constants.FILTER_PORTRAIT).get(i).name.equals("babypink")) {
                    mCurrentFilterIndex = i;
                }
            }

            if (mCurrentFilterIndex > 0) {
                mCurrentFilterGroupIndex = 0;
                mFilterAdapters.get(Constants.FILTER_PORTRAIT).setSelectedPosition(mCurrentFilterIndex);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setFilter(mFilterListMap.get(Constants.FILTER_PORTRAIT).get(mCurrentFilterIndex).model);
                ((ImageView) findViewById(R.id.iv_filter_group_portrait)).setImageDrawable(getResources().getDrawable(R.drawable.icon_portrait_selected));
                ((TextView) findViewById(R.id.tv_filter_group_portrait)).setTextColor(getResources().getColor(R.color.text_selected));
                mFilterAdapters.get(Constants.FILTER_PORTRAIT).notifyDataSetChanged();
            }
        }
    }

    /**
     * 根据所给的美颜类别下标来恢复某一类特效
     *
     * @param beautyOptionsPosition 美颜类别的下标
     */
    private void resetSetBeautyParam(int beautyOptionsPosition) {
        switch (beautyOptionsPosition) {
            case 0:
                // 基础美颜
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_BASE_WHITTEN], sBeautifyParams[0]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_BASE_REDDEN], sBeautifyParams[1]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_BASE_FACE_SMOOTH], sBeautifyParams[2]);
                break;
            case 1:
                // 美形
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_RESHAPE_SHRINK_FACE], sBeautifyParams[3]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_RESHAPE_ENLARGE_EYE], sBeautifyParams[4]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_RESHAPE_SHRINK_JAW], sBeautifyParams[5]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_RESHAPE_NARROW_FACE], sBeautifyParams[6]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_RESHAPE_ROUND_EYE], sBeautifyParams[7]);
                break;
            case 2:
                // 微整形
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_THIN_FACE], sBeautifyParams[8]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_CHIN_LENGTH], sBeautifyParams[9]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_HAIRLINE_HEIGHT], sBeautifyParams[10]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_APPLE_MUSLE], sBeautifyParams[11]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_NARROW_NOSE], sBeautifyParams[12]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_NOSE_LENGTH], sBeautifyParams[13]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_PROFILE_RHINOPLASTY], sBeautifyParams[14]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_MOUTH_SIZE], sBeautifyParams[15]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_PHILTRUM_LENGTH], sBeautifyParams[16]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_EYE_DISTANCE], sBeautifyParams[17]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_EYE_ANGLE], sBeautifyParams[18]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_OPEN_CANTHUS], sBeautifyParams[19]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_BRIGHT_EYE], sBeautifyParams[20]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_REMOVE_DARK_CIRCLES], sBeautifyParams[21]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_REMOVE_NASOLABIAL_FOLDS], sBeautifyParams[22]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_WHITE_TEETH], sBeautifyParams[23]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_PLASTIC_SHRINK_CHEEKBONE], sBeautifyParams[24]);
                break;
            case 5:
                // 调整
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_TONE_CONTRAST], sBeautifyParams[25]);
                QSenseTimeManager.INSTANCE.getSSenseTimePlugin().setBeauty(Constants.BEAUTY_TYPES[Constants.BEAUTY_TONE_SATURATION], sBeautifyParams[26]);
                break;
        }
    }

    /**
     * 根据所给美颜类别的下标恢复对应的进度条
     *
     * @param beautyOptionsPosition 美颜类别的下标
     */
    private void resetBeautyLists(int beautyOptionsPosition) {
        switch (beautyOptionsPosition) {
            case 0:
                // 基础美颜
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(0).setProgress((int) (sBeautifyParams[0] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(1).setProgress((int) (sBeautifyParams[1] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(2).setProgress((int) (sBeautifyParams[2] * 100));
                break;
            case 1:
                // 美形
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(0).setProgress((int) (sBeautifyParams[3] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(1).setProgress((int) (sBeautifyParams[4] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(2).setProgress((int) (sBeautifyParams[5] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(3).setProgress((int) (sBeautifyParams[6] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(4).setProgress((int) (sBeautifyParams[7] * 100));
                break;
            case 2:
                // 微整形
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(0).setProgress((int) (sBeautifyParams[8] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(1).setProgress((int) (sBeautifyParams[9] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(2).setProgress((int) (sBeautifyParams[10] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(3).setProgress((int) (sBeautifyParams[11] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(4).setProgress((int) (sBeautifyParams[12] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(5).setProgress((int) (sBeautifyParams[13] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(6).setProgress((int) (sBeautifyParams[14] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(7).setProgress((int) (sBeautifyParams[15] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(8).setProgress((int) (sBeautifyParams[16] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(9).setProgress((int) (sBeautifyParams[17] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(10).setProgress((int) (sBeautifyParams[18] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(11).setProgress((int) (sBeautifyParams[19] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(12).setProgress((int) (sBeautifyParams[20] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(13).setProgress((int) (sBeautifyParams[21] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(14).setProgress((int) (sBeautifyParams[22] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(15).setProgress((int) (sBeautifyParams[23] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(16).setProgress((int) (sBeautifyParams[24] * 100));
                break;
            case 5:
                // 调整
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(0).setProgress((int) (sBeautifyParams[25] * 100));
                mBeautyListMap.get(mBeautyOption.get(mBeautyOptionsPosition)).get(1).setProgress((int) (sBeautifyParams[26] * 100));
                break;
        }
    }


    // 分隔间距,继承于 RecyclerView.ItemDecoration
    static class BeautyItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public BeautyItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = space;
            outRect.right = space;
        }
    }

    // 分隔间距,继承于 RecyclerView.ItemDecoration
   public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.top = space;
            }
        }
    }
}
