package com.qlive.uikitcore.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qlive.uikitcore.R;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SmartRecyclerView extends FrameLayout {
    private SmartRefreshHelper smartRefreshHelper;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout smartRefreshLayout;
    private FrameLayout flRecyContent;
    public CommonEmptyView emptyView;

    public SwipeRefreshLayout getSmartRefreshLayout() {
        return smartRefreshLayout;
    }

    public FrameLayout getFlRecyContent() {
        return flRecyContent;
    }

    public SmartRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public SmartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SmartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray styled =
                context.obtainStyledAttributes(attrs, R.styleable.SmartRecyclerView, defStyleAttr, 0);
        int emptyIcon = styled.getResourceId(
                R.styleable.SmartRecyclerView_placeholder_empty_icon,
                com.qlive.uikitcore.R.drawable.kit_pic_empty
        );
        int emptyNoNetIcon = styled.getResourceId(
                R.styleable.SmartRecyclerView_placeholder_empty_no_net_icon,
                com.qlive.uikitcore.R.drawable.kit_pic_empty_network
        );
        String emptyTip = styled.getString(R.styleable.SmartRecyclerView_placeholder_empty_tips);
        emptyView.setEmptyIcon(emptyIcon);
        emptyView.setEmptyNoNetIcon(emptyNoNetIcon);
        if (TextUtils.isEmpty(emptyTip)) {
            emptyTip = "";
        }
        emptyView.setEmptyTips(emptyTip);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.kit_layout_refresh_recyclerview, this, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        flRecyContent = view.findViewById(R.id.flRecyContent);
        smartRefreshLayout = view.findViewById(R.id.refreshLayout);
        emptyView = view.findViewById(R.id.emptyView);
        addView(view);
    }

    /**
     * 获取　recyclerView
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 手动调用触发刷新
     */
    public void startRefresh() {
        smartRefreshHelper.refresh();
    }

    /**
     * 告诉view获取失败
     */
    public void onFetchDataError() {
        smartRefreshHelper.onFetchDataError();
    }

    /**
     * 请求成功　smartRefreshHelper处理页数记录空视图的显示
     *
     * @param goneIfNoData 已经到底了一直显示
     */
    public void onFetchDataFinish(List data, Boolean goneIfNoData) {
        smartRefreshHelper.onFetchDataFinish(data, goneIfNoData);
    }

    /**
     * 请求成功　smartRefreshHelper处理页数记录空视图的显示
     *
     * @param sureLoadMoreEnd 很明确没有下一页了　不需要请求下一页来确认
     */
    public void onFetchDataFinish(List data, Boolean goneIfNoData, boolean sureLoadMoreEnd) {
        smartRefreshHelper.onFetchDataFinish(data, goneIfNoData, sureLoadMoreEnd);
    }


    /**
     * 初始化
     *
     * @param adapter       适配器
     * @param preLoadNumber 静默加载滑到倒数第几个开始
     * @param fetcherFuc    刷新事件页回调　0开始
     */
    public void setUp(BaseQuickAdapter adapter
            , int preLoadNumber, Boolean loadmoreNeed, Boolean refreshNeed
            , Function1<Integer, Unit> fetcherFuc
    ) {
        recyclerView.setAdapter(adapter);
        smartRefreshHelper = new SmartRefreshHelper(adapter, recyclerView, smartRefreshLayout, emptyView, preLoadNumber, loadmoreNeed, refreshNeed, fetcherFuc);
    }

}
