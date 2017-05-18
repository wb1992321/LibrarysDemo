package cn.wang.img.selector.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

import cn.wang.img.selector.models.PictureModel;
import rx.Observable;

/**
 * author : wangshuai Created on 2017/5/12
 * email : wangs1992321@gmail.com
 */
public class MediaStoreCursorHelper {
    public static final String[] PHOTOS_PROJECTION =
            {
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.SIZE,
                    MediaStore.Images.ImageColumns.DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.TITLE,
                    MediaStore.Images.ImageColumns.DATE_ADDED,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.WIDTH,
                    MediaStore.Images.ImageColumns.HEIGHT,
                    MediaStore.Images.ImageColumns.DESCRIPTION,
                    MediaStore.Images.ImageColumns.LATITUDE,
                    MediaStore.Images.ImageColumns.LONGITUDE,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.ORIENTATION,
                    MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC,
                    MediaStore.Images.ImageColumns.BUCKET_ID,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
            };

    public static final String PHOTOS_ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";

    public static Cursor openPhotosCursor(Context context, long timeStamp) {
        return MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PHOTOS_PROJECTION, MediaStore.Images.Media.DATE_TAKEN + " > " + timeStamp, PHOTOS_ORDER_BY);
    }

    public static Observable<PictureModel> getAllPhotoFrom(Cursor cursor) {
        ArrayList<PictureModel> arrayList = new ArrayList<>(0);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    arrayList.add(new PictureModel(cursor));
                } catch (Exception e) {
                }
            } while (cursor.moveToNext());
            cursor.close();
            cursor = null;
        }
        return Observable.from(arrayList);
    }
}
