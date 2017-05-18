package cn.wang.img.selector.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * author : wangshuai Created on 2017/5/12
 * email : wangs1992321@gmail.com
 */
@Database(name = DB.DB_NAME, version = DB.DB_VERSION)
public class DB {
    public static final String DB_NAME = "pic_select"; // we will add the .db extension

    public static final int DB_VERSION = 1;
}
