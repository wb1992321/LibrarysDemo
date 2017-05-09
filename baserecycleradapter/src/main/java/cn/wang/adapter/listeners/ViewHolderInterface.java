package cn.wang.adapter.listeners;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by wang on 2016/9/19.
 * 暴漏的viewholder中定义的view方法
 */
public interface ViewHolderInterface {


    public void initView(int position, View convertView);

}
