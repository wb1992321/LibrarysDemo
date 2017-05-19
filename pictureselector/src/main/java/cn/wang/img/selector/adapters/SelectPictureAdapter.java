package cn.wang.img.selector.adapters;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.wang.adapter.bases.BaseAdapter;
import cn.wang.adapter.bases.ViewHolder;
import cn.wang.img.selector.R;
import cn.wang.img.selector.Utils.DateUtils;
import cn.wang.img.selector.Utils.ToastUtils;
import cn.wang.img.selector.db.PictureEvent;
import cn.wang.img.selector.models.DateModel;
import cn.wang.img.selector.models.PictureModel;
import cn.wang.img.selector.views.MyCheckTextView;
import cn.wang.img.selector.views.PictureStagger;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
public class SelectPictureAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private int paddingleft = 10;
    private int imageSize = 0;
    private PictureStagger lookup = null;
    public static final String TAG = "SelectPictureAdapter";
    private ArrayList<String> selPicIds = new ArrayList<>(0);
    private ArrayList<Long> selDate = new ArrayList<>(0);

    private int maxSize = -1;

    public SelectPictureAdapter(Context context, int maxSize) {
        super(context);
        this.maxSize = maxSize;
    }

    public void setLookup(PictureStagger lookup) {
        this.lookup = lookup;
        paddingleft = (int) getContext().getResources().getDimension(R.dimen.size_8);
        imageSize = (getContext().getResources().getDisplayMetrics().widthPixels - paddingleft - lookup.getColumnCount() * paddingleft) / lookup.getColumnCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof DateModel) {
            return PictureStagger.TYPE_DATE_DAY;
        } else {
            return PictureStagger.TYPE_PICTURE;
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        switch (viewType) {
            case PictureStagger.TYPE_DATE_DAY:
                return R.layout.list_item_date;
            case PictureStagger.TYPE_PICTURE:
                return R.layout.list_item_pic;
        }
        return 0;
    }

    @Override
    public void initView(int position, View convertView) {
        switch (getItemViewType(position)) {
            case PictureStagger.TYPE_DATE_DAY:
                dateTime(position, convertView);
                break;
            case PictureStagger.TYPE_PICTURE:
                picture(position, convertView);
                break;
        }
    }


    private void picture(int position, View convertView) {
        PictureModel model = (PictureModel) getItem(position);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) convertView.getLayoutParams();
        if (params == null)
            params = new RecyclerView.LayoutParams(paddingleft + imageSize, imageSize);
        params.width = paddingleft + imageSize;
        params.height = imageSize;
        params.topMargin = paddingleft;
        convertView.setLayoutParams(params);
        ImageView ivImage = ViewHolder.getView(convertView, R.id.iv_image);

        Glide.with(getContext())
                .load(model.getLocalPath())
                .crossFade(100).into(ivImage);
        CheckBox cbSel = ViewHolder.getView(convertView, R.id.cb_sel);
        cbSel.setOnCheckedChangeListener(null);
        cbSel.setTag(position);
        cbSel.setChecked(selPicIds.contains(model.getPhotoId()) || selDate.contains(model.getDayTime()));
        cbSel.setOnCheckedChangeListener(this);
        ViewHolder.getView(convertView, R.id.v_checked).setVisibility(cbSel.isChecked() ? View.VISIBLE : View.GONE);
    }

    private void dateTime(int position, View convertView) {
        DateModel model = (DateModel) getItem(position);
        TextView tvTitle = ViewHolder.getView(convertView, R.id.tv_title);
        tvTitle.setText(DateUtils.format(model.getDayTime(), DateUtils.FORMAT_DATE_DAY));
        MyCheckTextView tvCheck = ViewHolder.getView(convertView, R.id.tv_check);
        tvCheck.setOnCheckedChangeListener(null);
        tvCheck.setTag(position);
        tvCheck.setChecked(selDate.contains(model.getDayTime()));
        tvCheck.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        if (buttonView.getId() == R.id.tv_check) {
            DateModel dateModel = (DateModel) getItem(position);
            if (isChecked) {
                if (!selDate.contains(dateModel.getDayTime())) {
                    Observable.from(dateModel.getList())
                            .filter(pictureModel -> !selPicIds.contains(pictureModel.getPhotoId()))
                            .toList()
                            .filter(models -> {
                                boolean flag = models.size() + selPicIds.size() > maxSize;
                                if (flag) {
                                    models.clear();
                                    ToastUtils.show(getContext(), getContext().getString(R.string.pic_max_count, maxSize));
                                    selDate.remove(dateModel.getDayTime());
                                    updateItem(dateModel);
                                } else {
                                    selDate.add(dateModel.getDayTime());
                                }
                                return !flag;
                            })
                            .flatMap(models -> Observable.from(models))
                            .doOnNext(pictureModel -> selPicIds.add(pictureModel.getPhotoId()))
                            .toList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(pictureModels -> {
                                EventBus.getDefault().post(new PictureEvent(PictureEvent.ACTION_SELECT));
                            }, throwable -> {
                            });
                }
            } else {
                if (selDate.contains(dateModel.getDayTime())) {
                    selDate.remove(dateModel.getDayTime());
                    Observable.from(dateModel.getList())
                            .filter(pictureModel -> selPicIds.contains(pictureModel.getPhotoId()))
                            .doOnNext(pictureModel -> selPicIds.remove(pictureModel.getPhotoId()))
                            .toList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(pictureModels -> {
                                EventBus.getDefault().post(new PictureEvent(PictureEvent.ACTION_SELECT));
                            }, throwable -> {
                            });
                }
            }
            updateItem(position + 1, dateModel.getList().size());
        } else {
            PictureModel model = (PictureModel) getItem(position);
            boolean select = changeSelect(isChecked, model);

            Log.d(TAG, "onCheckedChanged");

            if (!select)
                buttonView.setChecked(!isChecked);
        }
    }

    public int getSelectCount() {
        return selPicIds.size();
    }


    public ArrayList<String> getSelectPhotoIds() {
        return selPicIds;
    }

    public boolean changeSelect(boolean isChecked, PictureModel model) {
        Log.d(TAG, selPicIds.size() + "");
        if (isChecked) {
            if (selPicIds.size() < maxSize && !selPicIds.contains(model.getPhotoId())) {
                selPicIds.add(model.getPhotoId());
                DateModel dateModel = (DateModel) getItem(getPosition(new DateModel(model.getDayTime())));
                Observable.defer(() -> Observable.from(dateModel.getList()))
                        .filter(pictureModel -> !selPicIds.contains(pictureModel.getPhotoId()))
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(pictureModels -> {
                            if (pictureModels == null || pictureModels.size() <= 0) {
                                selDate.add(dateModel.getDayTime());
                                updateItem(dateModel);
                            }
                        }, throwable -> {
                        });
            } else if (selPicIds.size() >= maxSize) {
                ToastUtils.show(getContext(), getContext().getString(R.string.pic_max_count, maxSize));
                return false;
            }
        } else {
            if (selPicIds.contains(model.getPhotoId())) {
                selPicIds.remove(model.getPhotoId());
                if (selDate.contains(model.getDayTime())) {
                    selDate.remove(model.getDayTime());
                    updateItem(getPosition(new DateModel(model.getDayTime())));
                }
            }
        }
        EventBus.getDefault().post(new PictureEvent(PictureEvent.ACTION_SELECT));
        updateItem(model);
        return true;
    }

    public List getDataList() {
        return list;
    }

    @Override
    public void addList(boolean isClear, Collection collection) {
        super.addList(isClear, collection);
    }

    @Override
    protected void handleMsg(Message message) {
        Log.d(TAG, "TYPE_ADD_LIST start");
        super.handleMsg(message);
        Log.d(TAG, "TYPE_ADD_LIST end");
    }

}
