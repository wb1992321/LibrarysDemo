package cn.wang.adapter.bases;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.wang.adapter.R;
import cn.wang.adapter.listeners.OnItemClickListener;
import cn.wang.adapter.listeners.ViewHolderInterface;

/**
 * author : wangshuai Created on 2017/5/3
 * email : wangs1992321@gmail.com
 */
public abstract class BaseAdapter<T extends Object> extends RecyclerView.Adapter<BaseViewHolder> implements ViewHolderInterface, View.OnClickListener {

    public static final String TAG = "BaseAdapter";

    public abstract int getLayoutId(int viewType);

    public abstract void initView(int position, View convertView);


    protected LayoutInflater inflate = null;
    private Context context;
    protected ArrayList<T> list = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handleMsg(msg);
        }
    };

    protected void handleMsg(Message message) {
        switch (message.what) {
            case Constant.TYPE_DELETE_ALL:
                if (list.size() > 0) {
                    list.clear();
                    super.notifyDataSetChanged();
                }
                break;
            case Constant.TYPE_ADD_LIST:
                if (message.obj != null) {
                    if (message.arg1 == -1) {
                        if (list.size() > 0) list.clear();
                        message.arg1 = 0;
                    }
                    if (message.arg1 >= 0 && message.arg1 <= getItemCount()) {
                        list.addAll(message.arg1, (Collection<T>) message.obj);
                    }
                    if (message.arg1 <= 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyItemRangeInserted(message.arg1, getItemCount() - message.arg1);
                        notifyItemRangeChanged(message.arg1, getItemCount() - message.arg1);
                    }
                }
                break;
            case Constant.TYPE_UPDATE_ITEM:
                if (message.obj != null && message.arg1 >= 0 && message.arg1 < getItemCount()) {
                    list.remove(message.arg1);
                    list.add(message.arg1, (T) message.obj);
                    notifyItemChanged(message.arg1);
//                    notifyItemRangeChanged(message.arg1, getItemCount() - message.arg1);
                } else if (message.arg1 >= 0 && message.arg1 < getItemCount()) {
                    notifyItemChanged(message.arg1);
//                    notifyItemRangeChanged(message.arg1, getItemCount() - message.arg1);
                }
                break;
            case Constant.TYPE_UPDATE_LIST:
                if (message.obj != null && message.arg1 >= 0 && message.arg1 < getItemCount()) {
                    for (int i = 0; i < message.arg2; i++) {
                        if (message.arg1 < getItemCount()) {
                            list.remove(message.arg1);
                        }
                    }
                    list.addAll(message.arg1, (Collection<? extends T>) message.obj);
                    notifyItemRangeChanged(message.arg1, message.arg2);
                } else if (message.arg1 >= 0 && message.arg1 < getItemCount()) {
                    notifyItemRangeChanged(message.arg1, message.arg2);
                }
                break;
            case Constant.TYPE_DELETE_ITEM:
                if (message.arg1 < getItemCount() && message.arg1 >= 0) {
                    list.remove(message.arg1);
                    notifyItemRemoved(message.arg1);
                    notifyItemRangeChanged(message.arg1, getItemCount() - message.arg1);
                } else if (message.arg1 == Constant.TYPE_DELETE_ALL) {
                    if (list.size() > 0) list.clear();
                    super.notifyDataSetChanged();
                }
                break;
        }
    }

    private OnItemClickListener onItemClickListener;

    public BaseAdapter(Context context) {
        this.context = context;
        inflate = LayoutInflater.from(context);
        this.list = new ArrayList<>(0);
    }

    public final Context getContext() {
        return this.context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (getLayoutId(viewType) == 0) return null;
        return new BaseViewHolder(inflate.inflate(getLayoutId(viewType),
                parent, false), this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder != null) {
            holder.getRootView().setTag(R.id.recycler_list_position_tag, position);
            holder.getRootView().setOnClickListener(this);
            holder.getListener().initView(position, holder.getRootView());
        }
    }

    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    //===========================getter and setter=====================//

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.recycler_list_position_tag);
        if (getOnItemClickListener() != null)
            getOnItemClickListener().onItemClick(position, v);
    }


    /**
     * =====================================操作item 的各种方法
     */

    public void clearAll() {
        handler.sendEmptyMessage(Constant.TYPE_DELETE_ALL);
    }

    public void addList(boolean isClear, Collection<? extends T> collection) {
        handler.sendMessage(handler.obtainMessage(Constant.TYPE_ADD_LIST, isClear ? -1 : getItemCount(), 0, collection));
    }

    public void addList(Collection<? extends T> collection) {
        addList(false, collection);
    }

    public void addList(int position, Collection<? extends T> collection) {
        handler.sendMessage(handler.obtainMessage(Constant.TYPE_ADD_LIST, position, 0, collection));
    }

    public void add(T item) {
        add(getItemCount(), item);
    }

    public void add(int position, T item) {
        ArrayList<T> arrayList = new ArrayList<>(1);
        arrayList.add(item);
        handler.sendMessage(handler.obtainMessage(Constant.TYPE_ADD_LIST, position, 0, arrayList));
    }

    public boolean containItem(T item) {
        return list.contains(item);
    }

    public int getPosition(T item) {
        return containItem(item) ? list.indexOf(item) : -1;
    }

    public void deleteItem(T item) {
        if (containItem(item)) {
            handler.sendMessage(handler.obtainMessage(Constant.TYPE_DELETE_ITEM, getPosition(item), 0, null));
        }
    }

    public void deleteItem(int position) {
        if (position >= 0 && position < getItemCount())
            handler.sendMessage(handler.obtainMessage(Constant.TYPE_DELETE_ITEM, position, 0, null));
    }

    public void updateItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            handler.sendMessage(handler.obtainMessage(Constant.TYPE_UPDATE_ITEM, position, 0, null));
        }
    }

    public void updateItem(T item) {
        if (containItem(item)) {
            handler.sendMessage(handler.obtainMessage(Constant.TYPE_UPDATE_ITEM, getPosition(item), 0, item));
        }
    }

    public void updateItem(int position, int count) {
        handler.sendMessage(handler.obtainMessage(Constant.TYPE_UPDATE_LIST, position, count, null));
    }

    public void updateItem(int position, int count, Collection<? extends T> collection) {
        handler.sendMessage(handler.obtainMessage(Constant.TYPE_UPDATE_LIST, position, count, collection));
    }

}
