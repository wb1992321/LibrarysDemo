package cn.wang.img.selector.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.wang.img.selector.models.PictureModel;

/**
 * author : wangshuai Created on 2017/5/18
 * email : wangs1992321@gmail.com
 */
public class ResultEvent implements Parcelable {

    private String jsonString;

    public ResultEvent(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getJsonString() {

        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jsonString);
    }

    protected ResultEvent(Parcel in) {
        this.jsonString = in.readString();
    }

    public static final Parcelable.Creator<ResultEvent> CREATOR = new Parcelable.Creator<ResultEvent>() {
        @Override
        public ResultEvent createFromParcel(Parcel source) {
            return new ResultEvent(source);
        }

        @Override
        public ResultEvent[] newArray(int size) {
            return new ResultEvent[size];
        }
    };
}
