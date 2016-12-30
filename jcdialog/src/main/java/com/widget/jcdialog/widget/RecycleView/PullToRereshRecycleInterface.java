package com.widget.jcdialog.widget.RecycleView;

import android.view.View;

/**
 * 类名称：PullToRereshRecycleInterface
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/11/17 14:31
 * 描述：下拉刷新列表对应的事件接口
 * 最近修改时间：2016/11/17 14:31
 * 修改人：Modify by liujc
 */
public interface PullToRereshRecycleInterface {
    void setLoadMoreFootView(View view);
    void setOnRefreshComplete();
    void setOnLoadMoreComplete();//onFinishLoading,加载更多完成
    void setEmptyView(View emptyView);
    View getEmptyView();
    void addHeaderView(View view);
    View getHeaderViewByType(int itemType);
    void setRefreshHeader(ArrowRefreshHeader refreshHeader);
    void setRefreshEnabled(boolean enabled);
    void setLoadMoreEnabled(boolean enabled);
    void setLoadMoreEnabled(boolean enabled, String loadMoreStr);
    void setRefreshProgressStyle(int style);
    void setRefreshProgressStyle(int style, int indicatorColor);
    void setLoadingMoreProgressStyle(int style);
    void setArrowImageView(int resId);
    void setHeaderRefreshTime(long refreshTime);
//    void setLoadMoreCount(int count);//如果不达到count数量不让加载更多
    void setNoMore(boolean noMore);//是有还有更多数据
    void setReset();
    void release();
}
