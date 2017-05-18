package cn.wang.img.selector.adapters;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.img.selector.R;
import cn.wang.img.selector.models.PictureModel;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * author : wangshuai Created on 2017/5/18
 * email : wangs1992321@gmail.com
 */
public class PicPreviewAdapter extends BaseAdapter<PictureModel> implements PhotoViewAttacher.OnPhotoTapListener {
    public PicPreviewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.pic_preview_list_item;
    }

    @Override
    protected void handleMsg(Message message) {
        super.handleMsg(message);
        if (this.loadFinish!=null){
            this.loadFinish.loadfinish(message.what);
        }
    }

    @Override
    public void initView(int position, View convertView) {
        PhotoView ivPic = ViewHolder.getView(convertView, R.id.iv_pic);
        Glide.with(getContext())
                .load(getItem(position).getLocalPath())
                .crossFade()
                .into(ivPic);
        ivPic.setOnPhotoTapListener(this);
    }

    private LoadFinish loadFinish;

    public void setLoadFinish(LoadFinish loadFinish) {
        this.loadFinish = loadFinish;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        if (getOnItemClickListener()!=null){
            getOnItemClickListener().onItemClick(0,view);
        }
    }

    @Override
    public void onOutsidePhotoTap() {

    }

    public interface LoadFinish{
        public void loadfinish(int what);
    }

}
