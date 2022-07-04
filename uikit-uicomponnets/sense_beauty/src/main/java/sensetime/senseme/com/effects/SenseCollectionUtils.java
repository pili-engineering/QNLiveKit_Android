package sensetime.senseme.com.effects;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 4/22/21 4:50 PM
 */
public class SenseCollectionUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static int getKey(LinkedHashMap<Integer, String> map, String value) {
        int result = 0;
        for (Integer key : map.keySet()) {
            String s = map.get(key);
            if (s.equals(value)) result = key;
        }
        return result;
    }

    public static void removeByValue(LinkedHashMap<Integer, String> map, String value) {
        if (map == null) return;
        Iterator<LinkedHashMap.Entry<Integer, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            if (value.equals(iterator.next().getValue())) {
                iterator.remove();
            }
        }
    }
}
