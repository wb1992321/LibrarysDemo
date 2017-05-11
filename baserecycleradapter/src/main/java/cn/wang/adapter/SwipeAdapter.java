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
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.adapter.beans.EmptyItem;
import cn.wang.adapter.views.SwipeLayout;

/**
 * author : wangshuai Created on 2017/5/9
 * email : wangs1992321@gmail.com
 */
public abstract class SwipeAdapter extends BaseEmptyAdapter implements SwipeLayout.SwipeListener {
    private SwipeLayout mSwipeLayout = null;

    public SwipeAdapter(Context context) {
        super(context);
    }

    public abstract int swipeLayoutId(int position);

    public abstract void swipeOp(View convertView, int position);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != EMPTY_CODE)
            return new BaseViewHolder(inflate.inflate(R.layout.swipe_layout,
                    parent, false), this);
        else return super.onCreateViewHolder(parent, viewType);
    }

    public abstract void doAction(int position, View convertView);

    @Override
    public void doView(int position, View convertView) {
        LinearLayout llButton = ViewHolder.getView(convertView, R.id.bottom_wrapper);
        LinearLayout llContent = ViewHolder.getView(convertView, R.id.ll_content);
        SwipeLayout slRoot = ViewHolder.getView(convertView, R.id.sl_root);
        slRoot.setTag(R.id.swipe_item_position, position);
        if (slRoot == mSwipeLayout) closeSwipe();
        if (swipeLayoutId(position) > 0) {
            llButton.setVisibility(View.VISIBLE);
            View buttonView = inflate.inflate(swipeLayoutId(position), llButton, false);
            if (llButton.getChildCount() > 0) llButton.removeAllViews();
            llButton.addView(buttonView);
            swipeOp(buttonView, position);
            slRoot.setDrag(SwipeLayout.DragEdge.Right, llButton);
            slRoot.addSwipeListener(this);
        } else llButton.setVisibility(View.GONE);
        if (llContent.getChildCount() > 0) llContent.removeAllViews();
        View contentView = inflate.inflate(getLayoutID(getViewType(position)), llContent, false);
        llContent.addView(contentView);
        doAction(position, contentView);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.recycler_list_position_tag);
        if (position == getSwipePosition()) return;
        if (getSwipePosition() >= 0) {
            closeSwipe();
        } else
            super.onClick(v);
    }

    @Override
    public void onStartOpen(SwipeLayout layout) {
        if (mSwipeLayout != layout) {
            closeSwipe();
        }
    }

    @Override
    public void onOpen(SwipeLayout layout) {
        if (mSwipeLayout != layout) {
            closeSwipe();
        }
        mSwipeLayout = layout;
    }

    @Override
    public void onStartClose(SwipeLayout layout) {
    }


    @Override
    public void onClose(SwipeLayout layout) {
    }

    @Override
    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

    }

    @Override
    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

    }

    private int getSwipePosition() {
        return mSwipeLayout == null ? -1 : (int) (mSwipeLayout.getTag(R.id.swipe_item_position) == null ? -1 : mSwipeLayout.getTag(R.id.swipe_item_position));
    }

    @Override
    protected void handleMsg(Message message) {
        if (message.what==Constant.TYPE_DELETE_ALL||message.what==Constant.TYPE_DELETE_ITEM){
            closeSwipe();
        }
        super.handleMsg(message);
    }

    private boolean closeSwipe() {
        if (mSwipeLayout != null && mSwipeLayout.getOpenStatus() != SwipeLayout.Status.Close) {
            mSwipeLayout.close();
            mSwipeLayout = null;
            return true;
        } else {
            mSwipeLayout = null;
            return false;
        }
    }

}
