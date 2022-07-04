package sensetime.senseme.com.effects.view;

import android.graphics.Bitmap;

import java.util.ArrayList;

import sensetime.senseme.com.effects.adapter.FilterInfo;
import sensetime.senseme.com.effects.adapter.MakeupInfo;

public class FullBeautyItem {

    private Bitmap unselectedtIcon;
    private Bitmap selectedIcon;
    private String text;

    private int makeupAllProgress = 100;
    private int filterProgress = 100;

    //美颜参数
    private float[] beautifyParamsBase = {0f, 0.64f, 0.75f, 0f, 0f};
    private float[] beautifyParamsProfessional = {0.11f, 0.2f, 0.1f, 0.12f, 0f};
    private float[] beautifyParamsMicro = {0f, 0f, 0.15f, 0.2f, 0f, 0.3f,
            0.21f, 0f, 0.1f, 0f, 0f,
            0f, 0.43f, 0.29f, 0.3f, 0.2f,
            0f, 0.0f };
    private float[] beautifyParamsAdjust = {0.0f, 0.0f, 0.32f};
    //美妆
    private ArrayList<MakeupInfo> makeupList;

    //滤镜参数
    private FilterInfo filter;

    public FullBeautyItem(String text, Bitmap unselectedIcon, Bitmap selectedIcon, float[] beautifyParamsBase, float[] beautifyParamsProfessional,
                          float[] beautifyParamsMicro, float[] beautifyParamsAdjust, ArrayList makeupList,
                          FilterInfo filter){
        this.text = text;
        this.unselectedtIcon = unselectedIcon;
        this.selectedIcon = selectedIcon;
        this.beautifyParamsBase = beautifyParamsBase;
        this.beautifyParamsProfessional = beautifyParamsProfessional;
        this.beautifyParamsMicro = beautifyParamsMicro;
        this.beautifyParamsAdjust = beautifyParamsAdjust;
        this.makeupList = makeupList;
        this.filter = filter;
    }

    public FullBeautyItem(String text, Bitmap unselectedIcon, Bitmap selectedIcon, ArrayList makeupList, FilterInfo filter){
        this.text = text;
        this.unselectedtIcon = unselectedIcon;
        this.selectedIcon = selectedIcon;
        this.makeupList = makeupList;
        this.filter = filter;
    }

    public FullBeautyItem(String text, Bitmap unselectedIcon, Bitmap selectedIcon, float[] beautifyParamsBase,  ArrayList makeupList,
                          FilterInfo filter){
        this.text = text;
        this.unselectedtIcon = unselectedIcon;
        this.selectedIcon = selectedIcon;
        this.beautifyParamsBase = beautifyParamsBase;
        this.makeupList = makeupList;
        this.filter = filter;
    }

    public Bitmap getUnselectedIcon() {
        return unselectedtIcon;
    }

    public void setUnselectedIcon(Bitmap unselectedIcon) {
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

    public float[] getBeautifyParams(int index){
        if(index == 1){
            return beautifyParamsBase;
        }else if(index == 2){
            return beautifyParamsProfessional;
        }else if(index == 3){
            return beautifyParamsMicro;
        }else if(index == 6){
            return beautifyParamsAdjust;
        }

        return beautifyParamsBase;
    }

    public ArrayList<MakeupInfo> getMakeupList(){
        return makeupList;
    }

    public FilterInfo getFilter(){
        return filter;
    }
    public int getMakeupAllProgress() {
        return makeupAllProgress;
    }

    public void setMakeupAllProgress(int makeupAllProgress) {
        this.makeupAllProgress = makeupAllProgress;
    }

    public int getFilterProgress() {
        return filterProgress;
    }

    public void setFilterProgress(int filterProgress) {
        this.filterProgress = filterProgress;
    }
}
