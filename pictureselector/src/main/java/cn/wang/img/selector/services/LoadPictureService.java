package cn.wang.img.selector.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.wang.img.selector.Utils.DateUtils;
import cn.wang.img.selector.db.LoadEvent;
import cn.wang.img.selector.models.BucketModel;
import cn.wang.img.selector.models.PictureModel;
import cn.wang.img.selector.Utils.MediaStoreCursorHelper;
import cn.wang.img.selector.db.DB;
import cn.wang.img.selector.db.PictureEvent;
import rx.Observable;

/**
 * author : wangshuai Created on 2017/5/12
 * email : wangs1992321@gmail.com
 */
public class LoadPictureService extends IntentService {

    private static final String TAG = "LoadPictureService";

    public LoadPictureService() {
        super("LoadPictureService");
    }


    public static void start(Context activity) {
        Log.d(TAG,"start");
        activity.startService(new Intent(activity, LoadPictureService.class));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        long startTime = System.currentTimeMillis();
        Cursor cursor = MediaStoreCursorHelper.openPhotosCursor(this, 0);
        MediaStoreCursorHelper.getAllPhotoFrom(cursor)
                .filter(pictureModel -> pictureModel != null && !TextUtils.isEmpty(pictureModel.getPhotoId()))
                .doOnNext(pictureModel -> pictureModel.setUpdateTime(startTime))
                .toList()
                .doOnNext(pictureModels -> {
                    FlowManager.getDatabase(DB.class)
                            .beginTransactionAsync(FastStoreModelTransaction.saveBuilder(FlowManager.getModelAdapter(PictureModel.class))
                                    .addAll(pictureModels)
                                    .build()).build().execute();
                    EventBus.getDefault().post(new LoadEvent(1));
                })
                .flatMap(pictureModel -> PictureModel.getOldData(startTime))
                .filter(pictureModel -> TextUtils.isEmpty(pictureModel.getPhotoId()) || TextUtils.isEmpty(pictureModel.getLocalPath()) || !new File(pictureModel.getLocalPath()).exists())
                .subscribe(pictureModel -> {
                    pictureModel.delete();
                }, throwable -> {
                });
    }
}
