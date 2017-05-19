package cn.wang.img.selector.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.listeners.OnItemClickListener;
import cn.wang.img.selector.R;
import cn.wang.img.selector.Utils.ToastUtils;
import cn.wang.img.selector.adapters.PicPreviewAdapter;
import cn.wang.img.selector.adapters.SelectPictureAdapter;
import cn.wang.img.selector.db.BucketEvent;
import cn.wang.img.selector.db.LoadEvent;
import cn.wang.img.selector.db.PictureEvent;
import cn.wang.img.selector.db.PictureResultEvent;
import cn.wang.img.selector.db.ResultEvent;
import cn.wang.img.selector.fragments.BucketFragment;
import cn.wang.img.selector.models.BucketModel;
import cn.wang.img.selector.models.PictureModel;
import cn.wang.img.selector.services.LoadPictureService;
import cn.wang.img.selector.views.PictureStagger;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
public class SelecPictureActivity extends AppCompatActivity implements OnItemClickListener {

    public static final String RESULT_DATA = "result_data";
    public static final String INTENT_EXTRA_COLUMN_COUNT = "intent_extra_column_count";
    public static final String INTENT_EXTRA_MAX_SIZE = "intent_extra_max_size";
    public static final String INTENT_EXTRA_MIN_SIZE = "intent_extra_min_size";
    public static final String INTENT_EXTRA_CONFIG_JSON = "intent_extra_config_json";
    public static final String INTENT_EXTRA_RESULT_ACHIEVE_OPPROACH = "intent_extra_result_achieve_opproach";
    public static final String INTENT_EXTRA_SEL_PIC = "intent_extra_sel_pic";

    private int columnCount = 4;
    private int maxSize = Integer.MAX_VALUE;
    private int minSize = -1;
    private String jsonfromatString = null;
    private int resultAchieveOpproach = 0;

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

    public static void open(Activity activity, int requestCode, int maxSize, int resuotAchieve, String jsonFormat, ArrayList<String> photoIds) {
        open(activity, requestCode, maxSize, 4, resuotAchieve, jsonFormat, photoIds);
    }

    /**
     * @param context       上下文
     * @param maxSize       最大选中的图片数据
     * @param columnCount   每一行显示多少列 默认4
     * @param resuotAchieve 获取选中图片路径，0，通过forResult方法,1、通过EventBust
     * @param jsonFormat    返回选中图片对象字段配置
     * @param photoIds      已经选中的图片id
     */
    public static void open(Activity activity, int requestCode, int maxSize, int columnCount, int resuotAchieve, String jsonFormat, ArrayList<String> photoIds) {
        Intent intent = new Intent(activity, SelecPictureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_EXTRA_COLUMN_COUNT, columnCount);
        bundle.putInt(INTENT_EXTRA_MAX_SIZE, maxSize);
        bundle.putInt(INTENT_EXTRA_RESULT_ACHIEVE_OPPROACH, resuotAchieve);
        if (!TextUtils.isEmpty(jsonFormat))
            bundle.putString(INTENT_EXTRA_CONFIG_JSON, jsonFormat);
        if (photoIds != null && photoIds.size() > 0)
            bundle.putStringArrayList(INTENT_EXTRA_SEL_PIC, photoIds);
        intent.putExtras(bundle);
        if (resuotAchieve == 0)
            activity.startActivityForResult(intent, requestCode);
        else activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntentExtra();
        init();

    }

    private void initIntentExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            maxSize = bundle.getInt(INTENT_EXTRA_MAX_SIZE, Integer.MAX_VALUE);
            minSize = bundle.getInt(INTENT_EXTRA_MIN_SIZE);
            jsonfromatString = bundle.getString(INTENT_EXTRA_CONFIG_JSON);
            resultAchieveOpproach = bundle.getInt(INTENT_EXTRA_RESULT_ACHIEVE_OPPROACH);
            columnCount = bundle.getInt(INTENT_EXTRA_COLUMN_COUNT, columnCount);
            ArrayList<String> list = bundle.getStringArrayList(INTENT_EXTRA_SEL_PIC);
            if (list != null && list.size() > 0) {
                adapter.getSelectPhotoIds().addAll(list);
            }
        }
    }

    private void init() {
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

        adapter = new SelectPictureAdapter(this, maxSize);
        adapter.setOnItemClickListener(this);
        PictureStagger stagger = new PictureStagger(adapter, columnCount);
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
                .doOnNext(arrayList -> adapter.addList(true, arrayList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            tvTitle.setText(mBucketModel == null ? "全部照片" : mBucketModel.getBucketDisplayName());
                        }
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

    @Subscribe
    public void onEvent(LoadEvent event) {
        if (event.getState() == 1) {
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PictureEvent event) {
        if (event.getAction().equals(PictureEvent.ACTION_SELECT)) {
            if (event.getPictureModel() != null) {
                Log.d(BaseAdapter.TAG, TAG + "---" + event.getAction());
                adapter.changeSelect(true, event.getPictureModel());
            }
            actionOkText();
        } else if (event.getAction().equals(PictureEvent.ACTION_SELECT_CANCEL)) {
            if (event.getPictureModel() != null) {
                adapter.changeSelect(false, event.getPictureModel());
            }
            actionOkText();
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
            PicPreviewActivity.open(this, mBucketModel == null ? null : mBucketModel.getBucketId(), ((PictureModel) adapter.getItem(position)).getLocalPath(), maxSize, adapter.getSelectPhotoIds());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            result(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ok) {
            result();
        }
        return super.onOptionsItemSelected(item);
    }

    private void result() {
        List<PictureModel> models = PictureModel.queryPicturesByIds(adapter.getSelectPhotoIds());
        if (models == null || models.size() < minSize) {
            ToastUtils.show(this, getString(R.string.pic_min_count, minSize));
        } else if (models != null && models.size() > maxSize) {
            ToastUtils.show(this, getString(R.string.pic_max_count, maxSize));
        } else {
            result(models);
        }
    }

    private JSONObject getJSONObject(JSONObject model, JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        for (String key : jsonObject.keySet()) {
            result.put(jsonObject.getString(key), model.getString(key));
        }
        return result;
    }

    private void getResultJSON(List<PictureModel> models) {
        JSONObject jsonObject = JSONObject.parseObject(jsonfromatString);
        if (models == null)
            models = new ArrayList<>(0);
        Observable.from(models)
                .map(model -> JSON.toJSONString(model))
                .map(s -> getJSONObject(JSONObject.parseObject(s), jsonObject))
                .toList()
                .map(jsonObjects -> JSON.toJSONString(jsonObjects))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    switch (resultAchieveOpproach) {
                        case 0:
                            Intent intent = new Intent();
                            intent.putExtra(RESULT_DATA, s);
                            setResult(RESULT_OK, intent);
                            finish();
                            break;
                        case 1:
                            EventBus.getDefault().post(new ResultEvent(s));
                            finish();
                            break;
                    }
                }, throwable -> {
                });
    }

    private void resultPicture(List<PictureModel> models) {
        ArrayList<PictureModel> arrayList = new ArrayList<>(models.size());
        if (models != null && models.size() > 0)
            arrayList.addAll(models);
        switch (resultAchieveOpproach) {
            case 0:
                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, arrayList);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 1:
                EventBus.getDefault().post(new PictureResultEvent(arrayList));
                finish();
                break;
        }
    }


    private void result(List<PictureModel> models) {
        if (TextUtils.isEmpty(jsonfromatString)) {
            resultPicture(models);
        } else {
            getResultJSON(models);
        }
    }

}
