package cn.wang.img.selector.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.File;
import java.util.List;

import cn.wang.img.selector.Utils.DateUtils;
import cn.wang.img.selector.models.PictureModel_Table;
import cn.wang.img.selector.db.DB;
import rx.Observable;

/**
 * author : wangshuai Created on 2017/5/12
 * email : wangs1992321@gmail.com
 */
@Table(database = DB.class)
public class PictureModel extends BaseModel implements Parcelable {
    @Column(name = "_id")
    @PrimaryKey
    String photoId;
    @Column(name = "display_name")
    String displayName;


    @Column(name = "bucket_id")
    String bucketId;

    @Column(name = "bucket_display_name")
    String bucketDisplayName;

    @Column(name = "title")
    String title;

    @Column(name = "local_path")
    String localPath;

    @Column(name = "width")
    int width;

    @Column(name = "height")
    int height;

    @Column(name = "latitude")
    double latitude;

    @Column(name = "longitude")
    double longitude;

    @Column(name = "mini_thumb_magic")
    String miniThumbMagic;

    @Column(name = "orientation")
    int orientation;

    @Column(name = "size")
    long size;

    @Column(name = "date_taken")
    long dateTaken;

    @Column(name = "date_added")
    long dateAdded;

    @Column(name = "mime_type")
    String mimeType;

    @Column(name = "exif_flash")
    int flash;

    @Column(name = "exif_maker")
    String maker;

    @Column(name = "exif_model")
    String model;

    @Column(name = "exif_white_balance")
    int whiteBalance;

    @Column(name = "loc_info_cache_flag")
    int locInfoCacheFlag;//位置信息缓存标志 0:未缓存 1:无位置信息 2:有位置信息

//    @Column(name = "loc_info_country")
//     String country;//国家

    @Column(name = "loc_info_province")
    String province;//省

    @Column(name = "loc_info_city")
    String city;//市

    @Column(name = "loc_info_district")
    String district;//区

    @Column(name = "loc_info_scenic")
    String scenic;//景区

    @Column(name = "loc_info_ex")
    String locInfoEx;//位置信息扩展字段

    @Column(name = "face_info_cache_flag")
    int faceInfoCacheFlag;//人脸信息缓存标志 0:未缓存 1:无人脸信息 2:有人脸信息

    @Column(name = "face_count")
    int faceCount;//图片包含的人脸数量

    @Column(name = "selfie_flag")
    int selfieFlag = -1;//自拍标志 -1:未检测 0:非自拍 1:自拍

    @Column(name = "url")
    String url;

    //中文时间
    @Column(name = "day_time")
    long dayTime;

    @Column(name = "objectKey")
    String objectKey;

    @Column(name = "need_upload", defaultValue = "true")
    boolean needUpload;

    @Column(name = "thumbPath")
    String thumbPath;


    @Column(name = "md5")
    String md5;

    @Column(name = "update_time")
    long updateTime;

