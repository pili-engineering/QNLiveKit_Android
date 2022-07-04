package sensetime.senseme.com.effects.view;

import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.sensetime.sensearsourcemanager.SenseArMaterial;

import org.jetbrains.annotations.NotNull;

import sensetime.senseme.com.effects.view.widget.EffectType;
import sensetime.senseme.com.effects.view.widget.LinkageEntity;

public class MakeupItem implements LinkageEntity {

    public String name;
    public Bitmap icon;
    public String pkgUrl;
    public String iconUrl;
    public String path;
    public String groupName;
    public SenseArMaterial material;
    public EffectType effectType;
    public Boolean selected;

    public StickerState state = StickerState.NORMAL_STATE;

    public MakeupItem(){}

    public MakeupItem(SenseArMaterial material, String name, Bitmap icon, String path) {
        this.name = name;
        this.icon = icon;
        this.path = path;
        this.material = material;
        if (TextUtils.isEmpty(this.path)) {
            state = StickerState.NORMAL_STATE;
        } else {
            state = StickerState.DONE_STATE;
        }
    }

    public MakeupItem(String name, Bitmap icon, String path) {
        this.name = name;
        this.icon = icon;
        this.path = path;
        if (TextUtils.isEmpty(this.path)) {
            state = StickerState.NORMAL_STATE;
        } else {
            state = StickerState.DONE_STATE;
        }
    }

    public MakeupItem(String name, Bitmap icon, String path, String iconUrl) {
        this.name = name;
        this.icon = icon;
        this.path = path;
        this.iconUrl = iconUrl;
        if (TextUtils.isEmpty(this.path)) {
            state = StickerState.NORMAL_STATE;
        } else {
            state = StickerState.DONE_STATE;
        }
    }

    public MakeupItem(String name, Bitmap icon, String path, StickerState state) {
        this.name = name;
        this.icon = icon;
        this.path = path;
        this.state = state;
    }

    public MakeupItem(String name, Bitmap icon, String path, StickerState state, String iconUrl) {
        this.name = name;
        this.icon = icon;
        this.path = path;
        this.state = state;
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "MakeupItem{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", state=" + state +
                '}';
    }

    @NotNull
    @Override
    public StickerState getState() {
        return state;
    }

    @Override
    public void setState(@NotNull StickerState state) {
        this.state = state;
    }

    @NotNull
    @Override
    public SenseArMaterial getSenseArMaterial() {
        return material;
    }

    @Override
    public void setPath(@NotNull String path) {
        this.path = path;
    }

    @NonNull
    @Override
    public String getPkgUrl() {
        return pkgUrl;
    }
}
