package cn.wang.adapter.bases;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.wang.adapter.listeners.ViewHolderInterface;

/**
 * Created by wang on 2016/9/19.
 * * @author 汪帅
 * BaseAdaper viewholder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder{
    private View rootView;
    private ViewHolderInterface listener=null;
    private BaseViewHolder(View itemView) {
        super(itemView);
        setRootView(itemView);
    }

    public BaseViewHolder(View itemView, ViewHolderInterface vf) {
        this(itemView);
        setListener(vf);
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public ViewHolderInterface getListener() {
        return listener;
    }

    public void setListener(ViewHolderInterface listener) {
        this.listener = listener;
    }
}
