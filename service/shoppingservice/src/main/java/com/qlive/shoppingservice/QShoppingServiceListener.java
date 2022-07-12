package com.qlive.shoppingservice;

import com.qlive.core.been.QExtension;

//购物服务监听
public interface QShoppingServiceListener {
    //正在展示中商品切换
    void onExplainingUpdate(QItem item);

    //商品扩展字段跟新
    void onExtensionUpdate(QItem item, QExtension extension);

}