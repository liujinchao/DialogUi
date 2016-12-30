package com.widget.jcdialog.holder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.widget.jcdialog.DialogUtils;
import com.widget.jcdialog.R;
import com.widget.jcdialog.adapter.TieAdapter;
import com.widget.jcdialog.bean.BuildBean;
import com.widget.jcdialog.listener.OnItemClickListener;
import com.widget.jcdialog.utils.ToolUtils;
import com.widget.jcdialog.widget.DividerGridItemDecoration;
import com.widget.jcdialog.widget.DividerItemDecoration;

/**
 * 类名称：SheetHolder
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:25
 * 描述：列表holder
 */
public class SheetHolder extends SuperHolder {

    private TextView tvTitle;
    private RecyclerView rView;
    private TextView tvBottom;

    public SheetHolder(Context context) {
        super(context);
    }

    @Override
    protected void findViews() {
        tvTitle = (TextView) rootView.findViewById(R.id.dialogui_tv_title);
        rView = (RecyclerView) rootView.findViewById(R.id.rlv);
        tvBottom = (TextView) rootView.findViewById(R.id.dialogui_tv_bottom);


    }

    @Override
    protected int setLayoutRes() {
        return R.layout.dialogui_holder_sheet;
    }

    @Override
    public void assingDatasAndEvents(final Context context, final BuildBean bean) {
        if (TextUtils.isEmpty(bean.title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(bean.title);
        }
        if (TextUtils.isEmpty(bean.bottomTxt)) {
            tvBottom.setVisibility(View.GONE);
        } else {
            tvBottom.setVisibility(View.VISIBLE);
            tvBottom.setText(bean.bottomTxt);
            tvBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.dismiss(bean.dialog, bean.alertDialog);
                    bean.itemListener.onBottomBtnClick();

                }
            });
        }
        if (bean.isVertical) {
            rView.setLayoutManager(new LinearLayoutManager(bean.mContext));
            rView.addItemDecoration(new DividerItemDecoration(bean.mContext, ToolUtils.dip2px(bean.mContext, 8), true, Color.TRANSPARENT, true, true, false));
        } else {
            rView.setLayoutManager(new GridLayoutManager(bean.mContext, bean.gridColumns));// 布局管理器。
            rView.addItemDecoration(new DividerGridItemDecoration(bean.mContext, bean.gridColumns));
        }
        rView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        rView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        if (bean.mAdapter == null) {
            TieAdapter adapter = new TieAdapter(bean.mContext, bean.mLists);
            bean.mAdapter = adapter;
        }
        rView.setAdapter(bean.mAdapter);
        bean.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DialogUtils.dismiss(bean.dialog, bean.alertDialog);
                bean.itemListener.onItemClick(bean.mLists.get(position).getTitle(), position);
            }
        });
    }


}
