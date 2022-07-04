package sensetime.senseme.com.effects.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sensetime.senseme.com.effects.view.MakeupItem;
import sensetime.senseme.com.effects.view.StickerItem;

public class CollectionSortUtils {
    private static final String TOP = "none";

    public static void sort(List<MakeupItem> list) {
        Collections.sort(list, new Comparator<MakeupItem>() {
            @Override
            public int compare(MakeupItem o1, MakeupItem o2) {
                if (o1.name.equals(TOP)) {
                    return -1;
                } else if (o2.name.equals(TOP)) {
                    return 1;
                } else {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.name, o2.name);
                }
            }
        });
    }

    public static void sortStyleList(List<StickerItem> list) {
        Collections.sort(list, new Comparator<StickerItem>() {
            @Override
            public int compare(StickerItem o1, StickerItem o2) {
                if (o1.name.equals(TOP)) {
                    return -1;
                } else if (o2.name.equals(TOP)) {
                    return 1;
                } else {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.name, o2.name);
                }
            }
        });
    }

    public static LinkedHashMap<Runnable, Long> sort(LinkedHashMap<Runnable, Long> map) {
        List<Map.Entry<Runnable, Long>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        LinkedHashMap<Runnable, Long> resultMap = new LinkedHashMap<>();
        for (Map.Entry<Runnable, Long> entry : list) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }
}
