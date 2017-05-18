package cn.wang.img.selector.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
public class PictureStagger extends GridLayoutManager.SpanSizeLookup {

    public static final int TYPE_PICTURE = 9999999;
    public static final int TYPE_DATE_DAY = 9999998;

    private RecyclerView.Adapter adapter = null;
    private int columnCount = 0;

    public PictureStagger(RecyclerView.Adapter adapter, int columnCount) {
        this.adapter = adapter;
        this.columnCount = columnCount;
    }

    @Override
    public int getSpanSize(int position) {
        int type = adapter.getItemViewType(position);
        switch (type) {
            case TYPE_PICTURE:
                return 1;
            case TYPE_DATE_DAY:
                return getColumnCount();
            default:
                return 1;
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
}
