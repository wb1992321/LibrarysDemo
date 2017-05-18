package cn.wang.img.selector.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

import cn.wang.img.selector.models.PictureModel_Table;
import cn.wang.img.selector.db.DB;
import rx.Observable;

/**
 * author : wangshuai Created on 2017/5/15
 * email : wangs1992321@gmail.com
 */
@QueryModel(database = DB.class)
public class BucketModel extends BaseQueryModel implements Parcelable {


    @Column(name = "bucket_id")
    String bucketId;

    @Column(name = "bucket_display_name")
    String bucketDisplayName;

    @Column(name = "local_path")
    String localPath;
    long count = 0;

    public BucketModel(String bucketDisplayName, String localPath) {
        this.bucketDisplayName = bucketDisplayName;
        this.localPath = localPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BucketModel that = (BucketModel) o;

        return !TextUtils.isEmpty(bucketId) ? bucketId.equals(that.bucketId) : TextUtils.isEmpty(that.bucketId);

    }

    public String getBucketId() {
        return bucketId;
    }

    public static Observable<BucketModel> getAllBucket() {
        return Observable.from(SQLite.select(PictureModel_Table.bucket_id, PictureModel_Table.bucket_display_name, PictureModel_Table.local_path).from(PictureModel.class)
                .groupBy(PictureModel_Table.bucket_id).queryCustomList(BucketModel.class));
    }

    public static String getBucketCover(String bucketId) {
        if (TextUtils.isEmpty(bucketId)) {
            return SQLite.select(PictureModel_Table.local_path)
                    .from(PictureModel.class)
                    .querySingle().getLocalPath();
        } else {
            return SQLite.select(PictureModel_Table.local_path)
                    .from(PictureModel.class)
                    .where(PictureModel_Table.bucket_id.eq(bucketId))
                    .querySingle().getLocalPath();
        }
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getCount() {
        if (count <= 0) {
            count = PictureModel.getBucketPictureCount(getBucketId());
        }
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bucketId);
        dest.writeString(this.bucketDisplayName);
        dest.writeString(this.localPath);
        dest.writeLong(this.count);
    }

    public BucketModel() {
    }

    protected BucketModel(Parcel in) {
        this.bucketId = in.readString();
        this.bucketDisplayName = in.readString();
        this.localPath = in.readString();
        this.count = in.readLong();
    }

    public static final Parcelable.Creator<BucketModel> CREATOR = new Parcelable.Creator<BucketModel>() {
        @Override
        public BucketModel createFromParcel(Parcel source) {
            return new BucketModel(source);
        }

        @Override
        public BucketModel[] newArray(int size) {
            return new BucketModel[size];
        }
    };
}
