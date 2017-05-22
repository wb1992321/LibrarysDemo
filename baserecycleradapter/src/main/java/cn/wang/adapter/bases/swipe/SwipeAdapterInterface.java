package cn.wang.adapter.bases.swipe;

/**
 * author : wangshuai Created on 2017/5/22
 * email : wangs1992321@gmail.com
 */
public interface SwipeAdapterInterface {

    int getSwipeLayoutResourceId(int position);

    void notifyDatasetChanged(int position);

}
