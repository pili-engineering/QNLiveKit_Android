package sensetime.senseme.com.effects.view.widget

import sensetime.senseme.com.effects.utils.LogUtils
import com.sensetime.stmobile.model.STMobileMakeupType.*
import com.sensetime.stmobile.params.STEffectBeautyType.*
import sensetime.senseme.com.effects.utils.Constants
import sensetime.senseme.com.effects.utils.ContextHolder
import java.util.*

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/23/21 12:52 PM
 */
enum class EffectType() {
    NATURAL_FACE(1, "自然脸"),
    ROUND_FACE(2, "圆脸"),
    SQUARE_FACE(3, "方脸"),
    LONG_FACE(4, "长脸"),
    CHF_FACE(5, "长方脸"),

    GROUP_EFFECT("基础美颜"),
    GROUP_STYLE("风格"),

    //GROUP_FILTER("滤镜"),
    //GROUP_MAKE_UP("美妆"),
    //GROUP_STICKER("特效"),
    //GROUP_FACE("漫画脸"),
    //GROUP_AVATAR("Avatar"),

    // TryOn
    TYPE_TRY_ON_BASIC("Tryon美颜"),
    TYPE_TRY_ON_LIP(EFFECT_BEAUTY_TRYON_LIPSTICK, "TryOn口红", "29", Constants.TRY_ON_LIP),
    TYPE_TRY_ON_HAIR(EFFECT_BEAUTY_TRYON_HAIR_COLOR, "TryOn染发", "30", Constants.TRY_ON_HAIR),
    TYPE_TRY_ON_CHUNXIAN(703, "TryOn唇线", "31", Constants.TRY_ON_LIP_LINE),
    TYPE_TRY_ON_YANYING(708, "TryOn眼影", "32", Constants.TRY_ON_EYE_SHADOW),
    TYPE_TRY_ON_YANXIAN(709, "TryOn眼线", "33", Constants.TRY_ON_EYELINER),

    TYPE_TRY_ON_YANYIN(711, "TryOn眼印", "34", Constants.TRY_ON_EYE_PRINT),
    TYPE_TRY_ON_YANJIEMAO(710, "TryOn眼睫毛", "35", Constants.TRY_ON_EYE_LASH),
    TYPE_TRY_ON_MEIMAO(705, "TryOn眉毛", "36", Constants.TRY_ON_EYE_BROW),
    TYPE_TRY_ON_SAIHONG(704, "TryOn腮红", "37", Constants.TRY_ON_BLUSH),
    TYPE_TRY_ON_XIURONG(707, "TryOn修容", "38", Constants.TRY_ON_TRIMMING),

    TYPE_TRY_ON_FENDI(706, "TryOn粉底", "39", Constants.TRY_ON_FOUNDATION),

    // 美颜
    TYPE_BASE(1, "基础美颜"),
    TYPE_RESHAPE(2, "美形"),
    TYPE_PLASTIC(3, "微整形"),
    TYPE_3D_PLASTIC("3D微整形"),
    TYPE_TONE(6, "调整"),
    TYPE_BOKEN(7, "背景虚化"),
    TYPE_BODY("美体"),

    // 滤镜
    TYPE_PEOPLE("人物"),
    TYPE_SCENERY("风景"),
    TYPE_STILL_LIFE("静物"),
    TYPE_FOOD("美食"),

    // 美妆
    TYPE_HAIR(ST_MAKEUP_TYPE_HAIR_DYE, "染发", "25"),
    TYPE_LIP(ST_MAKEUP_TYPE_LIP, "口红", "24"),
    TYPE_BLUSH(ST_MAKEUP_TYPE_BLUSH, "腮红", "21"),
    TYPE_XR(ST_MAKEUP_TYPE_HIGHLIGHT, "修容", "22"),
    TYPE_EYE_BROW(ST_MAKEUP_TYPE_BROW, "眉毛", "19"),
    TYPE_EYE_SHADOW(ST_MAKEUP_TYPE_EYE, "眼影", "17"),
    TYPE_EYE_LINER(ST_MAKEUP_TYPE_EYELINER, "眼线", "23"),
    TYPE_EYELASH(ST_MAKEUP_TYPE_EYELASH, "眼睫毛", "18"),
    TYPE_EYEBALL(ST_MAKEUP_TYPE_EYEBALL, "美瞳", "20"),

