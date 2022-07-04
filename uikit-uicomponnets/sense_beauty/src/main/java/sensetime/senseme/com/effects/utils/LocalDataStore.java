package sensetime.senseme.com.effects.utils;

import static com.sensetime.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_FROST;
import static com.sensetime.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_LUSTRE;
import static com.sensetime.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_MATTE;
import static com.sensetime.stmobile.model.STEffectLipStickFinish.EFFECT_LIPSTICK_METAL;

import android.annotation.SuppressLint;
import android.content.Context;

import sensetime.senseme.com.effects.SenseMeApplication;
import sensetime.senseme.com.effects.utils.LogUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sensetime.stmobile.model.STEffectLipStickFinish;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import sensetime.senseme.com.effects.SenseCollectionUtils;
import sensetime.senseme.com.effects.R;
import sensetime.senseme.com.effects.entity.BasicEffectEntity;
import sensetime.senseme.com.effects.entity.TryOnTitleEntity;
import sensetime.senseme.com.effects.view.BeautyItem;
import sensetime.senseme.com.effects.view.BeautyOptionsItem;
import sensetime.senseme.com.effects.view.FilterItem;
import sensetime.senseme.com.effects.view.MakeupItem;
import sensetime.senseme.com.effects.view.StickerItem;
import sensetime.senseme.com.effects.view.StickerState;
import sensetime.senseme.com.effects.view.widget.EffectType;

public class LocalDataStore implements LocalDataStoreI {
    private static final String TAG = "LocalDataStore";

    private LocalDataStore() {
    }

    public static LocalDataStore getInstance() {
        return LocalDataManagerHolder.instance;
    }

    private static class LocalDataManagerHolder {
        @SuppressLint("StaticFieldLeak")
        private static final LocalDataStore instance = new LocalDataStore();
    }

    private ArrayList<FilterItem> selfieList;
    private ArrayList<FilterItem> sceneryList;
    private ArrayList<FilterItem> objectsList;
    private ArrayList<FilterItem> foodsList;

    private ArrayList<MakeupItem> hairList;
    private ArrayList<MakeupItem> lipList;
    private ArrayList<MakeupItem> blushList;
    private ArrayList<MakeupItem> xrList;
    private ArrayList<MakeupItem> browList;
    private ArrayList<MakeupItem> eyeshadowList;
    private ArrayList<MakeupItem> eyelinerList;
    private ArrayList<MakeupItem> eyelashList;
    private ArrayList<MakeupItem> eyeballList;

