package sensetime.senseme.com.effects.utils;

import android.util.Log;

public class LogUtils {
    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;
    public static void vTag(final String tag, final String contents) {
        log(V, tag, contents);
    }


    public static void dTag(final String tag, final String contents) {
        log(D, tag, contents);
    }


    public static void iTag(final String tag, final String contents) {
        log(I, tag, contents);
    }


    public static void wTag(final String tag, final String contents) {
        log(W, tag, contents);
    }


    public static void eTag(final String tag, final String contents) {
        log(E, tag, contents);
    }

    public static void d(final String tag,final String contents) {
        log(D, "ddd", contents);
    }
    public static void d(final String contents) {
        log(D, "ddd", contents);
    }
    public static void e(final String contents) {
        log(D, "ddd", contents);
    }
    public static void e(final String tag,final String contents) {
        log(D, "ddd", contents);
    }
    public static void i(final String contents) {
        log(D, "ddd", contents);
    }
    public static void w(final String contents) {
        log(D, "ddd", contents);
    }


    public static void log(final int type, final String tag, final String contents) {
        Log.d(tag,contents);
    }
}