    // 特效
    TYPE_STICKER_NEW("最新", "41"),
    TYPE_STICKER_2D("2D贴纸", "4"),
    TYPE_STICKER_3D("3D贴纸", "5"),
    TYPE_STICKER_HANDLE("手势贴纸", "6"),
    TYPE_STICKER_BG("背景分割", "7"),
    TYPE_STICKER_FACE("脸部变形", "8"),
    TYPE_STICKER_AVATAR("Avatar", "15"),
    TYPE_STICKER_TRY_ON("TryOn", "16"),
    TYPE_STICKER_BEAUTY("美妆贴纸", "73bffb50f6c211e899f602f2be7c2171"),
    TYPE_STICKER_PARTICLE("粒子贴纸", "9"),
    TYPE_STICKER_TRACK("通用物体追踪"),
    TYPE_STICKER_LOCAL("本地贴纸"),
    TYPE_STICKER_ADD("叠加贴纸"),
    TYPE_STICKER_SYNC("同步"),
    TYPE_STICKER_CAT("猫脸", "10"),
    TYPE_STICKER_CARTOON("GAN", "2723987dcea34e2dafd6f83d9ff83e45"),
    TYPE_STICKER_BUCKLE("抠脸", "13"),
    TYPE_STICKER_PLAY("特效玩法", "14"),
    TYPE_STICKER_SHADOW("影分身", "11"),
    TYPE_STICKER_BIG_HEAD("大头特效", "12"),

    // 风格
    TYPE_ZIRAN(0, "自然", "26"),
    TYPE_QINGZHUANG(0, "轻妆", "27"),
    TYPE_FASHION(0, "流行", "28"),

    // Avatar
    TYPE_ANIMAL("动物类"),
    TYPE_ERCIYUAN("二次元"),
    TYPE_CARTOON("卡通类"),

    // 漫画脸
    TYPE_CARTOON_FACE("xx风格"),

    // 基础美颜
    TYPE_BASIC_BOKEH1(1, "背景虚化1", 0f),
    TYPE_BASIC_BOKEH2(1, "背景虚化2", 0f),

    //    TYPE_BASIC_BOKEH1(EFFECT_BEAUTY_TONE_BOKEH, "背景虚化1", 0f),
//    TYPE_BASIC_BOKEH2(EFFECT_BEAUTY_TONE_BOKEH, "背景虚化2", 0f),
    TYPE_BASIC_1(EFFECT_BEAUTY_BASE_WHITTEN, "美白1", 0f),
    TYPE_BASIC_2(EFFECT_BEAUTY_BASE_WHITTEN, "美白2", 0f),
    TYPE_BASIC_3(EFFECT_BEAUTY_BASE_WHITTEN, "美白3", 0f),
    TYPE_BASIC_4(EFFECT_BEAUTY_BASE_REDDEN, "红润", 0f),
    TYPE_BASIC_5(EFFECT_BEAUTY_BASE_FACE_SMOOTH, "磨皮1", 0f),
    TYPE_BASIC_6(EFFECT_BEAUTY_BASE_FACE_SMOOTH, "磨皮2", 0.6f),

    // 美形
    TYPE_MX_1(EFFECT_BEAUTY_RESHAPE_SHRINK_FACE, "瘦脸", 0.25f),
    TYPE_MX_HIGH_THIN_FACE(201, "高阶瘦脸", 0f),
    TYPE_MX_2(EFFECT_BEAUTY_RESHAPE_ENLARGE_EYE, "大眼", 0.50f),
    TYPE_MX_3(EFFECT_BEAUTY_RESHAPE_SHRINK_JAW, "小脸", 0.0f),
    TYPE_MX_4(EFFECT_BEAUTY_RESHAPE_NARROW_FACE, "窄脸", 0.05f),
    TYPE_MX_5(EFFECT_BEAUTY_RESHAPE_ROUND_EYE, "圆眼", 0.10f),

    // 高阶瘦脸
    TYPE_HIGH_BACK(0, "高阶瘦脸（返回按钮）", 0f),
    TYPE_HIGH_1(EFFECT_BEAUTY_PLASTIC_SHRINK_NATURAL_FACE, "自然", 0f),
    TYPE_HIGH_2(EFFECT_BEAUTY_PLASTIC_SHRINK_GODDESS_FACE, "女神", 0f),
    TYPE_HIGH_3(EFFECT_BEAUTY_PLASTIC_SHRINK_LONG_FACE, "长脸", 0.20f),
    TYPE_HIGH_4(EFFECT_BEAUTY_PLASTIC_SHRINK_ROUND_FACE, "圆脸", 0f),

