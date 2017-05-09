package demo.wang.cn.librarysdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.wang.adapter.BaseEmptyAdapter;
import cn.wang.adapter.StringArrayAdapter;
import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.adapter.beans.EmptyItem;
import cn.wang.adapter.listeners.OnItemClickListener;
import demo.wang.cn.librarysdemo.bean.ImageBean;

/**
 * author : wangshuai Created on 2017/5/3
 * email : wangs1992321@gmail.com
 */
public class CarouuseActivity extends AppCompatActivity {


    private ImageAdapter adapter = null;
    private RecyclerView list;
    private LinearLayoutManager manager;
    private LinearSnapHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (RecyclerView) findViewById(R.id.list);
        adapter = new ImageAdapter(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        list.setLayoutManager(manager);
        list.setAdapter(adapter);
        helper = new LinearSnapHelper();
        helper.attachToRecyclerView(list);
        adapter.add(new ImageBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494173903856&di=1ee77caca2bc9a16ab891354f5440320&imgtype=0&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F58%2F87%2F63bOOOPICb3_1024.jpg"));
        adapter.add(new ImageBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494173903856&di=c2722d7cbedf3ece47cee68eacc6b7b0&imgtype=0&src=http%3A%2F%2Fimg11.360buyimg.com%2Fcms%2Fg13%2FM09%2F09%2F16%2FrBEhU1Ne-uAIAAAAAAKlyOtUYcgAAMmugE8618AAqXg335.jpg"));
        adapter.add(new ImageBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494173903856&di=b730b8ebbbbf4bc40a04c239f50e471d&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F56%2F99%2F88f58PICuBh_1024.jpg"));
        adapter.add(new ImageBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494173903856&di=53f3eb21ba2c06a7f663c375a745ed78&imgtype=0&src=http%3A%2F%2Fpic31.nipic.com%2F20130702%2F2926417_003653575119_2.jpg"));
        adapter.add(new ImageBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494173903856&di=9bc274011114e65ba4242aa20adf2c84&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F17%2F25%2F97%2F01c58PICyH9_1024.jpg"));
        adapter.add(new ImageBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494173903856&di=557cf72be5f57748fe790c254b699837&imgtype=0&src=http%3A%2F%2Fimg007.hc360.cn%2Fg8%2FM00%2F7E%2FCF%2FwKhQt1Nu6haEB6irAAAAABzDZZ4852.jpg"));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View contentView) {
                Log.d("CarouuseActivity",position+"----"+manager.findFirstVisibleItemPosition()+"----"+manager.findLastVisibleItemPosition()+"----");
            adapter.deleteItem(position);
            }
        });

    }

    class ImageAdapter extends BaseEmptyAdapter {

        public ImageAdapter(Context context) {
            super(context);
        }

        @Override
        public int emptyLayoutId() {
            return R.layout.carouse_image_item;
        }

        @Override
        public void empty(View contentView, EmptyItem emptyItem) {
            ImageView imageView = ViewHolder.getView(contentView, R.id.image);
            Glide.with(CarouuseActivity.this)
                    .load("https://i.ytimg.com/vi/rziFh0PkDDU/maxresdefault.jpg")
                    .fitCenter()
                    .crossFade()
                    .into(imageView);
        }

        @Override
        public int getLayoutID(int type) {
            return R.layout.carouse_image_item;
        }

        @Override
        public void doView(int position, View convertView) {
            Log.d("CarouuseActivity", position + "---" + getItem(position));
            ImageView imageView = ViewHolder.getView(convertView, R.id.image);
            Glide.with(CarouuseActivity.this)
                    .load(((ImageBean)getItem(position)).getUrl())
                    .fitCenter()
                    .crossFade()
                    .into(imageView);
        }


    }

}
