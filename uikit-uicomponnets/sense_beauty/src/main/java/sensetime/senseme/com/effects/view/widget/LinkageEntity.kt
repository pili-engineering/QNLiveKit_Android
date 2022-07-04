package sensetime.senseme.com.effects.view.widget

import com.sensetime.sensearsourcemanager.SenseArMaterial
import sensetime.senseme.com.effects.view.StickerState

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/23/21 8:58 PM
 */
interface LinkageEntity {
    fun getState(): StickerState

    fun setState(state: StickerState)

    fun getSenseArMaterial(): SenseArMaterial?

    fun setPath(path: String)

    fun getPkgUrl():String
}