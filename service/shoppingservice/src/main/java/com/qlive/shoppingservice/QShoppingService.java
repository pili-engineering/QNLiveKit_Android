package com.qlive.shoppingservice;


import com.qlive.core.QLiveCallBack;
import com.qlive.core.been.QExtension;
import com.qlive.core.been.QItem;

import java.util.List;


//购物服务
public interface QShoppingService {

    //获取直播间所有商品
    void getItemList(QLiveCallBack<QItem> callBack);

    //跟新商品状态 并通知房间所有人
    void updateItemStatus(String ItemID, int status, QLiveCallBack<Void> callBack);

    //跟新商品扩展字段 并通知房间所有人
    void updateItemExtension(String ItemID, QExtension extension);

    //设置讲解中的商品 并通知房间所有人
    void setExplaining(String ItemID, QLiveCallBack<Void> callBack);

    //取消设置讲解中的商品 并通知房间所有人
    void cancelExplaining(QLiveCallBack<Void> callBack);

    //当前讲解中的
    QItem getExplaining();

    //跟新单个商品顺序
    void changeSingleOrder(QSingleOrderParam param, QLiveCallBack<Void> callBack);

    void changeOrder(List<QOrderParam> params, QLiveCallBack<Void> callBack);

    //删除商品
    void deleteItems(List<String> itemIDS,  QLiveCallBack<Void> callBack);

}




