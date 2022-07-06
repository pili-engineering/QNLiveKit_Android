package com.qlive.shoppingservice;


import com.qlive.core.QLiveCallBack;
import com.qlive.core.been.QExtension;

import java.util.List;


//商品
class QItem {

}

//购物服务监听
interface QShoppingServiceListener {
    //正在展示中商品切换
    void onExplainingUpdate(QItem item);

    //商品扩展字段跟新
    void onExtensionUpdate(QItem item, QExtension extension);

    //有商品状态变化
    void onStatusUpdate(QItem item);

}

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

class QOrderParam {
    public String ItemID;
    public int order;
}

class QSingleOrderParam {
    public String ItemID;
    public int from;
    public int to;
}

