package cn.wang.adapter.bases;

import android.util.SparseArray;
import android.view.View;

import cn.wang.adapter.R;

/**
 * Created by wang on 2016/9/5.
 */
public class ViewHolder {

    public static <T extends View> T getView(View view, int id) {
        SparseArray array = (SparseArray) view.getTag(R.id.viewholder_tag_sparse_array);
        if (array == null) {
            array = new SparseArray();
            view.setTag(R.id.viewholder_tag_sparse_array,array);
        }
        View childView = (View) array.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            array.put(id, childView);
        }
        return (T) childView;

    }

}