    public void prepareAsync() {
        if (SenseCollectionUtils.isEmpty(selfieList)) {// 人物
            selfieList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), "filter_portrait");
            sceneryList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), "filter_scenery");
            objectsList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), "filter_still_life");
            foodsList = FileUtils.getFilterFiles(SenseMeApplication.getContext(), "filter_food");
        }

        if (SenseCollectionUtils.isEmpty(hairList))
            hairList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_hairdye");

        if (SenseCollectionUtils.isEmpty(lipList))
            lipList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_lip");

        if (null == blushList || blushList.size() == 0) {
            blushList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_blush");
        }
        if (null == xrList || xrList.size() == 0) {
            xrList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_highlight");
        }
        if (null == browList || browList.size() == 0) {
            browList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_brow");
        }
        if (null == eyeshadowList || eyeshadowList.size() == 0) {
            eyeshadowList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeshadow");
        }
        if (null == eyelinerList || eyelinerList.size() == 0) {
            eyelinerList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeliner");
        }
        if (null == eyelashList || eyelashList.size() == 0) {
            eyelashList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyelash");
        }
        if (null == eyeballList || eyeballList.size() == 0) {
            eyeballList = FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeball");
        }
    }

    @Override
    public EnumMap<EffectType, Float> getMakeupStrengthMap() {
        EnumMap<EffectType, Float> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> makeupList = EffectType.Companion.getMakeupList();
        for (EffectType item : makeupList) {
            map.put(item, EffectInfoDataHelper.getInstance().getStrength(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Integer> getMakeupContentSelectedIndexMap() {
        EnumMap<EffectType, Integer> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> makeupList = EffectType.Companion.getMakeupList();
        for (EffectType item : makeupList) {
            map.put(item, EffectInfoDataHelper.getInstance().getContentSelectedIndex(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Integer> getFilterContentSelectedIndexMap() {
        EnumMap<EffectType, Integer> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> filterList = EffectType.Companion.getFilterList();
        for (EffectType item : filterList) {
            map.put(item, EffectInfoDataHelper.getInstance().getContentSelectedIndex(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Integer> getStyleContentSelectedIndexMap() {
        EnumMap<EffectType, Integer> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> styleList = EffectType.Companion.getStyleList();
        for (EffectType item : styleList) {
            map.put(item, EffectInfoDataHelper.getInstance().getContentSelectedIndex(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Float> getFilterStrengthMap() {
        EnumMap<EffectType, Float> map = new EnumMap<>(EffectType.class);
        ArrayList<EffectType> filterList = EffectType.Companion.getFilterList();
        for (EffectType item : filterList) {
            map.put(item, EffectInfoDataHelper.getInstance().getStrength(item));
        }

        ArrayList<EffectType> filterStrengthList = EffectType.Companion.getFilterList();
        for (EffectType item : filterStrengthList) {
            map.put(item, EffectInfoDataHelper.getInstance().getStrength(item));
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Float> getBasicStrengthMap() {
        // 默认参数基础上进行修改
        EnumMap<EffectType, Float> map = EffectType.Companion.getStrengthMap(EffectType.GROUP_EFFECT);

        ArrayList<EffectType> allBasicType = EffectType.Companion.getAllBasicType();
        for (EffectType item : allBasicType) {
            float basicStrength = EffectInfoDataHelper.getInstance().getStrength(item);
            // 数据库无
            if (basicStrength != -2f) {
                map.put(item, basicStrength);
            } else {
                LogUtils.iTag(TAG, item.getDesc() + "set default strength:" + item.getStrength());
                map.put(item, item.getStrength());
            }
        }
        return map;
    }

    @Override
    public EnumMap<EffectType, Float> getBasicStrengthMap(EffectType type) {
        // 默认参数基础上进行修改
        EnumMap<EffectType, Float> map = EffectType.Companion.getStrengthMap(type);

        ArrayList<EffectType> allBasicType;
        if (type == EffectType.TYPE_BASE) {
            allBasicType = EffectType.Companion.getBasicEffectList();
        } else if (type == EffectType.TYPE_RESHAPE) {
            allBasicType = EffectType.Companion.getMxEffectList();
        } else if (type == EffectType.TYPE_PLASTIC) {
            allBasicType = EffectType.Companion.getWzhEffectList();
        } else {
            allBasicType = EffectType.Companion.getAdjustList();
        }
        for (EffectType item : allBasicType) {
            float basicStrength = EffectInfoDataHelper.getInstance().getStrength(item);
            // 数据库无
            if (basicStrength != -2f) {
                map.put(item, basicStrength);
            } else {
                LogUtils.iTag(TAG, item.getDesc() + "set default strength:" + item.getStrength());
                map.put(item, item.getStrength());
            }
        }
        return map;
    }

    @Override
    public HashMap<String, ArrayList<MakeupItem>> getMakeupLists() {
        HashMap<String, ArrayList<MakeupItem>> mMakeupLists = new HashMap<>();
        mMakeupLists.put("makeup_lip", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_lip"));
        mMakeupLists.put("makeup_highlight", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_highlight"));
        mMakeupLists.put("makeup_blush", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_blush"));
        mMakeupLists.put("makeup_brow", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_brow"));
        mMakeupLists.put("makeup_eyeshadow", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeshadow"));
        mMakeupLists.put("makeup_eyeliner", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeliner"));
        mMakeupLists.put("makeup_eyelash", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyelash"));
        mMakeupLists.put("makeup_eyeball", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_eyeball"));
        mMakeupLists.put("makeup_hairdye", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_hairdye"));
        mMakeupLists.put("makeup_all", FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_all"));
        return mMakeupLists;
    }

    @Override
    public ArrayList<TryOnTitleEntity> getTryOnTitleList() {
        ArrayList<TryOnTitleEntity> list = new ArrayList<>();
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_BASIC, MultiLanguageUtils.getStr(R.string.try_on_title1)));// 美颜
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_LIP, MultiLanguageUtils.getStr(R.string.try_on_title2)));// 口红
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_HAIR, MultiLanguageUtils.getStr(R.string.try_on_title3)));//染发
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_CHUNXIAN, MultiLanguageUtils.getStr(R.string.try_on_title4)));// 唇线
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_YANYING, MultiLanguageUtils.getStr(R.string.try_on_title5)));//眼影

        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_YANXIAN, MultiLanguageUtils.getStr(R.string.try_on_title6)));// 眼线
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_YANYIN, MultiLanguageUtils.getStr(R.string.try_on_title7)));//
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_YANJIEMAO, MultiLanguageUtils.getStr(R.string.try_on_title8)));
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_MEIMAO, MultiLanguageUtils.getStr(R.string.try_on_title9)));
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_SAIHONG, MultiLanguageUtils.getStr(R.string.try_on_title10)));

        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_XIURONG, MultiLanguageUtils.getStr(R.string.try_on_title11)));
        list.add(new TryOnTitleEntity(EffectType.TYPE_TRY_ON_FENDI, MultiLanguageUtils.getStr(R.string.try_on_title12)));
        return list;
    }

    @Override
    public List<BasicEffectEntity> getTryOnBoyDefParams() {
        String json = ReadAssetsJsonFileUtils.getJson("localData/tryOn/style2.json");
        return new Gson().fromJson(json, new TypeToken<List<BasicEffectEntity>>() {
        }.getType());
    }

    @Override
    public List<BasicEffectEntity> getTryOnGirlDefParams() {
        String json = ReadAssetsJsonFileUtils.getJson("localData/tryOn/style1.json");
        return new Gson().fromJson(json, new TypeToken<List<BasicEffectEntity>>() {
        }.getType());
    }

    @Override
    public List<StickerItem> getTryOnLipList() {
        List<StickerItem> list = new ArrayList<>();
        list.add(new StickerItem(EFFECT_LIPSTICK_LUSTRE, false, "水润", "file:///android_asset/mock/try_on_lip_zhidi/shuirun.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_METAL, false, "金属", "file:///android_asset/mock/try_on_lip_zhidi/jinshu.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_FROST, false, "闪烁", "file:///android_asset/mock/try_on_lip_zhidi/shanshuo.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_MATTE, false, "雾化", "file:///android_asset/mock/try_on_lip_zhidi/wuhua.png"));
        list.add(new StickerItem(STEffectLipStickFinish.EFFECT_LIPSTICK_CREAMY, false, "自然", "file:///android_asset/mock/try_on_lip_zhidi/ziran.png"));
        return list;
    }

    @Override
    public List<StickerItem> getTryOnLipLineStyleList() {
        List<StickerItem> list = new ArrayList<>();
        list.add(new StickerItem(EFFECT_LIPSTICK_LUSTRE, false, "全部", "file:///android_asset/mock/try_on_lipline_style/all.png"));
        list.add(new StickerItem(EFFECT_LIPSTICK_METAL, false, "正常", "file:///android_asset/mock/try_on_lipline_style/half.png"));
        return list;
    }

    public EnumMap<EffectType, List<?>> getMakeupListsLocal2() {
        EnumMap<EffectType, List<?>> mMakeupLists = new EnumMap<>(EffectType.class);
        mMakeupLists.put(EffectType.TYPE_LIP, FileUtils.getMakeupFiles(SenseMeApplication.getContext(), "makeup_lip"));
        return mMakeupLists;
    }

    @Override
    public EnumMap<EffectType, ArrayList<MakeupItem>> getMakeupListsLocal() {
        return null;
    }

    @Override
    public HashMap<EffectType, List<?>> getMakeupListsNew() {
        HashMap<EffectType, List<?>> mMakeupLists = new HashMap<>();
        mMakeupLists.put(EffectType.TYPE_HAIR, hairList);
        mMakeupLists.put(EffectType.TYPE_LIP, lipList);
        mMakeupLists.put(EffectType.TYPE_BLUSH, blushList);
        mMakeupLists.put(EffectType.TYPE_XR, xrList);
        mMakeupLists.put(EffectType.TYPE_EYE_BROW, browList);
        mMakeupLists.put(EffectType.TYPE_EYE_SHADOW, eyeshadowList);
        mMakeupLists.put(EffectType.TYPE_EYE_LINER, eyelinerList);
        mMakeupLists.put(EffectType.TYPE_EYELASH, eyelashList);
        mMakeupLists.put(EffectType.TYPE_EYEBALL, eyeballList);
        return mMakeupLists;
    }

    @Override
    public ArrayList<BeautyOptionsItem> getBeautyOptionsList() {
        ArrayList<BeautyOptionsItem> mBeautyOptionsList = new ArrayList<>();
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_BASE, MultiLanguageUtils.getStr(R.string.menu_title_base)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_RESHAPE, MultiLanguageUtils.getStr(R.string.menu_title_reshape)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_PLASTIC, MultiLanguageUtils.getStr(R.string.menu_title_plastic)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_3D_PLASTIC, MultiLanguageUtils.getStr(R.string.menu_title_3d_plastic)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_TONE, MultiLanguageUtils.getStr(R.string.menu_title_tone)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_BOKEN, MultiLanguageUtils.getStr(R.string.menu_title_boken)));
        return mBeautyOptionsList;
    }

    @Override
    public ArrayList<BeautyOptionsItem> getStickerOptionsListNew() {
        ArrayList<BeautyOptionsItem> mStickerOptionsList = new ArrayList<>();
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_NEW,
                MultiLanguageUtils.getStr(R.string.sticker_new), Constants.GROUP_NEW));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_2D,
                MultiLanguageUtils.getStr(R.string.sticker_2D), Constants.GROUP_2D));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_3D,
                MultiLanguageUtils.getStr(R.string.sticker_3D), Constants.GROUP_3D));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_HANDLE,
                MultiLanguageUtils.getStr(R.string.sticker_handle), Constants.GROUP_HAND));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_BG,
                MultiLanguageUtils.getStr(R.string.sticker_bg), Constants.GROUP_BG));

        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_FACE,
                MultiLanguageUtils.getStr(R.string.sticker_bx), Constants.GROUP_FACE));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_PARTICLE,
                MultiLanguageUtils.getStr(R.string.sticker_lizi), Constants.GROUP_PARTICLE));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_CAT,
                MultiLanguageUtils.getStr(R.string.sticker_cat), Constants.GROUP_CAT));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_BIG_HEAD,
                MultiLanguageUtils.getStr(R.string.sticker_big_head), Constants.GROUP_BIG_HEAD));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_SHADOW,
                MultiLanguageUtils.getStr(R.string.sticker_clone), Constants.GROUP_FENSHEN));

        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_BUCKLE,
                MultiLanguageUtils.getStr(R.string.sticker_face), Constants.GROUP_MASK_FACE));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_PLAY,
                MultiLanguageUtils.getStr(R.string.sticker_play), Constants.GROUP_PLAY));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_TRACK,
                MultiLanguageUtils.getStr(R.string.sticker_track)));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_AVATAR,
                MultiLanguageUtils.getStr(R.string.sticker_avatar), Constants.GROUP_AVATAR));
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_TRY_ON,
                MultiLanguageUtils.getStr(R.string.sticker_tryon), Constants.GROUP_TRY_ON));

        //mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_CARTOON,MultiLanguageUtils.getStr(R.string.sticker_cartoon), Constants.GROUP_CARTOON));

        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_LOCAL,
                MultiLanguageUtils.getStr(R.string.sticker_local)));
        if (PropertiesUtil.INSTANCE.getBooleanValue(Constants.CFG_K_OPEN_ADD_STICKER)) {
            mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_ADD,
                    MultiLanguageUtils.getStr(R.string.sticker_add)));
        }
        mStickerOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_STICKER_SYNC,
                MultiLanguageUtils.getStr(R.string.sticker_sync)));
        return mStickerOptionsList;
    }

    @Override
    public ArrayList<BeautyOptionsItem> getStyleOptionsList() {
        ArrayList<BeautyOptionsItem> list = new ArrayList<>();
        list.add(new BeautyOptionsItem(EffectType.TYPE_ZIRAN, MultiLanguageUtils.getStr(R.string.camera_nature)));
        list.add(new BeautyOptionsItem(EffectType.TYPE_QINGZHUANG, MultiLanguageUtils.getStr(R.string.camera_light)));
        list.add(new BeautyOptionsItem(EffectType.TYPE_FASHION, MultiLanguageUtils.getStr(R.string.camera_popular)));
        return list;
    }

    @Override
    public HashMap<EffectType, List<StickerItem>> getStyleContentList() {
        HashMap<EffectType, List<StickerItem>> hashMap = new HashMap<>();
        ArrayList<StickerItem> lightlyList = FileUtils.getStickerFiles(getContext(), "style_lightly");
        CollectionSortUtils.sortStyleList(lightlyList);
        hashMap.put(EffectType.TYPE_ZIRAN, FileUtils.getStickerFiles(getContext(), "style_nature"));
        hashMap.put(EffectType.TYPE_QINGZHUANG, lightlyList);
        hashMap.put(EffectType.TYPE_FASHION, FileUtils.getStickerFiles(getContext(), "style_fashion"));
        return hashMap;
    }

    // 通用物体追踪
    public static ArrayList<StickerItem> getTraceContentList() {
        ArrayList<StickerItem> list = new ArrayList<>();
        list.add(new StickerItem("file:///android_asset/localData/object/object_hi.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_happy.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_star.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_sticker.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_love.png", StickerState.DONE_STATE));
        list.add(new StickerItem("file:///android_asset/localData/object/object_sun.png", StickerState.DONE_STATE));
        return list;
    }

    @Override
    public HashMap<EffectType, List<StickerItem>> getStickerContentList() {
        HashMap<EffectType, List<StickerItem>> hashMap = new HashMap<>();
        hashMap.put(EffectType.TYPE_STICKER_ADD, FileUtils.getStickerFiles(getContext(), Constants.STICKER_LOCAL));
        hashMap.put(EffectType.TYPE_STICKER_SYNC, FileUtils.getStickerFiles(getContext(), Constants.STICKER_SYNC));
        hashMap.put(EffectType.TYPE_STICKER_LOCAL, FileUtils.getStickerFiles(getContext(), Constants.STICKER_LOCAL));
        hashMap.put(EffectType.TYPE_STICKER_TRACK, getTraceContentList());
        return hashMap;
    }

    @Override
    public ArrayList<BeautyOptionsItem> getFilterOptionsList() {
        ArrayList<BeautyOptionsItem> mBeautyOptionsList = new ArrayList<>();
        mBeautyOptionsList.add(0, new BeautyOptionsItem(EffectType.TYPE_PEOPLE, MultiLanguageUtils.getStr(R.string.filter_portrait)));
        mBeautyOptionsList.add(1, new BeautyOptionsItem(EffectType.TYPE_SCENERY, MultiLanguageUtils.getStr(R.string.filter_scenery)));
        mBeautyOptionsList.add(2, new BeautyOptionsItem(EffectType.TYPE_STILL_LIFE, MultiLanguageUtils.getStr(R.string.filter_still_life)));
        mBeautyOptionsList.add(3, new BeautyOptionsItem(EffectType.TYPE_FOOD, MultiLanguageUtils.getStr(R.string.filter_food)));
        return mBeautyOptionsList;
    }

    @Override
    public ArrayList<BeautyOptionsItem> getMakeupOptionsList() {
        ArrayList<BeautyOptionsItem> mBeautyOptionsList = new ArrayList<>();
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_HAIR, MultiLanguageUtils.getStr(R.string.make_up_hair)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_LIP, MultiLanguageUtils.getStr(R.string.make_up_lipstick)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_BLUSH, MultiLanguageUtils.getStr(R.string.make_up_blusher)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_XR, MultiLanguageUtils.getStr(R.string.make_up_highlight)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_EYE_BROW, MultiLanguageUtils.getStr(R.string.make_up_brow)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_EYE_SHADOW, MultiLanguageUtils.getStr(R.string.make_up_eye_shadow)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_EYE_LINER, MultiLanguageUtils.getStr(R.string.make_up_eye_liner)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_EYELASH, MultiLanguageUtils.getStr(R.string.make_up_eyelash)));
        mBeautyOptionsList.add(new BeautyOptionsItem(EffectType.TYPE_EYEBALL, MultiLanguageUtils.getStr(R.string.make_up_eyeball)));
        return mBeautyOptionsList;
    }

    @Override
    public HashMap<EffectType, List<?>> getFilterContentList() {
        HashMap<EffectType, List<?>> data = new HashMap<>();
        data.put(EffectType.TYPE_PEOPLE, selfieList);
        data.put(EffectType.TYPE_SCENERY, sceneryList);
        data.put(EffectType.TYPE_FOOD, objectsList);
        data.put(EffectType.TYPE_STILL_LIFE, foodsList);
        return data;
    }

    // 微整形
    @Override
    public ArrayList<BeautyItem> getMicroBeautyList() {
        ArrayList<BeautyItem> mMicroBeautyItem = new ArrayList<>();
        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_THINNER_HEAD_1, MultiLanguageUtils.getStr(R.string.plastic_thinner_head),
                R.drawable.beauty_small_head_unselected,
                R.drawable.beauty_small_head_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_THINNER_HEAD_2, MultiLanguageUtils.getStr(R.string.plastic_thinner_head2),
                R.drawable.beauty_small_head_unselected,
                R.drawable.beauty_small_head_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_2, MultiLanguageUtils.getStr(R.string.plastic_thin_face),
                R.drawable.beauty_thin_face_unselected,
                R.drawable.beauty_thin_face_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_3, MultiLanguageUtils.getStr(R.string.plastic_chin_length),
                R.drawable.beauty_chin_unselected,
                R.drawable.beauty_chin_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_4, MultiLanguageUtils.getStr(R.string.plastic_hairline_height),
                R.drawable.beauty_forehead_unselected,
                R.drawable.beauty_forehead_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_JAW, MultiLanguageUtils.getStr(R.string.plastic_jaw),
                R.drawable.beauty_jaw_unselected,
                R.drawable.beauty_jaw_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_5, MultiLanguageUtils.getStr(R.string.plastic_apple_musle),
                R.drawable.beauty_apple_musle_unselected,
                R.drawable.beauty_apple_musle_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_6, MultiLanguageUtils.getStr(R.string.plastic_narrow_nose),
                R.drawable.beauty_thin_nose_unselected,
                R.drawable.beauty_thin_nose_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_7, MultiLanguageUtils.getStr(R.string.plastic_nose_length),
                R.drawable.beauty_long_nose_unselected,
                R.drawable.beauty_long_nose_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_8, MultiLanguageUtils.getStr(R.string.plastic_profile_rhinoplasty),
                R.drawable.beauty_profile_rhinoplasty_unselected,
                R.drawable.beauty_profile_rhinoplasty_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_9, MultiLanguageUtils.getStr(R.string.plastic_mouth_size),
                R.drawable.beauty_mouth_type_unselected,
                R.drawable.beauty_mouth_type_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_10, MultiLanguageUtils.getStr(R.string.plastic_philtrum_length),
                R.drawable.beauty_philtrum_unselected,
                R.drawable.beauty_philtrum_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_11, MultiLanguageUtils.getStr(R.string.plastic_eye_distance),
                R.drawable.beauty_eye_distance_unselected,
                R.drawable.beauty_eye_distance_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_12, MultiLanguageUtils.getStr(R.string.plastic_eye_angle),
                R.drawable.beauty_eye_angle_unselected,
                R.drawable.beauty_eye_angle_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_13, MultiLanguageUtils.getStr(R.string.plastic_open_canthus),
                R.drawable.beauty_open_canthus_unselected,
                R.drawable.beauty_open_canthus_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_14, MultiLanguageUtils.getStr(R.string.plastic_bright_eye),
                R.drawable.beauty_bright_eye_unselected,
                R.drawable.beauty_bright_eye_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_15, MultiLanguageUtils.getStr(R.string.plastic_black_eye),
                R.drawable.beauty_remove_dark_circles_unselected,
                R.drawable.beauty_remove_dark_circles_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_16, MultiLanguageUtils.getStr(R.string.plastic_wrinkle),
                R.drawable.beauty_remove_nasolabial_folds_unselected,
                R.drawable.beauty_remove_nasolabial_folds_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_17, MultiLanguageUtils.getStr(R.string.plastic_white_teeth),
                R.drawable.beauty_white_teeth_unselected,
                R.drawable.beauty_white_teeth_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_18, MultiLanguageUtils.getStr(R.string.plastic_cheekbone),
                R.drawable.beauty_thin_cheekbone_unselected,
                R.drawable.beauty_thin_cheekbone_selected));

        mMicroBeautyItem.add(new BeautyItem(EffectType.TYPE_WZH_19, MultiLanguageUtils.getStr(R.string.plastic_open_external_canthus),
                R.drawable.beauty_open_external_canthus_unselected,
                R.drawable.beauty_open_external_canthus_selected));
        return mMicroBeautyItem;
    }

    public ArrayList<BeautyItem> getBokenList() {
        ArrayList<BeautyItem> mBeautyBaseItem = new ArrayList<>();
        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_BOKEH1, MultiLanguageUtils.getStr(R.string.basic_boken1),
                R.drawable.beauty_boken_unselected,
                R.drawable.beauty_boken_selected));
        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_BOKEH2, MultiLanguageUtils.getStr(R.string.basic_boken2),
                R.drawable.beauty_boken_unselected,
                R.drawable.beauty_boken_selected));
        return mBeautyBaseItem;
    }

    @Override
    public ArrayList<BeautyItem> getBeautyBaseList() {
        ArrayList<BeautyItem> mBeautyBaseItem = new ArrayList<>();
        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_1, MultiLanguageUtils.getStr(R.string.basic_whiten1),
                R.drawable.beauty_whiten_unselected,
                R.drawable.beauty_whiten_selected));

        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_2, MultiLanguageUtils.getStr(R.string.basic_whiten2),
                R.drawable.beauty_whiten_unselected,
                R.drawable.beauty_whiten_selected));

        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_3, MultiLanguageUtils.getStr(R.string.basic_whiten3),
                R.drawable.beauty_whiten_unselected,
                R.drawable.beauty_whiten_selected));

        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_4, MultiLanguageUtils.getStr(R.string.basic_redden),
                R.drawable.beauty_redden_unselected,
                R.drawable.beauty_redden_selected));

        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_5, MultiLanguageUtils.getStr(R.string.basic_smooth1),
                R.drawable.beauty_smooth_unselected,
                R.drawable.beauty_smooth_selected));

        mBeautyBaseItem.add(new BeautyItem(EffectType.TYPE_BASIC_6, MultiLanguageUtils.getStr(R.string.basic_smooth2),
                R.drawable.beauty_smooth_unselected,
                R.drawable.beauty_smooth_selected));

        return mBeautyBaseItem;
    }

    @Override
    public ArrayList<BeautyItem> getProfessionalBeautyList() {
        ArrayList<BeautyItem> mProfessionalBeautyItem = new ArrayList<>();
        mProfessionalBeautyItem.add(new BeautyItem(EffectType.TYPE_MX_1, MultiLanguageUtils.getStr(R.string.reshape_shrink_face),
                R.drawable.beauty_shrink_face_unselected,
                R.drawable.beauty_shrink_face_selected));

        mProfessionalBeautyItem.add(new BeautyItem(EffectType.TYPE_MX_HIGH_THIN_FACE, MultiLanguageUtils.getStr(R.string.reshape_high_face),
                R.drawable.ic_camera_hight_thin_face_unselected,
                R.drawable.ic_camera_hight_thin_face_selected));

        mProfessionalBeautyItem.add(new BeautyItem(EffectType.TYPE_MX_2, MultiLanguageUtils.getStr(R.string.reshape_enlarge_eye),
                R.drawable.beauty_enlargeeye_unselected,
                R.drawable.beauty_enlargeeye_selected));

        mProfessionalBeautyItem.add(new BeautyItem(EffectType.TYPE_MX_3, MultiLanguageUtils.getStr(R.string.reshape_shrink_jaw),
                R.drawable.beauty_small_face_unselected,
                R.drawable.beauty_small_face_selected));

        mProfessionalBeautyItem.add(new BeautyItem(EffectType.TYPE_MX_4, MultiLanguageUtils.getStr(R.string.reshape_narrow_face),
                R.drawable.beauty_narrow_face_unselected,
                R.drawable.beauty_narrow_face_selected));

        mProfessionalBeautyItem.add(new BeautyItem(EffectType.TYPE_MX_5, MultiLanguageUtils.getStr(R.string.reshape_round_eye),
                R.drawable.beauty_round_eye_unselected,
                R.drawable.beauty_round_eye_selected));
        return mProfessionalBeautyItem;
    }

    @Override
    public ArrayList<BeautyItem> get3DWZhList() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        list.addAll(getProfessionalMouthList());
        list.addAll(getProfessionalNoseList());
        list.addAll(getProfessionalEyeList());
        //list.addAll(getProfessionalHeadList());
        list.addAll(getProfessionalFaceList());
        return list;
    }

    private ArrayList<BeautyItem> getProfessionalEyeList() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_EYE_4, MultiLanguageUtils.getStr(R.string.plastic_3d_eye_4),
                R.drawable.wzh3d_eye_4_unselected,
                R.drawable.wzh3d_eye_4_selected));

        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_EYE_5, MultiLanguageUtils.getStr(R.string.plastic_3d_eye_5),
                R.drawable.wzh3d_eye_5_unselected,
                R.drawable.wzh3d_eye_5_selected));

        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_EYE_6, MultiLanguageUtils.getStr(R.string.plastic_3d_eye_6),
                R.drawable.wzh3d_eye_6_unselected,
                R.drawable.wzh3d_eye_6_selected));

        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_EYE_7, MultiLanguageUtils.getStr(R.string.plastic_3d_eye_7),
                R.drawable.wzh3d_eye_7_unselected,
                R.drawable.wzh3d_eye_7_selected));

        // 外眼尾
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_EYE_OUTEREYETAIL, MultiLanguageUtils.getStr(R.string.plastic_3d_eye_outereyetail),
                R.drawable.wzh3d_eye_outereyetail_unselected,
                R.drawable.wzh3d_eye_outereyetail_selected));

        // 内眼角尖
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_EYE_INNERCORNER, MultiLanguageUtils.getStr(R.string.plastic_3d_eye_innercorner),
                R.drawable.wzh3d_eye_innercorner_unselected,
                R.drawable.wzh3d_eye_innercorner_selected));

        return list;
    }

    private ArrayList<BeautyItem> getProfessionalNoseList() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        // 鼻子比例
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_1, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_1),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_1_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_1_selected)));

        // 鼻宽
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_2, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_2),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_2_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_2_selected)));

