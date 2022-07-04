package sensetime.senseme.com.effects.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @Description handler
 * @Author Lu Guoqiang
 * @Time 4/19/21 2:11 PM
 */
public abstract class BaseHandler<T> extends Handler {

    private final WeakReference<T> mReference;

    public BaseHandler(T t) {
        super(Looper.getMainLooper());
        mReference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        T t = mReference.get();
        if (t != null) {
            handleMessage(t, msg);
        }
    }

    protected abstract void handleMessage(T t, Message msg);
}
