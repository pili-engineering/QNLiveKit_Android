package sensetime.senseme.com.effects.utils;

import android.content.Context;
import android.content.SharedPreferences;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import sensetime.senseme.com.effects.SenseCollectionUtils;


public class SpUtils {
    private static final String TAG = "SpUtils";

    private static final String FILE_NAME = "fileName";
    private static SharedPreferences prefs;

    public static void init(Context app) {
        prefs = app.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void setParam(Enum<?> type, String suffix, Object object) {
        setParam(type.name() + suffix, object);
    }

    public static void removeData(Enum<?> type, String suffix) {
        prefs.edit().remove(type.name() + suffix).apply();
    }

    public static Object getParam(Enum<?> type, String suffix, Object defaultObject) {
        return getParam(type.name() + suffix, defaultObject);
    }

    public static void setParam(String key, Object object) {
        LogUtils.iTag(TAG, "setParam() called with: key = [" + key + "], object = [" + object + "]");
        SharedPreferences.Editor editor = prefs.edit();

        if (object == null) {
            editor.putString(key, "null");
            return;
        } else {
            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            }
        }
        editor.apply();
    }

    public static Long getLong(String key, Long defValue) {
        return prefs.getLong(key, defValue);
    }

    public static Object getParam(String key, Object defaultObject) {
        LogUtils.iTag(TAG, "getParam() called with: key = [" + key + "], defaultObject = [" + defaultObject + "]");
        if (defaultObject instanceof String) {
            return prefs.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return prefs.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return prefs.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return prefs.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return prefs.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static void saveIntArray(String key, int[] intArray) {
        if (SenseCollectionUtils.isEmpty(intArray)) return;
        String json = arrayToJson(intArray);
        setParam(key, json);
    }

    public static int[] getIntArray(String key, int[] defaultData) {
        String json = (String) getParam(key, "");
        int[] floats = jsonToIntArray(json);
        if (SenseCollectionUtils.isEmpty(floats)) return defaultData;
        return floats;
    }

    public static void saveFloatArray(String key, float[] array) {
        if (SenseCollectionUtils.isEmpty(array)) return;
        LogUtils.iTag(TAG, "saveFloatArray() called with: key = [" + key + "], floatArray = " + Arrays.toString(array));
        String json = arrayToJson(array);
        setParam(key, json);
        LogUtils.iTag(TAG, json);
    }

    public static void saveStringArray(String key, String[] array) {
        if (SenseCollectionUtils.isEmpty(array)) return;
        String json = arrayToJson(array);
        setParam(key, json);
    }

    public static String[] getStringArray(String key, String[] defaultData) {
        String json = (String) getParam(key, "");
        String[] array = jsonToStringArray(json);
        if (SenseCollectionUtils.isEmpty(array)) return defaultData;
        return array;
    }

    public static float[] getFloatArray(String key, float[] defaultData) {
        LogUtils.iTag(TAG, "getFloatArray() called with: key = [" + key + "]");
        String json = (String) getParam(key, "");
        LogUtils.iTag(TAG, "getFloatArray: " + json);
        float[] floats = jsonToArray(json);
        if (SenseCollectionUtils.isEmpty(floats)) return defaultData;
        return floats;
    }

    private static String arrayToJson(float[] floats) {
        JSONArray jsonArray = new JSONArray();
        for (float f : floats) {
            try {
                jsonArray.put(f);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    private static String arrayToJson(String[] array) {
        JSONArray jsonArray = new JSONArray();
        for (String str : array) {
            jsonArray.put(str);
        }
        return jsonArray.toString();
    }

    private static String[] jsonToStringArray(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            String[] floats = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                floats[i] = jsonArray.getString(i);
            }
            return floats;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static float[] jsonToArray(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            float[] floats = new float[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                floats[i] = Float.parseFloat(jsonArray.getString(i));
            }
            return floats;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String arrayToJson(int[] ins) {
        JSONArray jsonArray = new JSONArray();
        for (int f : ins) {
            jsonArray.put(f);
        }
        return jsonArray.toString();
    }

    private static int[] jsonToIntArray(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            int[] ins = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                ins[i] = jsonArray.getInt(i);
            }
            return ins;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public static void removeData(String key) {
        prefs.edit().remove(key).apply();
    }
}
