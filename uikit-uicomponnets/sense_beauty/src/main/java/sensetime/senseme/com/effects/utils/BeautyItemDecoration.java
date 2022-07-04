package sensetime.senseme.com.effects.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 7/2/21 5:43 PM
 */
public class BeautyItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public BeautyItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = space;
        outRect.right = space;
    }
}
