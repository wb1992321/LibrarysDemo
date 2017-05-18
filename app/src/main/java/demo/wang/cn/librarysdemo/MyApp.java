package demo.wang.cn.librarysdemo;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * author : wangshuai Created on 2017/5/12
 * email : wangs1992321@gmail.com
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
