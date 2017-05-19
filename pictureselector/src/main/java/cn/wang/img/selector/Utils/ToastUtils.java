package cn.wang.img.selector.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : wangshuai Created on 2017/5/18
 * email : wangs1992321@gmail.com
 */
public class ToastUtils {

    private static long lastShowTime = 0;

    public static final void show(Context context, String text) {
        if (TextUtils.isEmpty(text) || System.currentTimeMillis() - lastShowTime <= 2000)
            return;
        Observable.defer(()->Observable.just(text))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> Toast.makeText(context,s,Toast.LENGTH_SHORT).show(),throwable -> {});
        lastShowTime=System.currentTimeMillis();
    }

}
