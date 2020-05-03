package com.widget.jcdialog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.widget.jcdialog.R;
import com.widget.jcdialog.bean.PopuBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：Create on 2016/12/26 16:21
 * 描述：PopuWindow适配器，结合PopuWindowView使用
 */
public class PopuWindowAdapter extends RecyclerView.Adapter<PopuWindowAdapter.ViewHolder> {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 供下拉的集合包括id
     */
    List<PopuBean> list;
    private PopupWindowClickListener clickListener;

    public void setClickListener(PopupWindowClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public PopuWindowAdapter(Context mContext, List<PopuBean> lists) {
        this.mContext = mContext;
        this.list = lists;
        if (list == null) {
            list = new ArrayList<PopuBean>();
        }
    }

    @NonNull
    @Override
    public PopuWindowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dialogui_popu_option_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PopuWindowAdapter.ViewHolder viewHolder, final int i) {
        TextView tvTitle = viewHolder.itemView.findViewById(R.id.tv_popup_title);
        tvTitle.setText(list.get(i).getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null){
                    clickListener.setOnclickListener(i,list.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface PopupWindowClickListener{
        void setOnclickListener(int position, PopuBean popuBean);
    }

}
