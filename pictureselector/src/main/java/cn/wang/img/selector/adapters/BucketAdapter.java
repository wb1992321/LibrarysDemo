package cn.wang.img.selector.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.img.selector.R;
import cn.wang.img.selector.models.BucketModel;

/**
 * author : wangshuai Created on 2017/5/17
 * email : wangs1992321@gmail.com
 */
public class BucketAdapter extends BaseAdapter<BucketModel> {
    public BucketAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.bucket_list_item;
    }

    @Override
    public void initView(int position, View convertView) {
        ImageView ivImage = ViewHolder.getView(convertView, R.id.iv_image);
        TextView tvName = ViewHolder.getView(convertView, R.id.tv_name);
        TextView tvCount = ViewHolder.getView(convertView, R.id.tv_count);
        BucketModel model = getItem(position);
        Glide.with(getContext())
                .load(model.getLocalPath())
                .crossFade()
                .into(ivImage);
        tvName.setText(model.getBucketDisplayName());
        tvCount.setText(model.getCount() + "张");
    }
}
