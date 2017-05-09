package cn.wang.adapter.beans;

import cn.wang.adapter.bases.ItemBean;

/**
 * author : wangshuai Created on 2017/5/3
 * email : wangs1992321@gmail.com
 */
public class EmptyItem implements ItemBean{
    private int type;//当前操作类型
    private Throwable error;
    public EmptyItem(int type) {
        this.type = type;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
