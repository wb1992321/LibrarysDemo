package cn.wang.adapter.bases.swipe;

import java.util.List;

import cn.wang.adapter.utils.Attributes;
import cn.wang.adapter.views.SwipeLayout;

/**
 * author : wangshuai Created on 2017/5/22
 * email : wangs1992321@gmail.com
 */
public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(SwipeLayout layout);

    void closeAllItems();

    List<Integer> getOpenItems();

    List<SwipeLayout> getOpenLayouts();

    void removeShownLayouts(SwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