    // 微整形
    TYPE_THINNER_HEAD_1(EFFECT_BEAUTY_PLASTIC_THINNER_HEAD, "小头1", 0f),
    TYPE_THINNER_HEAD_2(EFFECT_BEAUTY_PLASTIC_SHRINK_WHOLE_HEAD, "小头2", 0f),
    TYPE_WZH_2(EFFECT_BEAUTY_PLASTIC_THIN_FACE, "瘦脸型", 0.0f),
    TYPE_WZH_3(EFFECT_BEAUTY_PLASTIC_CHIN_LENGTH, "下巴", true, -0.15f),
    TYPE_WZH_4(EFFECT_BEAUTY_PLASTIC_HAIRLINE_HEIGHT, "额头", true, 0f),
    TYPE_WZH_JAW(EFFECT_BEAUTY_PLASTIC_SHRINK_JAWBONE, "瘦下颔", false, 0f),
    TYPE_WZH_5(EFFECT_BEAUTY_PLASTIC_APPLE_MUSLE, "苹果肌", 0f),
    TYPE_WZH_6(EFFECT_BEAUTY_PLASTIC_NARROW_NOSE, "瘦鼻翼", 0f),
    TYPE_WZH_7(EFFECT_BEAUTY_PLASTIC_NOSE_LENGTH, "长鼻", true, 0f),
    TYPE_WZH_8(EFFECT_BEAUTY_PLASTIC_PROFILE_RHINOPLASTY, "侧脸隆鼻", 0f),
    TYPE_WZH_9(EFFECT_BEAUTY_PLASTIC_MOUTH_SIZE, "嘴型", true, 0f),
    TYPE_WZH_10(EFFECT_BEAUTY_PLASTIC_PHILTRUM_LENGTH, "缩人中", true, 0.20f),
    TYPE_WZH_11(EFFECT_BEAUTY_PLASTIC_EYE_DISTANCE, "眼距", true, -0.2f),
    TYPE_WZH_12(EFFECT_BEAUTY_PLASTIC_EYE_ANGLE, "眼睛角度", true, 0f),
    TYPE_WZH_13(EFFECT_BEAUTY_PLASTIC_OPEN_CANTHUS, "开眼角", 0f),
    TYPE_WZH_14(EFFECT_BEAUTY_PLASTIC_BRIGHT_EYE, "亮眼", 0.25f),
    TYPE_WZH_15(EFFECT_BEAUTY_PLASTIC_REMOVE_DARK_CIRCLES, "祛黑眼圈", 0.69f),
    TYPE_WZH_16(EFFECT_BEAUTY_PLASTIC_REMOVE_NASOLABIAL_FOLDS, "祛法令纹", 0.6f),
    TYPE_WZH_17(EFFECT_BEAUTY_PLASTIC_WHITE_TEETH, "白牙", 0.2f),
    TYPE_WZH_18(EFFECT_BEAUTY_PLASTIC_SHRINK_CHEEKBONE, "瘦颧骨", 0.15f),
    TYPE_WZH_19(EFFECT_BEAUTY_PLASTIC_OPEN_EXTERNAL_CANTHUS, "开外眼角", 0f),

    // 3D微整形-眼睛(80100)
    TYPE_WZH_3D_EYE_1(80100, "eyeScale", true, 0f),        // 0 眼睛-眼睛比例
    TYPE_WZH_3D_EYE_2(80101, "eyeHeight", true, 0f),       // 1 眼睛-眼高
    TYPE_WZH_3D_EYE_3(80102, "eyeWidth", true, 0f),        // 2 眼睛-眼距
    TYPE_WZH_3D_EYE_4(80103, "eyeOuterWidth", true, 0f),   // 3 眼睛-外眼角
    TYPE_WZH_3D_EYE_5(80104, "eyeDepth", true, 0f),        // 4 眼睛-眼睛深浅
    TYPE_WZH_3D_EYE_6(80105, "eyeLowerDepth", true, 0f),   // 5 眼睛-卧蚕深浅
    TYPE_WZH_3D_EYE_7(80106, "eyeAngle", true, 0f),        // 6 眼睛-眼睛角度
    TYPE_WZH_3D_EYE_OUTEREYETAIL(80150, "Outereyetail", false, 0f), // 6 外眼尾
    TYPE_WZH_3D_EYE_INNERCORNER(80151, "Innercorner", false, 0f), // 6 内眼角尖

