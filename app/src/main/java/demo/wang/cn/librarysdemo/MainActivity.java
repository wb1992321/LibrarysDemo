package demo.wang.cn.librarysdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.wang.adapter.StringArrayAdapter;
import cn.wang.adapter.SwipeAdapter;
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.adapter.beans.EmptyItem;
import cn.wang.adapter.listeners.OnItemClickListener;
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
                ImageBean imageBean= (ImageBean) adapter.getItem(position);
                Toast.makeText(MainActivity.this,imageBean.getUrl(),Toast.LENGTH_SHORT).show();
                adapter.deleteItem(position);
            }
        });

    }

    private void getData() {
        ArrayList<ImageBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ImageBean("aaa"+i));
        }
        adapter.addList(false,list);
    }

    class MyAdapter extends SwipeAdapter{

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
            Button delete=ViewHolder.getView(convertView,R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }

        @Override
        public void doAction(int position, View convertView) {
            ImageBean imageBean= (ImageBean) getItem(position);
            TextView textView= ViewHolder.getView(convertView,R.id.text);
            textView.setText(imageBean.getUrl());
        }
    }

}
