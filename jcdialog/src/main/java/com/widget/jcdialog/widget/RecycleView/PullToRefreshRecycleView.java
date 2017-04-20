package com.widget.jcdialog.widget.RecycleView;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：PullToRefreshRecycleView
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/10/25 18:05
 * 描述：TODO
 * 最近修改时间：2016/10/25 18:05
 * 修改人：Modify by liujc
 */
public class PullToRefreshRecycleView extends RecyclerView implements PullToRereshRecycleInterface{
    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    private int mRefreshProgressStyle = 0;//下拉刷新样式
    private int mLoadingMoreProgressStyle = 1;//加载更多样式
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private WrapAdapter mWrapAdapter;
    private float mLastY = -1;
    private static final float DRAG_RATE = 3;
    private LoadingListener mLoadingListener;
    private ArrowRefreshHeader mRefreshHeader;
    private View mLoadMoreFootView;
    private View mEmptyView;
    private boolean refreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int TYPE_REFRESH_HEADER = 100000;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int TYPE_LOADMORE_FOOTER = 100001;
    private static final int HEADER_INIT_INDEX = 10002;
    private static List<Integer> sHeaderTypes = new ArrayList<>();//每个header必须有不同的type,不然滚动的时候顺序会变化
    private final AdapterDataObserver mDataObserver = new DataObserver();
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    public PullToRefreshRecycleView(Context context) {
        this(context, null);
    }
    public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public PullToRefreshRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (refreshEnabled) {
            mRefreshHeader = new ArrowRefreshHeader(context);
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
        LoadingMoreFooter footView = new LoadingMoreFooter(context);
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        mLoadMoreFootView = footView;
        mLoadMoreFootView.setVisibility(GONE);
    }

    //判断是否是SuperRecyclerView保留的itemViewType
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOADMORE_FOOTER  || sHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isNoMore && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true;
                if (mLoadMoreFootView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) mLoadMoreFootView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    mLoadMoreFootView.setVisibility(View.VISIBLE);
                }
                mLoadingListener.onLoadMore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && refreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1; // reset
                if (isOnTop() && refreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        if (mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setLoadMoreFootView(View view) {
         mLoadMoreFootView = view;
    }

    @Override
    public void setOnRefreshComplete() {
        mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    @Override
    public void setOnLoadMoreComplete() {
        isLoadingData = false;
        if (mLoadMoreFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mLoadMoreFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mLoadMoreFootView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    @Override
    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void addHeaderView(View view) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
    }

    @Override
    public View getHeaderViewByType(int itemType) {
        if(!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    //判断一个type是否为HeaderType
    private boolean isHeaderType(int itemViewType) {
        return  mHeaderViews.size() > 0 &&  sHeaderTypes.contains(itemViewType);
    }
    @Override
    public void setRefreshHeader(ArrowRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    @Override
    public void setRefreshEnabled(boolean enabled) {
        refreshEnabled = enabled;
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        setLoadMoreEnabled(enabled,"");
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled, String loadMoreStr) {
        if (!TextUtils.isEmpty(loadMoreStr)){
            ((LoadingMoreFooter) mLoadMoreFootView).setFooterLoadMoreString(loadMoreStr);
        }
        loadingMoreEnabled = enabled;
        if (!enabled) {
            if (mLoadMoreFootView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) mLoadMoreFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
            }
        }
    }

    @Override
    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    @Override
    public void setRefreshProgressStyle(int style, int indicatorColor) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style,indicatorColor);
        }
    }

    @Override
    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mLoadMoreFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mLoadMoreFootView).setProgressStyle(style);
        }
    }

    @Override
    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
    }

    @Override
    public void setHeaderRefreshTime(long refreshTime) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setHeaderRefreshTime(refreshTime);
        }
    }

    @Override
    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        if (mLoadMoreFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mLoadMoreFootView).setState(isNoMore ? LoadingMoreFooter.STATE_NOMORE : LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mLoadMoreFootView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setReset() {
        setNoMore(false);
        setOnLoadMoreComplete();
        setOnRefreshComplete();
    }

    @Override
    public void release() {
        this.release();
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
    @Override
    public void onChanged() {
        RecyclerView.Adapter<?> adapter = getAdapter();
        if (adapter != null && mEmptyView != null) {
            int emptyCount = 0;
            if (refreshEnabled) {
                emptyCount++;
            }
            if (loadingMoreEnabled) {
                emptyCount++;
            }
            if (adapter.getItemCount() == emptyCount) {
                mEmptyView.setVisibility(View.VISIBLE);
                PullToRefreshRecycleView.this.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                PullToRefreshRecycleView.this.setVisibility(View.VISIBLE);
            }
        }
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }
//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
////            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
//        }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
    }
}

public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter adapter;
    public WrapAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }
    public boolean isHeader(int position) {
        return position >= 1 && position < mHeaderViews.size() + 1;
    }
    public boolean isFooter(int position) {
        if (loadingMoreEnabled) {
            return position == getItemCount() - 1;
        } else {
            return false;
        }
    }
    public boolean isRefreshHeader(int position) {
        return position == 0;
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
//        return 0;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REFRESH_HEADER) {
            return new SimpleViewHolder(mRefreshHeader);
        }else if (isHeaderType(viewType)) {
            return new SimpleViewHolder(getHeaderViewByType(viewType));
        }  else if (viewType == TYPE_LOADMORE_FOOTER) {
            return new SimpleViewHolder(mLoadMoreFootView);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position) || isRefreshHeader(position)) {
            return;
        }
        int adjPosition = position - (getHeadersCount() + 1);

        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                adapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (loadingMoreEnabled) {
            if (adapter != null) {
                return getHeadersCount() + adapter.getItemCount() + 2;
            } else {
                return getHeadersCount() + 2;
            }
        } else {
            if (adapter != null) {
                return getHeadersCount() + adapter.getItemCount() + 1;
            } else {
                return getHeadersCount() + 1;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int adjPosition = position - (getHeadersCount() + 1);
        if (isReservedItemViewType(adapter.getItemViewType(adjPosition))) {
            throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
        }
        if (isRefreshHeader(position)) {
            return TYPE_REFRESH_HEADER;
        }
        if (isHeader(position)) {
            position = position - 1;
            return sHeaderTypes.get(position);
        }
        if (isFooter(position)) {
            return TYPE_LOADMORE_FOOTER;
        }

        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemViewType(adjPosition);
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (adapter != null && position >= getHeadersCount() + 1) {
            int adjPosition = position - (getHeadersCount() + 1);
            if (adjPosition < adapter.getItemCount()) {
                return adapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        adapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        adapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        adapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return adapter.onFailedToRecycleView(holder);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.registerAdapterDataObserver(observer);
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {
        void onRefresh();
        void onLoadMore();
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && refreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.getMeasuredHeight());
            mLoadingListener.onRefresh();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }
}
