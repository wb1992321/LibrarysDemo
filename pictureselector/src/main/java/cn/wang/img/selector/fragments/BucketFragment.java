package cn.wang.img.selector.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;

import cn.wang.adapter.listeners.OnItemClickListener;
import cn.wang.img.selector.R;
import cn.wang.img.selector.adapters.BucketAdapter;
import cn.wang.img.selector.db.BucketEvent;
import cn.wang.img.selector.models.BucketModel;
import cn.wang.img.selector.models.PictureModel;
import rx.schedulers.Schedulers;

/**
 * author : wangshuai Created on 2017/5/17
 * email : wangs1992321@gmail.com
 */
public class BucketFragment extends Fragment implements OnItemClickListener {

    private RecyclerView rvBucketList;

    private BucketAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bucket_list, container, false);
        rvBucketList = (RecyclerView) view.findViewById(R.id.rv_bucket_list);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rvBucketList.getLayoutParams();
        if (params == null) {
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActivity().getResources().getDisplayMetrics().heightPixels / 4 * 3);
        } else {
            params.height = getActivity().getResources().getDisplayMetrics().heightPixels / 4 * 3;
        }
        rvBucketList.setLayoutParams(params);
        adapter = new BucketAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        rvBucketList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvBucketList.setAdapter(adapter);
        initData();
        return view;
    }

    private void initData() {
        BucketModel.getAllBucket()
                .toList()
                .doOnNext(bucketModels -> bucketModels.add(0, new BucketModel("全部照片", BucketModel.getBucketCover(null))))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(bucketModels -> {
                    adapter.addList(true, bucketModels);
                }, throwable -> {
                });
    }

    @Override
    public void onItemClick(int position, View contentView) {
        EventBus.getDefault().post(new BucketEvent(BucketEvent.TYPE_BUCKET_SELECT, adapter.getItem(position)));
    }
}
