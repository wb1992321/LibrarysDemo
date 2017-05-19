package cn.wang.img.selector.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * author : wangshuai Created on 2017/5/18
 * email : wangs1992321@gmail.com
 */
public class ToastUtils {

    private static long lastShowTime = 0;

    public static final void show(Context context, String text) {
        if (TextUtils.isEmpty(text) || System.currentTimeMillis() - lastShowTime <= 2000)
            return;
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
        lastShowTime=System.currentTimeMillis();
    }

}
