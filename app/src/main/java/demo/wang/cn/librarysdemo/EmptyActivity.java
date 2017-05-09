package demo.wang.cn.librarysdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * author : wangshuai Created on 2017/5/5
 * email : wangs1992321@gmail.com
 */
public class EmptyActivity extends AppCompatActivity{

    private RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        list= (RecyclerView) findViewById(R.id.list);

    }
}
