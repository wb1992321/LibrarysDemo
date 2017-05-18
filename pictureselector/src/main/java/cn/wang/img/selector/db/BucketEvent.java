package cn.wang.img.selector.db;

import cn.wang.img.selector.models.BucketModel;

/**
 * author : wangshuai Created on 2017/5/18
 * email : wangs1992321@gmail.com
 */
public class BucketEvent {

    public static final int TYPE_BUCKET_SELECT = 0;
    public static final int TYPE_BUCKET_NONE = -1;

    private int type;
    private BucketModel mBucketModel;

    public BucketEvent(int type, BucketModel mBucketModel) {
        this.type = type;
        this.mBucketModel = mBucketModel;
    }

    public BucketEvent(int type) {
        this.type = type;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BucketModel getmBucketModel() {
        return mBucketModel;
    }

    public void setmBucketModel(BucketModel mBucketModel) {
        this.mBucketModel = mBucketModel;
    }
}
