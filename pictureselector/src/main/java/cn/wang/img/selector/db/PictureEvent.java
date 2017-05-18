package cn.wang.img.selector.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
public class PictureEvent implements Parcelable {

    public static final String ACTION_LOADFINISH = "action_load_finish";

    public static final String ACTION_SELECT="action_select";

    private String action;

    public PictureEvent(String action) {
        this.action = action;
    }

    public String getAction() {

        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
    }

    protected PictureEvent(Parcel in) {
        this.action = in.readString();
    }

    public static final Parcelable.Creator<PictureEvent> CREATOR = new Parcelable.Creator<PictureEvent>() {
        @Override
        public PictureEvent createFromParcel(Parcel source) {
            return new PictureEvent(source);
        }

        @Override
        public PictureEvent[] newArray(int size) {
            return new PictureEvent[size];
        }
    };
}
