package sensetime.senseme.com.effects.view;

import sensetime.senseme.com.effects.view.widget.EffectType;

public class MakeUpOptionsItem {
    public String groupName;
    public String groupId;
    public EffectType type;

    public MakeUpOptionsItem(EffectType type, String name, String groupId) {
        this.type = type;
        this.groupName = name;
        this.groupId = groupId;
    }
}
