package cn.wang.img.selector.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : wangshuai Created on 2017/5/19
 * email : wangs1992321@gmail.com
 */
public class LoadEvent implements Parcelable {

    private int state;

    public LoadEvent(int state) {
        this.state = state;
    }

    public int getState() {

        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state);
    }

    protected LoadEvent(Parcel in) {
        this.state = in.readInt();
    }

    public static final Parcelable.Creator<LoadEvent> CREATOR = new Parcelable.Creator<LoadEvent>() {
        @Override
        public LoadEvent createFromParcel(Parcel source) {
            return new LoadEvent(source);
        }

        @Override
        public LoadEvent[] newArray(int size) {
            return new LoadEvent[size];
        }
    };
}
