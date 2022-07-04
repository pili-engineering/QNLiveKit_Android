package sensetime.senseme.com.effects.utils;

import android.view.View;
import android.view.ViewGroup;

public class ReplaceViewHelper {
    public static void toReplaceView(View targetView, ViewGroup container) {
        if (null == targetView) {
            if (container != null) {
                container.removeAllViewsInLayout();
            }
        } else {
            ViewGroup p = (ViewGroup) targetView.getParent();
            if (p!= null) {
                p.removeAllViewsInLayout();
            }
            if (container != null) {
                container.removeAllViewsInLayout();
            }
            container.addView(targetView);
        }
    }

    public static void removeAllViewsInLayout(ViewGroup container) {
        if (container != null) {
            container.removeAllViewsInLayout();
        }
    }
}
