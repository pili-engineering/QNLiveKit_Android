package sensetime.senseme.com.effects.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/7/16 3:26 下午
 */
public class ToastUtils {
    private static final boolean isShow = true;
    private static Toast mToast = null;

    private ToastUtils() {
        throw new UnsupportedOperationException("toast error");
    }

    public void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    public static void showShort(CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(ContextHolder.getContext(), message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    public static void showShort(int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    private static Context getContext() {
        return ContextHolder.getContext();
    }
}
