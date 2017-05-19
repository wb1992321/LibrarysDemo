package cn.wang.img.selector.recieves;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.wang.img.selector.services.LoadPictureService;

/**
 * author : wangshuai Created on 2017/4/5
 * email : wangs1992321@gmail.com
 */
public class CameraCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("android.hardware.action.NEW_PICTURE")) {
            try {
                LoadPictureService.start(context.getApplicationContext());
            } catch (Exception e) {
            }
        }
//        else if (intent.getAction().equalsIgnoreCase("android.hardware.action.NEW_VIDEO")) {
//            try {
//                context.startService(new Intent(context, LoadMediaService.class));
//            } catch (Exception e) {
//            }
//        }

    }
}
