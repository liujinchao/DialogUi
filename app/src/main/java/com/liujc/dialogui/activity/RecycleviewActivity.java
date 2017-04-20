package com.liujc.dialogui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liujc.dialogui.R;
import com.widget.jcdialog.utils.ToastUitl;
import com.widget.jcdialog.widget.RecycleView.PullToRefreshRecycleView;
import com.widget.jcdialog.widget.RecycleView.animation.ScaleInAnimation;
import com.widget.jcdialog.widget.RecycleView.recycle.CommonRecycleViewAdapter;
import com.widget.jcdialog.widget.RecycleView.recycle.DividerItemDecoration;
import com.widget.jcdialog.widget.RecycleView.recycle.OnItemClickListener;
import com.widget.jcdialog.widget.RecycleView.recycle.ViewHolderHelper;

import java.util.ArrayList;

/**
 * 类名称：RecycleviewActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/10/26 09:08
 * 描述：下拉刷新  上拉加载更多
 * 最近修改时间：2016/10/26 09:08
 * 修改人：Modify by liujc
 */
public class RecycleviewActivity extends Activity implements PullToRefreshRecycleView.LoadingListener,OnItemClickListener {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private PullToRefreshRecycleView superRecyclerView;
//    private RecycleAdapter mAdapter;
    private CommonRecycleViewAdapter<String> mAdapter;
    private ArrayList<String> dataList = new ArrayList<>();
    private ArrayList<String> tempList = new ArrayList<>();
    FloatingActionButton fab;
    AppBarLayout app_bar_layout;
    private LinearLayout head_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        initView();
        dataList = initData(20);
        initAdapter();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        app_bar_layout = (AppBarLayout) findViewById(R.id.appbar);
        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("recycleview");
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    collapsingToolbar.setTitle("recycleview");
//                    head_layout.setVisibility(View.GONE);
                } else {
                    collapsingToolbar.setTitle(" ");
//                    head_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        superRecyclerView = (PullToRefreshRecycleView) findViewById(R.id.super_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        findViewById(R.id.swipefreshlayout).setEnabled(false);
        superRecyclerView.setLayoutManager(layoutManager);
        superRecyclerView.setRefreshEnabled(true);//可以定制是否开启下拉刷新
        superRecyclerView.setLoadMoreEnabled(true);//可以定制是否开启加载更多
        superRecyclerView.setLoadingListener(this);//下拉刷新，上拉加载的监听
        superRecyclerView.setEmptyView(findViewById(R.id.empty_view));
        superRecyclerView.setRefreshProgressStyle(0);//下拉刷新的样式
        superRecyclerView.setLoadingMoreProgressStyle(1);//上拉加载的样式
        superRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
//        superRecyclerView.setLoadMoreFootView(View.inflate(this, R.layout.pull_to_refresh_empty_view, null));
//        View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
//        superRecyclerView.addHeaderView(header);
        superRecyclerView.setArrowImageView(R.drawable.pull_arrow_icon);//设置下拉箭头
    }
    private ArrayList<String> initData(int size) {
        dataList.clear();
        for (int i = 1; i <= size; i++) {
            dataList.add("订单" + i);
        }
        return dataList;
    }

    //模拟加载更多的数据
    private ArrayList<String> getDataList(int size) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            data.add("新加的订单" + i);
        }
        return data;
    }


    private void initAdapter() {
//        mAdapter = new RecycleAdapter(this,dataList);
        mAdapter = new CommonRecycleViewAdapter<String>(this,R.layout.item_card_view) {
            @Override
            public void convert(ViewHolderHelper helper, String s) {
                helper.setText(R.id.name,s);
            }
        };
        mAdapter.setOnItemClickListener(this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        mAdapter.addAll(dataList);
        superRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataList = initData(20);
                superRecyclerView.setOnRefreshComplete();
                mAdapter.addAll(dataList);
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        if(dataList.size() >= 50){
            superRecyclerView.setNoMore(true);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tempList.clear();
                tempList = getDataList(20);
                dataList.addAll(tempList);
                mAdapter.addAll(dataList);
                superRecyclerView.setOnLoadMoreComplete();
            }
        }, 3000);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        ToastUitl.showToast(position+"");
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        ToastUitl.showToast(position+"  longClick");
        return true;
    }
}
