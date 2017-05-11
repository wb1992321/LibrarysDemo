package cn.wang.adapter;

import android.content.Context;
import android.view.View;

import cn.wang.adapter.beans.EmptyItem;

/**
 * author : wangshuai Created on 2017/5/9
 * email : wangs1992321@gmail.com
 */
public abstract class NormanSwipeAdapter extends SwipeAdapter {
    public NormanSwipeAdapter(Context context) {
        super(context);
    }

    @Override
    public int emptyLayoutId() {
        return 0;
    }

    @Override
    public void empty(View contentView, EmptyItem emptyItem) {

    }
}
