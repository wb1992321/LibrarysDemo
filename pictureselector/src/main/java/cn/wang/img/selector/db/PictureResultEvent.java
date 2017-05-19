package cn.wang.img.selector.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import cn.wang.img.selector.models.PictureModel;

/**
 * author : wangshuai Created on 2017/5/18
 * email : wangs1992321@gmail.com
 */
public class PictureResultEvent implements Parcelable {

    private ArrayList<PictureModel> pictureModels;

    public PictureResultEvent(ArrayList<PictureModel> pictureModels) {
        this.pictureModels = pictureModels;
    }

    public ArrayList<PictureModel> getPictureModels() {

        return pictureModels;
    }

    public void setPictureModels(ArrayList<PictureModel> pictureModels) {
        this.pictureModels = pictureModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.pictureModels);
    }

    protected PictureResultEvent(Parcel in) {
        this.pictureModels = in.createTypedArrayList(PictureModel.CREATOR);
    }

    public static final Parcelable.Creator<PictureResultEvent> CREATOR = new Parcelable.Creator<PictureResultEvent>() {
        @Override
        public PictureResultEvent createFromParcel(Parcel source) {
            return new PictureResultEvent(source);
        }

        @Override
        public PictureResultEvent[] newArray(int size) {
            return new PictureResultEvent[size];
        }
    };
}
