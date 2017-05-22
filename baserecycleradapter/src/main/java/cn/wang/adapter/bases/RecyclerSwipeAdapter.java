package cn.wang.adapter.bases;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import cn.wang.adapter.BaseEmptyAdapter;
import cn.wang.adapter.R;
import cn.wang.adapter.bases.swipe.SwipeAdapterInterface;
import cn.wang.adapter.bases.swipe.SwipeItemMangerImpl;
import cn.wang.adapter.bases.swipe.SwipeItemMangerInterface;
import cn.wang.adapter.utils.Attributes;
import cn.wang.adapter.views.SwipeLayout;

/**
 * author : wangshuai Created on 2017/5/22
 * email : wangs1992321@gmail.com
 */
public abstract class RecyclerSwipeAdapter extends BaseEmptyAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    public abstract void swipeOp(View convertView, int position);

    public abstract int swipeLayoutId(int position);

    public abstract void doAction(int position, View convertView);

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sl_root;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != EMPTY_CODE)
            return new BaseViewHolder(inflate.inflate(R.layout.swipe_layout,
                    parent, false), this);
        else return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void doView(int position, View convertView) {
        LinearLayout llButton = ViewHolder.getView(convertView, R.id.bottom_wrapper);
        LinearLayout llContent = ViewHolder.getView(convertView, R.id.ll_content);
        SwipeLayout slRoot = ViewHolder.getView(convertView, R.id.sl_root);
        slRoot.setTag(R.id.swipe_item_position, position);
//        if (slRoot == mSwipeLayout) closeSwipe();
        if (swipeLayoutId(position) > 0) {
            llButton.setVisibility(View.VISIBLE);
            View buttonView = inflate.inflate(swipeLayoutId(position), llButton, false);
            if (llButton.getChildCount() > 0) llButton.removeAllViews();
            llButton.addView(buttonView);
            llButton.setPadding(0, 0, 0, 0);
            swipeOp(buttonView, position);
            mItemManger.bind(slRoot, position);
//            slRoot.setDrag(SwipeLayout.DragEdge.Right, llButton);
        } else llButton.setVisibility(View.GONE);
        if (llContent.getChildCount() > 0) llContent.removeAllViews();
        View contentView = inflate.inflate(getLayoutID(getViewType(position)), llContent, false);
        llContent.addView(contentView);
        doAction(position, contentView);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.recycler_list_position_tag);
        mItemManger.closeAllItems();
        if (!mItemManger.isOpen(position) && getOnItemClickListener() != null)
            getOnItemClickListener().onItemClick(position, v);
    }

    public SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    public RecyclerSwipeAdapter(Context context) {
        super(context);
        setMode(Attributes.Mode.Single);
    }

    @Override
    public void notifyDatasetChanged(int position) {
        if (position < 0)
            super.notifyDataSetChanged();
        else updateItem(position);
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