//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_3, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_3),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_bichang_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_bichang_selected)));

        // 鼻高
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_4, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_4),
                R.drawable.wzh3d_nose_bigao_unselected,
                R.drawable.wzh3d_nose_bigao_selected));

        // 鼻根
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_5, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_5),
                R.drawable.wzh3d_nose_bigen_unselected,
                R.drawable.wzh3d_nose_bigen_selected));

        // 鼻子驼峰
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_6, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_6),
                R.drawable.wzh3d_nose_tuofeng_unselected,
                R.drawable.wzh3d_nose_tuofeng_selected));

        // 鼻尖
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_7, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_7),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_bj_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_nose_bj_selected)));
        //biyi
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NOSE_8, MultiLanguageUtils.getStr(R.string.plastic_3d_nose_8),
                R.drawable.wzh3d_nose_biyi_unselected,
                R.drawable.wzh3d_nose_biyi_selected));
        return list;
    }

    private ArrayList<BeautyItem> getProfessionalMouthList() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_MOUTH_1, MultiLanguageUtils.getStr(R.string.plastic_3d_mouth_1),
                R.drawable.wzh3d_nose_bili_unselected,
                R.drawable.wzh3d_nose_bili_selected));

        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_MOUTH_2, MultiLanguageUtils.getStr(R.string.plastic_3d_mouth_2),
                R.drawable.wzh3d_nose_gd_unselected,
                R.drawable.wzh3d_nose_gd_selected));
        // 嘴巴宽度
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_MOUTH_3, MultiLanguageUtils.getStr(R.string.plastic_3d_mouth_3),
                R.drawable.wzh3d_nose_kuan_unselected,
                R.drawable.wzh3d_nose_kuan_selected));

        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_MOUTH_4, MultiLanguageUtils.getStr(R.string.plastic_3d_mouth_4),
                R.drawable.wzh3d_nose_shen_unselected,
                R.drawable.wzh3d_nose_shen_selected));

        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_MOUTH_5, MultiLanguageUtils.getStr(R.string.plastic_3d_mouth_5),
                R.drawable.wzh3d_nose_hou_unselected,
                R.drawable.wzh3d_nose_hou_selected));

        // 嘟嘟唇
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NEW_1, MultiLanguageUtils.getStr(R.string.plastic_3d_face_5),
                R.drawable.wzh3d_chun_dudu_unselected,
                R.drawable.wzh3d_chun_dudu_selected));

        // 微笑唇
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NEW_2, MultiLanguageUtils.getStr(R.string.plastic_3d_face_6),
                R.drawable.wzh3d_chun_wx_unselected,
                R.drawable.wzh3d_chun_wx_selected));
        return list;
    }

    private ArrayList<BeautyItem> getProfessionalFaceList() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        // 脸部胖瘦
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_FACE_1, MultiLanguageUtils.getStr(R.string.plastic_3d_face_1),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_face_pshou_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_face_pshou_selected)));

        // 脸部角度
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_FACE_2, MultiLanguageUtils.getStr(R.string.plastic_3d_face_2),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_head_jiaodu_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_head_jiaodu_selected)));

        // 脸部外扩
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_FACE_3, MultiLanguageUtils.getStr(R.string.plastic_3d_face_3),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_face_waikuo_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_face_waikuo_selected)));

        // 脸部内缩
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_FACE_4, MultiLanguageUtils.getStr(R.string.plastic_3d_face_4),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_face_neisuo_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_face_neisuo_selected)));

        // 苹果肌
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_CHEEKBONE, MultiLanguageUtils.getStr(R.string.plastic_3d_face_7),
                R.drawable.wzh3d_pingguo_unselected,
                R.drawable.wzh3d_pingguo_selected));

        // 脸部轮廓
