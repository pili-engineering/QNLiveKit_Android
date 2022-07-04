package sensetime.senseme.com.effects.view;

import android.graphics.Bitmap;

import sensetime.senseme.com.effects.view.widget.EffectType;

public class BeautyItem {

    private float progress;
    private Bitmap unselectedtIcon;
    private Bitmap selectedIcon;
    private String text;
    public EffectType type;
    private int unselectedtIconRes;
    private int selectedtIconRes;

    public BeautyItem(EffectType type, String text, int unselectedtIcon, int selectedtIcon){
        this.type = type;
        this.text = text;
        this.unselectedtIconRes = unselectedtIcon;
        this.selectedtIconRes = selectedtIcon;
        this.progress = type.getStrength();
    }

    public BeautyItem(EffectType type, String text, Bitmap unselectedtIcon, Bitmap selectedtIcon){
        this.type = type;
        this.text = text;
        this.unselectedtIcon = unselectedtIcon;
        this.selectedIcon = selectedtIcon;
        this.progress = type.getStrength();
    }

    public BeautyItem(EffectType type, String text, Bitmap unselectedtIcon, Bitmap selectedtIcon, float progress){
        this.type = type;
        this.text = text;
        this.unselectedtIcon = unselectedtIcon;
        this.selectedIcon = selectedtIcon;
        this.progress = progress;
    }

    public BeautyItem(String text, Bitmap unselectedtIcon, Bitmap selectedtIcon){
        this.text = text;
        this.unselectedtIcon = unselectedtIcon;
        this.selectedIcon = selectedtIcon;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public Bitmap getUnselectedtIcon() {
        return unselectedtIcon;
    }

    public void setUnselectedtIcon(Bitmap unselectedtIcon) {
        this.unselectedtIcon = unselectedtIcon;
    }

    public Bitmap getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(Bitmap selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUnselectedtIconRes() {
        return unselectedtIconRes;
    }

    public void setUnselectedtIconRes(int unselectedtIconRes) {
        this.unselectedtIconRes = unselectedtIconRes;
    }

    public int getSelectedtIconRes() {
        return selectedtIconRes;
    }

    public void setSelectedtIconRes(int selectedtIconRes) {
        this.selectedtIconRes = selectedtIconRes;
    }

    @Override
    public String toString() {
        return "BeautyItem{" +
                "progress=" + progress +
                ", unselectedtIcon=" + unselectedtIcon +
                ", selectedIcon=" + selectedIcon +
                ", text='" + text + '\'' +
                ", type=" + type.getDesc() +
                '}';
    }
}
