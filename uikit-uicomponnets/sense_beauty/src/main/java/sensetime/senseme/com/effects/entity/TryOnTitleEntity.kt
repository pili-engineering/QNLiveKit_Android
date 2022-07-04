package sensetime.senseme.com.effects.entity

import sensetime.senseme.com.effects.view.widget.EffectType

data class TryOnTitleEntity(var type: EffectType, var title: String) {
    override fun toString(): String {
        return "TryOnTitleEntity(id=$type, title='$title')"
    }
}