    public PictureModel(Cursor cursor) {
        int IdColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
        int displayNameColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID);
        int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);
        int titleColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE);
        int pathColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        int mimeColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE);
        int widthColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
        int heightColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);
        int latitudeColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE);
        int longitudeColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE);
        int thumbColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC);
        int orientationColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);
        int sizeColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE);
        int dateTakenColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
        int dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED);

        this.photoId = cursor.getString(IdColumn);
        this.displayName = cursor.getString(displayNameColumn);
        this.bucketId = cursor.getString(bucketIdColumn);
        this.bucketDisplayName = cursor.getString(bucketNameColumn);
        this.title = cursor.getString(titleColumn);
        this.localPath = cursor.getString(pathColumn);
        this.mimeType = cursor.getString(mimeColumn);
        this.width = cursor.getInt(widthColumn);
        this.height = cursor.getInt(heightColumn);
        this.latitude = cursor.getDouble(latitudeColumn);
        this.longitude = cursor.getDouble(longitudeColumn);
        this.miniThumbMagic = cursor.getString(thumbColumn);
        this.orientation = cursor.getInt(orientationColumn);
        this.size = cursor.getLong(sizeColumn);
        this.setDateTaken(cursor.getLong(dateTakenColumn));
        this.dateAdded = cursor.getLong(dateAddedColumn);
        this.needUpload = true;
        this.setDayTime(DateUtils.datetime2DayTime(dateTaken));
    }

    public PictureModel() {
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBucketId() {
        return bucketId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMiniThumbMagic() {
        return miniThumbMagic;
    }

    public void setMiniThumbMagic(String miniThumbMagic) {
        this.miniThumbMagic = miniThumbMagic;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
        if (dateTaken <= 0) {
            dateTaken = new File(getLocalPath()).lastModified();
        }
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getFlash() {
        return flash;
    }

    public void setFlash(int flash) {
        this.flash = flash;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getWhiteBalance() {
        return whiteBalance;
    }

    public void setWhiteBalance(int whiteBalance) {
        this.whiteBalance = whiteBalance;
    }

    public int getLocInfoCacheFlag() {
        return locInfoCacheFlag;
    }

    public void setLocInfoCacheFlag(int locInfoCacheFlag) {
        this.locInfoCacheFlag = locInfoCacheFlag;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getScenic() {
        return scenic;
    }

    public void setScenic(String scenic) {
        this.scenic = scenic;
    }

    public String getLocInfoEx() {
        return locInfoEx;
    }

    public void setLocInfoEx(String locInfoEx) {
        this.locInfoEx = locInfoEx;
    }

    public int getFaceInfoCacheFlag() {
        return faceInfoCacheFlag;
    }

    public void setFaceInfoCacheFlag(int faceInfoCacheFlag) {
        this.faceInfoCacheFlag = faceInfoCacheFlag;
    }

    public int getFaceCount() {
        return faceCount;
    }

    public void setFaceCount(int faceCount) {
        this.faceCount = faceCount;
    }

    public int getSelfieFlag() {
        return selfieFlag;
    }

    public void setSelfieFlag(int selfieFlag) {
        this.selfieFlag = selfieFlag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public boolean isNeedUpload() {
        return needUpload;
    }

    public void setNeedUpload(boolean needUpload) {
        this.needUpload = needUpload;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public static PictureModel getPictureModel(String photoId) {
        return SQLite.select().from(PictureModel.class)
                .where(PictureModel_Table._id.eq(photoId))
                .querySingle();
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public static Observable<PictureModel> getAllPictureModel() {
        return rx.Observable.from(SQLite.select().from(PictureModel.class).queryList())
                .filter(pictureModel -> pictureModel != null && !TextUtils.isEmpty(pictureModel.getLocalPath()) && new File(pictureModel.getLocalPath()).exists());
    }

    public static Observable<DateModel> getBucketAllDatePictures(String bucketId) {
        return Observable.from(getBucketAllData(bucketId))
                .filter(dateModel -> {
                    dateModel.setList(getBucketAllDatePicture(bucketId, dateModel.getDayTime()));
                    return dateModel.getList().size() > 0;
                });
    }

    public static List<DateModel> getBucketAllData(String bucketId) {
        if (TextUtils.isEmpty(bucketId)) {
            return SQLite.select(PictureModel_Table.day_time)
                    .from(PictureModel.class)
                    .groupBy(PictureModel_Table.day_time)
                    .orderBy(PictureModel_Table.date_taken, false)
                    .queryCustomList(DateModel.class);
        } else {
            return SQLite.select(PictureModel_Table.day_time)
                    .from(PictureModel.class)
                    .where(PictureModel_Table.bucket_id.eq(bucketId))
                    .groupBy(PictureModel_Table.day_time)
                    .orderBy(PictureModel_Table.date_taken, false)
                    .queryCustomList(DateModel.class);
        }
    }

    public static List<PictureModel> queryPicturesByIds(List<String> list){
        return SQLite.select().from(PictureModel.class)
                .where(PictureModel_Table._id.in(list))
                .queryList();
    }

    public static long getBucketPictureCount(String bucketId) {
        if (TextUtils.isEmpty(bucketId)) {
            return SQLite.select().from(PictureModel.class)
                    .query().getCount();
        } else return SQLite.select().from(PictureModel.class)
                .where(PictureModel_Table.bucket_id.eq(bucketId))
                .query().getCount();
    }

    public static List<PictureModel> getBucketAllPicture(String bucketId) {
        if (TextUtils.isEmpty(bucketId)) {
            return SQLite.select()
                    .from(PictureModel.class)
                    .orderBy(PictureModel_Table.date_taken, false)
                    .queryList();
        } else {
            return SQLite.select()
                    .from(PictureModel.class)
                    .where(PictureModel_Table.bucket_id.eq(bucketId))
                    .orderBy(PictureModel_Table.date_taken, false)
                    .queryList();
        }
    }

    public static List<PictureModel> getBucketAllDatePicture(String bucketId, long dayTime) {
        if (TextUtils.isEmpty(bucketId)) {
            return SQLite.select()
                    .from(PictureModel.class)
                    .where(PictureModel_Table.day_time.eq(dayTime))
                    .orderBy(PictureModel_Table.date_taken, false)
                    .queryList();
        } else {
            return SQLite.select()
                    .from(PictureModel.class)
                    .where(PictureModel_Table.bucket_id.eq(bucketId))
                    .and(PictureModel_Table.day_time.eq(dayTime))
                    .orderBy(PictureModel_Table.date_taken, false)
                    .queryList();
        }
    }


    public static Observable<PictureModel> getOldData(long startTime) {
        return Observable.from(SQLite.select().from(PictureModel.class).where(PictureModel_Table.update_time.lessThan(startTime)).queryList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PictureModel that = (PictureModel) o;

        if (TextUtils.isEmpty(getPhotoId())) {
            return TextUtils.isEmpty(getLocalPath()) ? TextUtils.isEmpty(that.getPhotoId()) && TextUtils.isEmpty(that.getLocalPath()) : getLocalPath().equals(that.getLocalPath());
        } else return getPhotoId().equals(that.getPhotoId());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photoId);
        dest.writeString(this.displayName);
        dest.writeString(this.bucketId);
        dest.writeString(this.bucketDisplayName);
        dest.writeString(this.title);
        dest.writeString(this.localPath);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.miniThumbMagic);
        dest.writeInt(this.orientation);
        dest.writeLong(this.size);
        dest.writeLong(this.dateTaken);
        dest.writeLong(this.dateAdded);
        dest.writeString(this.mimeType);
        dest.writeInt(this.flash);
        dest.writeString(this.maker);
        dest.writeString(this.model);
        dest.writeInt(this.whiteBalance);
        dest.writeInt(this.locInfoCacheFlag);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.scenic);
        dest.writeString(this.locInfoEx);
        dest.writeInt(this.faceInfoCacheFlag);
        dest.writeInt(this.faceCount);
        dest.writeInt(this.selfieFlag);
        dest.writeString(this.url);
        dest.writeLong(this.dayTime);
        dest.writeString(this.objectKey);
        dest.writeByte(this.needUpload ? (byte) 1 : (byte) 0);
        dest.writeString(this.thumbPath);
        dest.writeString(this.md5);
        dest.writeLong(this.updateTime);
    }

    protected PictureModel(Parcel in) {
        this.photoId = in.readString();
        this.displayName = in.readString();
        this.bucketId = in.readString();
        this.bucketDisplayName = in.readString();
        this.title = in.readString();
        this.localPath = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.miniThumbMagic = in.readString();
        this.orientation = in.readInt();
        this.size = in.readLong();
        this.dateTaken = in.readLong();
        this.dateAdded = in.readLong();
        this.mimeType = in.readString();
        this.flash = in.readInt();
        this.maker = in.readString();
        this.model = in.readString();
        this.whiteBalance = in.readInt();
        this.locInfoCacheFlag = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.scenic = in.readString();
        this.locInfoEx = in.readString();
        this.faceInfoCacheFlag = in.readInt();
        this.faceCount = in.readInt();
        this.selfieFlag = in.readInt();
        this.url = in.readString();
        this.dayTime = in.readLong();
        this.objectKey = in.readString();
        this.needUpload = in.readByte() != 0;
        this.thumbPath = in.readString();
        this.md5 = in.readString();
        this.updateTime = in.readLong();
    }

    public static final Creator<PictureModel> CREATOR = new Creator<PictureModel>() {
        @Override
        public PictureModel createFromParcel(Parcel source) {
            return new PictureModel(source);
        }

        @Override
        public PictureModel[] newArray(int size) {
            return new PictureModel[size];
        }
    };
}
