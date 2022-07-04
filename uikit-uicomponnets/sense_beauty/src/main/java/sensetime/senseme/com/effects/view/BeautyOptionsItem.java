package sensetime.senseme.com.effects.view;

import sensetime.senseme.com.effects.view.widget.EffectType;

/**
 * Created by cuishiwang on 22/08/2017.
 */

public class BeautyOptionsItem {
    public String name;
    public String groupId;
    public EffectType type;

    public BeautyOptionsItem(String name) {
        this.name = name;
    }

//    public BeautyOptionsItem(String name, String groupId) {
//        this.name = name;
//        this.groupId = groupId;
//    }

    public BeautyOptionsItem(EffectType type, String name) {
        this.name = name;
        this.type = type;
    }

    public BeautyOptionsItem(EffectType type, String name, String groupId) {
        this.name = name;
        this.type = type;
        this.groupId = groupId;
    }
}
