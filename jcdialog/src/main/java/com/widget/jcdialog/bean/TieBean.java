package com.widget.jcdialog.bean;

/**
 * 类名称：TieBean
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:27
 * 描述：TODO
 */
public class TieBean {

    private int id;
    private String title;
    private boolean isSelect;

    public TieBean(String title) {
        this.title = title;
    }

    public TieBean(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
