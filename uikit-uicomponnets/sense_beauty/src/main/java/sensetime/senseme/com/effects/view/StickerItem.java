package sensetime.senseme.com.effects.view;

import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import sensetime.senseme.com.effects.utils.LogUtils;
import com.sensetime.sensearsourcemanager.SenseArMaterial;

import org.jetbrains.annotations.NotNull;

import sensetime.senseme.com.effects.view.widget.LinkageEntity;

/**
 * Created by sensetime on 17-6-8.
 */

public class StickerItem implements LinkageEntity {
    public Boolean selected = false;
    public String name;
    public int id;
    public Bitmap icon;
    public String iconUrl;
    public String path;
    public String pkgUrl;
    public int lipFinishType;// 口红质地
    public SenseArMaterial material;
    public StickerState state = StickerState.NORMAL_STATE;//0 未下载状态，也是默认状态，1，正在下载状态,2,下载完毕状态

    public long orderTimeStamp;

    public StickerItem(String iconUrl, StickerState state) {
        this.iconUrl = iconUrl;
        this.state = state;
    }

    public StickerItem(Boolean selected, String name, String iconUrl) {
        this.selected = selected;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public StickerItem(int lipFinishType, Boolean selected, String name, String iconUrl) {
        this.lipFinishType = lipFinishType;
        this.selected = selected;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public StickerItem(SenseArMaterial material, String name, Bitmap icon, String path) {
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

    public StickerItem(){}

    public StickerItem(Bitmap bitmap) {
        this.icon = bitmap;
        state = StickerState.DONE_STATE;
    }

    public void recycle() {
        if (icon != null && !icon.isRecycled()) {
            icon.recycle();
            icon = null;
        }
    }

    @Override
    public String toString() {
        return "StickerItem{" +
                "name='" + name + '\'' +
                "id='" + id + '\'' +
                ", icon=" + icon +
                ", path='" + path + '\'' +
                ", material=" + material +
                ", state=" + state +
                ", iconUrl='" + iconUrl + '\'' +
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
        LogUtils.iTag("lugq", "setPath() called with: path = [" + path + "]");
        this.path = path;
        if (TextUtils.isEmpty(this.path)) {
            state = StickerState.NORMAL_STATE;
        } else {
            state = StickerState.DONE_STATE;
        }
    }

    @NonNull
    @Override
    public String getPkgUrl() {
        return pkgUrl;
    }
}
