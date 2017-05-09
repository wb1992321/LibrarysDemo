package cn.wang.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.ViewHolder;

/**
 * author : wangshuai Created on 2017/5/3
 * email : wangs1992321@gmail.com
 */
public class StringArrayAdapter extends BaseAdapter<String> {

    private int layoutId;
    private int textId;

    public StringArrayAdapter(Context context, @LayoutRes int layoutId, int textId) {
        super(context);
        this.layoutId = layoutId;
        this.textId = textId;
    }



    @Override
    public int getLayoutId(int viewType) {
        return layoutId;
    }

    @Override
    public void initView(int position, View convertView) {
        TextView textView= ViewHolder.getView(convertView,textId);
        textView.setText(getItem(position));
        Log.d(TAG,position+"--"+getItem(position));
    }
}
