package sensetime.senseme.com.effects.view.widget

import sensetime.senseme.com.effects.view.BeautyOptionsItem
import java.util.*

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/23/21 12:34 PM
 */
interface ILinkageView {

    fun clearContentSelected()

    /**
     * titleData：标题的集合
     * contentData：标题对应的集合
     */
    fun init(
            titleData: ArrayList<BeautyOptionsItem>,
            contentData: HashMap<EffectType, MutableList<*>>?
    )

    fun setData(contentData: EnumMap<EffectType, MutableList<*>>?)
}