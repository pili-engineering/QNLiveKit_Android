package com.qlive.sdk;

import android.content.Context;

public interface QLiveUIKit {
    <T extends QPage> T getPage(Class<T> pageClass);
    void launch(Context context);
}
