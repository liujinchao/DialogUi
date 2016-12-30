package com.widget.jcdialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.widget.jcdialog.R;
import com.widget.jcdialog.bean.TieBean;
import com.widget.jcdialog.holder.SuperItemHolder;
import com.widget.jcdialog.holder.TieItemHolder;

import java.util.List;

/**
 * 类名称：TieAdapter
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:23
 * 描述：编码器适配器
 */
public class TieAdapter extends SuperAdapter<TieBean> {

    public TieAdapter(Context mContext, List<TieBean> list) {
        super(mContext, list);
    }

    @Override
    public SuperItemHolder getItemHolder(ViewGroup parent, int viewType) {
        return new TieItemHolder(mContext, mListener, LayoutInflater.from(mContext).
                inflate(R.layout.dialogui_holder_item_tie, parent, false));
    }
}
