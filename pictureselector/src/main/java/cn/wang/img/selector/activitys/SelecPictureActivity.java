package cn.wang.img.selector.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.wang.adapter.listeners.OnItemClickListener;
import cn.wang.img.selector.R;
import cn.wang.img.selector.adapters.PicPreviewAdapter;
import cn.wang.img.selector.adapters.SelectPictureAdapter;
import cn.wang.img.selector.db.BucketEvent;
import cn.wang.img.selector.db.PictureEvent;
import cn.wang.img.selector.fragments.BucketFragment;
import cn.wang.img.selector.models.BucketModel;
import cn.wang.img.selector.models.PictureModel;
import cn.wang.img.selector.services.LoadPictureService;
import cn.wang.img.selector.views.PictureStagger;
import rx.Observable;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
public class SelecPictureActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String TAG = "SelecPictureActivity";

    private Toolbar toolbar;
    private TextView tvTitle;

    private RecyclerView rvPicList;
    private SelectPictureAdapter adapter;
    private GridLayoutManager layoutManager = null;
    private BucketModel mBucketModel;
    private BucketFragment fragment;
    private boolean fragmentShow;
    private MenuItem actionOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pic_list);
        rvPicList = (RecyclerView) findViewById(R.id.rv_pic_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle.setText("所有照片");
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        LoadPictureService.start(SelecPictureActivity.this);
                    }
                }, throwable -> Log.d("LoadPictureService", "异常", throwable));

        adapter = new SelectPictureAdapter(this);
        adapter.setOnItemClickListener(this);
        PictureStagger stagger = new PictureStagger(adapter, 2);
        adapter.setLookup(stagger);
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager = new GridLayoutManager(this, stagger.getColumnCount(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(stagger);
        rvPicList.setLayoutManager(layoutManager);
        rvPicList.setAdapter(adapter);
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initData() {
        tvTitle.setText(mBucketModel == null ? "全部照片" : mBucketModel.getBucketDisplayName());
        PictureModel.getBucketAllDatePictures(mBucketModel == null ? null : mBucketModel.getBucketId())
                .toList()
                .map(dateModels -> {
                    ArrayList list = new ArrayList();
                    for (int i = 0; i < dateModels.size(); i++) {
                        list.add(dateModels.get(i));
                        list.addAll(dateModels.get(i).getList());
                    }
                    return list;
                })
                .subscribe(list -> adapter.addList(true, list)
                        , throwable -> Log.d(TAG, "异常", throwable));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture_select_menu, menu);
        actionOk = menu.findItem(R.id.action_ok);

        actionOkText();
        return super.onCreateOptionsMenu(menu);
    }

    private void actionOkText() {
        if (actionOk != null && adapter.getSelectCount() > 0) {
            actionOk.setVisible(true);
            actionOk.setTitle(String.format("确定(%d)", adapter.getSelectCount()));
        } else if (actionOk != null) {
            actionOk.setTitle("确定");
            actionOk.setVisible(false);
        }
    }

    public void changeBucket(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (fragmentShow) {
            transaction.hide(fragment);
            fragmentShow = false;
        } else {
            if (fragment == null) {
                fragment = new BucketFragment();
                transaction.add(R.id.fl_container, fragment);
            } else {
                transaction.show(fragment);
            }
            fragmentShow = true;
        }
        transaction.commit();
    }

    public void hideBucket(View view) {
        changeBucket(view);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PictureEvent event) {
        if (event.getAction().equals(PictureEvent.ACTION_SELECT)) {
            actionOkText();
            if (event.getPictureModel() != null) {
                adapter.changeSelect(true, event.getPictureModel());
            }
        } else if (event.getAction() .equals(PictureEvent.ACTION_SELECT_CANCEL)) {
            actionOkText();
            if (event.getPictureModel() != null) {
                adapter.changeSelect(false, event.getPictureModel());
            }
        } else if (event.getAction().equals(PictureEvent.ACTION_LOADFINISH)) {
            initData();
        } else if (event.getAction().equals(PictureEvent.ACTION_SELECT_CHECK)) {
            EventBus.getDefault().post(new PictureEvent(adapter.getSelectPhotoIds().contains(event.getPictureModel().getPhotoId()) ? PictureEvent.ACTION_SELECT : PictureEvent.ACTION_SELECT_CANCEL, event.getPictureModel()));
        }
    }

    @Subscribe
    public void onEvent(BucketEvent event) {
        switch (event.getType()) {
            case BucketEvent.TYPE_BUCKET_NONE:
                if (mBucketModel != null) {
                    mBucketModel = null;
                    initData();
                }
                break;
            case BucketEvent.TYPE_BUCKET_SELECT:
                if (mBucketModel == null || !mBucketModel.equals(event.getmBucketModel())) {
                    mBucketModel = event.getmBucketModel();
                    initData();
                }
                break;
        }
        changeBucket(null);
    }

    @Override
    public void onItemClick(int position, View contentView) {
        if (adapter.getItemViewType(position) == PictureStagger.TYPE_PICTURE) {
            PicPreviewActivity.open(this, mBucketModel == null ? null : mBucketModel.getBucketId(), ((PictureModel) adapter.getItem(position)).getLocalPath());
        }
    }
}
