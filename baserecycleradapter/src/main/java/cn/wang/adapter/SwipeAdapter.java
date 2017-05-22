package cn.wang.adapter;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.BaseViewHolder;
import cn.wang.adapter.bases.Constant;
import cn.wang.adapter.bases.ItemBean;
import cn.wang.adapter.bases.RecyclerSwipeAdapter;
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.adapter.beans.EmptyItem;
import cn.wang.adapter.views.SwipeLayout;

/**
 * author : wangshuai Created on 2017/5/9
 * email : wangs1992321@gmail.com
 */
public abstract class SwipeAdapter extends RecyclerSwipeAdapter {

    public SwipeAdapter(Context context) {
        super(context);
    }
}
