package cn.wang.img.selector.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

import java.util.ArrayList;
import java.util.List;

import cn.wang.img.selector.db.DB;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
@QueryModel(database = DB.class)
public class DateModel extends BaseQueryModel {
    @Column(name = "day_time")
    long dayTime;

    private List<PictureModel> list = null;

    public DateModel(long dayTime) {
        this.dayTime = dayTime;
    }

    public DateModel() {

    }

    public List<PictureModel> getList() {
        if (list == null) {
            list = new ArrayList<>(0);
        }
        return list;
    }

    public void setList(List<PictureModel> list) {
        this.list = list;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateModel dateModel = (DateModel) o;

        return dayTime == dateModel.dayTime;

    }

}
