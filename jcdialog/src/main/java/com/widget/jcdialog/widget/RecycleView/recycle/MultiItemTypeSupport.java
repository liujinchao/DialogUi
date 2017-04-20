package com.widget.jcdialog.widget.RecycleView.recycle;

public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int itemType);

	int getItemViewType(int position, T t);
}