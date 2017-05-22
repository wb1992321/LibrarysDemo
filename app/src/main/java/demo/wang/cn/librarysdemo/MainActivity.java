package demo.wang.cn.librarysdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import cn.wang.adapter.StringArrayAdapter;
import cn.wang.adapter.SwipeAdapter;
import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.RecyclerSwipeAdapter;
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.adapter.beans.EmptyItem;
import cn.wang.adapter.listeners.OnItemClickListener;
import cn.wang.img.selector.activitys.SelecPictureActivity;
import cn.wang.img.selector.services.LoadPictureService;
import demo.wang.cn.librarysdemo.bean.ImageBean;

/**
 * author : wangshuai Created on 2017/5/3
 * email : wangs1992321@gmail.com
 */
public class MainActivity extends AppCompatActivity {


    private MyAdapter adapter = null;
    private RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (RecyclerView) findViewById(R.id.list);
        adapter = new MyAdapter(this);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);
        getData();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View contentView) {
                ImageBean imageBean = (ImageBean) adapter.getItem(position);
                Toast.makeText(MainActivity.this, imageBean.getUrl(), Toast.LENGTH_SHORT).show();
                adapter.deleteItem(position);
            }
        });
        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        LoadPictureService.start(MainActivity.this);
                    }
                }, throwable -> Log.d("LoadPictureService", "异常", throwable));

        JSONObject format = new JSONObject();
        format.put("localPath", "url");

        SelecPictureActivity.open(this, 1001, 10, 0, format.toJSONString(), null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            Log.d("MainActivity", data.getStringExtra(SelecPictureActivity.RESULT_DATA));
        }
    }

    private void getData() {
        ArrayList<ImageBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ImageBean("aaa" + i));
        }
        adapter.addList(false, list);
    }


    class MyAdapter extends RecyclerSwipeAdapter {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public int emptyLayoutId() {
            return 0;
        }

        @Override
        public void empty(View contentView, EmptyItem emptyItem) {

        }

        @Override
        public int getLayoutID(int type) {
            return R.layout.text;
        }

        @Override
        public int swipeLayoutId(int position) {
            return R.layout.button_views;
        }

        @Override
        public void swipeOp(View convertView, final int position) {
            Button delete = ViewHolder.getView(convertView, R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }

        @Override
        public void doAction(int position, View convertView) {
            ImageBean imageBean = (ImageBean) getItem(position);
            TextView textView = ViewHolder.getView(convertView, R.id.text);
            textView.setText(imageBean.getUrl());
        }
    }

}
