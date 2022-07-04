package sensetime.senseme.com.effects.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import sensetime.senseme.com.effects.entity.BasicEffectEntity;
import sensetime.senseme.com.effects.entity.TryOnTitleEntity;
import sensetime.senseme.com.effects.view.BeautyItem;
import sensetime.senseme.com.effects.view.BeautyOptionsItem;
import sensetime.senseme.com.effects.view.MakeupItem;
import sensetime.senseme.com.effects.view.StickerItem;
import sensetime.senseme.com.effects.view.widget.EffectType;

public interface LocalDataStoreI {

    ArrayList<TryOnTitleEntity> getTryOnTitleList();

    List<BasicEffectEntity> getTryOnBoyDefParams();

    List<BasicEffectEntity> getTryOnGirlDefParams();

    List<StickerItem> getTryOnLipList();

    // 唇线样式
    List<StickerItem> getTryOnLipLineStyleList();

    EnumMap<EffectType, List<?>> getMakeupListsLocal2();

    EnumMap<EffectType, ArrayList<MakeupItem>> getMakeupListsLocal();

    // 美妆
    EnumMap<EffectType, Float> getMakeupStrengthMap();

    EnumMap<EffectType, Integer> getMakeupContentSelectedIndexMap();

    EnumMap<EffectType, Integer> getFilterContentSelectedIndexMap();

    EnumMap<EffectType, Integer> getStyleContentSelectedIndexMap();

    EnumMap<EffectType, Float> getFilterStrengthMap();

    EnumMap<EffectType, Float> getBasicStrengthMap();

    EnumMap<EffectType, Float> getBasicStrengthMap(EffectType type);

    // 整妆所有本地素材
    HashMap<String, ArrayList<MakeupItem>> getMakeupLists();

    HashMap<EffectType, List<?>> getMakeupListsNew();

    // 美颜标题
    ArrayList<BeautyOptionsItem> getBeautyOptionsList();

    // 贴纸标题（new）
    ArrayList<BeautyOptionsItem> getStickerOptionsListNew();

    // 风格标题
    ArrayList<BeautyOptionsItem> getStyleOptionsList();

    HashMap<EffectType, List<StickerItem>> getStyleContentList();

    HashMap<EffectType, List<StickerItem>> getStickerContentList();

    // 滤镜标题
    ArrayList<BeautyOptionsItem> getFilterOptionsList();

    ArrayList<BeautyOptionsItem> getMakeupOptionsList();

    HashMap<EffectType, List<?>> getFilterContentList();

    // 美形
    ArrayList<BeautyItem> getMicroBeautyList();

    // 基础美颜
    ArrayList<BeautyItem> getBeautyBaseList();

    // 微整形
    ArrayList<BeautyItem> getProfessionalBeautyList();

    // 3D微整形
    ArrayList<BeautyItem> get3DWZhList();

    ArrayList<BeautyItem> getBokenList();

    // 高阶瘦脸
    ArrayList<BeautyItem> getHighThinFaceBeautyList();

    // 调整
    ArrayList<BeautyItem> getAdjustBeautyList();

    // 美体
    ArrayList<BeautyItem> getBodyBeautyList();

}
