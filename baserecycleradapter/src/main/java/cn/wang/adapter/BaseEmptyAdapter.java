package cn.wang.adapter;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.BaseViewHolder;
import cn.wang.adapter.bases.ItemBean;
import cn.wang.adapter.beans.EmptyItem;

/**
 * author : wangshuai Created on 2017/5/3
 * email : wangs1992321@gmail.com
 */
public abstract class BaseEmptyAdapter extends BaseAdapter<ItemBean> {

    public static final int EMPTY_CODE = -1000000;
    private EmptyItem emptyItem;
    private int emptySize = 0;

    public abstract int emptyLayoutId();

    public abstract void empty(View contentView, EmptyItem emptyItem);

    public abstract int getLayoutID(int type);

    public abstract void doView(int position, View convertView);


    @Override
    protected void handleMsg(Message message) {
        deleteItem(emptyItem);
        super.handleMsg(message);
        empty(emptySize);
    }

    private void empty(int size) {
        if (emptyLayoutId()==0)return;
        if (list.size() <= size) {
            if (containItem(emptyItem)) updateItem(emptyItem);
            else add(size, emptyItem);
        }
    }

    public int getViewType(int position) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        if (getItem(position) instanceof EmptyItem) {
            return EMPTY_CODE;
        } else return getViewType(position);
    }

    @Override
    public final int getLayoutId(int viewType) {
        if (viewType == EMPTY_CODE)
            return emptyLayoutId();
        return getLayoutID(viewType);
    }

    @Override
    public final void initView(int position, View convertView) {
        switch (getItemViewType(position)) {
            case EMPTY_CODE:
                empty(convertView, (EmptyItem) getItem(position));
                break;
            default:
                doView(position, convertView);
                break;
        }
    }

    public BaseEmptyAdapter(Context context) {
        super(context);
        emptyItem = new EmptyItem(0);
    }

    public void emptyType(int type) {
        getEmptyItem().setType(type);
    }

    public void emptyType(int type, Throwable error) {
        getEmptyItem().setError(error);
        getEmptyItem().setType(type);
    }

    public EmptyItem getEmptyItem() {
        return emptyItem;
    }

    public void setEmptyItem(EmptyItem emptyItem) {
        this.emptyItem = emptyItem;
    }

    public int getEmptySize() {
        return emptySize;
    }

    public void setEmptySize(int emptySize) {
        this.emptySize = emptySize;
    }
}