    // 3D微整形-鼻子(80200)
    TYPE_WZH_3D_NOSE_1(80107, "noseScale", true, 0f), // 7 鼻子比例
    TYPE_WZH_3D_NOSE_2(80108, "noseWidth", true, 0f), // 8 鼻宽
    TYPE_WZH_3D_NOSE_3(80109, "noseHeight", true, 0f),// 9 鼻长
    TYPE_WZH_3D_NOSE_4(80110, "noseDepth", true, 0f),// 10 鼻高
    TYPE_WZH_3D_NOSE_5(80111, "noseRidgeUpper", true, 0f),// 11 鼻根
    TYPE_WZH_3D_NOSE_6(80112, "noseRidgeCurve", true, 0f),// 12 鼻子驼峰
    TYPE_WZH_3D_NOSE_7(80113, "noseTipHeight", true, 0f),// 13 鼻尖
    TYPE_WZH_3D_NOSE_8(80114, "nostrilWidth", true, 0f),// 14 鼻翼

    // 3D微整形-嘴巴(80300)
    TYPE_WZH_3D_MOUTH_1(80115, "mouthScale", true, 0f),// 15 嘴巴比例
    TYPE_WZH_3D_MOUTH_2(80116, "mouthHeight", true, 0f),// 16 嘴巴高度
    TYPE_WZH_3D_MOUTH_3(80117, "mouthWidth", true, 0f),// 17 嘴巴宽度
    TYPE_WZH_3D_MOUTH_4(80118, "mouthDepth", true, 0f),// 18 嘴巴深度
    TYPE_WZH_3D_MOUTH_5(80119, "lipThin", true, 0f),// 19 嘴巴厚度
    TYPE_WZH_3D_NEW_1(80126, "Beeplips", false, 0f),// 20 嘟嘟唇
    TYPE_WZH_3D_NEW_2(80127, "Smilinglips", false, 0f),// 21 微笑唇

    // 3D微整形-头部(80400)
    TYPE_WZH_3D_HEAD_1(80120, "headScale", true, 0f),// 22 头部比例
    TYPE_WZH_3D_HEAD_2(80121, "headOuterWidth", true, 0f),// 23 头部宽度

    // 3D微整形-脸部(80500)
    // new
    TYPE_WZH_3D_CHEEKBONE(80500, "Cheekbone", false, 0f),// 28 苹果肌
    TYPE_WZH_3D_FOREHEAD(80501, "Forehead", false, 0f),// 额头
    TYPE_WZH_3D_NASOLABIAL(80502, "Nasolabial", false, 0f),//法令纹
    TYPE_WZH_3D_TEARDITCH(80503, "Tearditch", false, 0f),// 泪沟
    TYPE_WZH_3D_BROWBONE(80504, "Browbone", false, 0f),// 眉骨
    TYPE_WZH_3D_RAISEEYEBROWS(80505, "Raiseeyebrows", false, 0f),// 挑眉
    TYPE_WZH_3D_TEMPLE(80506, "Temple", false, 0f),//太阳穴
    TYPE_WZH_3D_FOREHEADTWO(80507, "Foreheadtwo", false, 0f),//侧额头

    TYPE_WZH_3D_NEW_4(80129, "Goldenline", false, 0f),// 29 下颌线
    TYPE_WZH_3D_FACE_1(80122, "faceHeavy", true, 0f),// 24 脸部胖瘦
    TYPE_WZH_3D_FACE_2(80123, "faceAngle", true, 0f),// 25 脸部角度
    TYPE_WZH_3D_FACE_3(80124, "faceCenterDepthEnlarge", false, 0f),// 26 脸部外扩
    TYPE_WZH_3D_FACE_4(80125, "faceCenterDepthShrink", false, 0f),// 27 脸部内缩

    // 8.5.1新增

    // 调整
    TYPE_TZH_1(EFFECT_BEAUTY_TONE_CONTRAST, "对比度", 0f),
    TYPE_TZH_2(EFFECT_BEAUTY_TONE_SATURATION, "饱和度", 0f),
    TYPE_TZH_3(EFFECT_BEAUTY_TONE_SHARPEN, "锐化", 0.5f),
    TYPE_TZH_4(EFFECT_BEAUTY_TONE_CLEAR, "清晰度", 0.2f),

    TYPE_BODY_1(0, "整体效果", 0f),
    TYPE_BODY_2(0, "瘦头", 0f),
    TYPE_BODY_3(0, "瘦肩", 0f),
    TYPE_BODY_4(0, "美臀", 0f),
    TYPE_BODY_5(0, "瘦腿", 0f);

