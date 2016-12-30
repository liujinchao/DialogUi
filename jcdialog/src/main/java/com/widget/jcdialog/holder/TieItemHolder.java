package com.widget.jcdialog.holder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widget.jcdialog.R;
import com.widget.jcdialog.bean.TieBean;
import com.widget.jcdialog.listener.OnItemClickListener;


/**
 * 类名称：TieItemHolder
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:26
 * 描述：消息的item
 */
public class TieItemHolder extends SuperItemHolder<TieBean> {


    LinearLayout llBg;
    TextView tvTitle;

    public TieItemHolder(Context mContext, OnItemClickListener listener, View itemView) {
        super(mContext, listener, itemView);
        llBg = (LinearLayout) itemView.findViewById(R.id.ll_bg);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void refreshView() {
        TieBean data = getData();
        llBg.setSelected(data.isSelect());
        tvTitle.setText("" + data.getTitle());
    }
}
