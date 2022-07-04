package sensetime.senseme.com.effects.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.MotionEvent;

import sensetime.senseme.com.effects.utils.LogUtils;

public class DoubleClickUtils {
    private static final String TAG = "DoubleClickUtils";

    private DoubleClickUtils() {
    }

    public static DoubleClickUtils getInstance() {
        return DoubleClickUtilsHolder.instance;
    }

    private static class DoubleClickUtilsHolder {
        @SuppressLint("StaticFieldLeak")
        private static final DoubleClickUtils instance = new DoubleClickUtils();
    }

    private static final int timeout = 400;
    private int clickCount = 0;
    private final Handler mHandler = new Handler();

    public interface Listener {
        void oneClick();

        void doubleClick();
    }

    public void onTouch(MotionEvent event, final Listener listener) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            click(listener);
        }
    }

    public void onClick(final Listener listener) {
        click(listener);
    }

    private void click(final Listener listener) {
        clickCount++;
        mHandler.postDelayed(() -> {
            if (clickCount == 1) {
                LogUtils.iTag(TAG, "one click");
                listener.oneClick();
            } else if (clickCount == 2) {
                LogUtils.iTag(TAG, "double click");
                listener.doubleClick();
            }
            mHandler.removeCallbacksAndMessages(null);
            clickCount = 0;
        }, timeout);
    }


}