    var code: Int = 0
    var desc: String? = null
    var startCenterSeekBar: Boolean = false
    var strength: Float = 0f
        get() {
            // 男，磨皮为40
            if (ContextHolder.getGender() == 1 && this == TYPE_BASIC_6) {
                return 0.4f
            }
            // 默认自然脸
            when (ContextHolder.getFaceType()) {
                3 -> {// 方脸
                    if (this == TYPE_WZH_JAW) {// 瘦下颔
                        LogUtils.iTag("EffectType", "${this.desc} ${TYPE_WZH_JAW.desc}")
                        return 0.15f
                    }
                }
                2 -> {// 圆脸
//                    if (this == TYPE_WZH_2) {// 瘦脸型
//                        Log.d("EffectType", "${this.desc} ${TYPE_WZH_JAW.desc}")
//                        return 0.4f
//                    }
//                    if (this == TYPE_WZH_JAW) {// 瘦下颔
//                        Log.d("EffectType", "圆脸 瘦下颔")
//                        return 0.15f
//                    }
//                    if (this == TYPE_MX_4) {// 窄脸
//                        return 0.1f
//                    }
                }
                4 -> {// 长脸
//                    if (this == TYPE_MX_3) {// 小脸
//                        Log.d("EffectType", "长脸 小脸")
//                        return 0.25f
//                    }
//                    if (this == TYPE_WZH_3) {
//                        Log.d("EffectType", "长脸 下巴")
//                        return 0f
//                    }
                }
                5 -> {// 长方脸
//                    if (this == TYPE_MX_3) {
//                        Log.d("EffectType", "长方脸 小脸")
//                        return 0.25f
//                    }
//                    if (this == TYPE_WZH_3) {
//                        Log.d("EffectType", "长方脸 下巴")
//                        return 0f
//                    }
                    if (this == TYPE_WZH_JAW) {
                        LogUtils.iTag("EffectType", "长方脸 瘦下颔")
                        return 0.15f
                    }
                }
            }
            return field
        }

    var groupId: String = ""
    var assetPath: String = ""

    constructor(code: Int) : this() {
        this.code = code
    }

    constructor(desc: String) : this() {
        this.desc = desc
    }

    constructor(desc: String, groupId: String) : this() {
        this.desc = desc
        this.groupId = groupId
    }

    constructor(code: Int, desc: String) : this(code) {
        this.desc = desc
    }

    constructor(code: Int, desc: String, groupId: String) : this(code, desc) {
        this.groupId = groupId
    }

    constructor(code: Int, desc: String, groupId: String, assetPath: String) : this(code, desc) {
        this.groupId = groupId
        this.assetPath = assetPath
    }

    constructor(code: Int, desc: String, defStrength: Float) : this(code, desc) {
        this.strength = defStrength
    }

    constructor(code: Int, desc: String, startCenterSeekBar: Boolean, defStrength: Float) : this(
        code
    ) {
        this.desc = desc
        this.startCenterSeekBar = startCenterSeekBar
        strength = defStrength
    }