//        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NEW_4, MultiLanguageUtils.getStr(R.string.plastic_3d_face_8),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_xiahexian_unselected),
//                BitmapFactory.decodeResource(getResources(), R.drawable.wzh3d_xiahexian_selected)));

        // 额头
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_FOREHEAD, MultiLanguageUtils.getStr(R.string.micro_3d_forehead),
                R.drawable.micro_3d_forehead_unselected,
                R.drawable.micro_3d_forehead_selected));
        // 法令纹
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_NASOLABIAL, MultiLanguageUtils.getStr(R.string.micro_3d_nasolabial),
                R.drawable.micro_3d_nasolabial_unselected,
                R.drawable.micro_3d_nasolabial_selected));
        // 泪沟
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_TEARDITCH, MultiLanguageUtils.getStr(R.string.micro_3d_tearditch),
                R.drawable.micro_3d_tearditch_unselected,
                R.drawable.micro_3d_tearditch_selected));
        // 眉骨
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_BROWBONE, MultiLanguageUtils.getStr(R.string.micro_3d_browbone),
                R.drawable.micro_3d_browbone_unselected,
                R.drawable.micro_3d_browbone_selected));
        // 挑眉
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_RAISEEYEBROWS, MultiLanguageUtils.getStr(R.string.micro_3d_raiseeyebrows),
                R.drawable.micro_3d_raiseeyebrows_unselected,
                R.drawable.micro_3d_raiseeyebrows_selected));
        // 太阳穴
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_TEMPLE, MultiLanguageUtils.getStr(R.string.micro_3d_temple),
                R.drawable.micro_3d_temple_unselected,
                R.drawable.micro_3d_temple_selected));
        // 侧额头
        list.add(new BeautyItem(EffectType.TYPE_WZH_3D_FOREHEADTWO, MultiLanguageUtils.getStr(R.string.micro_3d_foreheadtwo),
                R.drawable.micro_3d_forehead_unselected,
                R.drawable.micro_3d_forehead_selected));
        return list;
    }

    @Override
    public ArrayList<BeautyItem> getHighThinFaceBeautyList() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        list.add(new BeautyItem(EffectType.TYPE_HIGH_BACK, MultiLanguageUtils.getStr(R.string.reshape_high_face),
                R.drawable.ic_camera_hight_face_back,
                R.drawable.ic_camera_hight_face_back));

        list.add(new BeautyItem(EffectType.TYPE_HIGH_1, MultiLanguageUtils.getStr(R.string.reshape_nature),
                R.drawable.ic_camera_nature_face_unselected,
                R.drawable.ic_camera_nature_face_selected));

        list.add(new BeautyItem(EffectType.TYPE_HIGH_2, MultiLanguageUtils.getStr(R.string.reshape_nvsh),
                R.drawable.ic_camera_nvsh_unselected,
                R.drawable.ic_camera_nvsh_selected));

        list.add(new BeautyItem(EffectType.TYPE_HIGH_3, MultiLanguageUtils.getStr(R.string.reshape_long_face),
                R.drawable.ic_camera_long_face_unselected,
                R.drawable.ic_camera_long_face_selected));

        list.add(new BeautyItem(EffectType.TYPE_HIGH_4, MultiLanguageUtils.getStr(R.string.reshape_round_face),
                R.drawable.ic_camera_round_face_unselected,
                R.drawable.ic_camera_round_face_selected));

        return list;
    }

    @Override
    public ArrayList<BeautyItem> getAdjustBeautyList() {
        ArrayList<BeautyItem> mAdjustBeautyItem = new ArrayList<>();
        mAdjustBeautyItem.add(new BeautyItem(EffectType.TYPE_TZH_1, MultiLanguageUtils.getStr(R.string.adjust_contrast),
                R.drawable.beauty_contrast_unselected,
                R.drawable.beauty_contrast_selected));

        mAdjustBeautyItem.add(new BeautyItem(EffectType.TYPE_TZH_2, MultiLanguageUtils.getStr(R.string.adjust_saturation),
                R.drawable.beauty_saturation_unselected,
                R.drawable.beauty_saturation_selected));

        mAdjustBeautyItem.add(new BeautyItem(EffectType.TYPE_TZH_3, MultiLanguageUtils.getStr(R.string.adjust_sharpen),
                R.drawable.beauty_sharp_unselected,
                R.drawable.beauty_sharp_selected));

        mAdjustBeautyItem.add(new BeautyItem(EffectType.TYPE_TZH_4, MultiLanguageUtils.getStr(R.string.adjust_clear),
                R.drawable.beauty_clear_unselected,
                R.drawable.beauty_clear_selected));

        return mAdjustBeautyItem;
    }

    @Override
    public ArrayList<BeautyItem> getBodyBeautyList() {
        ArrayList<BeautyItem> list = new ArrayList<>();
        list.add(new BeautyItem(EffectType.TYPE_BODY_1, "整体效果",
                R.drawable.beauty_contrast_unselected,
                R.drawable.beauty_contrast_unselected));

        list.add(new BeautyItem(EffectType.TYPE_BODY_2, "瘦头",
                R.drawable.ic_body_head_unselected,
                R.drawable.ic_body_head_unselected));

        list.add(new BeautyItem(EffectType.TYPE_BODY_3, "瘦肩",
                R.drawable.ic_body_j_unselected,
                R.drawable.ic_body_j_unselected));

        list.add(new BeautyItem(EffectType.TYPE_BODY_4, "美臀",
                R.drawable.ic_body_tun_unselected,
                R.drawable.ic_body_tun_unselected));

        list.add(new BeautyItem(EffectType.TYPE_BODY_5, "瘦腿",
                R.drawable.ic_body_t_unselected,
                R.drawable.ic_body_t_unselected));

        return list;
    }

    private static Context getContext() {
        return SenseMeApplication.getContext();
    }
}
