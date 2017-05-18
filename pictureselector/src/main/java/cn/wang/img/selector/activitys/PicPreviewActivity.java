package cn.wang.img.selector.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.wang.adapter.bases.Constant;
import cn.wang.adapter.listeners.OnItemClickListener;
import cn.wang.img.selector.R;
import cn.wang.img.selector.adapters.PicPreviewAdapter;
import cn.wang.img.selector.db.PictureEvent;
import cn.wang.img.selector.models.PictureModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : wangshuai Created on 2017/5/18
 * email : wangs1992321@gmail.com
 */
public class PicPreviewActivity extends AppCompatActivity implements PicPreviewAdapter.LoadFinish, CompoundButton.OnCheckedChangeListener, OnItemClickListener {

    public static final String INTENT_EXTRA_BUCKET = "intent_extra_bucket_id";
    public static final String INTENT_EXTRA_IMAGE_URL = "intent_extra_image_url";

    private RecyclerView rvPicList;
    private TextView tvPosition;
    private CheckBox cbSel;

    private String mBucketId, mCurrentPicturePath;
    private PictureModel mCurrentPictureModel;


    private PicPreviewAdapter adapter;
    private LinearLayoutManager manager;

    public static void open(Context context, String currentPath) {
        open(context, null, currentPath);
    }

    public static void open(Context context, String bucketId, String currentPath) {
        Intent intent = new Intent(context, PicPreviewActivity.class);
        intent.putExtra(INTENT_EXTRA_IMAGE_URL, currentPath);
        if (!TextUtils.isEmpty(bucketId))
            intent.putExtra(INTENT_EXTRA_BUCKET, bucketId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_preview);
        rvPicList = (RecyclerView) findViewById(R.id.rv_pic_list);
        tvPosition = (TextView) findViewById(R.id.tv_position);
        cbSel = (CheckBox) findViewById(R.id.cb_sel);
        cbSel.setOnCheckedChangeListener(this);

        mBucketId = getIntent().getStringExtra(INTENT_EXTRA_BUCKET);
        mCurrentPicturePath = getIntent().getStringExtra(INTENT_EXTRA_IMAGE_URL);

        adapter = new PicPreviewAdapter(this);
        adapter.setLoadFinish(this);
        adapter.setOnItemClickListener(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvPicList.setLayoutManager(manager);
        rvPicList.setAdapter(adapter);
        new PagerSnapHelper().attachToRecyclerView(rvPicList);
        rvPicList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int postion = manager.findFirstVisibleItemPosition();
                tvPosition.setText(String.format("%d / %d", postion + 1, adapter.getItemCount()));
                mCurrentPictureModel = adapter.getItem(postion);
                EventBus.getDefault().post(new PictureEvent(PictureEvent.ACTION_SELECT_CHECK, mCurrentPictureModel));
            }
        });
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initData() {
        Observable.defer(() -> Observable.from(PictureModel.getBucketAllPicture(mBucketId)))
                .doOnNext(model -> {
                    if (model.getLocalPath().equals(mCurrentPicturePath)) {
                        mCurrentPictureModel = model;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pics -> {
                    if (pics != null && pics.size() > 0) {
                        adapter.addList(true, pics);
                    } else {
                        Toast.makeText(this, "查询本地照片失败", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(this, "查询本地照片失败", Toast.LENGTH_SHORT).show();
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PictureEvent event) {
        if (event.getPictureModel() != null && event.getPictureModel().equals(mCurrentPictureModel)) {
            if (event.getAction().equals(PictureEvent.ACTION_SELECT)) {
                cbSel.setChecked(true);
            } else if (event.getAction().equals(PictureEvent.ACTION_SELECT_CANCEL)) {
                cbSel.setChecked(false);
            }
        }
    }

    public void closeUI(View view){
        finish();
    }

    @Override
    public void loadfinish(int what) {
        if (what == Constant.TYPE_ADD_LIST) {
            tvPosition.setText(String.format("%d / %d", adapter.getPosition(mCurrentPictureModel) + 1, adapter.getItemCount()));
            rvPicList.scrollToPosition(adapter.getPosition(mCurrentPictureModel));
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            EventBus.getDefault().post(new PictureEvent(PictureEvent.ACTION_SELECT, mCurrentPictureModel));
        } else {
            EventBus.getDefault().post(new PictureEvent(PictureEvent.ACTION_SELECT_CANCEL, mCurrentPictureModel));
        }
    }

    @Override
    public void onItemClick(int position, View contentView) {
        finish();
    }
}