    companion object {
        val basicList = arrayListOf(
            TYPE_BASE,
            TYPE_RESHAPE,
            TYPE_PLASTIC,
            TYPE_3D_PLASTIC,
            TYPE_TONE,
            TYPE_BODY,
            TYPE_BOKEN
        )

        val filterList = arrayListOf(TYPE_PEOPLE, TYPE_SCENERY, TYPE_STILL_LIFE, TYPE_FOOD)

        val makeupList = arrayListOf(
            TYPE_HAIR, TYPE_LIP, TYPE_BLUSH, TYPE_XR, TYPE_EYE_BROW,
            TYPE_EYE_SHADOW, TYPE_EYE_LINER, TYPE_EYELASH, TYPE_EYEBALL
        )

        val tryOnList = arrayListOf(TYPE_TRY_ON_LIP, TYPE_TRY_ON_HAIR)

        val styleList = arrayListOf(TYPE_ZIRAN, TYPE_QINGZHUANG, TYPE_FASHION)

        val basicMap = mapOf(
            // 基础美颜
            TYPE_BASE to arrayListOf(
                TYPE_BASIC_BOKEH1,
                TYPE_BASIC_BOKEH2,
                TYPE_BASIC_1,
                TYPE_BASIC_2,
                TYPE_BASIC_3,
                TYPE_BASIC_4,
                TYPE_BASIC_5,
                TYPE_BASIC_6
            ),
            // 美形
            TYPE_RESHAPE to arrayListOf(
                TYPE_MX_1,
                TYPE_MX_2,
                TYPE_MX_3,
                TYPE_MX_4,
                TYPE_MX_5,
                TYPE_HIGH_BACK,
                TYPE_HIGH_1,
                TYPE_HIGH_2,
                TYPE_HIGH_3,
                TYPE_HIGH_4
            ),
            // 微整形
            TYPE_PLASTIC to arrayListOf(
                TYPE_THINNER_HEAD_1,
                TYPE_THINNER_HEAD_2,
                TYPE_WZH_2,
                TYPE_WZH_3,
                TYPE_WZH_4,
                TYPE_WZH_JAW,
                TYPE_WZH_5,
                TYPE_WZH_6,
                TYPE_WZH_7,
                TYPE_WZH_8,
                TYPE_WZH_9,
                TYPE_WZH_10,
                TYPE_WZH_11,
                TYPE_WZH_12,
                TYPE_WZH_13,
                TYPE_WZH_14,
                TYPE_WZH_15,
                TYPE_WZH_16,
                TYPE_WZH_17,
                TYPE_WZH_18,
                TYPE_WZH_19
            ),
            // 3D微整形
            TYPE_3D_PLASTIC to arrayListOf(
                TYPE_WZH_3D_EYE_1, TYPE_WZH_3D_EYE_2,
                TYPE_WZH_3D_EYE_3, TYPE_WZH_3D_EYE_4,
                TYPE_WZH_3D_EYE_5, TYPE_WZH_3D_EYE_6,
                TYPE_WZH_3D_EYE_7, TYPE_WZH_3D_EYE_OUTEREYETAIL,
                TYPE_WZH_3D_EYE_INNERCORNER,
                TYPE_WZH_3D_NOSE_1, TYPE_WZH_3D_NOSE_2,
                TYPE_WZH_3D_NOSE_3, TYPE_WZH_3D_NOSE_4,
                TYPE_WZH_3D_NOSE_5, TYPE_WZH_3D_NOSE_6,
                TYPE_WZH_3D_NOSE_7, TYPE_WZH_3D_NOSE_8,
                TYPE_WZH_3D_MOUTH_1, TYPE_WZH_3D_MOUTH_2,
                TYPE_WZH_3D_MOUTH_3, TYPE_WZH_3D_MOUTH_4,
                TYPE_WZH_3D_MOUTH_5,
                TYPE_WZH_3D_HEAD_1, TYPE_WZH_3D_HEAD_2,
                TYPE_WZH_3D_FACE_1, TYPE_WZH_3D_FACE_2,
                TYPE_WZH_3D_FACE_3, TYPE_WZH_3D_FACE_4,
                TYPE_WZH_3D_NEW_1, TYPE_WZH_3D_NEW_2,
                TYPE_WZH_3D_CHEEKBONE, TYPE_WZH_3D_NEW_4,
                TYPE_WZH_3D_FOREHEAD, TYPE_WZH_3D_NASOLABIAL,
                TYPE_WZH_3D_TEARDITCH, TYPE_WZH_3D_BROWBONE,
                TYPE_WZH_3D_RAISEEYEBROWS, TYPE_WZH_3D_TEMPLE,
                TYPE_WZH_3D_FOREHEADTWO
            ),
            // 调整
            TYPE_TONE to arrayListOf(TYPE_TZH_1, TYPE_TZH_2, TYPE_TZH_3, TYPE_TZH_4),
            // 背景虚化
            TYPE_BOKEN to arrayListOf(TYPE_BASIC_BOKEH1, TYPE_BASIC_BOKEH2)
        )

        // 基础美颜
        val basicEffectList = arrayListOf(
            TYPE_BASIC_BOKEH1,
            TYPE_BASIC_BOKEH2,
            TYPE_BASIC_1,
            TYPE_BASIC_2,
            TYPE_BASIC_3,
            TYPE_BASIC_4,
            TYPE_BASIC_5,
            TYPE_BASIC_6
        )
        val mxEffectList = arrayListOf(TYPE_MX_1, TYPE_MX_2, TYPE_MX_3, TYPE_MX_4, TYPE_MX_5)
        val wzhEffectList = arrayListOf(
            TYPE_THINNER_HEAD_1,
            TYPE_THINNER_HEAD_2,
            TYPE_WZH_2,
            TYPE_WZH_3,
            TYPE_WZH_4,
            TYPE_WZH_JAW,
            TYPE_WZH_5,
            TYPE_WZH_6,
            TYPE_WZH_7,
            TYPE_WZH_8,
            TYPE_WZH_9,
            TYPE_WZH_10,
            TYPE_WZH_11,
            TYPE_WZH_12,
            TYPE_WZH_13,
            TYPE_WZH_14,
            TYPE_WZH_15,
            TYPE_WZH_16,
            TYPE_WZH_17,
            TYPE_WZH_18,
            TYPE_WZH_19
        )
        val adjustList = arrayListOf(TYPE_TZH_1, TYPE_TZH_2, TYPE_TZH_3, TYPE_TZH_4)
        val bokenList = arrayListOf(TYPE_BASIC_BOKEH1, TYPE_BASIC_BOKEH2)
        val highThinFaceList =
            arrayListOf(TYPE_HIGH_BACK, TYPE_HIGH_1, TYPE_HIGH_2, TYPE_HIGH_3, TYPE_HIGH_4)
        val wzh3d = arrayListOf(
            TYPE_WZH_3D_EYE_1, TYPE_WZH_3D_EYE_2,
            TYPE_WZH_3D_EYE_3, TYPE_WZH_3D_EYE_4,
            TYPE_WZH_3D_EYE_5, TYPE_WZH_3D_EYE_6,
            TYPE_WZH_3D_EYE_7, TYPE_WZH_3D_EYE_OUTEREYETAIL,
            TYPE_WZH_3D_EYE_INNERCORNER,
            TYPE_WZH_3D_NOSE_1, TYPE_WZH_3D_NOSE_2,
            TYPE_WZH_3D_NOSE_3, TYPE_WZH_3D_NOSE_4,
            TYPE_WZH_3D_NOSE_5, TYPE_WZH_3D_NOSE_6,
            TYPE_WZH_3D_NOSE_7, TYPE_WZH_3D_NOSE_8,
            TYPE_WZH_3D_MOUTH_1, TYPE_WZH_3D_MOUTH_2,
            TYPE_WZH_3D_MOUTH_3, TYPE_WZH_3D_MOUTH_4,
            TYPE_WZH_3D_MOUTH_5,
            TYPE_WZH_3D_HEAD_1, TYPE_WZH_3D_HEAD_2,
            TYPE_WZH_3D_FACE_1, TYPE_WZH_3D_FACE_2,
            TYPE_WZH_3D_FACE_3, TYPE_WZH_3D_FACE_4,
            TYPE_WZH_3D_NEW_1, TYPE_WZH_3D_NEW_2,
            TYPE_WZH_3D_CHEEKBONE, TYPE_WZH_3D_NEW_4,
            TYPE_WZH_3D_FOREHEAD, TYPE_WZH_3D_NASOLABIAL,
            TYPE_WZH_3D_TEARDITCH, TYPE_WZH_3D_BROWBONE,
            TYPE_WZH_3D_RAISEEYEBROWS, TYPE_WZH_3D_TEMPLE,
            TYPE_WZH_3D_FOREHEADTWO
        )

        val mutualHighFaceList = arrayListOf(TYPE_HIGH_1, TYPE_HIGH_2, TYPE_HIGH_3, TYPE_HIGH_4)
        val mutualSmoothList = arrayListOf(TYPE_BASIC_5, TYPE_BASIC_6)
        val mutualBokenList = arrayListOf(TYPE_BASIC_BOKEH1, TYPE_BASIC_BOKEH2)
        val mutualWhiteList = arrayListOf(TYPE_BASIC_1, TYPE_BASIC_2, TYPE_BASIC_3)
        //val mutualThinHeadList = arrayListOf(TYPE_THINNER_HEAD_1, TYPE_THINNER_HEAD_2)

        fun getAllBasicType(): ArrayList<EffectType> {
            val total = arrayListOf<EffectType>()
            total.addAll(basicEffectList)
            total.addAll(mxEffectList)
            total.addAll(wzhEffectList)
            total.addAll(adjustList)
            total.addAll(highThinFaceList)
            total.addAll(wzh3d)
            return total
        }

        //基础美颜
        fun getStrengthMap(type: EffectType): EnumMap<EffectType, Float> {
            val strengthsMap = EnumMap<EffectType, Float>(EffectType::class.java)
            when (type) {
                TYPE_3D_PLASTIC -> {
                    for (i in wzh3d.indices) {
                        strengthsMap[wzh3d[i]] = wzh3d[i].strength
                    }
                }
                TYPE_BASE -> {
                    for (i in basicEffectList.indices) {
                        strengthsMap[basicEffectList[i]] = basicEffectList[i].strength
                    }
                }
                TYPE_RESHAPE -> {
                    for (i in mxEffectList.indices) {
                        strengthsMap[mxEffectList[i]] = mxEffectList[i].strength
                        strengthsMap[highThinFaceList[i]] = highThinFaceList[i].strength
                    }
                }
                TYPE_PLASTIC -> {
                    for (i in wzhEffectList.indices) {
                        strengthsMap[wzhEffectList[i]] = wzhEffectList[i].strength
                    }
                }
                TYPE_TONE -> {
                    for (i in adjustList.indices) {
                        strengthsMap[adjustList[i]] = adjustList[i].strength
                    }
                }
                TYPE_BOKEN -> {
                    for (i in bokenList.indices) {
                        strengthsMap[bokenList[i]] = bokenList[i].strength
                    }
                }
                TYPE_BODY -> {

                }

                GROUP_EFFECT -> {//一整组
                    for (i in basicEffectList.indices) {
                        strengthsMap[basicEffectList[i]] = basicEffectList[i].strength
                    }
                    for (i in mxEffectList.indices) {
                        strengthsMap[mxEffectList[i]] = mxEffectList[i].strength
                    }
                    for (i in wzhEffectList.indices) {
                        strengthsMap[wzhEffectList[i]] = wzhEffectList[i].strength
                    }
                    for (i in adjustList.indices) {
                        strengthsMap[adjustList[i]] = adjustList[i].strength
                    }
                }

                else -> {
                }
            }
            return strengthsMap
        }

        val wzh3dEyeList = arrayListOf(
            TYPE_WZH_3D_EYE_1, TYPE_WZH_3D_EYE_2,
            TYPE_WZH_3D_EYE_3, TYPE_WZH_3D_EYE_4,
            TYPE_WZH_3D_EYE_5, TYPE_WZH_3D_EYE_6,
            TYPE_WZH_3D_EYE_7, TYPE_WZH_3D_EYE_OUTEREYETAIL,
            TYPE_WZH_3D_EYE_INNERCORNER
        )

        // 鼻子一组
        val wzh3dNoseList = arrayListOf(
            TYPE_WZH_3D_NOSE_1, TYPE_WZH_3D_NOSE_2,
            TYPE_WZH_3D_NOSE_3, TYPE_WZH_3D_NOSE_4,
            TYPE_WZH_3D_NOSE_5, TYPE_WZH_3D_NOSE_6,
            TYPE_WZH_3D_NOSE_7, TYPE_WZH_3D_NOSE_8
        )

        // 嘴巴一组
        val wzh3dMouthList = arrayListOf(
            TYPE_WZH_3D_MOUTH_1, TYPE_WZH_3D_MOUTH_2,
            TYPE_WZH_3D_MOUTH_3, TYPE_WZH_3D_MOUTH_4,
            TYPE_WZH_3D_MOUTH_5, TYPE_WZH_3D_NEW_1,
            TYPE_WZH_3D_NEW_2
        )

        val wzh3dHeadList = arrayListOf(
            TYPE_WZH_3D_HEAD_1, TYPE_WZH_3D_HEAD_2
        )

        val wzh3dFaceList = arrayListOf(
            TYPE_WZH_3D_CHEEKBONE, TYPE_WZH_3D_FOREHEAD,
            TYPE_WZH_3D_NASOLABIAL, TYPE_WZH_3D_TEARDITCH,
            TYPE_WZH_3D_BROWBONE, TYPE_WZH_3D_RAISEEYEBROWS,
            TYPE_WZH_3D_TEMPLE, TYPE_WZH_3D_FOREHEADTWO
        )

        val stickerList = arrayListOf(
            TYPE_STICKER_NEW, TYPE_STICKER_2D,
            TYPE_STICKER_3D, TYPE_STICKER_HANDLE,
            TYPE_STICKER_BG, TYPE_STICKER_FACE,
            TYPE_STICKER_AVATAR, TYPE_STICKER_BEAUTY,
            TYPE_STICKER_PARTICLE, TYPE_STICKER_TRACK,
            TYPE_STICKER_LOCAL, TYPE_STICKER_ADD,
            TYPE_STICKER_SYNC, TYPE_STICKER_CAT,
            TYPE_STICKER_BUCKLE, TYPE_STICKER_PLAY,
            TYPE_STICKER_SHADOW, TYPE_STICKER_BIG_HEAD,
            TYPE_STICKER_TRY_ON,
            TYPE_STICKER_CARTOON
        )

        fun getTypeByCode(code: Int): EffectType {
            for (item in values()) {
                if (code == TYPE_BASIC_5.code)
                    return TYPE_BASIC_5
                if (code == TYPE_BASIC_1.code)
                    return TYPE_BASIC_1
                if (item.code == code) {
                    return item
                }
            }
            return TYPE_HAIR
        }

        fun getTypeByGroupId(groupId: String): EffectType {
            for (item in values()) {
                if (item.groupId == groupId) {
                    return item
                }
            }
            return TYPE_HAIR
        }

        var mCurrFace: EffectType = NATURAL_FACE
        fun setCurrFace(type: EffectType) {
            mCurrFace = type
        }
    }
}